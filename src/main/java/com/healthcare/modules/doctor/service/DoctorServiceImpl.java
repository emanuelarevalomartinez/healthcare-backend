package com.healthcare.modules.doctor.service;

import com.healthcare.modules.auth.service.AuthService;
import com.healthcare.modules.doctor.dto.*;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor.entity.specifications.DoctorSpecifications;
import com.healthcare.modules.doctor.repository.DoctorRepository;
import com.healthcare.modules.doctor_schedule.dto.CreateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedule.dto.UpdateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedule.entity.DoctorScheduleEntity;
import com.healthcare.modules.doctor_schedule.service.DoctorScheduleService;
import com.healthcare.modules.user.dto.CreateUserDTO;
import com.healthcare.modules.user.dto.UpdateUserDTO;
import com.healthcare.modules.user.dto.UserWithDoctorAndSchedulesResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.response.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserService userService;
    private final AuthService authService;
    private final DoctorScheduleService doctorScheduleService;

    public DoctorServiceImpl(DoctorRepository doctorRepository, UserService userService, AuthService authService, DoctorScheduleService doctorScheduleService) {
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.authService = authService;
        this.doctorScheduleService = doctorScheduleService;
    }

    @Override
    public DoctorResponseDTO createDoctor(CreateDoctorDTO createDoctorDTO) {

        if (doctorRepository.existsByUserId(createDoctorDTO.userId())) {
            throw new ApplicationException(ErrorMessage.DOCTOR_ALREADY_EXISTS_FOR_USER, "");
        }

        UserEntity userEntity = this.userService.findUserEntityById(createDoctorDTO.userId());

        if (!userEntity.getRole().equals(UserRole.DOCTOR)) {
            throw new ApplicationException(ErrorMessage.USER_NOT_DOCTOR, "");
        }

        if (doctorRepository.existsByLicenseNumber(createDoctorDTO.licenseNumber())) {
            throw new ApplicationException(ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS, createDoctorDTO.licenseNumber());
        }

        UUID userId = authService.getCurrentUserId();
        UserEntity autenticateUserEntity = this.userService.findUserEntityById(userId);

        DoctorEntity newDoctor = new DoctorEntity();
        newDoctor.setUser(userEntity);
        newDoctor.setModifiedBy(autenticateUserEntity);
        newDoctor.setSpecialty(createDoctorDTO.specialty());
        newDoctor.setLicenseNumber(createDoctorDTO.licenseNumber());
        newDoctor.setDefaultConsultationDuration(createDoctorDTO.defaultConsultationDuration());

        this.doctorRepository.save(newDoctor);

        return DoctorResponseDTO.fromEntity(newDoctor);
    }

    @Override
    public DoctorResponseDTO updateDoctor(UUID id, UpdateDoctorDTO updateDoctorDTO) {

        DoctorEntity findDoctor = this.findDoctorEntityById(id);

        if (updateDoctorDTO.specialty() != null) {
            findDoctor.setSpecialty(updateDoctorDTO.specialty());
        }

        if (updateDoctorDTO.licenseNumber() != null && !updateDoctorDTO.licenseNumber().equals(findDoctor.getLicenseNumber())) {

            if (doctorRepository.existsByLicenseNumber(updateDoctorDTO.licenseNumber())) {
                throw new ApplicationException(ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS, updateDoctorDTO.licenseNumber()
                );
            }
            findDoctor.setLicenseNumber(updateDoctorDTO.licenseNumber());
        }

        if (updateDoctorDTO.defaultConsultationDuration() != null) {
            findDoctor.setDefaultConsultationDuration(updateDoctorDTO.defaultConsultationDuration());
        }

        UUID userId = authService.getCurrentUserId();

        UserEntity autenticateUserEntity = this.userService.findUserEntityById(userId);
        findDoctor.setModifiedBy(autenticateUserEntity);

        DoctorEntity doctorUpdated = this.doctorRepository.save(findDoctor);
        return DoctorResponseDTO.fromEntity(doctorUpdated);
    }

    @Transactional
    @Override
    public DoctorWithUserAndScheduleResponseDTO createDoctorWithUserAndSchedule(CreateDoctorWithUserAndScheduleDTO createDoctorWithUserAndScheduleDTO) {

        CreateUserDTO userDTO = createDoctorWithUserAndScheduleDTO.user();

        if (!UserRole.DOCTOR.equals(userDTO.role())) {
            throw new ApplicationException(ErrorMessage.USER_NOT_DOCTOR, "");
        }

        UserWithDoctorAndSchedulesResponseDTO userResponse = userService.createUser(userDTO);
        UserEntity user = userService.findUserEntityById(userResponse.id());

        CreateDoctorWithoutUserDTO doctorDTO = createDoctorWithUserAndScheduleDTO.doctor();

        if (doctorRepository.existsByLicenseNumber(doctorDTO.licenseNumber())) {
            throw new ApplicationException(ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS, doctorDTO.licenseNumber());
        }

        UUID userId = authService.getCurrentUserId();
        UserEntity currentUser = userService.findUserEntityById(userId);

        DoctorEntity newDoctor = new DoctorEntity();
        newDoctor.setUser(user);
        newDoctor.setModifiedBy(currentUser);
        newDoctor.setSpecialty(doctorDTO.specialty());
        newDoctor.setLicenseNumber(doctorDTO.licenseNumber());
        newDoctor.setDefaultConsultationDuration(doctorDTO.defaultConsultationDuration());

        DoctorEntity doctorSaved;

        try {
            doctorSaved = doctorRepository.save(newDoctor);
        } catch (DataIntegrityViolationException ex) {
            throw new ApplicationException(
                    ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS,
                    doctorDTO.licenseNumber()
            );
        }

        List<DoctorScheduleEntity> scheduleSaved = new ArrayList<>();
        if (createDoctorWithUserAndScheduleDTO.schedule() != null && createDoctorWithUserAndScheduleDTO.schedule().schedules() != null && !createDoctorWithUserAndScheduleDTO.schedule().schedules().isEmpty()) {

            CreateDoctorScheduleDTO scheduleDTO = new CreateDoctorScheduleDTO(
                    doctorSaved.getId(),
                    createDoctorWithUserAndScheduleDTO.schedule().schedules()
            );
            scheduleSaved = doctorScheduleService.createDoctorSchedulesResponseEntities(scheduleDTO);
        }

        return DoctorWithUserAndScheduleResponseDTO.fromEntities(user, newDoctor, scheduleSaved);
    }

    @Transactional
    @Override
    public DoctorWithUserAndScheduleResponseDTO updateDoctorWithUserAndSchedule(UUID userId, UpdateDoctorWithUserAndScheduleDTO updateDoctorWithUserDTO) {

        UserEntity findUser = userService.findUserEntityById(userId);
        DoctorEntity findDoctor = null;

        UpdateUserDTO userDTO = updateDoctorWithUserDTO.user();
        UpdateDoctorDTO doctorDTO = updateDoctorWithUserDTO.doctor();

        try {
            findDoctor = findDoctorEntityByUserId(userId);

        } catch (ApplicationException ex) {
            if (ex.getType().equals(ErrorMessage.DOCTOR_NOT_FOUND_USER_ID.getType())) {

                if (doctorDTO.specialty() == null || doctorDTO.licenseNumber() == null || doctorDTO.defaultConsultationDuration() == null) {

                    throw new ApplicationException(ErrorMessage.REQUIRED_FIELDS_MISSING, "especialidad, numero de licencia y/o duracion predeterminada de la consulta");
                }

                if (doctorRepository.existsByLicenseNumber(doctorDTO.licenseNumber())) {
                    throw new ApplicationException(ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS, doctorDTO.licenseNumber()
                    );
                }

                CreateDoctorDTO newDoctor = new CreateDoctorDTO(userId, doctorDTO.specialty(), doctorDTO.licenseNumber(), doctorDTO.defaultConsultationDuration());

                UpdateUserDTO userUpdateRole = new UpdateUserDTO(null, null, null, UserRole.DOCTOR, null);
                userService.updateUser(userId, userUpdateRole);
                this.createDoctor(newDoctor);
                findDoctor = this.findDoctorEntityByUserId(userId);
            }
        }

        UpdateUserDTO userUpdate = new UpdateUserDTO(null, null, null, null, null);

        if (userDTO.username() != null && !userDTO.username().equals(findUser.getUsername())) {
            userUpdate = userUpdate.withUsername(userDTO.username());
        }

        if (userDTO.email() != null && !userDTO.email().equals(findUser.getEmail())) {
            userUpdate = userUpdate.withEmail(userDTO.username());
        }

        if (userDTO.password() != null) {
            userUpdate = userUpdate.withPassword(userDTO.password());
        }

        if (userDTO.role() != null) {
            userUpdate = userUpdate.withRole(userDTO.role());
        }

        if (userDTO.isActive() != null) {
            userUpdate = userUpdate.withIsActive(userDTO.isActive());
        }

        if (doctorDTO.specialty() != null) {
            findDoctor.setSpecialty(doctorDTO.specialty());
        }

        if (doctorDTO.licenseNumber() != null && !doctorDTO.licenseNumber().equals(findDoctor.getLicenseNumber())) {
            if (doctorRepository.existsByLicenseNumber(doctorDTO.licenseNumber())) {
                throw new ApplicationException(ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS, doctorDTO.licenseNumber()
                );
            }
            findDoctor.setLicenseNumber(doctorDTO.licenseNumber());
        }

        if (doctorDTO.defaultConsultationDuration() != null) {
            findDoctor.setDefaultConsultationDuration(doctorDTO.defaultConsultationDuration());
        }

        UUID userUuid = authService.getCurrentUserId();
        findDoctor.setModifiedBy(userService.findUserEntityById(userUuid));

        this.userService.updateUser(findUser.getId(), userUpdate);

        try {
            doctorRepository.save(findDoctor);
        } catch (DataIntegrityViolationException ex) {
            throw new ApplicationException(ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS, doctorDTO.licenseNumber());
        }

        List<DoctorScheduleEntity> scheduleUpdated = new ArrayList<>();
        if (updateDoctorWithUserDTO.schedule() != null && updateDoctorWithUserDTO.schedule().schedules() != null && !updateDoctorWithUserDTO.schedule().schedules().isEmpty()) {

            UpdateDoctorScheduleDTO scheduleDTO = new UpdateDoctorScheduleDTO(
                    updateDoctorWithUserDTO.schedule().schedules()
            );
            scheduleUpdated = doctorScheduleService.updateDoctorSchedulesResponseEntities(scheduleDTO);
        }

        return DoctorWithUserAndScheduleResponseDTO.fromEntities(findUser, findDoctor, scheduleUpdated);
    }

    @Override
    public PageResponse<DoctorResponseDTO> findAllDoctors(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorEntity> result = doctorRepository.findAllDoctorsPaged(pageable);

        return new PageResponse<>(
                result.getContent()
                        .stream()
                        .map(DoctorResponseDTO::fromEntity)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public DoctorResponseDTO findDoctorById(UUID id) {

        DoctorEntity findDoctorById = this.doctorRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.DOCTOR_NOT_FOUND_ID, "")
                );

        return DoctorResponseDTO.fromEntity(findDoctorById);
    }

    @Override
    public PageResponse<DoctorWithUserAndScheduleResponseDTO> findDoctorsFiltered(int page, int size, String search) {
        Specification<DoctorEntity> spec = Specification.where(DoctorSpecifications.search(search));

        Pageable pageable = PageRequest.of(
                page,
                Math.min(size, 10),
                Sort.by(Sort.Direction.ASC, "licenseNumber")
        );

        Page<DoctorEntity> result = doctorRepository.findAll(spec, pageable);

        return new PageResponse<>(
                result.getContent()
                        .stream()
                        .map(doctor ->
                                DoctorWithUserAndScheduleResponseDTO.fromEntities(
                                        doctor.getUser(),
                                        doctor,
                                        doctor.getSchedules()
                                        )
                        )
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
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
        DoctorEntity doctor = this.findDoctorEntityById(id);
        doctorRepository.deleteById(doctor.getId());
    }

    @Override
    @Transactional
    public void deleteDoctorAndItScheduleByUserId(UUID userId) {
        DoctorEntity doctor = this.findDoctorEntityByUserId(userId);
        doctorRepository.delete(doctor);
    }

    private DoctorEntity findDoctorEntityByUserId(UUID userId) {
        return doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.DOCTOR_NOT_FOUND_USER_ID, userId));
    }
}
