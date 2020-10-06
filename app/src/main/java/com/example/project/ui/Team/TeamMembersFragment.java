package com.example.cse110project.ui.Team;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cse110project.Cloud.ICloudCollection;
import com.example.cse110project.Cloud.TeamMemberCloudAdapter;
import com.example.cse110project.Models.CloudUser;
import com.example.cse110project.R;
import com.example.cse110project.ui.ReusableUI.TeamMemberListViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class TeamMembersFragment extends Fragment {


    private final String TAG = "TEAM_MEMBERS_FRAGMENT";
    private ICloudCollection teamMemberCloudAdapter;

    private TeamMemberListViewAdapter routesListViewAdapter;
    private ListView routesListView;
    private List<CloudUser> users;
    private View root;
    private ImageButton refresh;
    private Fragment thisFragment;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        users = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        this.root = inflater.inflate(R.layout.team_fragment_members, null);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        this.refresh = view.findViewById(R.id.TeamMemberRefresh);
        routesListView = root.findViewById(R.id.team_fragment_listView);
        root.findViewById(R.id.add_team_member).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                root.findViewById(R.id.search_container).setVisibility(View.VISIBLE);
            }
        });

        root.findViewById(R.id.search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTeamMember();
            }
        });

        root.findViewById(R.id.cancel_search_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelSearch();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        teamMemberCloudAdapter = new TeamMemberCloudAdapter(routesListView,
                getContext(), users);
        teamMemberCloudAdapter.get();
        teamMemberCloudAdapter.addListener();
        thisFragment = this;
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                thisFragment.onStart();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();

        teamMemberCloudAdapter.removeListener();
    }

    public void addTeamMember(){

        Log.i(TAG, "addTeamMember");

//        final EditText nameNewTeamMemberEditText =
//                root.findViewById(R.id.name_add_team_member_edit_text);
        final EditText emailNewTeamMemberEditText =
                root.findViewById(R.id.email_add_team_member_edit_text);

        CloudUser newUser = new CloudUser();
//        newUser.setName(nameNewTeamMemberEditText.getText().toString());
        newUser.setEmail(emailNewTeamMemberEditText.getText().toString());

        this.teamMemberCloudAdapter.add(newUser);
    }



    public void cancelSearch(){
        root.findViewById(R.id.search_container).setVisibility(View.INVISIBLE);
        //teamMemberCloudAdapter = new TeamMemberCloudAdapter(routesListView, getContext());
        //teamMemberCloudAdapter.get();
    }
}
