package com.example.cse110project.Cloud;

import android.app.ListActivity;
import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.example.cse110project.FireStoreConnection.IQueryConnect;
import com.example.cse110project.Models.ProposedWalkCloudModel;
import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.SharedEvents;
import com.example.cse110project.ui.ReusableUI.TeamProposedWalkLisViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamProposedWalkCloudAdapter implements ICloudCollection{

    private final String TAG = "TEAM_MEMBERS_FRAGMENT";
    private final String COLLECTION = "planned_events/proposed/routes";
    private Context context;
    private FirebaseFirestore database;
    private ListView listView;
    private List<ProposedWalkCloudModel> models;


    public TeamProposedWalkCloudAdapter(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;
        this.database = FirebaseFirestore.getInstance();
        this.models = new ArrayList<>();
    }


    public TeamProposedWalkCloudAdapter(Context context,
                                        ListView listView,
                                        List<ProposedWalkCloudModel> models) {
        this.context = context;
        this.database = FirebaseFirestore.getInstance();
        this.listView = listView;
        this.models = models;
    }

    @Override
    public void addListener() {

    }

    @Override
    public void removeListener() {

    }

    @Override
    public void get() {

        CollectionReference colref = database.collection(COLLECTION);

        colref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                final List<String> routeTitle = new ArrayList<>();
                final List<String> creatorTitle = new ArrayList<>();
                final List<ProposedWalkCloudModel> models2 = new ArrayList<>();
                final List<String> status = new ArrayList<>();
                
                if (task.isSuccessful()){
                    
                    for (QueryDocumentSnapshot doc : task.getResult()){

                        ProposedWalkCloudModel event = doc.toObject(ProposedWalkCloudModel.class);
                        routeTitle.add(event.title);
                        status.add(event.getStatus());
                        creatorTitle.add(event.getCreator());
                        models.add(event);

                    }
                    Log.i(TAG, "Data grab was successful");
                    TeamProposedWalkLisViewAdapter adapter =
                            new TeamProposedWalkLisViewAdapter(context,
                                    routeTitle, creatorTitle, status);
                    models = models2;
                    listView.setAdapter(adapter);
                }
            }
        });

    }

    @Override
    public void add(Object o) {

        String collection = "planned_events/proposed/routes";
        ProposedWalkCloudModel newWalk = (ProposedWalkCloudModel) o;
        database.collection(collection).document(newWalk.getTitle()).set(newWalk);
        Log.i(TAG, "data was added");

    }

    @Override
    public void update(Object o, Object data, String field) {

        String memberName = (String) data;
        String collection = "planned_events/proposed/routes";
        database.collection(collection).document(field).update(
                "invitedMembers."+memberName, "accepted"
        );

    }
    //-----------------------------------------------------------------------------------------------
    public void updateAccept(String memberName, String routeName){
        String collection = "planned_events/proposed/routes";
        Log.i(TAG, "routeName =" + routeName);
        String userToUpdate = "invitedMembers." + memberName;
        database.collection(collection).document(routeName).update(
                userToUpdate, "accepted"
        );
    }
    public void updateDecline(String memberName, String routeName){

        String collection = "planned_events/proposed/routes";
        String userToUpdate = "invitedMembers." + memberName;
        database.collection(collection).document(routeName).update(
                userToUpdate, "declined"
        );
    }
    public void deleteProposal(String routeName) {
        String collection = "planned_events/proposed/routes";
        database.collection(collection).document(routeName).delete();
    }
    /*public void getInvitedMembersStatus(String routeName, String memberName, IQueryConnect queryConnect){
        DocumentSnapshot doc;
        String collection = "planned_events/proposed/routes";
        database.collection(collection).document(routeName).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                queryConnect.onInvitedMembersStatusQuery((String)document.get("invitedMembers."+memberName));
                            }
                        } else {
                            Log.d("ERR", "" + task.getException());
                        }
                    }
                });
    }*/
//-------------------------------------------------------------------------------------------------------------

    private void addEvent(SharedEvents event){

        database.collection(COLLECTION).document(event.getTitle())
                .set(event).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "New Event added");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error getting long doc" + e);
            }
        });

    }


    private void updateEventField(String name,
                                  String field,
                                  String data){
        database.collection(COLLECTION).document(name)
                .update(field, data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "User: " + name + " " + field +
                        " Changed to " + data);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Error Adding updating event " + e);
            }
        });
    }


}
