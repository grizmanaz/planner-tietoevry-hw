package com.nd.planner;

import com.nd.planner.model.Day;
import com.nd.planner.model.Request;
import com.nd.planner.model.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
public class Controller {

    private static final int MAX_WORK_HOURS = 8;

    @PostMapping("/planner")
    public ResponseEntity<Response> calculateStudySchedule(@RequestBody Request request) {
        List<Day> days = request.getBusyDays();

        days.sort(Comparator.comparing(Day::getDate));

        for (Day day : days) {
            int freeHours = MAX_WORK_HOURS - day.getBusyHours();
            if (freeHours > 0) {
                if (request.getWorkHoursNeeded() > freeHours) {
                    day.setWorkingHours(freeHours);
                    request.setWorkHoursNeeded(request.getWorkHoursNeeded() - freeHours);
                } else {
                    day.setWorkingHours(request.getWorkHoursNeeded());
                    request.setWorkHoursNeeded(0);
                    break;
                }
            }
        }

        Response response = new Response();
        if (request.getWorkHoursNeeded() > 0) {
            response.setMessage("You will not finish your thesis on time :(");
        } else {
            response.setMessage("You will finish your thesis on time :)");
        }
        response.setBusyDays(days);

        return ResponseEntity.ok(response);
    }
}