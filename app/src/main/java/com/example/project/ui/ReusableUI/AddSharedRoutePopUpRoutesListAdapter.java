package com.example.cse110project.ui.ReusableUI;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.cse110project.R;

import java.util.List;

public class AddSharedRoutePopUpRoutesListAdapter extends BaseAdapter {

    private final String TAG = "AddSharedRoutePopUpRoutesListAdapter";

    List<String> reusableList;
    private Context context;


    public AddSharedRoutePopUpRoutesListAdapter(List<String> routeTitles, Context context) {
        this.reusableList = routeTitles;
        this.context = context;
    }

    @Override
    public int getCount() {
        Log.i(TAG, "list size: " + reusableList.size());
        return reusableList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.list_for_just_title, parent, false);
        }

        convertView.setClickable(true);

        TextView textView =
                convertView.findViewById(R.id.reusableListViewOnlyTextLabel);

        textView.setText(reusableList.get(position));

        return convertView;

    }
}
