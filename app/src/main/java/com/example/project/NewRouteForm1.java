package com.example.cse110project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cse110project.Cloud.TeamProposedWalkCloudAdapter;
import com.example.cse110project.Cloud.TeamSharedRoutesCloudAdapter;
import com.example.cse110project.JsonWorker.JsonParser;
import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.SharedEvents;
import com.example.cse110project.Models.SharedRouteCloudModel;
import com.example.cse110project.Models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;

import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;

public class NewRouteForm1 extends AppCompatActivity {

    private User curUser;
    private JsonParser jp;
    private boolean enableSave;
    public boolean walked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_route_form_1);

        Intent intent = getIntent();
        walked = intent.getBooleanExtra("Walked", false);
        setUpSpinners();

        Button saveButton = findViewById(R.id.saveButton);
        enableSave = false;

        final EditText title = findViewById(R.id.routeNameRouteDetailsForm);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(title.getText().length()!=0) {
                    enableSave = true;
                }
            }
        });

        final EditText starting = findViewById(R.id.startingLocationRouteDetailsForm);
        title.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                if(starting.getText().length()==0) {
                    enableSave = true;
                }
            }
        });


        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(!enableSave) {
                    Toast.makeText(getApplicationContext(),"Route Name and Starting Location Cannot Be Empty", LENGTH_SHORT).show();
                    return;
                }

                Map<String, String> features = new HashMap<String, String>();
                features.put(RouteForm.DIRECTION, getDirection());
                features.put(RouteForm.SURFACE,getSurface());
                features.put(RouteForm.DIFFICULTY, getDifficulty());
                features.put(RouteForm.TRAIL, getTrail());
                features.put(RouteForm.CONSISTENCY, getConsistency());
                features.put(RouteForm.FAVORITE, String.valueOf(getFavorite()));

                RouteForm currentNewRoute = new RouteForm(walked, getNotes(), getFavorite(), getRouteTitle(), getStartLoc(), features);
                curUser.addRouteToUser(currentNewRoute);
                Log.i("NEWROUTEFORM", currentNewRoute.toString());
                Log.d("New Route Form", curUser.getRoutes().toString());
                  //dbManager.addRoute(getApplicationContext(),"dbConnectTest",currentNewRoute);
                curUser.saveToSharedPreferences(getApplicationContext());
                TeamSharedRoutesCloudAdapter Tc = new TeamSharedRoutesCloudAdapter(getApplicationContext(), null);
                SharedRouteCloudModel Sr = new SharedRouteCloudModel();
                Sr.setCreator(GoogleSignIn.getLastSignedInAccount(getApplicationContext()).getDisplayName());
                Sr.setCreatorIcon(Sr.randomColorGen());
                Sr.setRoute(currentNewRoute);
                Tc.add(Sr);
                // TODO: DELETE THIS JUST PROVING IT WORKS
                Log.i("Routes Screen", "after user added");




                //findViewById(R.id.navigation_routes).performClick();
/*
                Fragment fragment = new MyRoutesFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.linearLayout, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
 */
                finish();
                Log.d("MyRoutesFragment", "1");
            }
        });

        jp = new JsonParser();
        curUser = jp.getFromSharedPreferences(getApplicationContext());
    }


    public void setUpSpinners(){
        Spinner directionSpinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                        R.array.direction_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directionSpinner.setAdapter(adapter);

        Spinner surfaceSpinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.surface_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        surfaceSpinner.setAdapter(adapter2);

        Spinner difficultySpinner = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,
                R.array.difficulty_array, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter3);

        Spinner trailSpinner = findViewById(R.id.spinner4);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this,
                R.array.trail_array, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trailSpinner.setAdapter(adapter4);

        Spinner consistencySpinner = findViewById(R.id.spinner5);
        ArrayAdapter<CharSequence> adapter5 = ArrayAdapter.createFromResource(this,
                R.array.consistency_array, android.R.layout.simple_spinner_item);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        consistencySpinner.setAdapter(adapter5);
    }

    public String getRouteTitle(){
        return ((EditText) findViewById(R.id.routeNameRouteDetailsForm)).getText().toString();
    }

    public String getStartLoc(){
        return ((EditText) findViewById(R.id.startingLocationRouteDetailsForm)).getText().toString();
    }
    public Boolean getFavorite(){
        return ((Switch) findViewById(R.id.switch1)).isChecked();
    }
    public String getDirection(){
        return ((Spinner) findViewById(R.id.spinner)).getSelectedItem().toString();
    }
    public String getSurface(){
        return ((Spinner) findViewById(R.id.spinner2)).getSelectedItem().toString();
    }
    public String getDifficulty(){
        return ((Spinner) findViewById(R.id.spinner3)).getSelectedItem().toString();
    }
    public String getTrail(){
        return ((Spinner) findViewById(R.id.spinner4)).getSelectedItem().toString();
    }
    public String getConsistency(){
        return ((Spinner) findViewById(R.id.spinner5)).getSelectedItem().toString();
    }
    public String getNotes(){
        return ((EditText) findViewById(R.id.notesTextEdit)).getText().toString();
    }
}
