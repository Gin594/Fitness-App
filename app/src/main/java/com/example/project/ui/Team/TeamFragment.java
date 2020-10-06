package com.example.cse110project.ui.Team;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cse110project.MainActivity;
import com.example.cse110project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TeamFragment extends Fragment {

    View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.fragment_team, null);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BottomNavigationView navView = root.findViewById(R.id.team_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.team_navigation_members, R.id.team_navigation_proposed_walks)
                .build();
        NavController navController = Navigation.findNavController(this.getActivity(), R.id.nav_team_fragment);
        NavigationUI.setupActionBarWithNavController((AppCompatActivity) this.getActivity(), navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
}
