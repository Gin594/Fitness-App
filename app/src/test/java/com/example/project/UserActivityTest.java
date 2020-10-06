package com.example.cse110project;

import com.example.cse110project.Models.*;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UserActivityTest {
    @Test
    public void ctor1Test(){
        UserActivity toTest = new UserActivity();
        assertEquals(toTest.getWalkStartTime(), 0);
        assertEquals(toTest.getWalkEndTime(), 0);
        assertEquals(toTest.getWalkSteps(), 0);
        assertEquals((int)toTest.getWalkDist(), 0);
    }

    @Test
    public void ctor2Test(){
        long walkStartTime = 1234453423;
        int walkSteps = 1412215;
        double walkDist = 3.5;
        UserActivity toTest = new UserActivity(walkStartTime, walkSteps, walkDist);
        assertEquals(toTest.getWalkStartTime(), walkStartTime);
        assertEquals(toTest.getWalkEndTime(), 0);
        assertEquals(toTest.getWalkSteps(), walkSteps);
        assertEquals(toTest.getWalkDist(), walkDist, 1e-15);
    }
}
