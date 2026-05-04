package com.healthcare.modules.doctor.service;

import com.healthcare.modules.doctor.dto.CreateDoctorDTO;
import com.healthcare.modules.doctor.dto.DoctorResposeDTO;
import com.healthcare.modules.doctor.dto.UpdateDoctorDTO;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor.repository.DoctorRepository;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserService userService;

    public DoctorServiceImpl(DoctorRepository doctorRepository, UserService userService) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
    }

    @Override
    public DoctorResposeDTO createDoctor(CreateDoctorDTO createDoctorDTO) {
        try {

            if (doctorRepository.existsByUserId(createDoctorDTO.userId())) {
                throw new ApplicationException(ErrorMessage.DOCTOR_ALREADY_EXISTS_FOR_USER, "");
            }

            UserEntity userEntity = this.userService.findUserEntityById(createDoctorDTO.userId());

            if(!userEntity.getRole().equals(UserRole.DOCTOR)){
                throw new ApplicationException(
                        ErrorMessage.USER_NOT_DOCTOR, ""
                );
            }

            if (!userEntity.isActive()) {
                throw new ApplicationException(
                        ErrorMessage.USER_INACTIVE, ""
                );
            }

            DoctorEntity newDoctor = new DoctorEntity();
            newDoctor.setUser(userEntity);
            newDoctor.setSpecialty(createDoctorDTO.specialty());
            newDoctor.setLicenseNumber(createDoctorDTO.licenseNumber());
            newDoctor.setDefaultConsultationDuration(createDoctorDTO.defaultConsultationDuration());

             this.doctorRepository.save(newDoctor);

            return DoctorResposeDTO.fromEntity(newDoctor);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public DoctorResposeDTO updateDoctor(UUID id, UpdateDoctorDTO updateDoctorDTO) {
        try {

            DoctorEntity findDoctor = this.findDoctorEntityById(id);

            if (updateDoctorDTO.specialty() != null) {
                findDoctor.setSpecialty(updateDoctorDTO.specialty());
            }

            if (updateDoctorDTO.licenseNumber() != null) {
                findDoctor.setLicenseNumber(updateDoctorDTO.licenseNumber());
            }

            if (updateDoctorDTO.defaultConsultationDuration() != null) {
                findDoctor.setDefaultConsultationDuration(updateDoctorDTO.defaultConsultationDuration());
            }

            DoctorEntity doctorUpdated = this.doctorRepository.save(findDoctor);
            return DoctorResposeDTO.fromEntity(doctorUpdated);

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex){
            throw new ApplicationException(ex);
        }
    }

    @Override
    public PageResponse<DoctorResposeDTO> findAllDoctors(int page, int size) {
        try{

            Pageable pageable = PageRequest.of(page, size);
            Page<DoctorEntity> result = doctorRepository.findAllDoctorsPaged(pageable);

            return new PageResponse<>(
                    result.getContent()
                            .stream()
                            .map(DoctorResposeDTO::fromEntity)
                            .toList(),
                    result.getNumber(),
                    result.getSize(),
                    result.getTotalElements(),
                    result.getTotalPages()
            );

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public DoctorResposeDTO findDoctorById(UUID id) {
        try{

            DoctorEntity findDoctorById = this.doctorRepository.findById(id)
                    .orElseThrow( () -> new ApplicationException(ErrorMessage.DOCTOR_NOT_FOUND_ID, "")
                    );

            return DoctorResposeDTO.fromEntity(findDoctorById);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public DoctorEntity findDoctorEntityById(UUID id) {

        return this.doctorRepository.findById(id)
                .orElseThrow(() -> {
                    return new ApplicationException(ErrorMessage.DOCTOR_NOT_FOUND_ID, id);
                });
    }

    @Override
    public void deleteDoctor(UUID id) {
        try {
            DoctorEntity doctor = this.findDoctorEntityById(id);
            doctorRepository.deleteById(doctor.getId());

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }
}
