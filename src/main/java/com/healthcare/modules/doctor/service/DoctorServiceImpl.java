package com.healthcare.modules.doctor.service;

import com.healthcare.modules.doctor.dto.*;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor.repository.DoctorRepository;
import com.healthcare.modules.user.dto.UserResponseDTO;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.enums.UserRole;
import com.healthcare.modules.user.repository.UserRepository;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.providers.CustomUserDetails;
import com.healthcare.shared.response.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DoctorServiceImpl(DoctorRepository doctorRepository, UserRepository userRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.doctorRepository = doctorRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userAuthenticatedDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID userId = userAuthenticatedDetails.getId();

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userAuthenticatedDetails = (CustomUserDetails) authentication.getPrincipal();
        UUID userId = userAuthenticatedDetails.getId();

        UserEntity autenticateUserEntity = this.userService.findUserEntityById(userId);
        findDoctor.setModifiedBy(autenticateUserEntity);

        DoctorEntity doctorUpdated = this.doctorRepository.save(findDoctor);
        return DoctorResponseDTO.fromEntity(doctorUpdated);
    }

    @Transactional
    @Override
    public DoctorResponseWithUserDTO createDoctorWithUser(CreateDoctorWithUserDTO createDoctorWithUserDTO) {

        if (userRepository.findByUsername(createDoctorWithUserDTO.username()).isPresent()) {
            throw new ApplicationException(ErrorMessage.USERNAME_CONFLICT, "");
        }

        if (userRepository.findByEmail(createDoctorWithUserDTO.email()).isPresent()) {
            throw new ApplicationException(ErrorMessage.EMAIL_CONFLICT, "");
        }

        if (!UserRole.DOCTOR.equals(UserRole.DOCTOR)) {
            throw new ApplicationException(ErrorMessage.USER_NOT_DOCTOR, "");
        }

        if (doctorRepository.existsByLicenseNumber(createDoctorWithUserDTO.licenseNumber())) {
            throw new ApplicationException(ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS, createDoctorWithUserDTO.licenseNumber());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userAuthenticatedDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity autenticateUserEntity = this.userService.findUserEntityById(userAuthenticatedDetails.getId());

        UserEntity newUser = new UserEntity();
        newUser.setUsername(createDoctorWithUserDTO.username());
        newUser.setEmail(createDoctorWithUserDTO.email());
        newUser.setPasswordHash(passwordEncoder.encode(createDoctorWithUserDTO.password()));
        newUser.setRole(createDoctorWithUserDTO.role());
        newUser.setActive(createDoctorWithUserDTO.isActive());

        UserEntity userSaved = this.userRepository.save(newUser);

        DoctorEntity newDoctor = new DoctorEntity();
        newDoctor.setUser(userSaved);
        newDoctor.setModifiedBy(autenticateUserEntity);
        newDoctor.setSpecialty(createDoctorWithUserDTO.specialty());
        newDoctor.setLicenseNumber(createDoctorWithUserDTO.licenseNumber());
        newDoctor.setDefaultConsultationDuration(createDoctorWithUserDTO.defaultConsultationDuration());

        DoctorEntity doctorSaved;

        try {
            doctorSaved = doctorRepository.save(newDoctor);
        } catch (DataIntegrityViolationException ex) {
            throw new ApplicationException(
                    ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS,
                    createDoctorWithUserDTO.licenseNumber()
            );
        }

        return DoctorResponseWithUserDTO.fromEntity(newUser, newDoctor);
    }

    @Transactional
    @Override
    public DoctorResponseWithUserDTO updateDoctorWithUser(UUID userId, UpdateDoctorWithUserDTO updateDoctorWithUserDTO) {

        UserEntity findUser = userService.findUserEntityById(userId);
        DoctorEntity findDoctor = findDoctorEntityByUserId(userId);

        if (updateDoctorWithUserDTO.username() != null && !updateDoctorWithUserDTO.username().equals(findUser.getUsername())) {

            if (userRepository.findByUsername(updateDoctorWithUserDTO.username()).isPresent()) {
                throw new ApplicationException(ErrorMessage.USERNAME_CONFLICT, updateDoctorWithUserDTO.username());
            }
            findUser.setUsername(updateDoctorWithUserDTO.username());
        }

        if (updateDoctorWithUserDTO.email() != null && !updateDoctorWithUserDTO.email().equals(findUser.getEmail())) {

            if (userRepository.findByEmail(updateDoctorWithUserDTO.email()).isPresent()) {
                throw new ApplicationException(ErrorMessage.EMAIL_CONFLICT, updateDoctorWithUserDTO.email());
            }
            findUser.setEmail(updateDoctorWithUserDTO.email());
        }

        if (updateDoctorWithUserDTO.password() != null) {
            findUser.setPasswordHash(passwordEncoder.encode(updateDoctorWithUserDTO.password()));
        }

        if (updateDoctorWithUserDTO.role() != null) {
            findUser.setRole(updateDoctorWithUserDTO.role());
        }

        if (updateDoctorWithUserDTO.isActive() != null) {
            findUser.setActive(updateDoctorWithUserDTO.isActive());
        }

        if (updateDoctorWithUserDTO.specialty() != null) {
            findDoctor.setSpecialty(updateDoctorWithUserDTO.specialty());
        }

        if (updateDoctorWithUserDTO.licenseNumber() != null && !updateDoctorWithUserDTO.licenseNumber().equals(findDoctor.getLicenseNumber())) {

            if (doctorRepository.existsByLicenseNumber(updateDoctorWithUserDTO.licenseNumber())) {
                throw new ApplicationException(ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS, updateDoctorWithUserDTO.licenseNumber()
                );
            }
            findDoctor.setLicenseNumber(updateDoctorWithUserDTO.licenseNumber());
        }

        if (updateDoctorWithUserDTO.defaultConsultationDuration() != null) {
            findDoctor.setDefaultConsultationDuration(updateDoctorWithUserDTO.defaultConsultationDuration());
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userAuthenticatedDetails = (CustomUserDetails) authentication.getPrincipal();
        UserEntity autenticateUserEntity = this.userService.findUserEntityById(userAuthenticatedDetails.getId());
        findDoctor.setModifiedBy(userService.findUserEntityById(autenticateUserEntity.getId()));

        userRepository.save(findUser);

        try {
            doctorRepository.save(findDoctor);
        } catch (DataIntegrityViolationException ex) {
            throw new ApplicationException(ErrorMessage.DOCTOR_LICENSE_NUMBER_ALREADY_EXISTS, updateDoctorWithUserDTO.licenseNumber());
        }

        return DoctorResponseWithUserDTO.fromEntity(findUser, findDoctor);

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

    private DoctorEntity findDoctorEntityByUserId(UUID userId) {
        return doctorRepository.findByUserId(userId)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.DOCTOR_NOT_FOUND_USER_ID, userId));
    }
}
