package com.healthcare.modules.reminder.service;

import com.healthcare.modules.reminder.dto.CreateReminderDTO;
import com.healthcare.modules.reminder.dto.ReminderResponseDTO;
import com.healthcare.modules.reminder.dto.UpdateReminderDTO;
import com.healthcare.modules.reminder.entity.ReminderEntity;
import com.healthcare.shared.response.PageResponse;

import java.util.UUID;

public interface ReminderService {
    ReminderResponseDTO createReminder(CreateReminderDTO createReminderDTO);
    ReminderResponseDTO updateReminder(UUID id, UpdateReminderDTO updateReminderDTO);
    PageResponse<ReminderResponseDTO> findAllReminders(int page, int size);
    ReminderResponseDTO findReminderById(UUID id);
    ReminderEntity findReminderEntityById(UUID id);
    void deleteReminder(UUID id);
}
