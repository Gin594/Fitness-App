package com.example.cse110project.FireStoreConnection;

import com.example.cse110project.FireStoreConnection.IFireFunctionalities;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreConnector implements IFireFunctionalities{

    final String MEMBER_COL_KEY = "members";
    final String ROUTES_COL_KEY = "routes";


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public FirestoreConnector(){
        CollectionReference membersCollectionRef = db.collection(MEMBER_COL_KEY);
    }



    //interface functions
    public void initTeamRouteListener(){
    }


}
