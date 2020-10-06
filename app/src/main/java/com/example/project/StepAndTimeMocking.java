package com.example.cse110project;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cse110project.Models.User;
import com.example.cse110project.ui.home.HomeFragment;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class StepAndTimeMocking extends Fragment {

    private static Clock clock = Clock.systemDefaultZone();
    private static ZoneId zone = ZoneId.systemDefault();

    public static boolean usingMocking = false;
    public static User mockedUser;

    public HomeFragment fragment;
    public Button walkButton;
    public Button stepsButton;
    public EditText setTimeText;
    public TextView title;
    public TextView time;
    public long totalTime = 0;
    public long totalSteps = 0;
    public long startTime = 0;
    public long endTime = 0;

    // Used to keep alternating through start and end walk button on the same button
    private boolean alternate = false;
    private boolean once = true;

    @Override
    public void onCreate(@Nullable Bundle savedInstance) {
        super.onCreate(savedInstance);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstance) {
        View root =inflater.inflate(R.layout.fragment_demo, parent, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstance) {
        super.onViewCreated(view, savedInstance);

        mockedUser = new User();

        walkButton = view.findViewById(R.id.demo_walk_button);
        stepsButton = view.findViewById(R.id.demo_steps_button);
        setTimeText = view.findViewById(R.id.demo_set_time);
        title = view.findViewById(R.id.demo_title);
        time = view.findViewById(R.id.time_display);
        walkButton.setEnabled(false);

        // Set this buttons to be invisible!
        stepsButton.setVisibility(View.INVISIBLE);
        time.setVisibility(View.INVISIBLE);

        setTimeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if(setTimeText.getText().length()==0) {
                    walkButton.setEnabled(false);
                } else {
                    walkButton.setEnabled(true);
                }
            }
        });

        walkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setVisibility(View.INVISIBLE);
                usingMocking = true;
//                if(once) {
//                    fragment = new HomeFragment();
//                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.demo_fragment, fragment);
//                    //fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                    once = false;
//                }

                if(!alternate) {
                    stepsButton.setVisibility(View.VISIBLE);

                    // Grab the Start Time
                    Log.d("[test]", setTimeText.getText().toString());
                    startTime =
                            Long.parseLong(setTimeText.getText().toString());
                    mockedUser.activity.setWalkStartTime(startTime);


                    setTimeText.setText("");
                    setTimeText.setHint("Enter a Later Time in milliseconds!");

                    walkButton.setBackgroundColor(Color.RED);
                    walkButton.setText("End Demo Walk/Run");

                    alternate = true;
                } else {
                    // Going to be used for getting the time entered by the user
                    // Grab the End Time
                    Log.d("[test]", setTimeText.getText().toString());
                    endTime = Integer.parseInt(setTimeText.getText().toString());

                    if( startTime > endTime) {
                        setTimeText.setText("");
                        setTimeText.setHint("Enter a Number Greater than the Start Time!");

                        alternate = true;
                        return;

                    } else {

                        mockedUser.activity.setWalkEndTime(endTime);
                        //mockedUser.jp.saveToSharedPreferences(getContext(),this);

                        stepsButton.setVisibility(View.INVISIBLE);

                        setTimeText.setText("");
                        setTimeText.setHint("Set Time in milliseconds!");

                        walkButton.setBackgroundColor(Color.GREEN);
                        walkButton.setText("");
                        walkButton.setHint("Start Demo Walk/Run");

                        Log.d("[test]", "totalSteps taken: " + totalSteps);
//                        fragment.setStepsTexView(""+totalSteps);
                        alternate = false;
                    }

                    // Calculate the totaltime
                    totalTime = ((endTime - startTime) / 1000) / 60 ; // minutes
                    Log.d("[test]", "totalTime = " + totalTime + " minutes");
                    time.setVisibility(View.VISIBLE);
                    time.setText("Time taken: "+totalTime+" minutes");

                }
            }
        });

        stepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mockedUser.setArtificialSteps(500);
                //mockedUser.jp.saveToSharedPreferences(getContext(),mockedUser);
                    totalSteps = totalSteps + 500;
                    Log.d("[test]", "Added 500 steps!");
                    //fragment.setStepsTexView(" "+totalSteps);

            }
        });

    }

    /////////////////////////////////////////////////////////
    public static LocalDateTime now() {
        return LocalDateTime.now(getClock());
    }

    public static void useFixedClockAt(LocalDateTime date) {
        clock = Clock.fixed(date.atZone(zone).toInstant(), zone);
    }

    private static Clock getClock(){
        return clock;
    }
}
