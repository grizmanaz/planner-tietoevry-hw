package com.nd.planner.service;

import com.nd.planner.config.Constant;
import com.nd.planner.model.Day;
import com.nd.planner.model.Request;
import com.nd.planner.model.Response;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class StudyPlannerService {

    public Response calculateStudySchedule(Request request) {

        validateRequest(request);

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
            if (Constant.HOLIDAYS.contains(day.getDate())) {
                day.setJobHours(0);
                continue;
            }

            day.setJobHours(Constant.JOB_HOURS_PER_DAY_OF_WEEK.get(day.getDate().getDayOfWeek()));

            int freeHours = Constant.MAX_WORK_HOURS - day.getJobHours() - day.getOtherBusyHours();
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

        return response;
    }

    private void validateRequest(Request request) {
        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getDueDate();
        if(startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if(request.getThesisHoursNeeded() <= 0) {
            throw new IllegalArgumentException("Thesis hours needed must be greater than 0!");
        }

        if(request.getBusyDays() != null) {
            for(Day day : request.getBusyDays()) {
                if(day.getOtherBusyHours() > Constant.MAX_WORK_HOURS) {
                    throw new IllegalArgumentException("Busy hours in a day must not exceed " + Constant.MAX_WORK_HOURS);
                }
            }
        }
    }
}

