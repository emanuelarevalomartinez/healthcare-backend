package com.healthcare.modules.doctor_schedule.enums;

import java.time.DayOfWeek;

public enum DoctorScheduleDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY;

    public static DoctorScheduleDay fromDayOfWeek(DayOfWeek dayOfWeek) {
        return DoctorScheduleDay.valueOf(dayOfWeek.name());
    }

}
