package com.example.hw04_gymlog_v300.viewHolder;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.example.hw04_gymlog_v300.database.entites.GymLog;

public class GymLogAdator extends ListAdapter<GymLog,GymLogViewHolder> {
    public GymLogAdator(@NonNull DiffUtil.ItemCallback<GymLog> diffcallback){
        super(diffcallback);


    }
@NonNull
@Override
    public GymLogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        return GymLogViewHolder.create(parent);
}

    @Override
    public void onBindViewHolder(@NonNull GymLogViewHolder holder, int position) {
        GymLog current = getItem(position);
        holder.bind(current.toString());
    }

    public static class GymlogDiff extends DiffUtil.ItemCallback<GymLog>{
    @Override
    public boolean areItemsTheSame(@NonNull GymLog oldItem, @NonNull GymLog newItem) {
        return oldItem == newItem;
    }

    @Override
    public boolean areContentsTheSame(@NonNull GymLog oldItem, @NonNull GymLog newItem) {
        return oldItem.equals(newItem);
    }
}


}
