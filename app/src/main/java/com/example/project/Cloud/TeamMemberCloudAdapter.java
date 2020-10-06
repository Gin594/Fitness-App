package com.example.cse110project.Cloud;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.cse110project.Models.CloudUser;
import com.example.cse110project.Models.TeamMember;
import com.example.cse110project.Models.User;

import com.example.cse110project.ui.ReusableUI.TeamMemberListViewAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import javax.annotation.Nullable;

public class TeamMemberCloudAdapter implements ICloudCollection{

    private static final String TAG = "TEAM_MEMBER_CLOUD_ADAPTER";
    private static final String MEMBERS = "team_members";
    private static final String MEMBER_STATUS = "status";
    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_ACCEPTED = "accepted";
    private static final String USERS = "users";
    private static final String FRIEND_REQUEST = "friend_request";
    private static final String FRIENDS = "friend";


    private List<ListenerRegistration> listeners;
    private FirebaseFirestore database;
    private ListView memberList;
    private CollectionReference membersCollection, usersCollection, friendReqestCollection, friendCollection;
    private Context context;
    private FirebaseAuth mAuth;
    private String senderId, receiveId, senderName, senderEmail, selfName, receiver_name, memberName;
    private boolean memberExist;
    private TeamMemberListViewAdapter listAdapter;

    private List<CloudUser> users;


    public TeamMemberCloudAdapter(ListView memberList,
                                  Context context) {
        this.database = FirebaseFirestore.getInstance();
        this.memberList = memberList;
        this.context = context;
        this.membersCollection = database.collection(MEMBERS);
        this.friendCollection = database.collection(FRIEND_REQUEST);
        this.usersCollection = database.collection(USERS);
        this.friendCollection = database.collection(FRIENDS);
        mAuth = FirebaseAuth.getInstance();
        senderId = mAuth.getCurrentUser().getUid();
        this.users = new ArrayList<>();

    }


    public TeamMemberCloudAdapter(ListView memberList, Context context, List<CloudUser> users) {
        this.database = FirebaseFirestore.getInstance();
        this.memberList = memberList;
        this.membersCollection = database.collection(MEMBERS);
        this.context = context;
        this.users = users;
        this.friendReqestCollection = database.collection(FRIEND_REQUEST);
        this.usersCollection = database.collection(USERS);
        this.friendCollection = database.collection(FRIENDS);
        mAuth = FirebaseAuth.getInstance();
        senderId = mAuth.getCurrentUser().getUid();
    }

    @Override
    public void get() {

        membersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                final List<String> memberNames = new ArrayList<>();
                final List<String> status = new ArrayList<>();
                final List<String> color = new ArrayList<>();
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot doc : task.getResult()) {

                        TeamMember teamMember = doc.toObject(TeamMember.class);

                        memberNames.add(teamMember.name);
                        status.add(teamMember.status);
                        color.add(randomColorGen());
                        Log.i(TAG,
                                "TeamFragment AdapterSetup data " +
                                        "retrieved");
                    }

                     listAdapter =
                            new TeamMemberListViewAdapter(context,
                                    memberNames, status, color, senderId, friendReqestCollection, membersCollection, friendCollection);
                    memberList.setAdapter(listAdapter);
                }
            }
        });
    }




    // Should be called when a new user is added... Check out
    // Models/CloudUser to see how data is shapped
    @Override
    public void add(Object o) {

        addNewUser((CloudUser) o );

    }

    @Override
    public void update(Object o, Object data, String field) {

        database.collection(MEMBERS).document((String)o).update(field,
                data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "couldnt update team member");
            }
        });

    }


    // should listen for status updates here when the user accepts the
    // notification -> this should be called in the onStart function of
    // calling class

    @Override
    public void addListener() {

    }

    @Override
    public void removeListener() {

    }

    private void addListenerHelper(String docId){

        Log.i(TAG, "addListenerHelper");

        ListenerRegistration userStatusListener =
                membersCollection.whereEqualTo("status", "pending").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {


                        if (e != null){

                            Log.e(TAG, "Error getting status pending member " +
                                    "listener" + e.toString());

                            return;
                        }

                        List<String> pending = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            if (doc.get("status") != null){


                                pending.add(doc.getString("name"));
                                Log.i(TAG,
                                        "QuerySnapShot for status " + doc.getString("name"));

                            }
                        }
                    }
                });

        this.listeners.add(userStatusListener);

    }

    private void addNewUser(CloudUser user){


        user.setIconColor(randomColorGen());
        user.setStatus(STATUS_PENDING);
        CollectionReference userRef = database.collection("users");
        String name = user.name;
        //String email = user.email;



//
//        if(!name.equals("")) {
//            // check member already in the list or not
//            membersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    for (QueryDocumentSnapshot doc : task.getResult()) {
//                        if ((doc.getId().equals(name) && Objects.equals(doc.getString("status"), "accepted"))) {
//                            memberExist = true;
//                            break;
//                        } else {
//
//                            memberExist = false;
//                        }
//                    }
//                }
//            });
//        }else {
//            usersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    for(QueryDocumentSnapshot doc : task.getResult()){
//                        if(doc.getString("email").equals(user.email)){
//                            memberName = doc.getString("name");
//                            break;
//                        }
//                    }
//
//                    membersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            for (QueryDocumentSnapshot doc : task.getResult()){
//                                if ((doc.getId().equals(memberName) && Objects.equals(doc.getString("status"), "accepted"))){
//                                    memberExist = true;
//                                    break;
//                                }else {
//                                    memberExist = false;
//                                }
//                            }
//                        }
//                    });
//                }
//            });
//        }


        // search user list and check the user whether exist, if the user exist, then add the user to member list
        usersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    //add user self to member list
                    usersCollection.document(senderId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                           if(Objects.equals(Objects.requireNonNull(documentSnapshot).getString("name"), user.name) ||
                                   Objects.equals(documentSnapshot.getString("email"), user.email)){
                               usersCollection.document(senderId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                   @Override
                                   public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                       if (Objects.requireNonNull(documentSnapshot).contains("name")) {
                                           selfName = documentSnapshot.getString("name");
                                           HashMap<String, Object> userSelfMap = new HashMap<>();
                                           userSelfMap.put("email", user.getEmail());
                                           userSelfMap.put("iconColor", user.getIconColor());
                                           userSelfMap.put("name", selfName);
                                           userSelfMap.put("status", "accepted");
                                           membersCollection.document(Objects.requireNonNull(selfName)).set(userSelfMap);
                                           Toast.makeText(context, "Add userself to the member list", Toast.LENGTH_SHORT).show();
                                       }
                                   }
                               });
                           }

                        }

                    });

                    for (QueryDocumentSnapshot doc : Objects.requireNonNull(task.getResult())) {

                            if (Objects.equals(doc.getString("name"), name) || Objects.equals(doc.getString("email"), user.email)) {
                                receiveId = doc.getId();
                                receiver_name = doc.getString("name");
                               break;

                            }
                        }
                membersCollection.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            if (doc.getId().equals(receiver_name) && Objects.requireNonNull(doc.getString("status")).equals("accepted")) {
                                Toast.makeText(context, "member already exist", Toast.LENGTH_SHORT).show();
                                memberExist = true;
                                break;
                            }else{
                                memberExist = false;
                            }
                        }
                        if(!memberExist) {
                            usersCollection.document(senderId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                                    senderName = Objects.requireNonNull(documentSnapshot).getString("name");
                                    senderEmail = documentSnapshot.getString("email");

                                    if(!Objects.equals(senderEmail, user.email)){
                                        user.setName(receiver_name);
                                        membersCollection.document(receiver_name).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.i(TAG, "Added new CloudUser successfully");
                                                String toastText = "Sent Team Invite to: " + receiver_name;
                                                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
                                                sendRequest(receiver_name);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i(TAG, "Adding new CloudUser failed");
                                                String toastText = "Invite to: " + receiver_name + "Failed";
                                                Toast.makeText(context, toastText, Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        usersCollection.document(receiveId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {

                                            }
                                        });

                                    }

                                }
                            });
                        }

                    }
                });


            }
        });

        //listAdapter.notifyDataSetChanged();
    }


    private String randomColorGen(){
        Random random = new Random();
        int random_hex = random.nextInt(0xffffff + 1);
        return String.format("#%06x", random_hex);
    }


    // enter user name and send request to that user
    private void sendRequest(String receiverName){
        usersCollection.document(senderId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@androidx.annotation.Nullable DocumentSnapshot documentSnapshot, @androidx.annotation.Nullable FirebaseFirestoreException e) {
                senderName = Objects.requireNonNull(documentSnapshot).getString("name");
                // set the collection and documents
                // record sender and receiver information under corresponding user ID
                CollectionReference firendRef = database.collection(FRIEND_REQUEST);
                HashMap<String, Object> senderMap = new HashMap<>();
                senderMap.put("request_type", "sent");
                senderMap.put("receiver", receiverName);
                senderMap.put("sender", senderName);


                if(!receiverName.equals(senderName)) {
                    firendRef.document(senderId).set(senderMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        HashMap<String, Object> receiveMap = new HashMap<>();
                                        receiveMap.put("request_type", "received");
                                        receiveMap.put("sender", senderName);
                                        receiveMap.put("receiver", receiverName);
                                        firendRef.document(receiveId).set(receiveMap)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            //do something
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                }
            }
        });
    }
}
