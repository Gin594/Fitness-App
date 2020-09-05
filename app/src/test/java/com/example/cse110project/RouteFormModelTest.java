package com.example.cse110project;

import com.example.cse110project.Models.*;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RouteFormModelTest {
    @Test
    public void ctor1Test(){
        String notesTest = "notes";
        Boolean favoriteTest = true;
        String titleTest = "test title";
        String startLocTest = "start loc title";
        Map<String, String> featuresMapTest = new HashMap<>();
        RouteForm toTest = new RouteForm(notesTest, favoriteTest, titleTest,
                                                    startLocTest, featuresMapTest);
        assertEquals(toTest.getNotes(), notesTest);
        assertEquals(toTest.getStart_loc(), startLocTest);
        assertEquals(toTest.getTitle(), titleTest);
    }

    @Test
    public void ctor2Test(){
        String notesTest = "notes";
        Boolean favoriteTest = true;
        String titleTest = "test title";
        String startLocTest = "start loc title";
        RouteForm toTest = new RouteForm(notesTest, favoriteTest, titleTest, startLocTest);

        assertEquals(toTest.getNotes(), notesTest);
        assertEquals(toTest.getStart_loc(), startLocTest);
        assertEquals(toTest.getTitle(), titleTest);
    }

    @Test
    public void getFeaturesMapElementTest(){
        String notesTest = "notes";
        Boolean favoriteTest = true;
        String titleTest = "test title";
        String startLocTest = "start loc title";
        Map<String, String> featuresMapTest = new HashMap<>();
        featuresMapTest.put(RouteForm.DIRECTION, "loop");
        featuresMapTest.put(RouteForm.SURFACE, "flat");
        featuresMapTest.put(RouteForm.DIFFICULTY, "easy");
        featuresMapTest.put(RouteForm.TRAIL, "trail");
        featuresMapTest.put(RouteForm.CONSISTENCY, "even");
        RouteForm toTest = new RouteForm(notesTest, favoriteTest, titleTest,
                                                    startLocTest, featuresMapTest);


        assertEquals(toTest.getFeaturesMapElement(RouteForm.DIRECTION, ""), "loop");
        assertEquals(toTest.getFeaturesMapElement(RouteForm.SURFACE, ""), "flat");
        assertEquals(toTest.getFeaturesMapElement(RouteForm.DIFFICULTY, ""), "easy");
        assertEquals(toTest.getFeaturesMapElement(RouteForm.TRAIL, ""), "trail");
        assertEquals(toTest.getFeaturesMapElement(RouteForm.CONSISTENCY, ""), "even");
    }
}
