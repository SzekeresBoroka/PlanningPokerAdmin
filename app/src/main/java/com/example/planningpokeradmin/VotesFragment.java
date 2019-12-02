package com.example.planningpokeradmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VotesFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> votes;
    private String groupCode;
    private String question;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        groupCode = sharedPref.getString(getString(R.string.active_group),"");
        question = sharedPref.getString(getString(R.string.active_question),"");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_votes, container, false);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference().child("groups").child(groupCode).child("questions").child(question).child("votes");

        votes = new ArrayList<>();

        recyclerView = v.findViewById(R.id.votes_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                votes.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    votes.add(ds.getKey() + " - " + ds.getValue());
                }
                mAdapter = new VotesAdapter(votes, context);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        TextView tv_group_code = v.findViewById(R.id.tv_group_code);
        tv_group_code.setText(groupCode);

        TextView tv_question = v.findViewById(R.id.tv_question);
        tv_question.setText(question);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        View v = getView();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference().child("groups").child(groupCode).child("questions").child(question).child("votes");

        votes = new ArrayList<>();

        recyclerView = v.findViewById(R.id.votes_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                votes.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    votes.add(ds.getKey() + " - " + ds.getValue());
                }
                mAdapter = new VotesAdapter(votes, context);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

