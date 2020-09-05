
package com.example.cse110project.Models;
public class UserActivity {

    public long walkStartTime;
    public long walkEndTime;
    public long walkStepsStart;
    public long walkSteps;
    public double walkDist;

    public UserActivity() {

        this.walkStartTime = 0;
        this.walkEndTime = 0;
        this.walkSteps = 0;
        this.walkDist = 0.0;
    }

    public UserActivity(long walkStartTime, int walkSteps, double walkDist) {
        this.walkStartTime = walkStartTime;
        this.walkEndTime = 0;
        this.walkSteps = walkSteps;
        this.walkDist = walkDist;
    }

    public long getWalkStartTime() {
        return walkStartTime;
    }

    public void setWalkStartTime(long walkStartTime) {
        this.walkStartTime = walkStartTime;
    }

    public long getWalkEndTime() {
        return walkEndTime;
    }

    public void setWalkEndTime(long walkEndTime) {
        this.walkEndTime = walkEndTime;
    }

    public long getWalkSteps() {
        return walkSteps;
    }

    public void setWalkSteps(int walkSteps) {
        this.walkSteps = walkSteps;
    }

    public double getWalkDist() {
        return walkDist;
    }

    public void setWalkDist(double walkDist) {
        this.walkDist = walkDist;
    }
}
