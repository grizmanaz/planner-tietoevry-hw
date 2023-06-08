package com.nd.planner.model;

import java.time.LocalDate;

public class Day {
    private LocalDate date;
    private int jobHours;
    private int thesisHours;
    private int otherBusyHours;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getJobHours() {
        return jobHours;
    }

    public void setJobHours(int jobHours) {
        this.jobHours = jobHours;
    }

    public int getThesisHours() {
        return thesisHours;
    }

    public void setThesisHours(int thesisHours) {
        this.thesisHours = thesisHours;
    }

    public int getOtherBusyHours() {
        return otherBusyHours;
    }

    public void setOtherBusyHours(int otherBusyHours) {
        this.otherBusyHours = otherBusyHours;
    }
}
