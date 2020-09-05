package com.example.cse110project;

import androidx.annotation.NonNull;

import com.example.cse110project.Models.CloudUser;
import com.example.cse110project.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;

public class DatabaseUserTests {

    @Test
    public void putDataIntoDatabase() {
        CloudUser curUser = new CloudUser();

        curUser.setName("Testing Unit");
        curUser.setEmail("testing@ucsd.edu");
        curUser.setInitials("TU");
        curUser.setStatus("pending");
        curUser.setIconColor("#FF5733");

        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();

        firebaseFirestore.collection("TESTING").document("userTesting")
                .set(curUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                assertEquals(true, true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assertEquals(true, false);
            }
        });
    }

    @Test
    public void getNameFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    CloudUser user = doc.toObject(CloudUser.class);
                    assertEquals(user.getName(), "Testing Unit");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assertEquals(true, false);
            }
        });
    }

    @Test
    public void getEmailFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    CloudUser user = doc.toObject(CloudUser.class);
                    assertEquals(user.getEmail(), "testing@ucsd.edu");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assertEquals(true, false);
            }
        });
    }

    @Test
    public void getInitialsFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    CloudUser user = doc.toObject(CloudUser.class);
                    assertEquals(user.getInitials(), "TU");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assertEquals(true, false);
            }
        });
    }

    @Test
    public void getStatusFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    CloudUser user = doc.toObject(CloudUser.class);
                    assertEquals(user.getStatus(), "pending");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assertEquals(true, false);
            }
        });
    }

    @Test
    public void getIconColorFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    CloudUser user = doc.toObject(CloudUser.class);
                    assertEquals(user.getIconColor(), "#FF5733");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                assertEquals(true, false);
            }
        });
    }
}
