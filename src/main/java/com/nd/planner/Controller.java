package com.nd.planner;

import com.nd.planner.model.Day;
import com.nd.planner.model.Request;
import com.nd.planner.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

    private static final int MAX_AWAKE_HOURS = 16;
    private static final int PERSONAL_TIME = 8;
    private static final int MAX_WORK_HOURS = MAX_AWAKE_HOURS - PERSONAL_TIME;

    private static final List<LocalDate> HOLIDAYS = Arrays.asList(
            LocalDate.of(2023, 1, 1), // New year
            LocalDate.of(2023, 12, 25), // Christmas
            LocalDate.of(2023, 6, 11) // test holiday
    );

    @PostMapping("/planner")
    public ResponseEntity<Response> calculateStudySchedule(@RequestBody Request request) {
        List<Day> days = request.getBusyDays();

        days.sort(Comparator.comparing(Day::getDate));

        LocalDate lastWorkingDay = null;
        for (Day day : days) {
            if (HOLIDAYS.contains(day.getDate())) {
                day.setWorkingHours(0);
                continue;
            }

            int freeHours = MAX_WORK_HOURS - day.getBusyHours();
            if (freeHours > 0) {
                if (request.getWorkHoursNeeded() > freeHours) {
                    day.setWorkingHours(freeHours);
                    request.setWorkHoursNeeded(request.getWorkHoursNeeded() - freeHours);
                } else {
                    day.setWorkingHours(request.getWorkHoursNeeded());
                    request.setWorkHoursNeeded(0);
                    lastWorkingDay = day.getDate();
                    break;
                }
            }
        }

        Response response = new Response();
        if (request.getWorkHoursNeeded() > 0 || lastWorkingDay.isAfter(request.getDueDate())) {
            response.setMessage("You will not finish your thesis on time :(");
        } else {
            response.setMessage("You will finish your thesis on time :)");
        }
        response.setBusyDays(days);

        return ResponseEntity.ok(response);
    }

}
