package com.example.cse110project.ui.routes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cse110project.JsonWorker.JsonParser;
import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.User;
import com.example.cse110project.NewRouteForm1;
import com.example.cse110project.R;
import com.example.cse110project.ui.ReusableUI.MyRouteAdapter;
import com.example.cse110project.ui.home.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MyRoutesFragment extends Fragment {

    private final String TAG = "MY_ROUTES_FRAGMENT";

    private RoutesViewModel routesViewModel;
    private JsonParser jp;
    public User curUser;
    private View root;
    public Context mContext;
    private ListView routes;
    final List<String> arr = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private HomeFragment homeFragment;
    private ImageButton refresh;
    private Fragment thisFragment;
    //final ArrayList<Boolean> favorites = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.routes_fragment_my_routes, null);
        FloatingActionButton newRouteButton = root.findViewById(R.id.floatingActionButton);
        newRouteButton.bringToFront();
        newRouteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewRoute();
            }
        });

        newRouteButton.bringToFront();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.refresh = view.findViewById(R.id.MyRouteRefresh);
        this.routes = view.findViewById(R.id.my_routes_view);
        Log.d(TAG , "onCreate");

    }

    @Override
    public void onStart() {
        super.onStart();
        jp = new JsonParser();
        curUser = jp.getFromSharedPreferences(getContext());
        Set<String> keySet = (curUser.getRoutes()).keySet();
        arr.clear();
        arr.addAll(keySet);

        List<String> memberInitials = new ArrayList<>();
        List<String> color = new ArrayList<>();
        RouteForm currRoute;
        for (int i=0; i<arr.size(); i++){
            memberInitials.add(String.valueOf(arr.get(i).charAt(0)));
            currRoute = curUser.getRoutes().get(arr.get(i));
            Random random = new Random();
            int random_hex = random.nextInt(0xffffff + 1);
            color.add(String.format("#%06x", random_hex));
        }
        //adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,arr);
        //routes.setAdapter(adapter);

        MyRouteAdapter mla =
                new MyRouteAdapter(mContext, arr,
                        memberInitials, color, curUser);
        routes.setAdapter(mla);

        routes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                /*Log.d("routes", "d");
                Toast.makeText(getContext(), arr.get(position), Toast.LENGTH_SHORT).show();
                Log.d("routes", "e");*/

                String title = arr.get(position);

                RouteForm currRoute = curUser.getRoutes().get(title);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(title)
                        .setMessage("Start Location: " + currRoute.getStart_loc() + "\n\n" +
                                "Favorite: " + currRoute.getFeaturesMapElement(RouteForm.FAVORITE, "") + "\n\n" +
                                "WALKED: " + currRoute.getWalked() + "\n\n" +
                                "Difficulty: " + currRoute.getFeaturesMapElement(RouteForm.DIFFICULTY, "") + "\n\n" +
                                "Trail: " + currRoute.getFeaturesMapElement(RouteForm.TRAIL, "") + "\n\n" +
                                "Direction: " + currRoute.getFeaturesMapElement(RouteForm.DIRECTION, "") + "\n\n" +
                                "Surface: " + currRoute.getFeaturesMapElement(RouteForm.SURFACE, "") + "\n\n" +
                                "Consistency: " + currRoute.getFeaturesMapElement(RouteForm.CONSISTENCY, "") + "\n\n" +
                                "Notes: \n" + currRoute.getNotes());

                builder.setNeutralButton("BACK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setNegativeButton("Start Walk", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        currRoute.setWalked(true);
                        Log.i("currRoute", currRoute.getTitle() + " " +String.valueOf(currRoute.getWalked()));
                        curUser.jp.saveToSharedPreferences(getContext(), curUser);
                        Toast.makeText(mContext, "Starts Walk from Home Tab", Toast.LENGTH_LONG).show();
                        transition();
                        homeFragment = new HomeFragment();
                        homeFragment.startWalkFromRoute(getFragment().getActivity());
                    }
                });

                builder.setPositiveButton("Propose Walk", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(mContext, "Proposes Walk to Team", Toast.LENGTH_LONG).show();

                    }
                });

                builder.create().show();

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

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public void createNewRoute(){
        Intent intent = new Intent(getActivity(), NewRouteForm1.class);
        startActivity(intent);
        resetList();
        //adapter.notifyDataSetChanged();
        //routes.invalidateViews();
        BottomNavigationView bnv = (BottomNavigationView) root.findViewById(R.id.mobile_navigation);
        //bnv.getMenu().getItem(R.id.navigation_routes).setChecked(true);
        Log.d("MyRoutesFragment", "2");
    }
    public void resetList(){
        //check for updates if floating button is used
        arr.clear();
        arr.addAll((curUser.getRoutes()).keySet());
    }

    public ArrayAdapter<String> getAdapter (){
        return adapter;
    }
    public ListView getListview (){
        return routes;
    }
    public Fragment getFragment (){
        return this;
    }

    public void transition(){
        BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}