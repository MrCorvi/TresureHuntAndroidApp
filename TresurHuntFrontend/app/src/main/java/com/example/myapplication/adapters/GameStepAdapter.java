package com.example.myapplication.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.myapplication.R;
import com.example.myapplication.models.Step;
import java.util.ArrayList;
import java.util.List;

//This class translate the Step model class into an item to be interred in a ViewList
public class GameStepAdapter extends ArrayAdapter<Step> {
    public GameStepAdapter(Context context, List<Step> users) {
        super(context, 0, users);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Step step = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.game_list_item, parent, false);
        }
        // Lookup view for data population
        TextView questionView = (TextView) convertView.findViewById(R.id.itemQuestion);
        ImageView iconView = (ImageView) convertView.findViewById(R.id.itemIcon);
        // Populate the data into the template view using the data object
        if(step.isPositionQuestion){
            iconView.setImageDrawable(getContext().getDrawable(R.drawable.ic_flag));
        }else{
            iconView.setImageDrawable(getContext().getDrawable(R.drawable.ic_camera));
        }
        questionView.setText(step.question);
        // Return the completed view to render on screen
        return convertView;

    }
}
