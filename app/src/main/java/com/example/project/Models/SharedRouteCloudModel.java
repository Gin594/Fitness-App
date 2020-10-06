package com.example.cse110project.Models;


import java.util.Random;

public class SharedRouteCloudModel {

    public String creator;
    public RouteForm route;
    public String creatorIcon;


    public SharedRouteCloudModel() {
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public RouteForm getRoute() {
        return route;
    }

    public void setRoute(RouteForm route) {
        this.route = route;
    }

    public String getCreatorIcon() {
        return creatorIcon;
    }

    public void setCreatorIcon(String creatorIcon) {
        this.creatorIcon = creatorIcon;
    }


    public String getCreatorInitials(){

        String[] initList = this.creator.split(" ");

        String firstChar = String.valueOf(initList[0].charAt(0));
        String secondChar = String.valueOf(initList[1].charAt(0));

        return firstChar + secondChar;
    }

    public String randomColorGen(){
        Random random = new Random();
        int random_hex = random.nextInt(0xffffff + 1);
        return String.format("#%06x", random_hex);
    }
}
