package com.example.cse110project;

import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.SharedEvents;
import com.example.cse110project.Models.SharedRouteCloudModel;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Route;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SharedRouteCloudModelTests {


    @Test
    public void setterCreatorTest(){
        SharedRouteCloudModel sharedRoute = new SharedRouteCloudModel();
        sharedRoute.setCreator("test creator");
        assertEquals("test creator", sharedRoute.getCreator());
    }

    @Test
    public void setterRouteTest(){
        SharedRouteCloudModel sharedRoute = new SharedRouteCloudModel();

        RouteForm route = new RouteForm();
        sharedRoute.setRoute(route);
        assertEquals(route, sharedRoute.getRoute());
    }

    @Test
    public void getCreatorInitialsTest(){
        SharedRouteCloudModel sharedRoute = new SharedRouteCloudModel();

        sharedRoute.setCreator("First Last");
        assertEquals("FL", sharedRoute.getCreatorInitials());
    }

}