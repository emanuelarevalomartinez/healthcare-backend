package com.healthcare.modules.reminder.controller;

import com.healthcare.modules.doctor.dto.CreateDoctorDTO;
import com.healthcare.modules.doctor.dto.DoctorResponseDTO;
import com.healthcare.modules.doctor.dto.UpdateDoctorDTO;
import com.healthcare.modules.reminder.dto.CreateReminderDTO;
import com.healthcare.modules.reminder.dto.ReminderResponseDTO;
import com.healthcare.modules.reminder.dto.UpdateReminderDTO;
import com.healthcare.modules.reminder.service.ReminderService;
import com.healthcare.shared.response.ApiResponse;
import com.healthcare.shared.response.PageResponse;
import com.healthcare.shared.response.ResponseHandler;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ReminderResponseDTO>> createReminder(@Valid @RequestBody CreateReminderDTO createReminderDTO) {

        ReminderResponseDTO reminder = reminderService.createReminder(createReminderDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.CREATED,
                "Successfully created reminder",
                reminder
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<ApiResponse<ReminderResponseDTO>> findReminderById(@PathVariable UUID id) {

        ReminderResponseDTO reminder = this.reminderService.findReminderById(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                reminder
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReminderResponseDTO>>> findAllReminders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        PageResponse<ReminderResponseDTO> reminders = reminderService.findAllReminders(page, size);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                null,
                reminders
        );
    }

    @PutMapping("{id}")
    public ResponseEntity<ApiResponse<ReminderResponseDTO>> updateReminder(@PathVariable UUID id, @Valid @RequestBody UpdateReminderDTO updateReminderDTO) {

        ReminderResponseDTO reminderUpdate = this.reminderService.updateReminder(id, updateReminderDTO);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Reminder updated successfully",
                reminderUpdate
        );
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<Boolean>> deleteReminderById(@PathVariable UUID id) {

        this.reminderService.deleteReminder(id);

        return ResponseHandler.generateResponse(
                HttpStatus.OK,
                "Successfully delete reminder",
                null
        );
    }

}
