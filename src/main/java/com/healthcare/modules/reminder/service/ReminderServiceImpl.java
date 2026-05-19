package com.healthcare.modules.reminder.service;

import com.healthcare.modules.reminder.dto.CreateReminderDTO;
import com.healthcare.modules.reminder.dto.ReminderResponseDTO;
import com.healthcare.modules.reminder.dto.UpdateReminderDTO;
import com.healthcare.modules.reminder.entity.ReminderEntity;
import com.healthcare.shared.response.PageResponse;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ReminderServiceImpl implements ReminderService {
    @Override
    public ReminderResponseDTO createReminder(CreateReminderDTO createReminderDTO) {
        return null;
    }

    @Override
    public ReminderResponseDTO updateReminder(UUID id, UpdateReminderDTO updateReminderDTO) {
        return null;
    }

    @Override
    public PageResponse<ReminderResponseDTO> findAllReminders(int page, int size) {
        return null;
    }

    @Override
    public ReminderResponseDTO findReminderById(UUID id) {
        return null;
    }

    @Override
    public ReminderEntity findReminderEntityById(UUID id) {
        return null;
    }

    @Override
    public void deleteReminder(UUID id) {

    }
}
