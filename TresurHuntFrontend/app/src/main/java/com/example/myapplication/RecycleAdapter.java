package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {

    private final int MIN_STEP=1;
    private final String alertMessage = "Min length achieved";

    private ArrayList<String> recapList;
    private Game game;
    private Context context;

    public RecycleAdapter(ArrayList<String> recapList, Game game, Context context) {
        this.recapList  = recapList ;
        this.game = game;
        this.context= context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            textView = (TextView) view.findViewById(R.id.recycleText);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycle_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getTextView().setText(recapList.get(position));

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)viewHolder.itemView.findViewById(R.id.recycleText);
        listItemText.setText(recapList.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button)viewHolder.itemView.findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(getItemCount()>MIN_STEP){
                    recapList.remove(position);

                    game.remove(position);
                    notifyItemRemoved(position); //notifyDataSetChanged();
                }
                else{
                    Toast.makeText(context, alertMessage , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return recapList.size(); //localDataSet.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}