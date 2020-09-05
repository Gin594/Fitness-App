package com.example.cse110project.Models;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class RouteForm {

    private static final String TAG = "RouteForm";

    //changed them to public so I can access them in NewRouteForm
    public static final String FAVORITE = "FAVORITE";
    public static final String DIRECTION = "DIRECTION";
    public static final String SURFACE = "SURFACE";
    public static final String DIFFICULTY = "DIFFICULTY";
    public static final String TRAIL = "TRAIL";
    public static final String CONSISTENCY = "CONSISTENCY";
    public static final String WALKED = "WALKED";

    public String title;
    public String start_loc;
    public Boolean walked = false;
    Map<String, String> featuresMap = new HashMap<>();
    boolean favorite;
    String notes;

    public RouteForm() {
    }

    public RouteForm(String notes, Boolean favorite, String title, String start_loc, Map<String, String> featuresMap) {
        this.title = title;
        this.start_loc = start_loc;
        this.featuresMap = featuresMap;
        this.favorite = favorite;
        this.notes = notes;
    }

    public RouteForm(Boolean walked, String notes, Boolean favorite, String title, String start_loc, Map<String, String> featuresMap) {
        this.walked = walked;
        this.title = title;
        this.start_loc = start_loc;
        this.featuresMap = featuresMap;
        this.favorite = favorite;
        this.notes = notes;
    }


    public RouteForm(String notes, Boolean favorite, String title, String start_loc) {
        this.title = title;
        this.start_loc = start_loc;
        this.featuresMap = null;
        this.favorite = favorite;
        this.notes = notes;
    }

//    public void setFeatureMapElement(String key, String data){
//        if (this.featuresMap.containsKey(key)){
//            this.featuresMap.put(key, data);
//        } else {
//            Log.i(TAG, "setFeatureKeyMap - key not valid");
//        }
//    }

    public String getFeaturesMapElement(String key, String data){
        if (this.featuresMap.containsKey(key)){
            return this.featuresMap.get(key);
        } else {
            Log.i(TAG, "setFeatureKeyMap - key not valid");
        }
        return null;
    }

    public void setWalked(Boolean walked){ this.walked = walked;}
    public Boolean getWalked(){ return walked;}

    public String getTitle() {
        return title;
    }

    public String getStart_loc() {
        return start_loc;
    }

    public String getNotes(){return this.notes;}

    @Override
    public String toString() {
        return "RouteForm{" +
                "title='" + title + '\'' +
                ", start_loc='" + start_loc + '\'' +
                ", featuresMap=" + featuresMap +
                '}';
    }



    ////////////////////////////
    // NEEDED FOR EASY CLOUD ACCESS

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStart_loc(String start_loc) {
        this.start_loc = start_loc;
    }

    public Map<String, String> getFeaturesMap() {
        return featuresMap;
    }

    public void setFeaturesMap(Map<String, String> featuresMap) {
        this.featuresMap = featuresMap;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}