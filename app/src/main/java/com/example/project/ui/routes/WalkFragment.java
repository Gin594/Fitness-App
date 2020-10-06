package com.example.cse110project.ui.routes;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.cse110project.R;

public class WalkFragment extends Fragment {

    private RoutesViewModel routesViewModel;

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
        return root;
    }
}