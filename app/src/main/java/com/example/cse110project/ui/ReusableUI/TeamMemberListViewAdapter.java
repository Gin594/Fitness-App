package com.example.cse110project.ui.ReusableUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.ComponentActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cse110project.Cloud.TeamMemberCloudAdapter;
import com.example.cse110project.MainActivity;
import com.example.cse110project.Models.CloudUser;
import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.SharedRouteCloudModel;
import com.example.cse110project.Models.User;
import com.example.cse110project.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TeamMemberListViewAdapter extends BaseAdapter{

    private final String TAG = "TEAM_ROUTES_LIST_VIEW_ADAPTER";
    private final String FRIENDS = "friends";
    private FirebaseAuth mAuth;
    private FirebaseFirestore database;
    private List<String> memberNames, memberInitials, status, color;
    private Context context;
    private String userId, receiverName, sender;
    private CollectionReference frienRequestdCollection, memberCollection, friendCollection;
    private boolean firstClick;
    private TeamMemberCloudAdapter adapter;



    public TeamMemberListViewAdapter(Context context,
                                     List<String> memberNames,
                                     List<String> status,
                                     List<String> color,
                                     String userId,
                                     CollectionReference requestCollection,
                                     CollectionReference memberCollection,
                                     CollectionReference friendCollection
                                     ) {
        this.memberNames = memberNames;
        this.status = status;
        this.context = context;
        this.userId = userId;
        this.color=color;
        this.memberInitials = genInitials(memberNames);
        this.memberCollection = memberCollection;
        this.frienRequestdCollection = requestCollection;
        this.friendCollection = friendCollection;
    }

    private List<String> genInitials(List<String> memberNames){
        List<String> toReturn = new ArrayList<>();
        for (int i=0; i<memberNames.size(); i++){
            String first = "?";
            String second = "?";
            String name = memberNames.get(i);
            if(!name.isEmpty()) {
                first = String.valueOf(name.charAt(0));
            }
            for(int j=0; j<name.length(); j++){
                if(String.valueOf(name.charAt(j)).equals(" ")){
                    second = String.valueOf(name.charAt(j+1));
                }
            }
            toReturn.add(i, (first+second).toUpperCase());
        }
        return toReturn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.team_member_list_cell,
                            parent,false);

            ListView listview = convertView.findViewById(R.id.team_fragment_listView);
            adapter = new TeamMemberCloudAdapter(listview, context);

        }

        // go through friends request collection to
        // check the receiver whether received request from sender
        // if received, then retrieve username information, and
        // search the member list to find receiver, then make
        // those two button visible, and call onclick method to make actions
        View finalConvertView = convertView;
        frienRequestdCollection.document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(Objects.equals(Objects.requireNonNull(documentSnapshot).getString("request_type"), "received")){
                  frienRequestdCollection.document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                      @Override
                      public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                          if(Objects.requireNonNull(documentSnapshot).contains("receiver")){

                              receiverName = documentSnapshot.getString("receiver");
                              sender = documentSnapshot.getString("sender");

                              // search the member list to find the specific position to display two buttons
                              if(memberNames.get(position).equals(receiverName)){
                                  finalConvertView.findViewById(R.id.btn_accept).setVisibility(View.VISIBLE);
                                  finalConvertView.findViewById(R.id.btn_decline).setVisibility(View.VISIBLE);
                                  acceptRequestOrdecline(finalConvertView, receiverName, sender);


                              }
                          }
                      }
                  });
                }
            }
        });


        TextView memberNameTextView = (TextView) convertView.findViewById(R.id.teamMemberListCellTitle);
        memberNameTextView.setText(this.memberNames.get(position));
//        TextView memberStatusTextView =
//                convertView.findViewById(R.id.teamMemberListCellName);

//        memberStatusTextView.setText(this.status.get(position));

        // set tag in order to find clicker position
        // to get the corresponding user name, then
        // delete it from member list
        memberNameTextView.setTag(position);
        firstClick = true;
        memberNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(firstClick) {
                    finalConvertView.findViewById(R.id.btn_delete).setVisibility(View.VISIBLE);
                    firstClick = false;
                }else {
                    finalConvertView.findViewById(R.id.btn_delete).setVisibility(View.INVISIBLE);
                    firstClick = true;
                }
                int i = (int) v.getTag();

                finalConvertView.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        memberCollection.document(memberNames.get(i)).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(context, memberNames.get(i) + "was deleted from member list ", Toast.LENGTH_SHORT).show();
//                                    memberNames.remove(i);
                                    finalConvertView.findViewById(R.id.btn_delete).setVisibility(View.INVISIBLE);
//                                    notifyDataSetInvalidated();
                                }
                            }
                        });
                    }
                });


            }
        });
        Button icon = convertView.findViewById(R.id.member_icon);
        //TextView memberStatusTextView = convertView.findViewById(R.id.teamMemberListCellName);
        Drawable iconBackground = ContextCompat.getDrawable(context, R.drawable.custom_rounded_shape);
        iconBackground.setAlpha(200);
        iconBackground = DrawableCompat.wrap(iconBackground);
        DrawableCompat.setTint(iconBackground,
                Color.parseColor(color.get(position)));
        if(icon==null){
            Log.d("memberInitials", position+"");
        }
        icon.setText(this.memberInitials.get(position));
        icon.setBackground(iconBackground);

        Log.d("memberNames", this.memberNames.get(position));
        Log.d("memberNames", Boolean.toString(memberNameTextView==null));
        //Log.d("status", this.status.get(position));

        if((this.status.get(position)!=null)&&(this.status.get(position).equals("accepted"))) {
            memberNameTextView.setTextColor(Color.BLACK);
            memberNameTextView.setTypeface(null, Typeface.NORMAL);
        }
        else{
            memberNameTextView.setTypeface(null, Typeface.ITALIC);
        }

        convertView.invalidate();
        return convertView;
    }

    // user actions, if accept, update receiver and sender's status
    // if decline, remove pending member in the member list
    private void acceptRequestOrdecline(View finalConvertView, String receiverName, String sender) {
        finalConvertView.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               frienRequestdCollection.document(userId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {
                       finalConvertView.findViewById(R.id.btn_accept).setVisibility(View.INVISIBLE);
                       finalConvertView.findViewById(R.id.btn_decline).setVisibility(View.INVISIBLE);
                       memberCollection.document(receiverName).update("status","accepted");
                       memberCollection.document(sender).update("status", "accepted");
                       HashMap<String, Object> friendMap = new HashMap<>();
                       friendMap.put("sender", sender);
                       friendMap.put("receiver", receiverName);
                       friendCollection.document(userId).set(friendMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               Toast.makeText(context, "Friend added", Toast.LENGTH_SHORT).show();

                           }
                       });

                       for (RouteForm route : MainActivity.curUser.routes.values()){

                           SharedRouteCloudModel sharedRoute =
                                   new SharedRouteCloudModel();
                           sharedRoute.setRoute(route);
                           sharedRoute.setCreator(GoogleSignIn.getLastSignedInAccount(context).getDisplayName());
                           sharedRoute.setCreatorIcon(sharedRoute.randomColorGen());

                           FirebaseFirestore.getInstance().collection(
                                   "shared_routes").add(sharedRoute);

                       }
                   }
               });
            }
        });

        finalConvertView.findViewById(R.id.btn_decline).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberCollection.document(receiverName).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            finalConvertView.findViewById(R.id.btn_accept).setVisibility(View.INVISIBLE);
                            finalConvertView.findViewById(R.id.btn_decline).setVisibility(View.INVISIBLE);
                           Toast.makeText(context, "member " + receiverName + " removed", Toast.LENGTH_SHORT).show();
//                            int i = (int) finalConvertView.getTag();
//                            memberNames.remove(i);
//                            notifyDataSetChanged();
                        }
                    }
                });
            }
        });


    }

    @Override
    public int getCount() {
        return this.memberNames.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
