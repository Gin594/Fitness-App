package com.example.cse110project.fitness;

public interface FitnessService {

    int getRequestCode();
    void setup();
    void updateStepCount(boolean isMock);
//    void updateStepCount();
    void updateNewWalk();
    void finishNewWalk();
    void getStepCount();

}