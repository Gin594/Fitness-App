package com.example.cse110project.ui.CustomAlerts;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;


public class ScheduleWalkDialog extends Dialog {

    private ListView routesList;
    private ListView teamMembersList;

    private Button proposeWalkButton;
    private Button cancelButton;


    public ScheduleWalkDialog(Activity activity) {
        super(activity);


    }
}
