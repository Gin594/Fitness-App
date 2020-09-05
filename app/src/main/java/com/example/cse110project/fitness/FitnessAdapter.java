
package com.example.cse110project.fitness;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cse110project.MainActivity;
import com.example.cse110project.R;
import com.example.cse110project.ui.home.HomeFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptionsExtension;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Observable;

import static com.example.cse110project.ui.home.HomeFragment.buttonStateWalkActive;
import static java.security.AccessController.getContext;


public class FitnessAdapter extends Observable implements FitnessService {

    static int i = 0;

    private final String TAG = "FitnessAdapter";
    private final int GOOGLE_FIT_PERMISSIONS_REQUEST_CODE =
                                    System.identityHashCode(this) & 0xFFFF;

    private GoogleSignInAccount account;
    private HomeFragment fragment;
    static boolean oneTime = true;
    public static boolean isActive = false;

    long stepTotal = 0;
    int counter = 0;

    public FitnessAdapter(HomeFragment fragment){
        this.fragment = fragment;
    };


    @Override
    public void setup() {


        GoogleSignInOptionsExtension fitnessSignInOptions =
                FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA,
                                FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA)
                        .build();

        account = GoogleSignIn.getAccountForExtension(fragment.getContext(),
                fitnessSignInOptions);
        if(!GoogleSignIn.hasPermissions(account, fitnessSignInOptions) && oneTime){
//            GoogleSignIn.requestPermissions(activity,
//                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
//                    account,
//                    fitnessSignInOptions);
            GoogleSignIn.requestPermissions(fragment,
                    GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                    account,
                    Fitness.SCOPE_ACTIVITY_READ);
            oneTime = false;
        } else {

            //updateStepsFromGoogle();
            startRecordSession();

        }
    }

    // updates UI elements with new step counts from google or mock screen
    @Override
    public void updateStepCount(boolean isMocking) {

        if (isMocking){

            upDateMockedSteps();

        } else {

            updateStepsFromGoogle();

        }

    }



    public void updateStepsFromGoogle() {
                Task<DataSet> res = Fitness.getHistoryClient(fragment.getContext(),
                    account).readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA);

        res.addOnSuccessListener(new OnSuccessListener<DataSet>() {
            @Override
            public void onSuccess(DataSet dataSet) {
                Log.i(TAG, "DataSet: " + dataSet.toString());

                stepTotal = dataSet.isEmpty() ? 0 :
                        dataSet.getDataPoints().get(0)
                                .getValue(Field.FIELD_STEPS)
                                .asInt();
                while (counter > 0){
                    stepTotal = stepTotal + 500;
                    counter--;
                }
                // you have the step count
                // send it the activity here!
                Context c = fragment.getContext();
                Log.d("FitnessAdapter", "Context: "+c);

                double stridesPerFeet = convertDistance(c, stepTotal);
                Log.d("FitnessAdapter", "stridesPerFeet: "+stridesPerFeet);

                Double distance = (stepTotal * stridesPerFeet) / 5280;
                Log.d("FitnessAdapter", "stepTotal: "+stepTotal);
                Log.d("FitnessAdapter", "distance: "+distance);
                DecimalFormat df = new DecimalFormat("#.##");

                String dispDist = df.format(distance);
                Log.d("FitnessAdapter", "distanceString: "+dispDist);
                //Log.d(TAG, "Step total: " + stepTotal);

                fragment.setStepsTexView(String.valueOf(stepTotal));
                fragment.setDistTextView(dispDist);

                fragment.setTotalSteps((int)stepTotal);
                fragment.setTotalDist(distance);
                if(buttonStateWalkActive){
                    Log.d("buttonStateWalkActive", "true2");
                    Log.d("buttonStateWalkActive", "stepTotal: "+stepTotal);
                    Log.d("buttonStateWalkActive", "distance: "+distance);
                    fragment.curUpdate((int)stepTotal, distance);
                }
            }
        });

        res.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "couldn't get any historical data from users steps");
            }
        });
    }

    private void upDateMockedSteps(){

        long artificialSteps = fragment.curUser.getArtificialSteps();

        getStepCount();
        long artificialDailyTotal =
                fragment.curUser.dailyStepTotal + artificialSteps;

        double artificialDailyDist = convertDistance(fragment.getContext(),
                artificialDailyTotal);

        double artificialWalkDist = convertDistance(fragment.getContext(),
                artificialSteps);

        long startTime = fragment.curUser.activity.getWalkStartTime();
        long endTime = fragment.curUser.activity.getWalkEndTime();

        long totalTime = (endTime - startTime);

        DecimalFormat df = new DecimalFormat("#.##");
        String decimalDist = df.format(artificialWalkDist);
        String dailyDistString = df.format(artificialDailyDist);

        Date date = new Date(totalTime);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String time = sdf.format(date);


        fragment.setStepsTexView(String.valueOf(artificialDailyTotal));
        fragment.setDistTextView(dailyDistString);
        fragment.setCurWalkDistanceViewText(String.valueOf(decimalDist));
        fragment.setCurWalkStepsViewText(String.valueOf(artificialSteps));
        fragment.setCurWalkStepsViewText(String.valueOf(artificialSteps));
        fragment.setChronometer(time);
    }

    private void startRecordSession(){

        final Task<Void> res = Fitness.getRecordingClient(fragment.getContext(), account)
                .subscribe(DataType.TYPE_STEP_COUNT_DELTA);

        res.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG, "Results from the recording session" + res.toString());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "Couldn't get results from daily steps");
            }
        });
    }

    private double convertDistance(Context context, long stepTotal){
        double r = 0.0;

        SharedPreferences preferences = context.getSharedPreferences(
                "PREFERENCES", Context.MODE_PRIVATE);

        if (preferences.contains("userHeight")){
            int height = preferences.getInt("userHeight", 67);
            Log.i(TAG, "the height was: " + height);

            r = height * 0.413;
            // feet per stride
            r = r / 12;

            Double distance = (stepTotal * r) / 5280;

            return distance;
        } else {
            Log.i(TAG, "height was not in preferences");
        }

        return r;
    }

    @Override
    public int getRequestCode() {
        return GOOGLE_FIT_PERMISSIONS_REQUEST_CODE;
    }

    @Override
    public void finishNewWalk() {
        Log.i(TAG, "Finished a walk");

        long stepStart = fragment.curUser.activity.walkStepsStart;
        getStepCount();
        long finishSteps = fragment.curUser.dailyStepTotal;
        long totalWalkSteps = finishSteps - stepStart;
        fragment.curUser.activity.walkSteps = totalWalkSteps;
        fragment.setCurWalkDistanceViewText(String.valueOf(totalWalkSteps));
    }

    @Override
    public void updateNewWalk() {
        Log.i(TAG, "Started a walk");


        Task<DataSet> res = Fitness.getHistoryClient(fragment.getContext(),
                account).readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA);

        res.addOnSuccessListener(new OnSuccessListener<DataSet>() {
            @Override
            public void onSuccess(DataSet dataSet) {
                Log.i(TAG, "Got Data");
                long stepTotal = dataSet.isEmpty() ? 0 :
                        dataSet.getDataPoints().get(0)
                                .getValue(Field.FIELD_STEPS)
                                .asInt();

                double dist = convertDistance(fragment.getContext(),
                        stepTotal);
                fragment.updateUserDailyTotals(stepTotal, dist);
                fragment.updateUserWalkStartData(stepTotal, dist);
                fragment.setCurWalkDistanceViewText("0.0 miles");
                fragment.setCurWalkStepsViewText("0");

            }
        });

    }

    @Override
    public void getStepCount() {
        final Task<DataSet> res = Fitness.getHistoryClient(fragment.getContext(),
                account).readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA);


        res.addOnSuccessListener(new OnSuccessListener<DataSet>() {
            @Override
            public void onSuccess(DataSet dataSet) {
                Log.i(TAG, "DataSet: " + dataSet.toString());

                long stepTotal = dataSet.isEmpty() ? 0 :
                        dataSet.getDataPoints().get(0)
                                .getValue(Field.FIELD_STEPS)
                                .asInt();
                double dist = convertDistance(fragment.getContext(), stepTotal);

                fragment.updateUserDailyTotals(stepTotal, dist);
            }
        });

        res.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "couldn't get any historical data from users steps");
            }
        });

    }

    public Fragment getFragment(){
        return this.fragment;
    }
}

