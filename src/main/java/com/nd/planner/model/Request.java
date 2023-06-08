package com.nd.planner.model;

import java.time.LocalDate;
import java.util.List;

public class Request {
    private int thesisHoursNeeded;
    private LocalDate startDate;
    private LocalDate dueDate;
    private List<Day> busyDays;

    public int getThesisHoursNeeded() {
        return thesisHoursNeeded;
    }

    public void setThesisHoursNeeded(int thesisHoursNeeded) {
        this.thesisHoursNeeded = thesisHoursNeeded;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public List<Day> getBusyDays() {
        return busyDays;
    }

    public void setBusyDays(List<Day> busyDays) {
        this.busyDays = busyDays;
    }
}
