package com.nd.planner.model;

import java.time.LocalDate;
import java.util.List;

public class Request {
    public int workHoursNeeded;
    private List<Day> days;
    private LocalDate dueDate;

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

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }
}
