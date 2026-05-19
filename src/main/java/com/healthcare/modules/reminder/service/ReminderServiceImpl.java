package com.healthcare.modules.reminder.service;

import com.healthcare.modules.doctor.dto.DoctorResponseDTO;
import com.healthcare.modules.doctor.entity.DoctorEntity;
import com.healthcare.modules.reminder.dto.CreateReminderDTO;
import com.healthcare.modules.reminder.dto.ReminderResponseDTO;
import com.healthcare.modules.reminder.dto.UpdateReminderDTO;
import com.healthcare.modules.reminder.entity.ReminderEntity;
import com.healthcare.modules.reminder.repository.ReminderRepository;
import com.healthcare.modules.user.entity.UserEntity;
import com.healthcare.modules.user.service.UserService;
import com.healthcare.shared.exceptions.ApplicationException;
import com.healthcare.shared.exceptions.ErrorMessage;
import com.healthcare.shared.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ReminderServiceImpl implements ReminderService {

    private final UserService userService;
    private final ReminderRepository reminderRepository;

    public ReminderServiceImpl(UserService userService, ReminderRepository reminderRepository) {
        this.userService = userService;
        this.reminderRepository = reminderRepository;
    }

    @Override
    public ReminderResponseDTO createReminder(CreateReminderDTO createReminderDTO) {
        try {

            UserEntity userEntity = this.userService.findUserEntityById(createReminderDTO.userId());

            ReminderEntity newReminder = new ReminderEntity();
            newReminder.setUser(userEntity);
            newReminder.setType(createReminderDTO.type());
            newReminder.setMessage(createReminderDTO.message());
            newReminder.setReminderDate(createReminderDTO.reminderDate());
            newReminder.setRead(false);
            newReminder.setCreationDate(LocalDateTime.now());

            this.reminderRepository.save(newReminder);
            return ReminderResponseDTO.fromEntity(newReminder);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public ReminderResponseDTO updateReminder(UUID id, UpdateReminderDTO updateReminderDTO) {
        try {

            ReminderEntity findReminder = this.findReminderEntityById(id);

            if (updateReminderDTO.message() != null) {
                findReminder.setMessage(updateReminderDTO.message());
            }

            if (updateReminderDTO.type() != null) {
                findReminder.setType(updateReminderDTO.type());
            }

            if (updateReminderDTO.reminderDate() != null) {
                findReminder.setReminderDate(updateReminderDTO.reminderDate());
            }

            if (updateReminderDTO.isRead() != null) {
                findReminder.setRead(updateReminderDTO.isRead());
            }

            ReminderEntity reminderUpdated = this.reminderRepository.save(findReminder);
            return ReminderResponseDTO.fromEntity(reminderUpdated);

        } catch(ApplicationException ex){
            throw ex;
        } catch(Exception ex){
            throw new ApplicationException(ex);
        }
    }

    @Override
    public PageResponse<ReminderResponseDTO> findAllReminders(int page, int size) {
        try{

            Pageable pageable = PageRequest.of(page, size);
            Page<ReminderEntity> result = reminderRepository.findAllRemindersPaged(pageable);

            return new PageResponse<>(
                    result.getContent()
                            .stream()
                            .map(ReminderResponseDTO::fromEntity)
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
    public ReminderResponseDTO findReminderById(UUID id) {
        try{

            ReminderEntity findReminderById = this.reminderRepository.findById(id)
                    .orElseThrow( () -> new ApplicationException(ErrorMessage.REMINDER_NOT_FOUND_ID, "")
                    );

            return ReminderResponseDTO.fromEntity(findReminderById);

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }

    @Override
    public ReminderEntity findReminderEntityById(UUID id) {
        return this.reminderRepository.findById(id)
                .orElseThrow(() -> {
                    return new ApplicationException(ErrorMessage.REMINDER_NOT_FOUND_ID, id);
                });
    }

    @Override
    public void deleteReminder(UUID id) {
        try {
            ReminderEntity reminder = this.findReminderEntityById(id);
            reminderRepository.deleteById(reminder.getId());

        } catch (ApplicationException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ApplicationException(ex);
        }
    }
}
