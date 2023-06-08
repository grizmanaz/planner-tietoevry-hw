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

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
    }
}
