package com.example.cse110project;

import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.SharedEvents;
import com.example.cse110project.Models.User;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class SharedEventsTests {


    @Test
    public void ctorTest(){
        SharedEvents test = new SharedEvents("testTitle", "testDate", "testRoute");
        assertEquals("testTitle", test.getTitle());
        assertEquals("testDate", test.getDate());
        assertEquals("testRoute", test.getRoute());

    }

    @Test
    public void setterTitleTest(){
        SharedEvents testEvents = new SharedEvents();
        testEvents.setTitle("title change");
        assertEquals("title change", testEvents.getTitle());
    }

    @Test
    public void setterDateTest(){
        SharedEvents testEvents = new SharedEvents();
        testEvents.setDate("new date");
        assertEquals("new date", testEvents.getDate());
    }

    @Test
    public void setterRouteTest(){
        SharedEvents testEvents = new SharedEvents();
        testEvents.setRoute("new route");
        assertEquals("new route", testEvents.getRoute());
    }

    @Test
    public void setterConfirmedTest(){
        SharedEvents testEvents = new SharedEvents();
        List<String> confirmed = new ArrayList<>();
        confirmed.add("confirmed1");
        confirmed.add("confirmed2");
        testEvents.setConfirmed(confirmed);


        assertEquals(confirmed,testEvents.getConfirmed());
    }

    @Test
    public void setterDeniedTest(){
        SharedEvents testEvents = new SharedEvents();
        List<String> denied = new ArrayList<>();
        denied.add("denied1");
        denied.add("denied2");
        testEvents.setDenied(denied);


        assertEquals(denied,testEvents.getDenied());
    }
}