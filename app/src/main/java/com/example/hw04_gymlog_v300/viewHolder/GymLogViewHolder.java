package com.example.hw04_gymlog_v300.viewHolder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.hw04_gymlog_v300.R;

public class GymLogViewHolder extends RecyclerView.ViewHolder {

    private final TextView gymLogViewItem;
    private GymLogViewHolder(View gymLogView){
        super(gymLogView);
        gymLogViewItem = gymLogView.findViewById(R.id.recylerItemTextView);

    }

    public void bind (String text){

        gymLogViewItem.setText(text);
    }
    static GymLogViewHolder create(ViewGroup parent){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gymlog_recyler_item,parent,false);
        return new GymLogViewHolder(view);
    }
}
