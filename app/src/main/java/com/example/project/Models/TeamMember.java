package com.example.cse110project.Models;

public class TeamMember {

    public String name;
    public String status;


    public TeamMember() {
    }

    public TeamMember(String name, String status) {
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



}
