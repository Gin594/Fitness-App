package com.example.cse110project.Models;

import java.util.HashMap;
import java.util.Map;

public class ProposedWalkCloudModel {


    public String title;
    public String status;
    public String creator;
    // issue if users have the same name
    public Map<String, String> invitedMembers;
    public RouteForm route;
    public String scheduledDate;
    public ProposedWalkCloudModel() {
    }



    public ProposedWalkCloudModel(String title,
                                  String status, String creator,
                                  RouteForm route) {
        this.title = title;
        this.status = status;
        this.creator = creator;
        this.invitedMembers = new HashMap<>();
        this.route = route;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, String> getInvitedMembers() {
        return invitedMembers;
    }

    public void setInvitedMembers(Map<String, String> invitedMembers) {
        this.invitedMembers = invitedMembers;
    }

    public RouteForm getRoute() {
        return route;
    }

    public void setRoute(RouteForm route) {
        this.route = route;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(String scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

}
