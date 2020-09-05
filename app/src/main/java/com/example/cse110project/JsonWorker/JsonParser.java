
package com.example.cse110project.JsonWorker;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.cse110project.MainActivity;
import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.User;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonParser {

    private static final String TAG = "JsonParser";


    public JsonParser() {
    }

    // creates json string for output
    public String getJsonString(User user){
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<User> userAdapter = moshi.adapter(User.class);
        return userAdapter.toJson(user);
    }

    public User makeUserFromJson(String json){
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<User> userAdapter = moshi.adapter(User.class);
        try {
            return userAdapter.fromJson(json);
        } catch (IOException e){
            e.printStackTrace();
            Log.d(TAG, "Couldn't convert json to user Moshi");
        }
        return null;
    }

    // make this async
    public void saveToSharedPreferences(Context context, User user){
        SharedPreferences preferences = context.getSharedPreferences(
                "PREFERENCES", Context.MODE_PRIVATE);
        String json = getJsonString(user);
        preferences.edit().putString(MainActivity.USER_STRING_LOCATION,
                json).apply();
    }

    public User getFromSharedPreferences(Context context){
        SharedPreferences preferences = context.getSharedPreferences(
                "PREFERENCES", Context.MODE_PRIVATE);
        if(preferences.contains(MainActivity.USER_STRING_LOCATION)){
            String json =
                    preferences.getString(MainActivity.USER_STRING_LOCATION,
                            "");
            return makeUserFromJson(json);
        } else {
            int userHeight = 0;
            if (preferences.contains("userHeight")){
                userHeight = preferences.getInt("userHeight", 0);
            }
            User curUser = new User();
            curUser.setHeightInches(userHeight);

            String userJson = getJsonString(curUser);
            preferences.edit().putString(MainActivity.USER_STRING_LOCATION,
                    userJson).apply();
            return curUser;
        }

    }




}

