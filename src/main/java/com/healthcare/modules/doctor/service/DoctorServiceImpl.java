package com.healthcare.modules.doctor.service;

import com.healthcare.modules.doctor.dto.CreateDoctorDTO;
import com.healthcare.modules.doctor.dto.DoctorResposeDTO;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor.repository.DoctorRepository;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import org.springframework.stereotype.Service;

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

            return DoctorResposeDTO.fromEntity(newDoctor, userEntity);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }
}
