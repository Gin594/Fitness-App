
package com.example.cse110project;


import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cse110project.JsonWorker.JsonParser;
import com.example.cse110project.Models.CloudUser;
import com.example.cse110project.Models.User;
import com.example.cse110project.Notifications.NotificationBroadcasReciever;
import com.example.cse110project.database.FireStoreSubscriptionManager;
import com.example.cse110project.database.IFireStoreSubScriptionManager;
import com.example.cse110project.ui.home.HomeFragment;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity {

    public static final String USER_STRING_LOCATION = "USERDATA";
    public static final String USER_BUNDLE_KEY = "USER_BUNDLE";
    private static final int SIGN_IN =1;
    private final String fitnessServiceKey = "GOOGLE_FIT";
    private final String TAG = "MAIN_ACTIVITY";
    private int STORAGE_PERMISSION_CODE = 1;
    public static User curUser;
    private JsonParser jp;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleApiClient;
    public boolean isWalkActive = false;
    private BroadcastReceiver broadcastReceiver;
    private CollectionReference userCollection;
    private FirebaseFirestore database;
    private String userID;
    public View menu;

    private NotificationBroadcasReciever nbr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //menu = this.findViewById(R.id.navigation);
        //menu.setVisibility(View.INVISIBLE);

        checkLogin();
        /*if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }
        }*/

        // first run app permission check
        showStartDialog();

        showHeightDialog();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_routes, R.id.navigation_demo, R.id.navigation_team)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);



        // Check if this is the app's first run
        // learned dialog code from: https://stackoverflow.com/questions/22126351/built-in-single-input-text-field-dialog-for-android


        jp = new JsonParser();
        curUser = jp.getFromSharedPreferences(getApplicationContext());
        Log.i(TAG, "USER WAS LOADED: ");


        //** This is how you can send a string from one fragment to
        // another. Activities are a little different!! **********************
        Bundle homeFragmentBundle = new Bundle();
        homeFragmentBundle.putString("service_key", fitnessServiceKey);
        homeFragmentBundle.putString(USER_BUNDLE_KEY, curUser.getUserJsonString());

        HomeFragment homeFragment = new HomeFragment();
        homeFragment.setArguments(homeFragmentBundle);
        //homeFragment.update();

        IFireStoreSubScriptionManager subManager =
                new FireStoreSubscriptionManager();

        subManager.subscribeAll();

        nbr = new NotificationBroadcasReciever();

        IntentFilter filter = new IntentFilter("WALK_STATUS_UPDATE");
        IntentFilter filter2 = new IntentFilter("NEW_TEAM_WALK");
        IntentFilter filter3 = new IntentFilter("ACTION_STATUS");
        this.registerReceiver(nbr, filter);
        this.registerReceiver(nbr, filter2);
        this.registerReceiver(nbr, filter3);
    }

    private void showHeightDialog() {
        // Check if this is the app's first run
        final SharedPreferences prefs = getSharedPreferences("PREFERENCES", MODE_PRIVATE);


        if (prefs.getBoolean("isFirstRun", true)) {
            // Dialog pop up for entering height
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Please Enter Height In Inches");
            builder.setCancelable(false);
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    prefs.edit().putInt("userHeight", Integer.parseInt(input.getText().toString())).apply();

                }
            });
            final AlertDialog dialog = builder.create();

            input.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //gets confirm button from dialogue
                    final Button btnConfirm = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    if(input.getText().length()==0) {
                        btnConfirm.setEnabled(false);
                    } else {
                        btnConfirm.setEnabled(true);
                    }
                }
            });

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        }

        prefs.edit().putBoolean("isFirstRun", false).apply();

    }

    @Override
    protected void onPause() {
        super.onPause();

        // This line causes the routes to be overwritten. Maybe we need to find a different way to
        // to save user data
        //jp.saveToSharedPreferences(getApplicationContext(), curUser);
        Log.i(TAG, "USER SAVING NOT SET UP: ");
    }

    private void showStartDialog() {
        // if permission already granted, then do nothing
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            // if permission not allowed, then request permission
            requestLocationPermission();
        }

    }

    private void requestLocationPermission() {
        // request location access permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This activity is required to access your location")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, STORAGE_PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, STORAGE_PERMISSION_CODE);

        }
    }

    // check user allowed permission or denied
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(nbr);
        //this.unregisterReceiver(broadcastReceiver);
    }

    public void setWalkActive(boolean b){
        isWalkActive = b;
    }

    public boolean getWalkActive(){
        return isWalkActive;
    }


    // set up log in system with google account
    private void checkLogin(){
        mAuth = FirebaseAuth.getInstance();
        // if user not logged in then log in use google account
        if(mAuth.getCurrentUser() == null){
            // set up log in client
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            googleApiClient = GoogleSignIn.getClient(this, gso);
            signIn();
        }
    }

    // sign in activity
    private void signIn() {
        Intent signInIntent = googleApiClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN);
    }

    // get the result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> task) {
        try{
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Toast.makeText(MainActivity.this, "Sign In Successful", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(account);
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Sign In Failed", Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(null);

        }
    }

    // add authorization with google account in order to further use
    private void FirebaseGoogleAuth(GoogleSignInAccount account) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                    updateUI();
                } else {
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }


    private void updateUI() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (account != null){
            String personName = account.getDisplayName();
            String personEmail = account.getEmail();
            Toast.makeText(MainActivity.this, personName + "  " + personEmail, Toast.LENGTH_SHORT).show();
//            addUser(personName, personEmail);
            sendUserToDatabase();
        }
        // Send the owner/user to the Database
        sendUserToDatabase();

    }

//    private void addUser(String personName, String personEmail) {
//        database = FirebaseFirestore.getInstance();
//        userCollection = database.collection("users");
//        userID = mAuth.getCurrentUser().getUid();
//        HashMap<String, String> userMap = new HashMap<>();
//        userMap.put("username", personName);
//        userMap.put("useremail", personEmail);
////        if(!userCollection.document(userID).getId().equals(userID)) {
//            userCollection.document(userID).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                @Override
//                public void onComplete(@NonNull Task<Void> task) {
//                    if (task.isSuccessful()) {
//                        Toast.makeText(MainActivity.this, "user " + personName + " added to the user list",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
////        }
//    }

    // Sends the owner of the app to the Database
    public void sendUserToDatabase() {
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.
                getLastSignedInAccount(getApplicationContext());
        String fullName = googleSignInAccount.getDisplayName();
        String name[] = fullName.split(" ");
        String firstName = name[0];
        String lastName = name[1];

        String initials = String.valueOf(firstName.charAt(0)) + String.valueOf(lastName.charAt(0));
        Log.i(TAG, "Username: " + fullName + " User initials: " + initials);

        CloudUser cloudUser = new CloudUser();
        cloudUser.setEmail(googleSignInAccount.getEmail());
        cloudUser.setName(googleSignInAccount.getDisplayName());
        cloudUser.setInitials(initials);
        cloudUser.setIconColor("#FFFF00");
        cloudUser.setStatus("accepted");
        Log.i(TAG, "CloudUser object created: " + cloudUser);


        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String userId = mAuth.getCurrentUser().getUid();
        database.collection("users").document(userId).set(cloudUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "User was added to the database sucessfully");
                    }
                });


        System.out.println(database.getApp().toString());
        SharedPreferences sharedPref = getSharedPreferences("APPUSER", Context.MODE_PRIVATE);
        sharedPref.edit().putString("USERNAME", googleSignInAccount.getDisplayName()).commit();
        sharedPref.edit().putString("DOC_ID", mAuth.getCurrentUser().getUid());
    }





    public void sendUserToDatabaseTester(Context context, CloudUser cloudUser, FirebaseFirestore firestore) {
        //CloudUser cloudUser = new CloudUser();
        cloudUser.setEmail("test@test.com");
        cloudUser.setName("test");
        cloudUser.setInitials("TT");
        cloudUser.setIconColor("#184751");
        cloudUser.setStatus("accepted");

        //database = firestore.getInstance();
        String userId = "testUID";
        database.collection("users").document(userId).set(cloudUser)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i(TAG, "User was added to the database sucessfully");
                    }
                });

        Log.d("TEST", "test method");
        SharedPreferences sharedPref = context.getSharedPreferences("APPUSER", Context.MODE_PRIVATE);
        sharedPref.edit().putString("USERNAME", "test").commit();
        sharedPref.edit().putString("DOC_ID", "testUID");
    }
}

