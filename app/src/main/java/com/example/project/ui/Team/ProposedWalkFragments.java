package com.example.cse110project.ui.Team;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.example.cse110project.Cloud.ICloudCollection;
import com.example.cse110project.Cloud.TeamProposedWalkCloudAdapter;
import com.example.cse110project.FireStoreConnection.IQueryConnect;
import com.example.cse110project.Models.ProposedWalkCloudModel;
import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;
import java.util.List;

public class ProposedWalkFragments extends Fragment {

    ProposedWalkCloudModel proposedWalkCloudModel;
    private final String TAG = "PROPOSED_WALK_FRAGMENT";
    private ICloudCollection proposedWalkAdapter;

    private ListView walkListView;
    private List<String> routeTitles;
    private List<String> creators;
    private List<ProposedWalkCloudModel> models;

    private FloatingActionButton addProposedWalkButton;
    private View root;

    String thisAppUserEmail;
    String thisAppUserName;

    private ImageButton refresh;
    private Fragment thisFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("refresh", "onCreate");
        AccountManager manager = AccountManager.get(getContext()); // "this" references the current Context
        Account[] accounts = manager.getAccountsByType("com.google");
        this.thisAppUserEmail = accounts[0].name;
        this.thisAppUserName = getContext().getSharedPreferences("APPUSER", getContext().MODE_PRIVATE).getString("USERNAME", "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        Log.i("refresh", "onCreateView");
        this.root = inflater.inflate(R.layout.team_fragment_proposed_walks, null);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i("refresh", "onViewCreated");
        this.refresh = view.findViewById(R.id.ProposedRefresh);
        walkListView = root.findViewById(R.id.proposedWalkListView);

        routeTitles = new ArrayList<>();
        creators = new ArrayList<>();

        routeTitles.add("test");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("refresh", "onStart");
        models = new ArrayList<>();
        proposedWalkAdapter = new TeamProposedWalkCloudAdapter(getContext(),
                walkListView, models);
        proposedWalkAdapter.get();
        this.walkListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                ProposedWalkCloudModel curModel = models.get(position);
                //checkCurrentUserWithCreator(curModel, position, curModel.getCreator());
                createPopUp(curModel, curModel.getCreator().equals(thisAppUserName), position);
            }
        });
        thisFragment = this;
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisFragment.onStart();
            }
        });
    }

    /*public void checkCurrentUserWithCreator(ProposedWalkCloudModel curModel, int position, String creatorName){
        new UserCloudAdapter(FirebaseFirestore.getInstance()).getUser(creatorName, new IQueryConnect() {
            @Override
            public void onRouteQuery(RouteForm currRoute) {}

            @Override
            public void onUserEmailQuery(String email) {
                createPopUp(curModel, email == thisAppUserName, position);
            }
        });
    }*/

    public void searchMap(int position) {
        View v = walkListView.getAdapter().getView(position, null, walkListView);
        String toSearchFor = ((TextView)v.findViewById(R.id.proposedWalkCellUsername)).getText().toString();

        //reference: https://developers.google.com/maps/documentation/urls/android-intents
        // Create a Uri from an intent string. Use the result to create an Intent.
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + toSearchFor);
        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        // Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps");
        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent);
    }

    public void createPopUp(ProposedWalkCloudModel curModel, Boolean userSameAsCreator, int position){
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        String attendees = "";
        String notAttending = "";
        List<String> invitedKeys = new ArrayList<>(curModel.invitedMembers.keySet());
        Log.i("createPopUp", attendees);
        for(int i = 0; i < invitedKeys.size(); i++){
            if(curModel.invitedMembers.get(invitedKeys.get(i)).equals("accepted")){
                attendees = attendees + "\n" + invitedKeys.get(i);
                //attendees = attendees + "\n" + "test";
            }
            else if(curModel.invitedMembers.get(invitedKeys.get(i)).equals("declined")){
                notAttending = notAttending + "\n" + invitedKeys.get(i);
            }
        }

        dialog.setTitle(curModel.getTitle())
                .setMessage("Date of Event: " + curModel.getScheduledDate() +
                        "\n\n Accepted:" + attendees +
                        "\n\n Declined:" + notAttending);


            String negText = (userSameAsCreator) ? "Cancel Proposal" : "Decline";
            String neutralText =(userSameAsCreator) ? "Schedule Walk" : "Accept";


        dialog.setNegativeButton(negText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(userSameAsCreator)
                    ((TeamProposedWalkCloudAdapter) proposedWalkAdapter).deleteProposal(curModel.getTitle());
                else
                    ((TeamProposedWalkCloudAdapter) proposedWalkAdapter).updateDecline(thisAppUserName, curModel.getTitle());

            }
        });
        dialog.setNeutralButton(neutralText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (userSameAsCreator) {
                    scheduleEvent(curModel);
                }
                else {
                    ((TeamProposedWalkCloudAdapter) proposedWalkAdapter).updateAccept(thisAppUserName, curModel.getTitle());
                    proposedWalkAdapter.get();
                }
            }
        });
        dialog.setPositiveButton("search map", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                searchMap(position);
            }
        });
        dialog.create().show();

    }

    public void scheduleEvent(ProposedWalkCloudModel model){ //todo
        model.setStatus("Scheduled");
        Log.i("event", model.getStatus());
        FirebaseFirestore.getInstance().collection("planned_events/proposed/routes")
                .document(model.getTitle()).update("status", "Scheduled").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("event", "route statsus was changed");
            }
        });
    }
}
