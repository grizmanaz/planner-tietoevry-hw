package com.nd.planner.model;

import java.util.List;

public class Request {
    public int workHoursNeeded;
    private List<Day> days;

    public int getWorkHoursNeeded() {
        return workHoursNeeded;
    }

    public void setWorkHoursNeeded(int workHoursNeeded) {
        this.workHoursNeeded = workHoursNeeded;
    }

    public List<Day> getBusyDays() {
        return days;
    }

    public void setBusyDays(List<Day> days) {
        this.days = days;
    }
}
