package com.example.cse110project;

import android.content.Context;

import com.example.cse110project.Models.CloudUser;
import com.example.cse110project.Models.ProposedWalkCloudModel;
import com.example.cse110project.Tester.Tester;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ProposedWalkCloudModelTests {
    @Test
    public void cTorTest() {
        ProposedWalkCloudModel walkCloudModel = new ProposedWalkCloudModel(
                "test", "test", "test", null
        );

        assertEquals("test",walkCloudModel.getTitle());
        assertEquals("test",walkCloudModel.getStatus());
        assertEquals("test",walkCloudModel.getCreator());
        assertEquals(null,walkCloudModel.getRoute());
    }

    @Test
    public void setterTitleTest(){
        ProposedWalkCloudModel walkCloudModel = new ProposedWalkCloudModel();

        walkCloudModel.setTitle("test title");
        assertEquals("test title", walkCloudModel.getTitle());

    }

    @Test
    public void setterStatusTests(){
        ProposedWalkCloudModel walkCloudModel = new ProposedWalkCloudModel();
        walkCloudModel.setCreator("test creator");
        assertEquals("test creator", walkCloudModel.getCreator());
    }

    @Test
    public void setterCreatorTests(){
        ProposedWalkCloudModel walkCloudModel = new ProposedWalkCloudModel();
        walkCloudModel.setStatus("test state");
        assertEquals("test state", walkCloudModel.getStatus());
    }

    @Test
    public void setterInvitedMembersTests(){
        ProposedWalkCloudModel walkCloudModel = new ProposedWalkCloudModel();
        Map<String, String> testMap = new HashMap<>();
        testMap.put("test1", "test1 value");
        testMap.put("test2", "test2 value");
        walkCloudModel.setInvitedMembers(testMap);
        String[] expected = {"test2", "test1"};
        assertEquals(expected, (walkCloudModel.getInvitedMembers().keySet()).toArray());
    }

    @Test
    public void setterSheduledDateTest(){
        ProposedWalkCloudModel walkCloudModel = new ProposedWalkCloudModel();
        walkCloudModel.setScheduledDate("scheduled date test");
        assertEquals("scheduled date test", walkCloudModel.getScheduledDate());
    }
}
