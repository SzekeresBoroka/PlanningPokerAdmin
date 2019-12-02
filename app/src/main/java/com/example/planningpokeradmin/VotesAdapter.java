package com.example.planningpokeradmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class VotesAdapter extends RecyclerView.Adapter<VotesAdapter.MyViewHolder>{
    private ArrayList<String> votes;
    private Context context;

    public VotesAdapter(ArrayList<String> dataList, Context context) {
        this.votes = dataList;
        this.context = context;
    }

    @NonNull
    @Override
    public VotesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_votes_recyclerview, viewGroup, false);
        return new VotesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VotesAdapter.MyViewHolder viewHolder, int i) {
        String data = votes.get(i);
        viewHolder.tv_vote.setText(data);
    }

    @Override
    public int getItemCount() {
        return votes.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_vote;
        public MyViewHolder(View itemView) {
            super(itemView);
            tv_vote = itemView.findViewById((R.id.tv_vote));
        }
    }
}
