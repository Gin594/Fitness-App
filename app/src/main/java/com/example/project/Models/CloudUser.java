package com.example.cse110project.Models;

public class CloudUser {

    public String email;
    public String name;
    public String status;
    public String iconColor;
    public String initials;


    public CloudUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getIconColor() {
        return iconColor;
    }

    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }

    public String getInitials(){return this.initials;}

    public void setInitials(String initials){this.initials = initials;}
}
