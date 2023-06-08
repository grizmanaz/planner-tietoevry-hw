package com.nd.planner;

import com.nd.planner.config.Constant;
import com.nd.planner.model.Day;
import com.nd.planner.model.Request;
import com.nd.planner.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    private Constant constant;

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
            if (constant.HOLIDAYS.contains(day.getDate())) {
                day.setJobHours(0);
                continue;
            }

            day.setJobHours(constant.JOB_HOURS_PER_DAY_OF_WEEK.get(day.getDate().getDayOfWeek()));

            int freeHours = constant.MAX_WORK_HOURS - day.getJobHours() - day.getOtherBusyHours();
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
