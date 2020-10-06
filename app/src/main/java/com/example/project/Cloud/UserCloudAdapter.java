package com.example.cse110project.Cloud;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.cse110project.FireStoreConnection.IQueryConnect;
import com.example.cse110project.Models.CloudUser;
import com.example.cse110project.Models.RouteForm;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserCloudAdapter implements ICloudCollection {

    private static final String TAG = "USER_CLOUD_COLLECTION";

    private String COLLECTION = "users";

    private FirebaseFirestore database;
    public List<CloudUser> users;


    public UserCloudAdapter(FirebaseFirestore database) {
        this.database = database;
        this.users = new ArrayList<>();
    }

    @Override
    public void get() {

        getAllUsers();

    }

    @Override
    public void add(Object o) {

        addNewUser((CloudUser) o );


    }

    @Override
    public void addListener() {

    }

    @Override
    public void removeListener() {

    }

    @Override
    public void update(Object o, Object data, String field) {

        updateCloudUserByField((String) o, (String) data, field);

    }


    private void getAllUsers() {
        CollectionReference userCol = database.collection("users");

        userCol.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot doc : task.getResult()){
                        CloudUser user = doc.toObject(CloudUser.class);
                        users.add(user);
                        Log.i(TAG, "GOT A NEW USER" + user.name);
                    }

                } else {
                    Log.e(TAG,
                            "Error getting all users " + task.getException());
                }
            }
        });
    }


    private void addNewUser(CloudUser user){
        CollectionReference userCol = database.collection("users");
        userCol.document(user.name).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i(TAG, "Added new CloudUser successfully");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "Adding new CloudUser failed");
            }
        });
    }

    private void updateCloudUserByField(String name, String data, String field){

        database.collection("users").document(name)
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

    //--------------------------------------------------------------------------
   /* public void getUser(String name, IQueryConnect queryConnect){
        DocumentSnapshot doc;
        database.collection(COLLECTION).document(name).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null) {
                                queryConnect.onUserEmailQuery((String) document.get("email"));
                            } else {
                                Log.d("ERR", "doc does not exist");
                            }
                            return;
                        } else {
                            Log.d("ERR", "" + task.getException());
                        }
                    }
                });    }*/

}
