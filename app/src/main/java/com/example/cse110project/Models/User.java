package com.example.cse110project.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cse110project.JsonWorker.JsonParser;
import com.example.cse110project.MainActivity;
import com.example.cse110project.ui.home.HomeFragment;
import com.squareup.moshi.Json;

import java.util.Map;
import java.util.TreeMap;

public class User {

    private final static String TAG = "User";
    public static final String USER_STRING_LOCATION = "USERDATA";
    public JsonParser jp;

    public int userID;
    public int heightInches;
    public long dailyStepTotal;
    public double dailyDistanceTotal;
    private int artificialSteps;
    public UserActivity activity;
    public Map<String, RouteForm> routes;

    public User(int userID, int heightInches) {
        this.userID = userID;
        this.heightInches = heightInches;
        this.dailyStepTotal = 0;
        this.dailyDistanceTotal = 0.0;
        this.artificialSteps = 0;
        this.routes = new TreeMap<>();
        this.activity = new UserActivity();
        this.jp = new JsonParser();
    }

    public User() {
        this.userID = 0;
        this.heightInches = 0;
        this.dailyStepTotal = 0;
        this.dailyDistanceTotal = 0.0;
        this.artificialSteps = 0;
        this.routes = new TreeMap<>();
        this.activity = new UserActivity();
        this.jp = new JsonParser();
    }

    public void setArtificialSteps(int steps){
        this.artificialSteps += steps;
    }

    public int getArtificialSteps(){
        return this.artificialSteps;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getHeightInches() {
        return heightInches;
    }

    public void setHeightInches(int heightInches) {
        this.heightInches = heightInches;
    }

    public UserActivity getActivity() {
        return activity;
    }

    public void setActivity(UserActivity activity) {
        this.activity = activity;
    }

    public Map<String, RouteForm> getRoutes() {
        return routes;
    }

    public void setRoutes(Map<String, RouteForm> routes) {
        this.routes = routes;
    }

    public RouteForm getRouteByName(String routeName){
        if (this.routes.containsKey(routeName)){
            return this.routes.get(routeName);
        } else {
            Log.i(TAG, "getRouteByName - invalid route name");
        }
        return null;
    }

    public void addRouteToUser(RouteForm newRoute){
        this.routes.put(newRoute.title, newRoute);

    }

    // make this async
    public void saveToSharedPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(
                "PREFERENCES", Context.MODE_PRIVATE);
        String json = getUserJsonString();
        preferences.edit().putString(MainActivity.USER_STRING_LOCATION,
                json).apply();
    }

    public String getUserJsonString(){
        return jp.getJsonString(this);
    }

}
