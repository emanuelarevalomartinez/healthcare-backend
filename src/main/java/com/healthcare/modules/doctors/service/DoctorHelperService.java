package com.healthcare.modules.doctors.service;

import com.healthcare.modules.doctors.entity.DoctorEntity;
import com.healthcare.modules.doctors.repository.DoctorRepository;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DoctorHelperService {

    private final DoctorRepository doctorRepository;

    public DoctorHelperService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public DoctorEntity findDoctorEntityById(UUID id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.DOCTOR_NOT_FOUND_ID, id));
    }

}
