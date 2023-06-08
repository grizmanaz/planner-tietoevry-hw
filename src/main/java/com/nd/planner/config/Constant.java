package com.nd.planner.config;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Constant {

    public static final int MAX_AWAKE_HOURS = 16;
    public static final int PERSONAL_TIME = 8;
    public static final int MAX_WORK_HOURS = MAX_AWAKE_HOURS - PERSONAL_TIME;
    public static final Map<DayOfWeek, Integer> JOB_HOURS_PER_DAY_OF_WEEK = Map.of(
            DayOfWeek.MONDAY, 5,
            DayOfWeek.TUESDAY, 4,
            DayOfWeek.WEDNESDAY, 6,
            DayOfWeek.THURSDAY, 4,
            DayOfWeek.FRIDAY, 5,
            DayOfWeek.SATURDAY, 0,
            DayOfWeek.SUNDAY, 0
    );

    public static final List<LocalDate> HOLIDAYS = List.of(
            LocalDate.of(2023, 1, 1), // New year
            LocalDate.of(2023, 12, 25), // Christmas
            LocalDate.of(2023, 6, 11) // Job vacation day
    );
}
