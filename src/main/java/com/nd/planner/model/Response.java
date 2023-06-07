package com.nd.planner.model;

import java.util.List;

public class Response {
    private String message;
    private List<Day> days;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Day> getBusyDays() {
        return days;
    }

    public void setBusyDays(List<Day> days) {
        this.days = days;
    }
}
