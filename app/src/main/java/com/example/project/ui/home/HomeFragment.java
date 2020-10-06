package com.example.cse110project.ui.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import com.example.cse110project.JsonWorker.JsonParser;
import com.example.cse110project.Models.SharedEvents;
import com.example.cse110project.Models.User;
import com.example.cse110project.NewRouteForm1;
import com.example.cse110project.R;
import com.example.cse110project.StepAndTimeMocking;
import com.example.cse110project.fitness.FitnessAdapter;
import com.example.cse110project.fitness.FitnessFactory;
import com.example.cse110project.fitness.FitnessService;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.DecimalFormat;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private final String TAG = "HomeFragment";
    private String serviceKey;
    public static boolean usingMockedUser = false;
    private static boolean startWalkFromRoute = false;

    // pass the json string through intent
    public User curUser;
    private JsonParser jp;
    private Button startWalkButton;
    private TextView stepsTexView;
    private TextView distTextView;
    private TextView curWalkDistanceView;
    private TextView curWalkStepsView;
    private TextView timer;
    private TextView walkHeader;
    private Chronometer chronometer;
    private TrackChronometer trackChronometer;

    private View menu;
    private BottomNavigationView navView;

    private boolean running;
    private long lastPause;
    static public boolean buttonStateWalkActive = false;
    static private int stepTotal = 0;
    static private double distTotal = 0;
    public int counter = 0;
    static int startSteps = 0;
    static double startDist = 0;
    static int endSteps = 0;
    static double endDist = 0;

    private Activity mActivity;

    private FitnessService fitnessService;

    SharedPreferences oldDataPref;
    private View view;

    // Constructor
    public HomeFragment() { }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity){
            mActivity = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        if (getArguments() != null) {
            serviceKey = getArguments().getString("service_key");
        }

        // This has to be here according to fragment lifecycle. Because the
        // blueprint creates this fragment in map.
        FitnessFactory.put(serviceKey, new FitnessFactory.BluePrint() {
            @Override
            public FitnessService create(HomeFragment fragment) {
                return new FitnessAdapter(fragment);
            }
        });

        ////////////////////////////////////////////
        jp = new JsonParser();
        curUser = jp.getFromSharedPreferences(getContext());
        ///////////////////////////////////////////



        oldDataPref = getActivity().getPreferences(MODE_PRIVATE);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // the FitnessFactory was bassed a valid fragment at this
        navView = getActivity().findViewById(R.id.nav_view);
        usingMockedUser = StepAndTimeMocking.usingMocking;
        Log.i(TAG, "mocked user bool " + usingMockedUser);

        if (usingMockedUser) {
            curUser = StepAndTimeMocking.mockedUser;
            jp = new JsonParser();
        } else {
            jp = new JsonParser();
            curUser = jp.getFromSharedPreferences(getContext());
            Log.i(TAG, "USER WAS LOADED: ");
        }

        Log.i(TAG, "USER was created from a file or new");
        Log.i(TAG, String.valueOf(curUser.getHeightInches()));
        fitnessService = FitnessFactory.create(serviceKey, this);
        walkHeader = view.findViewById(R.id.walk_header);
        startWalkButton = view.findViewById(R.id.homeScreenStartButton);
        stepsTexView = view.findViewById(R.id.homeScreenStepsTextView);
        distTextView = view.findViewById(R.id.homeScreenDistTextView);

        curWalkDistanceView = view.findViewById(R.id.homeScreenWalkDistView);
        curWalkStepsView = view.findViewById(R.id.homeScreenWalkStepsViews);
        chronometer = view.findViewById(R.id.homeScreenChronometer);
        timer = view.findViewById(R.id.timer_label);


        trackChronometer = new TrackChronometer();

        startWalkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageCurWalkState();

            }
        });

        if(startWalkFromRoute) {
            Log.d("ROUTE", "Starting Walk from Route Screen");
            startWalkButton.callOnClick();
        }

        fitnessService.setup();
        fitnessService.updateStepCount(usingMockedUser);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       /* homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "Home fragment start");

        if(buttonStateWalkActive) {
            Log.d("buttonStateWalkActive", "walk active");
            keepDisplay();
        } else {
            if (!usingMockedUser)
                curUpdate(0, 0);
            Log.d("buttonStateWalkActive", "walk inactive");
        }
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == fitnessService.getRequestCode()) {
                fitnessService.updateStepCount(false);
            }
        } else {
            Log.e(TAG, "ERROR, google fit result code: " + resultCode);
        }
    }

    public void setChronometer(String time) {
        chronometer.setVisibility(View.VISIBLE);
        timer.setVisibility(View.VISIBLE);
        this.chronometer.setText(time);
    }

    public void setDistTextView(String dist) {

        double total = curUser.activity.walkDist + Double.valueOf(dist);
        //curUser.activity.walkDist = 0;
        String newDist = "Dist: " + (total) + " " +
                "miles";
        distTextView.setText(newDist);
    }

    public void setStepsTexView(String steps) {
        long s = Integer.valueOf(steps) + curUser.activity.walkSteps;
        //curUser.activity.walkSteps = 0;
        String newSteps = "Steps: " + s;
        stepsTexView.setText(newSteps);
    }

    public void setCurWalkStepsViewText(String steps) {

        curWalkStepsView.setVisibility(View.VISIBLE);
        String newSteps = "Steps: " + steps;
        curWalkStepsView.setText(newSteps);

    }

    public void setCurWalkDistanceViewText(String dist) {

        curWalkDistanceView.setVisibility(View.VISIBLE);
        String newDistance = "Distance: " + dist;
        curWalkDistanceView.setText(newDistance);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    public void startChronometer() {

        if (!running) {
            // if chronometer helper method stores time is 0
            if (trackChronometer.getmStartTime() == 0) {
                trackChronometer.setmStartTime(SystemClock.elapsedRealtime());
                chronometer.setBase(SystemClock.elapsedRealtime() - lastPause);
            } else {
                // else get the time from tracked chronometer
                chronometer.setBase(trackChronometer.getmStartTime());

            }

            chronometer.start();
            running = true;
        }
    }

    public void updateUserDailyTotals(long steps, double dist) {

        curUser.dailyDistanceTotal = dist;
        curUser.dailyStepTotal = steps;
        Log.i(TAG, "Users Daily Steps: " + curUser.dailyStepTotal);

    }

    public void finishChronometer() {
        if (running) {
            chronometer.stop();
            // pause offset make more accuracy
            lastPause = SystemClock.elapsedRealtime() - chronometer.getBase();
            // when press finish button reset time tracer to 0
            trackChronometer.setmStartTime(0);
            // reset delay
            lastPause = 0;
            running = false;

        }

        if(!startWalkFromRoute) {
            // switch to route activity in order to save walk detail
            Intent intent = new Intent(getActivity(), NewRouteForm1.class);
            intent.putExtra("Walked", true);
            startActivity(intent);
        }
        else{
            BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
            navView.setSelectedItemId(R.id.navigation_routes);
        }
        startWalkFromRoute = false;
    }

    public void manageCurWalkState() {
        if (buttonStateWalkActive) {
            navView.setVisibility(View.VISIBLE);
            int green =
                    getResources().getColor(R.color.startWalkButtonGreen, null);

            buttonStateWalkActive = false;
            Log.d("buttonStateWalkActive", "set to true");
            startWalkButton.setBackgroundColor(green);
            startWalkButton.setText("START NEW WALK");
            walkHeader.setText("Last Walk");
            finishChronometer();
            //endSteps = oldDataPref.getInt("keySteps", 0);
            //endDist = oldDataPref.getFloat("keyDist", 0);

            //curWalkStepsView.setText("Steps: "+ (endSteps - startSteps));
            //curWalkDistanceView.setText("Dist: " + (endDist - startDist) + " miles");
            curUpdate(endSteps, endDist);

            curUser.activity.setWalkEndTime(SystemClock.elapsedRealtime());
            curUser.saveToSharedPreferences(getContext());

            String curUserInfo = curUser.getUserJsonString();
            Log.i(TAG, "The cur users info: " + curUserInfo);

        } else {
            navView.setVisibility(View.INVISIBLE);
            startSteps = stepTotal;
            startDist = distTotal;

            curUpdate(0, 0);
            int red =
                    getResources().getColor(R.color.finishWalkButtonRed, null);
            walkHeader.setText("Current Walk");
            startChronometer();

            //startSteps = oldDataPref.getInt("keySteps", 0);
            //startDist = oldDataPref.getFloat("keyDist", 0);

            buttonStateWalkActive = true;
            Log.d("buttonStateWalkActive", "set to true");
            curUser.activity.walkStartTime = trackChronometer.getmStartTime();
            Log.i(TAG,
                    "Users current walk start time was set to: " + String.valueOf(curUser.activity.walkStartTime));

            startWalkButton.setBackgroundColor(red);
            startWalkButton.setText("Finish Walk");
        }
    }

    public void keepDisplay() {
        int red =
                getResources().getColor(R.color.finishWalkButtonRed, null);
        startChronometer();

        curUser.activity.walkStartTime = trackChronometer.getmStartTime();
        startWalkButton.setBackgroundColor(red);
        startWalkButton.setText("Finish Walk");
    }

    public void setTotalSteps(long st) {
        this.stepTotal = (int) st;
        Log.d("setters", "st: " + st);
    }

    public void setTotalDist(double ds) {
        this.distTotal = (double) ds;
        Log.d("setters", "ds: " + ds);
    }

    public void updateUserWalkStartData(long steps, double dist) {
        curUser.activity.walkStepsStart = steps;
        curUser.activity.walkDist = dist;
    }

    public void curUpdate(int steps, double dist) {
        endSteps = steps;
        endDist = dist;
        Log.d("curUpdate", "steps: " + steps);
        if (steps - startSteps <= 0) {
            curWalkStepsView.setText("Steps: " + 0);
        } else {
            while (counter > 0) {
                steps = steps + 500;
                counter--;
            }
            Log.d("curUpdate", "steps added: " + steps);
            curWalkStepsView.setText("Steps: " + (steps - startSteps));
        }
        if (dist - startDist <= 0) {
            curWalkDistanceView.setText("Dist: " + 0.0 + " miles");
        } else {
            DecimalFormat df = new DecimalFormat("#.##");
            String dispDist = df.format(dist - startDist);
            curWalkDistanceView.setText("Dist: " + dispDist + " miles");
        }
    }

    public void startWalkFromRoute(FragmentActivity fragmentActivity) {
        startWalkFromRoute = true;
    }

    public void incCounter() {
        counter++;
    }
}