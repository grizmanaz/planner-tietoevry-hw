package com.nd.planner.model;

import java.time.LocalDate;

public class Day {
    private LocalDate date;
    private int busyHours;
    private int workingHours;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getBusyHours() {
        return busyHours;
    }

    public void setBusyHours(int busyHours) {
        this.busyHours = busyHours;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(int workingHours) {
        this.workingHours = workingHours;
    }
}
