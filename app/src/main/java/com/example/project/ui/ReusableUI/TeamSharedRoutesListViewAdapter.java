package com.example.cse110project.ui.ReusableUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.cse110project.R;

import java.util.List;

public class TeamSharedRoutesListViewAdapter extends BaseAdapter {

    private final String TAG = "MEMBER_LIST_VIEW_ADAPTER_2";

    private List<String> memberNames;
    private List<String> memberInitials;
    private List<String> color;
    private Context context;


    public TeamSharedRoutesListViewAdapter(Context context,
                                           List<String> memberNames,
                                           List<String> memberInitials,
                                           List<String> color) {
        this.memberNames = memberNames;
        this.memberInitials = memberInitials;
        this.color = color;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.custom_list_view,
                            parent,false);
        }

        Button icon = convertView.findViewById(R.id.member_icon);
        TextView itemTitle = convertView.findViewById(R.id.item_title);
        Drawable iconBackground = ContextCompat.getDrawable(context, R.drawable.custom_rounded_shape);
        iconBackground.setAlpha(200);
        iconBackground = DrawableCompat.wrap(iconBackground);
        DrawableCompat.setTint(iconBackground,
                Color.parseColor(color.get(position)));

        //icon.setText(this.memberInitials.get(position));
        //String[] s = this.memberNames.get(position).split(" ");
        //String first = String.valueOf(s[0].charAt(0)) + String.valueOf(s[1].charAt(0));
        icon.setText(this.memberInitials.get(position));
        icon.setBackground(iconBackground);
        itemTitle.setText(this.memberNames.get(position));
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
