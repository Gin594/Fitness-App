package com.example.cse110project.ui.routes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cse110project.Cloud.ICloudCollection;
import com.example.cse110project.Cloud.TeamProposedWalkCloudAdapter;
import com.example.cse110project.Cloud.TeamSharedRoutesCloudAdapter;

import com.example.cse110project.JsonWorker.JsonParser;
import com.example.cse110project.Models.CloudUser;
import com.example.cse110project.Models.ProposedWalkCloudModel;
import com.example.cse110project.Models.SharedRouteCloudModel;
import com.example.cse110project.Models.User;
import com.example.cse110project.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.cse110project.FireStoreConnection.IQueryConnect;
import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.ui.ReusableUI.TeamSharedRoutesListViewAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeamRoutesFragment extends Fragment{


    private ICloudCollection teamRoutesAdapter;
    private static final String TAG = "TEAM_ROUTES_FRAGMENT";
    private RoutesViewModel routesViewModel;
    private JsonParser jp;
    private User curUser;
    private View root;
    public Context mContext;
    private ListView teamRoutes;
    final ArrayList<String> arr = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    private List<SharedRouteCloudModel> cloudRoutes;
    private ImageButton refresh;
    private Fragment thisFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cloudRoutes = new ArrayList<>();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.routes_fragment_team_routes, null);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.refresh = view.findViewById(R.id.TeamRouteRefresh);
        this.teamRoutes = root.findViewById(R.id.team_routes_view);
        RemoteMessage message = new RemoteMessage.Builder(
                "348790533005" + "@gcm.googleapis.com").setMessageId(Integer.toString(1))
                .addData("Key", "value").build();

        FirebaseMessaging.getInstance().send(message);

    }

    @Override
    public void onStart() {
        super.onStart();
/*        jp = new JsonParser();
        curUser = jp.getFromSharedPreferences(getContext());
        Set<String> keySet = (curUser.getRoutes()).keySet();  //TODO -- change this later to represent all routes instrad of user routes
        arr.clear();
        arr.addAll(keySet);
        //routes = (ListView) getActivity().findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(mContext,android.R.layout.simple_list_item_1,arr);
        this.teamRoutes.setAdapter(adapter);
        Log.d("MyRoutesFragment", "3");

*/
        this.teamRoutesAdapter =
                new TeamSharedRoutesCloudAdapter(getContext(), teamRoutes,
                        cloudRoutes);
        this.teamRoutesAdapter.get();
        teamRoutesAdapter.addListener();


//        MemberListViewAdapter customMemberList = new MemberListViewAdapter(mContext, initials, titles, colors);
//        this.teamRoutes.setAdapter(customMemberList);
        //List<String> titleList = ((TeamSharedRoutesListViewAdapter)teamRoutes.getAdapter()).routeNames();
        this.teamRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Log.i(TAG,
                        "Cur route at pos pressed = " + cloudRoutes.get(position).getRoute().toString());

                RouteForm routeAtPoistion =
                        cloudRoutes.get(position).getRoute();

                String routeName = ((TeamSharedRoutesListViewAdapter)teamRoutes.getAdapter()).routeNames().get(position);
                ArrayList<RouteForm> store = new ArrayList<>();

                String msgBody = new String("Start Location: " + routeAtPoistion.getStart_loc() + "\n\n" +
                        "Difficulty: " + routeAtPoistion.getFeaturesMapElement(RouteForm.DIFFICULTY, "") + "\n\n" +
                        "Trail: " + routeAtPoistion.getFeaturesMapElement(RouteForm.TRAIL, "") + "\n\n" +
                        "Direction: " + routeAtPoistion.getFeaturesMapElement(RouteForm.DIRECTION, "") + "\n\n" +
                        "Surface: " + routeAtPoistion.getFeaturesMapElement(RouteForm.SURFACE, "") + "\n\n" +
                        "Consistency: " + routeAtPoistion.getFeaturesMapElement(RouteForm.CONSISTENCY, "") + "\n\n" +
                        "Notes: \n" + routeAtPoistion.getNotes());
                routeClicked(routeName, msgBody, routeAtPoistion);

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

    public void routeClicked(String routeName, String msgBody,
                             RouteForm routeForm){
        AlertDialog dialogPop = new AlertDialog.Builder(getActivity())
                .setTitle(routeName)
                .setMessage(msgBody)
                .setPositiveButton("Propose Walk", null)
                .setNegativeButton("back", null).create();
        dialogPop.show();

        Button proposeWalk = dialogPop.getButton(AlertDialog.BUTTON_POSITIVE);
        proposeWalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proposeWalkDialog("test", routeForm);
                dialogPop.dismiss();
            }
        });

        Button back = dialogPop.getButton(AlertDialog.BUTTON_NEGATIVE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {dialogPop.dismiss();}
        });

    }

    public void proposeWalkDialog(String title, RouteForm routeForm){
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View proposeCustomDialog = inflater.inflate(R.layout.custom_propose_walk_dialog_view, null);
        AlertDialog dialogPop = new AlertDialog.Builder(getActivity())
                .setView(proposeCustomDialog)
                .setPositiveButton("send", null)
                .setNegativeButton("cancel", null).create();
        dialogPop.show();

        Button sendProposal = dialogPop.getButton(AlertDialog.BUTTON_POSITIVE);


        sendProposal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Date scheduledDate = validateTimeOfProposal(proposeCustomDialog.findViewById(R.id.datePicker),
                        ((EditText)proposeCustomDialog.findViewById(R.id.timeText)).getText().toString());

                if(scheduledDate != null){


                    Task<QuerySnapshot> teamMembers =
                            FirebaseFirestore.getInstance().collection(
                            "team_members").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                    Map<String, String> invited = new HashMap<>();

                                    for (QueryDocumentSnapshot doc :
                                            task.getResult()){
                                        CloudUser user = doc.toObject(CloudUser.class);
                                        invited.put(user.getName(), "pending");
                                    }

                                    ProposedWalkCloudModel proposedWalkCloudModel =
                                            new ProposedWalkCloudModel();

                                    proposedWalkCloudModel.setScheduledDate(scheduledDate.toString());
                                    proposedWalkCloudModel.setRoute(routeForm);
                                    proposedWalkCloudModel.setInvitedMembers(invited);
                                    proposedWalkCloudModel.setTitle(routeForm.getTitle());
                                    proposedWalkCloudModel.setCreator(GoogleSignIn.getLastSignedInAccount(getContext()).getDisplayName());
                                    proposedWalkCloudModel.setInvitedMembers(invited);
                                    proposedWalkCloudModel.setStatus("Proposed");
                                    TeamProposedWalkCloudAdapter adapter =
                                            new TeamProposedWalkCloudAdapter(getContext(),
                                                    null);

                                    adapter.add(proposedWalkCloudModel);

                                }
                            });

                    dialogPop.dismiss();
                }
                else{
                    Toast.makeText(getContext(),"invalid input date, try again",Toast.LENGTH_SHORT).show();
                }
        }
        });

        Button cancel = dialogPop.getButton(AlertDialog.BUTTON_NEGATIVE);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {dialogPop.dismiss();}
        });

    }

    public Date validateTimeOfProposal(DatePicker datepicker, String time){
        if(time.matches("((0[1-9])|(1[0-9])|2[0-4]):[0-5][0-9]")){
            String[] time2 = time.split(":");
            return new Date(datepicker.getYear() - 1900, datepicker.getMonth(), datepicker.getDayOfMonth(),
                                    Integer.parseInt(time2[0]),Integer.parseInt(time2[1]));

        }
        return null;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}
