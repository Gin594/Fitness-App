package com.example.cse110project.ui.ReusableUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.cse110project.BuildConfig;
import com.example.cse110project.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TeamProposedWalkLisViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> proposedWalks;
    private List<String> creators;
    private List<String > status;
    List<String> color = new ArrayList<>();

    public TeamProposedWalkLisViewAdapter(Context context,
                                          List<String> proposedWalks,
                                          List<String> creators) {
        this.context = context;
        this.proposedWalks = proposedWalks;
        this.creators = creators;
    }

    public TeamProposedWalkLisViewAdapter(Context context, List<String> proposedWalks, List<String> creators, List<String> status) {
        this.context = context;
        this.proposedWalks = proposedWalks;
        this.creators = creators;
        this.status = status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.proposed_walk_list_cell, parent, false);
        }

        TextView proposedWalkTitle =
                convertView.findViewById(R.id.proposedWalkCellUsername);
        TextView proposedWalkCreator =
                convertView.findViewById(R.id.proposedWalkCellSubHead);

        color = genColor();

        Button icon = convertView.findViewById(R.id.member_icon);
        Drawable iconBackground = ContextCompat.getDrawable(context, R.drawable.custom_rounded_shape);
        iconBackground.setAlpha(200);
        iconBackground = DrawableCompat.wrap(iconBackground);
        DrawableCompat.setTint(iconBackground,
                Color.parseColor(color.get(position)));

        String name[] = creators.get(position).split(" ");
        String init = String.valueOf(name[0].charAt(0)) + String.valueOf(name[1].charAt(0));

        icon.setText(init);
        icon.setBackground(iconBackground);

        proposedWalkCreator.setText(status.get(position));

        proposedWalkTitle.setText(this.proposedWalks.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        return this.proposedWalks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private List<String> genColor(){
        List<String> color = new ArrayList<>();
        for (int i=0; i<proposedWalks.size(); i++){
            Random random = new Random();
            int random_hex = random.nextInt(0xffffff + 1);
            color.add(String.format("#%06x", random_hex));
        }
        return color;
    }
}
