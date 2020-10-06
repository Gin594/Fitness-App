package com.example.cse110project.database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class FireStoreSubscriptionManager implements IFireStoreSubScriptionManager {

    private final String TAG = "FIRE_STORE_SUB_MANAGER";

    private final String NEW_TEAM_WALK = "/topics/new-team-walk";
    private final String MEMBER_STATUS_CHAGE = "/topics/team-walk-status" +
            "-change";
    private final String BASE_TOPIC = "/topic/";

    private final String TEST_TOPIC = "/topics/test-topics";
    private final String TEST_TOPIC2 = "/topics/UCSD";

    // need some way of showing new notifications on the UI .. like the
    // broadcast receiver ...

//    IntentFilter filter2 = new IntentFilter("ACTION_STATUS");
//    broadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String msg = intent.getStringExtra("body");
//            Toast.makeText(getApplicationContext(), "Status updated",
//                    Toast.LENGTH_LONG).show();
//        }
//    };
//
//        this.registerReceiver(broadcastReceiver, filter2);

    private FirebaseMessaging firebaseMessaging;

    public FireStoreSubscriptionManager() {

        this.firebaseMessaging = FirebaseMessaging.getInstance();
    }

    @Override
    public void subscribeAll() {

        firebaseMessaging.subscribeToTopic(NEW_TEAM_WALK)
                .addOnCompleteListener(task -> Log.i(TAG,
                        "Subscribed to: " + NEW_TEAM_WALK));

        firebaseMessaging.subscribeToTopic(MEMBER_STATUS_CHAGE)
                .addOnCompleteListener(task -> Log.i(TAG,
                        "Subscribed to: " + MEMBER_STATUS_CHAGE));

        firebaseMessaging.subscribeToTopic(TEST_TOPIC)
                .addOnCompleteListener(task -> Log.i(TAG,
                        "Subscribed to: " + TEST_TOPIC));

        firebaseMessaging.subscribeToTopic(TEST_TOPIC2)
                .addOnCompleteListener(task -> Log.i(TAG,
                        "Subscribed to: " + TEST_TOPIC2));

    }


    @Override
    public void subscribeToNewTopic(String topicName) {

        firebaseMessaging.subscribeToTopic(BASE_TOPIC + topicName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Log.i(TAG, "subscribed to a new topic" + topicName);

            }
        }).addOnFailureListener(e -> Log.i(TAG, e.toString()));


    }

    @Override
    public void unsubscribe(String topicName) {

        firebaseMessaging.unsubscribeFromTopic(BASE_TOPIC + topicName).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG, "unsubscribed from: " + topicName);
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Failed to unsubscribe from: " + topicName);
            Log.e(TAG, e.toString());
        });

    }


}
