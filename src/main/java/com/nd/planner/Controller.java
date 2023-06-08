package com.nd.planner;

import com.nd.planner.model.Day;
import com.nd.planner.model.Request;
import com.nd.planner.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {

    private static final int MAX_AWAKE_HOURS = 16;
    private static final int PERSONAL_TIME = 8;
    private static final int MAX_WORK_HOURS = MAX_AWAKE_HOURS - PERSONAL_TIME;
    private static final Map<DayOfWeek, Integer> JOB_HOURS_PER_DAY_OF_WEEK = Map.of(
            DayOfWeek.MONDAY, 5,
            DayOfWeek.TUESDAY, 4,
            DayOfWeek.WEDNESDAY, 6,
            DayOfWeek.THURSDAY, 4,
            DayOfWeek.FRIDAY, 5,
            DayOfWeek.SATURDAY, 0,
            DayOfWeek.SUNDAY, 0
    );

    private static final List<LocalDate> HOLIDAYS = Arrays.asList(
            LocalDate.of(2023, 1, 1), // New year
            LocalDate.of(2023, 12, 25), // Christmas
            LocalDate.of(2023, 6, 11) // Job vacation day (for testing)
    );

    @PostMapping("/planner")
    public ResponseEntity<Response> calculateStudySchedule(@RequestBody Request request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getDueDate();

        Map<LocalDate, Day> dayMap = new HashMap<>();

        if (request.getBusyDays() != null) {
            for (Day busyDay : request.getBusyDays()) {
                dayMap.put(busyDay.getDate(), busyDay);
            }
        }

        List<Day> days = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            Day day = dayMap.getOrDefault(date, new Day());
            day.setDate(date);
            days.add(day);
        }

        days.sort(Comparator.comparing(Day::getDate));

        LocalDate thesisFinishDay = null;
        for (Day day : days) {
            if (HOLIDAYS.contains(day.getDate())) {
                day.setJobHours(0);
                continue;
            }

            day.setJobHours(JOB_HOURS_PER_DAY_OF_WEEK.get(day.getDate().getDayOfWeek()));

            int freeHours = MAX_WORK_HOURS - day.getJobHours() - day.getOtherBusyHours();
            if (freeHours > 0) {
                if (request.getThesisHoursNeeded() > freeHours) {
                    day.setThesisHours(freeHours);
                    request.setThesisHoursNeeded(request.getThesisHoursNeeded() - freeHours);
                } else {
                    day.setThesisHours(request.getThesisHoursNeeded());
                    request.setThesisHoursNeeded(0);
                    thesisFinishDay = day.getDate();
                    break;
                }
            }
        }

        Response response = new Response();
        if (request.getThesisHoursNeeded() > 0 || thesisFinishDay.isAfter(request.getDueDate())) {
            response.setMessage("You will not finish your thesis on time :(");
        } else {
            response.setMessage("You will finish your thesis on time :) " + "Estimated day of finish: " + thesisFinishDay);
        }
        response.setDays(days);

        return ResponseEntity.ok(response);
    }
}
