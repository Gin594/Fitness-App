package com.example.cse110project.Models;

import java.util.ArrayList;
import java.util.List;

public class SharedEvents {

    public String date;
    public String route;
    public String title;
    public List<String> confirmed;
    public List<String> denied;

    public SharedEvents() {
    }

    public SharedEvents(String title, String date, String route) {
        this.date = date;
        this.route = route;
        this.title = title;
        this.confirmed = new ArrayList<>();
        this.denied = new ArrayList<>();

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public List<String> getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(List<String> confirmed) {
        this.confirmed = confirmed;
    }

    public List<String> getDenied() {
        return denied;
    }

    public void setDenied(List<String> denied) {
        this.denied = denied;
    }
}

