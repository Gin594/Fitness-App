package com.example.cse110project.Cloud;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cse110project.FireStoreConnection.IQueryConnect;
import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.SharedEvents;
import com.example.cse110project.Models.SharedRouteCloudModel;
import com.example.cse110project.R;
import com.example.cse110project.ui.ReusableUI.TeamSharedRoutesListViewAdapter;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TeamSharedRoutesCloudAdapter implements ICloudCollection {

    private final String TAG = "TEAM_SHARED_ROUTES_ADAPTER";
    private final String COLLECTION = "shared_routes";

    private Context context;
    private ListView listView;
    private FirebaseFirestore database;
    private List<SharedRouteCloudModel> routes;



    public TeamSharedRoutesCloudAdapter(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        this.database = FirebaseFirestore.getInstance();
        this.routes = new ArrayList<>();
    }

    public TeamSharedRoutesCloudAdapter(Context context, ListView listView,
                                        List<SharedRouteCloudModel> routes) {
        this.context = context;
        this.listView = listView;
        this.database = FirebaseFirestore.getInstance();
        this.routes = routes;
    }

    @Override
    public void addListener() {

        FirebaseMessaging.getInstance().subscribeToTopic(
                "testing")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(context, "A team routes button message" +
//                                " happend", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void removeListener() {

    }

    @Override
    public void get() {

        CollectionReference team = database.collection(COLLECTION);

        team.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                final List<String> routeTitles = new ArrayList<>();
                final List<String> memberInitials = new ArrayList<>();
                final List<String> color = new ArrayList<>();

                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot doc : task.getResult()){


                        SharedRouteCloudModel sharedRoutes =
                                doc.toObject(SharedRouteCloudModel.class);



                        System.out.println(sharedRoutes.getCreatorInitials());
                        routeTitles.add(sharedRoutes.getRoute().getTitle());
                        memberInitials.add(sharedRoutes.getCreatorInitials());
                        color.add(sharedRoutes.getCreatorIcon());
                        routes.add(sharedRoutes);

                    }

                    TeamSharedRoutesListViewAdapter mla =
                            new TeamSharedRoutesListViewAdapter(context, routeTitles,
                                    memberInitials, color);
                    listView.setAdapter(mla);

                } else {
                    Log.e(TAG,
                            "Error getting team member documents: " + task.getException());
                }
            }
        });

    }

    @Override
    public void add(Object o) {

        SharedRouteCloudModel event = (SharedRouteCloudModel) o;

        database.collection(COLLECTION).document().set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "added new shared event");
            }
        });



    }

    @Override
    public void update(Object o, Object data, String field) {

    }


    private void addNewRoute(RouteForm route){

        Log.i(TAG,
                "Adding Document");

        database.collection(COLLECTION).document(route.getTitle())
                .set(route).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG,
                        "Document Added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error Adding New Member: ", e);
            }
        });
    }



    private void updateRoute(String name, String data, String field){

        database.collection(COLLECTION).document(name)
                .update(field, data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "Route: " + name + " " + field +
                        " Changed to " + data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error Adding updating event " + e);
            }
        });
    }


    public void getRouteForm(String routeTitle, IQueryConnect queryConnect){
        DocumentSnapshot doc;
            database.collection(COLLECTION).document(routeTitle).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                    String notes = (String) document.get("notes");
                                    Boolean favorite = (Boolean) document.get("favorite");
                                    String startLoc = (String) document.get("start_loc");
                                    String title = (String) document.get("title");
                                    Map featuresMap = (Map) document.get("featuresMap");
                                    RouteForm currRoute = new RouteForm(notes, favorite, title, startLoc, featuresMap);
                                    queryConnect.onRouteQuery(currRoute);
                                } else {
                                    Log.d("ERR", "doc does not exist");
                                }
                                return;
                            } else {
                                Log.d("ERR", "" + task.getException());
                            }
                        }
                    });
    }

    private String randomColorGen(){
        Random random = new Random();
        int random_hex = random.nextInt(0xffffff + 1);
        return String.format("#%06x", random_hex);
    }

}
