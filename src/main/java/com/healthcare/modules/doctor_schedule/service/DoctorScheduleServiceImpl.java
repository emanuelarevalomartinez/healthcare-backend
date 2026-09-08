package com.healthcare.modules.doctor_schedule.service;

import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.doctor.service.DoctorHelperService;
import com.healthcare.modules.doctor_schedule.dto.CreateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedule.dto.DoctorScheduleResponseDTO;
import com.healthcare.modules.doctor_schedule.dto.UpdateDoctorScheduleDTO;
import com.healthcare.modules.doctor_schedule.entity.DoctorScheduleEntity;
import com.healthcare.modules.doctor_schedule.enums.DoctorScheduleDay;
import com.healthcare.modules.doctor_schedule.repository.DoctorScheduleRepository;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorHelperService doctorHelperService;
 //   private final DoctorService doctorService;

    public DoctorScheduleServiceImpl(DoctorScheduleRepository doctorScheduleRepository, DoctorHelperService doctorHelperService) {
        this.doctorScheduleRepository = doctorScheduleRepository;
        this.doctorHelperService = doctorHelperService;
    }


  /*  @Override
    public <T> List<T> createDoctorSchedules(CreateDoctorScheduleDTO createDoctorScheduleDTO, Class<T> returnType) {

        DoctorEntity doctor = this.doctorService.findDoctorEntityById(createDoctorScheduleDTO.doctorId());

        List<DoctorScheduleEntity> schedules = new ArrayList<>();
        Set<DoctorScheduleDay> days = new HashSet<>();

        for (CreateDoctorScheduleDTO.DayScheduleDTO scheduleDTO : createDoctorScheduleDTO.schedules()) {

            if (!days.add(scheduleDTO.dayOfWeek())) {
                throw new ApplicationException(ErrorMessage.DOCTOR_SCHEDULE_DUPLICATED_DAY, scheduleDTO.dayOfWeek());
            }

            boolean exists = this.doctorScheduleRepository
                    .existsByDoctorIdAndDayOfWeek(doctor.getId(), scheduleDTO.dayOfWeek());

            if (exists) {
                throw new ApplicationException(ErrorMessage.DOCTOR_SCHEDULE_ALREADY_EXISTS, scheduleDTO.dayOfWeek());
            }

            DoctorScheduleEntity newDoctorSchedule = new DoctorScheduleEntity();

            newDoctorSchedule.setDoctor(doctor);
            newDoctorSchedule.setDayOfWeek(scheduleDTO.dayOfWeek());
            newDoctorSchedule.setStartTime(scheduleDTO.startTime());
            newDoctorSchedule.setEndTime(scheduleDTO.endTime());
            newDoctorSchedule.setAvailable(scheduleDTO.available());
            newDoctorSchedule.setNotes(scheduleDTO.notes());

            schedules.add(newDoctorSchedule);
        }

        List<DoctorScheduleEntity> savedSchedules = this.doctorScheduleRepository.saveAll(schedules);

        if (returnType == DoctorScheduleEntity.class) {
            return (List<T>) savedSchedules;
        } else if (returnType == DoctorScheduleResponseDTO.class) {
            return (List<T>) savedSchedules.stream()
                    .map(DoctorScheduleResponseDTO::fromEntity)
                    .toList();
        } else {
            throw new IllegalArgumentException(
                    "Tipo de retorno no soportado: " + returnType.getName()
            );
        }
    }*/

    @Transactional
    public List<DoctorScheduleResponseDTO> createDoctorSchedulesResponseDTO(
            CreateDoctorScheduleDTO createDoctorScheduleDTO) {

        return createDoctorSchedules(
                createDoctorScheduleDTO,
                DoctorScheduleResponseDTO.class
        );
    }

    @Transactional
    public List<DoctorScheduleEntity> createDoctorSchedulesResponseEntities(
            CreateDoctorScheduleDTO createDoctorScheduleDTO) {

        return createDoctorSchedules(
                createDoctorScheduleDTO,
                DoctorScheduleEntity.class
        );
    }

    @Transactional
    public <T> List<T> createDoctorSchedules(CreateDoctorScheduleDTO createDoctorScheduleDTO, Class<T> returnType) {

        DoctorEntity doctor = this.doctorHelperService.findDoctorEntityById(
                createDoctorScheduleDTO.doctorId()
        );

        List<DoctorScheduleEntity> schedules = new ArrayList<>();
        Set<DoctorScheduleDay> days = new HashSet<>();

        for (CreateDoctorScheduleDTO.DayScheduleDTO scheduleDTO : createDoctorScheduleDTO.schedules()) {

            if (!days.add(scheduleDTO.dayOfWeek())) {
                throw new ApplicationException(
                        ErrorMessage.DOCTOR_SCHEDULE_DUPLICATED_DAY,
                        scheduleDTO.dayOfWeek()
                );
            }

            boolean exists = this.doctorScheduleRepository
                    .existsByDoctorIdAndDayOfWeek(doctor.getId(), scheduleDTO.dayOfWeek());

            if (exists) {
                throw new ApplicationException(
                        ErrorMessage.DOCTOR_SCHEDULE_ALREADY_EXISTS,
                        scheduleDTO.dayOfWeek()
                );
            }

            DoctorScheduleEntity newDoctorSchedule = new DoctorScheduleEntity();
            newDoctorSchedule.setDoctor(doctor);
            newDoctorSchedule.setDayOfWeek(scheduleDTO.dayOfWeek());
            newDoctorSchedule.setStartTime(scheduleDTO.startTime());
            newDoctorSchedule.setEndTime(scheduleDTO.endTime());
            newDoctorSchedule.setAvailable(scheduleDTO.available());
            newDoctorSchedule.setNotes(scheduleDTO.notes());

            schedules.add(newDoctorSchedule);
        }

        List<DoctorScheduleEntity> savedSchedules = this.doctorScheduleRepository.saveAll(schedules);

        if (returnType == DoctorScheduleEntity.class) {
            return (List<T>) savedSchedules;
        } else if (returnType == DoctorScheduleResponseDTO.class) {
            return (List<T>) savedSchedules.stream()
                    .map(DoctorScheduleResponseDTO::fromEntity)
                    .toList();
        } else {
            // TODO corregir mensaje de erroor
            throw new ApplicationException(ErrorMessage.DOCTOR_SCHEDULE_DUPLICATED_DAY, "ERROR DE PRUEBAAAAAAAAA");
        }
    }

    @Override
    public List<DoctorScheduleResponseDTO> updateDoctorSchedules(UpdateDoctorScheduleDTO updateDoctorScheduleDTO) {

        List<DoctorScheduleEntity> schedules = new ArrayList<>();
        Set<DoctorScheduleDay> days = new HashSet<>();

        for (UpdateDoctorScheduleDTO.DayScheduleDTO scheduleDTO : updateDoctorScheduleDTO.schedules()) {

            if (!days.add(scheduleDTO.dayOfWeek())) {
                throw new ApplicationException(ErrorMessage.DOCTOR_SCHEDULE_DUPLICATED_DAY, scheduleDTO.dayOfWeek());
            }

            DoctorScheduleEntity findSchedule = this.findDoctorScheduleEntityById(scheduleDTO.id());

            UUID doctorId = findSchedule.getDoctor().getId();

            boolean exists = this.doctorScheduleRepository
                    .existsByDoctorIdAndDayOfWeekAndIdNot(
                            doctorId,
                            scheduleDTO.dayOfWeek(),
                            scheduleDTO.id()
                    );

            if (exists) {
                throw new ApplicationException(ErrorMessage.DOCTOR_SCHEDULE_ALREADY_EXISTS, scheduleDTO.dayOfWeek());
            }

            findSchedule.setDayOfWeek(scheduleDTO.dayOfWeek());
            findSchedule.setStartTime(scheduleDTO.startTime());
            findSchedule.setEndTime(scheduleDTO.endTime());
            findSchedule.setAvailable(scheduleDTO.available());
            findSchedule.setNotes(scheduleDTO.notes());

            schedules.add(findSchedule);
        }

        List<DoctorScheduleEntity> savedSchedules = this.doctorScheduleRepository.saveAll(schedules);

        return savedSchedules.stream()
                .map(DoctorScheduleResponseDTO::fromEntity)
                .toList();
    }

    @Override
    public PageResponse<DoctorScheduleResponseDTO> findAllDoctorSchedules(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<DoctorScheduleEntity> result = doctorScheduleRepository.findAllDoctorSchedulesPaged(pageable);

        return new PageResponse<>(
                result.getContent()
                        .stream()
                        .map(DoctorScheduleResponseDTO::fromEntity)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }

    @Override
    public DoctorScheduleResponseDTO findDoctorScheduleById(UUID id) {
        DoctorScheduleEntity findDoctorScheduleById = this.doctorScheduleRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorMessage.DOCTOR_SCHEDULE_NOT_FOUND_ID, "")
                );

        return DoctorScheduleResponseDTO.fromEntity(findDoctorScheduleById);
    }

    @Override
    public DoctorScheduleEntity findDoctorScheduleEntityById(UUID id) {
        return this.doctorScheduleRepository.findById(id)
                .orElseThrow(() -> {
                    return new ApplicationException(ErrorMessage.DOCTOR_SCHEDULE_NOT_FOUND_ID, id);
                });
    }

    @Override
    public void deleteDoctorSchedule(UUID id) {
        DoctorScheduleEntity doctorSchedule = this.findDoctorScheduleEntityById(id);
        doctorScheduleRepository.deleteById(doctorSchedule.getId());
    }
}
