package com.example.planningpokeradmin;


import android.content.Context;
import android.content.SharedPreferences;
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

public class QuestionsActiveFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Question> questions;
    private String groupCode;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        groupCode = sharedPref.getString(getString(R.string.active_group),"");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_questions_active, container, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.active_group), groupCode);
        editor.putString(context.getString(R.string.active_question), "Active Question");
        editor.apply();

        recyclerView = v.findViewById(R.id.list_questions_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);

        questions = new ArrayList<>();

        TextView tv_group_code = v.findViewById(R.id.tv_group_code);
        tv_group_code.setText(groupCode);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference().child("groups");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Group group = ds.getValue(Group.class);
                    if(group.getGroupCode().equals(groupCode)){
                        questions = group.getQuestions();
                        break;
                    }
                }
                mAdapter = new QuestionsActiveAdapter(questions, context);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        View v = getView();

        recyclerView = v.findViewById(R.id.list_questions_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);

        questions = new ArrayList<>();

        TextView tv_group_code = v.findViewById(R.id.tv_group_code);
        tv_group_code.setText(groupCode);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference().child("groups");

        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Group group = ds.getValue(Group.class);
                    if(group.getGroupCode().equals(groupCode)){
                        questions = group.getQuestions();
                        break;
                    }
                }
                mAdapter = new QuestionsActiveAdapter(questions, context);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
