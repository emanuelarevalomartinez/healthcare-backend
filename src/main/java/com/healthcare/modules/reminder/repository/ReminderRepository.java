package com.healthcare.modules.reminder.repository;

import com.healthcare.modules.reminder.entity.ReminderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReminderRepository extends JpaRepository<ReminderEntity, UUID> {

    @Query("SELECT r FROM ReminderEntity r")
    Page<ReminderEntity> findAllRemindersPaged(Pageable pageable);

}
