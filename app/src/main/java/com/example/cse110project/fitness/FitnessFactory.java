package com.example.cse110project.fitness;

import android.app.Activity;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.cse110project.MainActivity;
import com.example.cse110project.ui.home.HomeFragment;

import java.util.HashMap;
import java.util.Map;

public class FitnessFactory {

    private static final String TAG = "FinessFactory";
    private static Map<String, BluePrint> bluePrintMap = new HashMap<>();

    public static void put(String key, BluePrint bluePrint){
        bluePrintMap.put(key, bluePrint);
    }

    public static FitnessService create(String key, HomeFragment fragment){
        Log.i(TAG, "created FitnessService: " + key);
        return bluePrintMap.get(key).create(fragment);
    }




    public interface BluePrint {
        FitnessService create(HomeFragment fragment);
    }

}
