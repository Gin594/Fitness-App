package com.example.cse110project.Notifications;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.cse110project.MainActivity;

public class NotificationBroadcasReciever extends BroadcastReceiver {

    private final String TAG = "NOTIFICATION_BROADCAST_RECEIVER";

    public enum NBR {
        UPDATED_STATUS, WALK_PROPOSED
    }

    public NotificationBroadcasReciever() {
        super();

    }


    // make sure to unsubscribe to any topics that you dont need

    @Override
    public void onReceive(Context context, Intent intent) {

        String data = intent.getStringExtra("data");
        String topic = intent.getStringExtra("topic");

        switch (topic){

            case "walk-status-change":
                userAcceptedNotification(context, data);
                break;
            case "walk-proposed":
                newWalkProposed(context, data);
                break;
            case "test-topics":
                walkWasCanceled(context, data);
                break;

            default:
                Log.e(TAG, "Switch case was not a recognized topic");
        }
    }


    private void userAcceptedNotification(Context context, String data){

        Log.i(TAG, "userAcceptedNotification - data = " + data);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("A Teammate changed their status to a proposed walk")
                .setMessage(data)
                .setPositiveButton("OK", null)
                .create();

        dialog.show();

    }

    private void newWalkProposed(Context context, String data){
        Log.i(TAG, "data = " + data);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("A new walk was proposed")
                .setMessage(data)
                .setPositiveButton("Accept", null)
                .setNegativeButton("Decline", null)
                .create();

        dialog.show();
    }

    private void walkWasCanceled(Context context, String data){
        Log.i(TAG, "walkCanceled - data = " + data);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("A walk was canceled")
                .setMessage(data)
                .setPositiveButton("OK", null)
                .create();

        dialog.show();
    }




}
