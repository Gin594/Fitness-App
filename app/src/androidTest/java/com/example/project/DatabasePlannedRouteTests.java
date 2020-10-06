package com.example.cse110project;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110project.Models.ProposedWalkCloudModel;
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

import okhttp3.Route;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class DatabasePlannedRouteTests {
    Map<String, String> features = new HashMap<>();
    Map<String, String> teamMembers = new HashMap<>();

    @Test
    public void putDataIntoDatabase() {
        RouteForm routeForm = new RouteForm();
        ProposedWalkCloudModel proposedWalkCloudModel = new ProposedWalkCloudModel();

        features.put(RouteForm.DIRECTION, "loop");
        features.put(RouteForm.SURFACE, "flat");
        features.put(RouteForm.DIFFICULTY, "hard");
        features.put(RouteForm.TRAIL, "trail");
        features.put(RouteForm.CONSISTENCY, "even");
        features.put(RouteForm.FAVORITE, "true");

        routeForm.setTitle("Hard Walk");
        routeForm.setStart_loc("La Jolla");
        routeForm.setFavorite(true);
        routeForm.setWalked(true);
        routeForm.setFeaturesMap(features);

        teamMembers.put("friend1", "Oguz Sayim");
        teamMembers.put("friend2", "Ervey Delario");
        teamMembers.put("friend3", "Yudong Zhao");
        teamMembers.put("friend4", "Oliver Thurn");

        proposedWalkCloudModel.setCreator("Testing Unit");
        proposedWalkCloudModel.setTitle("Torrey Pines");
        proposedWalkCloudModel.setScheduledDate("Mar 15 03:00 pm");
        proposedWalkCloudModel.setStatus("accepted");
        proposedWalkCloudModel.setInvitedMembers(teamMembers);
        proposedWalkCloudModel.setRoute(routeForm);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("TESTING").document("plannedRoutesTesting")
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
    public void getPlannedRouteTitleFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    ProposedWalkCloudModel user = doc.toObject(ProposedWalkCloudModel.class);
                    assertEquals(user.getTitle(), "Torrey Pines");
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
    public void getPlannedRouteCreatorFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    ProposedWalkCloudModel user = doc.toObject(ProposedWalkCloudModel.class);
                    assertEquals(user.getCreator(), "Testing Unit");
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
    public void getPlannedRouteDateFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    ProposedWalkCloudModel user = doc.toObject(ProposedWalkCloudModel.class);
                    assertEquals(user.getScheduledDate(), "Mar 15 03:00 pm");
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
    public void getPlannedRouteStatusFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    ProposedWalkCloudModel user = doc.toObject(ProposedWalkCloudModel.class);
                    assertEquals(user.getStatus(), "accepted");
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
    public void getPlannedRouteRoutesTitleFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    ProposedWalkCloudModel user = doc.toObject(ProposedWalkCloudModel.class);
                    RouteForm newRoute = user.getRoute();
                    assertEquals(newRoute.getTitle(), "Hard Walk");
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
    public void getPlannedRouteFavoriteFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    ProposedWalkCloudModel user = doc.toObject(ProposedWalkCloudModel.class);
                    RouteForm newRoute = user.getRoute();
                    assertTrue(newRoute.isFavorite());
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
    public void getPlannedRouteWalkedFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    ProposedWalkCloudModel user = doc.toObject(ProposedWalkCloudModel.class);
                    RouteForm newRoute = user.getRoute();
                    assertTrue(newRoute.getWalked());
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
    public void getPlannedRouteStartingLocationFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    ProposedWalkCloudModel user = doc.toObject(ProposedWalkCloudModel.class);
                    RouteForm newRoute = user.getRoute();
                    assertEquals(newRoute.getStart_loc(), "La Jolla");
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
    public void getPlannedRouteFeaturesFromDatabase(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("TESTING").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot doc :
                        task.getResult()){
                    ProposedWalkCloudModel user = doc.toObject(ProposedWalkCloudModel.class);
                    RouteForm newRoute = user.getRoute();
                    assertEquals(newRoute.getFeaturesMap(), features);
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
