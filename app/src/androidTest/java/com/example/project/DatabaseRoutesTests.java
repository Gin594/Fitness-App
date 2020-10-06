package com.example.cse110project;

import androidx.annotation.NonNull;

import com.example.cse110project.Models.CloudUser;
import com.example.cse110project.Models.RouteForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class DatabaseRoutesTests {
    Map<String, String> features = new HashMap<>();

    @Test
    public void putDataIntoDatabase() {

        features.put(RouteForm.DIRECTION, "loop");
        features.put(RouteForm.SURFACE, "flat");
        features.put(RouteForm.DIFFICULTY, "hard");
        features.put(RouteForm.TRAIL, "trail");
        features.put(RouteForm.CONSISTENCY, "even");
        features.put(RouteForm.FAVORITE, "true");

        RouteForm routeForm = new RouteForm();

        routeForm.setTitle("Hard Walk");
        routeForm.setStart_loc("La Jolla");
        routeForm.setFavorite(true);
        routeForm.setWalked(true);
        routeForm.setFeaturesMap(features);

        FirebaseFirestore firebaseFirestore= FirebaseFirestore.getInstance();

        firebaseFirestore.collection("TESTING").document("routesTesting")
                .set(routeForm).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void getTitleFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    RouteForm user = doc.toObject(RouteForm.class);
                    assertEquals(user.getTitle(), "Hard Walk");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) { }
        });
    }

    @Test
    public void getStartLocationFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    RouteForm user = doc.toObject(RouteForm.class);
                    assertEquals(user.getStart_loc(), "La Jolla");
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
    public void getWalkedFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    RouteForm user = doc.toObject(RouteForm.class);
                    assertTrue(user.getWalked());
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
    public void getFavoriteFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    RouteForm user = doc.toObject(RouteForm.class);
                    assertTrue(user.isFavorite());
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
    public void getFeaturesFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, String> map = new HashMap<>();
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    RouteForm user = doc.toObject(RouteForm.class);
                    assertEquals(user.getFeaturesMap(), features);
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
