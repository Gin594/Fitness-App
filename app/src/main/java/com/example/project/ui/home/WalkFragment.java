package com.example.cse110project.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cse110project.NewRouteForm1;
import com.example.cse110project.R;
import com.example.cse110project.ui.routes.RoutesViewModel;

public class WalkFragment extends Fragment {
    private Chronometer chronometer;
    private boolean running;
    private Button btnStop;
    private long lastPause;


    private RoutesViewModel routesViewModel; // included in built in activity

    TrackChronometer trackChronometer = new TrackChronometer();

    public WalkFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        chronometer = view.findViewById(R.id.timer);

        startChronometer(view);
        btnStop = view.findViewById(R.id.finish_walk_button);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishChronometer(view);


            }
        });


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        /*routesViewModel =
                ViewModelProviders.of(this).get(RoutesViewModel.class);
        final TextView textView = root.findViewById(R.id.text_routes);
        routesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        View root = inflater.inflate(R.layout.fragment_walk, container, false);
        Button switchToWalkScreen = root.findViewById(R.id.finish_walk_button);

        switchToWalkScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewRoute();
            }
        });
        return root;
    }

    public void createNewRoute() {
        Intent intent = new Intent(getActivity(), NewRouteForm1.class);
        startActivity(intent);
    }

    public void startChronometer(View v) {


        if(!running) {
            // if chronometer helper method stores time is 0
            if (trackChronometer.getmStartTime() == 0 ) {
                trackChronometer.setmStartTime(SystemClock.elapsedRealtime());
                chronometer.setBase(SystemClock.elapsedRealtime() - lastPause);
            }else {
                // else get the time from tracked chronometer
                chronometer.setBase(trackChronometer.getmStartTime());

            }

            chronometer.start();
            running = true;
        }



    }

    public void finishChronometer(View v) {
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

        // switch to route activity in order to save walk detail
        Intent intent = new Intent(getActivity(), NewRouteForm1.class);
        startActivity(intent);
    }

}