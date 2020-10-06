package com.example.cse110project;

import android.app.Notification;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private final String TAG = "MY_FIREBASE_MESSAGING_SERVICE";



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        need to implement this if you want to do something when you receive
//        a notification while app is in the foreground.


        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
//        Toast.makeText(getApplicationContext(), remoteMessage.toString(),
//                Toast.LENGTH_LONG).show();

        //////// the final comment




        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//
//                Log.i(TAG, "message had data")
//
//            } else {
//                // Handle message within 10 seconds
//
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

            Log.d(TAG,
                    "Message Notification from: " + remoteMessage.getFrom());

            // This is how to notify UI to show a notification has come in
            if (remoteMessage.getFrom().equals("/topics/UCSD")){
                Intent i = new Intent();
                i.putExtra("body", remoteMessage.getNotification().getBody());
                i.setAction("ACTION_UCSD");
                sendBroadcast(i);

            }

            if(remoteMessage.getFrom().equals("/topics/test-topics")){
                Intent i = new Intent();
                i.putExtra("data", remoteMessage.getNotification().getTitle());
                i.putExtra("topic", "test-topics");
                i.setAction("ACTION_STATUS");
                sendBroadcast(i);
            }


            if(remoteMessage.getFrom().equals("/topics/Oliver")){
                Intent i = new Intent();
                i.putExtra("body", remoteMessage.getNotification().getBody());
                i.setAction("ACTION_OLIVER");
                sendBroadcast(i);
            }

            if (remoteMessage.getFrom().equals("/topics/new-team-walk")){
                Log.i(TAG, "a new team walk was added");
                Intent i = new Intent();
                i.putExtra("topic", "walk-proposed");
                i.putExtra("data", remoteMessage.getNotification().getBody());
                i.setAction("NEW_TEAM_WALK");
                sendBroadcast(i);
            }

            if (remoteMessage.getFrom().equals("/topics/team-walk-status" +
                    "-change")){

                Intent i = new Intent();
                i.putExtra("topic", "walk-status-change");
                i.putExtra("body", remoteMessage.getNotification().getBody());
                i.putExtra("data", formatAcceptedMembersForAlert(remoteMessage));

                i.setAction("WALK_STATUS_UPDATE");
                sendBroadcast(i);

            }


        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

//        need to implement this if you want to do something when you receive a notification while app is in the foreground.


    }

    private String formatAcceptedMembersForAlert(RemoteMessage message){
        Map<String, String> data = message.getData();

        String msg = "";

        for (String name : data.keySet()){

            String val = data.get(name);

            if (val.equals("pending")){
                msg += name + " is " + val + "\n";
            } else if (val.equals("accepted") || val.equals("declined")){
                msg += name + " has " + val + "\n";
            } else {
                Log.e(TAG, "the user status was not pending, accepted or " +
                        "declined");
            }

        }

        return msg;
    }

}
