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

        // Validates the request for study schedule
        validateRequest(request);

        LocalDate startDate = request.getStartDate();
        LocalDate endDate = request.getDueDate();

        // Creates the list of days between start and end date considering the busy days
        List<Day> days = createDaysList(request, startDate, endDate);

        // Sorts the days in ascending order
        days.sort(Comparator.comparing(Day::getDate));

        // Calculates the job and other busy hours for each day and the possible thesis finish day
        LocalDate thesisFinishDay = calculateBusyHours(request, days);

        // Creates the response with the study schedule and the message whether the thesis will be finished on time
        return createResponse(request, days, thesisFinishDay);
    }

    private List<Day> createDaysList(Request request, LocalDate startDate, LocalDate endDate) {
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
        return days;
    }

    private LocalDate calculateBusyHours(Request request, List<Day> days) {
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
        return thesisFinishDay;
    }

    private Response createResponse(Request request, List<Day> days, LocalDate thesisFinishDay) {
        Response response = new Response();
        if (request.getThesisHoursNeeded() > 0 || (thesisFinishDay != null && thesisFinishDay.isAfter(request.getDueDate()))) {
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
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        if (request.getThesisHoursNeeded() <= 0) {
            throw new IllegalArgumentException("Thesis hours needed must be greater than 0!");
        }

        if (request.getBusyDays() != null) {
            for (Day day : request.getBusyDays()) {
                if (day.getOtherBusyHours() > Constant.MAX_WORK_HOURS) {
                    throw new IllegalArgumentException("Busy hours in a day must not exceed " + Constant.MAX_WORK_HOURS);
                }
            }
        }
    }
}
