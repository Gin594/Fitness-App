package com.example.cse110project;

import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.TeamMember;
import com.example.cse110project.Models.User;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class TeamMemberTests {

    @Test
    public void ctorInitTest(){
        TeamMember member = new TeamMember("name", "test status");
        assertEquals("test status", member.getStatus());
        assertEquals("name", member.getName());
    }

    @Test
    public void setterNameTest(){
        TeamMember member = new TeamMember("name", "test status");
        member.setName("test name");
        assertEquals("test name", member.getName());
    }

    @Test
    public void setterStatusTest(){
        TeamMember member = new TeamMember("name", "test status");
        member.setName("test name");
        assertEquals("test name", member.getName());
    }

}