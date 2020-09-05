package com.example.cse110project.ui.ReusableUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.tv.TvInputService;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.cse110project.Models.RouteForm;
import com.example.cse110project.Models.User;
import com.example.cse110project.R;

import java.util.List;

public class MyRouteAdapter extends BaseAdapter {

    private final String TAG = "MEMBER_LIST_VIEW_ADAPTER_2";

    private List<String> memberNames;
    private List<String> memberInitials;
    private List<String> color;
    private Context context;
    private User curUser;


    public MyRouteAdapter(Context context,
                                           List<String> memberNames,
                                           List<String> memberInitials,
                                           List<String> color,
                                           User curUser) {

        this.memberNames = memberNames;
        this.memberInitials = memberInitials;
        this.color = color;
        this.context = context;
        this.curUser = curUser;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.proposed_walk_list_cell,
                            parent,false);
        }

        String title = memberNames.get(position);
        RouteForm currRoute = curUser.getRoutes().get(title);

        Button icon = convertView.findViewById(R.id.member_icon);
//        ImageButton imageButton = convertView.findViewById(R.id.StarButton);

        TextView itemTitle = convertView.findViewById(R.id.proposedWalkCellUsername);
        TextView favorite = convertView.findViewById(R.id.proposedWalkCellSubHead);
        Drawable iconBackground = ContextCompat.getDrawable(context, R.drawable.custom_rounded_shape);
        iconBackground.setAlpha(200);
        iconBackground = DrawableCompat.wrap(iconBackground);
        DrawableCompat.setTint(iconBackground,
                Color.parseColor(color.get(position)));

        //icon.setText(this.memberInitials.get(position));
        icon.setText(String.valueOf(this.memberNames.get(position).charAt(0)));
        icon.setBackground(iconBackground);
        itemTitle.setText(this.memberNames.get(position));
        Log.i("currRoute", currRoute.getTitle() + " " +String.valueOf(currRoute.getWalked()));
        if(currRoute.getWalked()){
            itemTitle.setTextColor(Color.BLACK);
        }
        if(currRoute.isFavorite()){
            favorite.setText("favorite");
        }
        else{
            favorite.setText("not favorite");
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return this.memberNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public List<String> routeNames(){
        return this.memberNames;
    }



}
