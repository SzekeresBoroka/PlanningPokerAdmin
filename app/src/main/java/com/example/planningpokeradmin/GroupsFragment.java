package com.example.planningpokeradmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {
    private Context context;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<Group> groups;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        //reset groupCode
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.active_group), "Active Group");
        editor.apply();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_groups, container, false);

        //set the recyclerview that contains the existing groups
        //by clicking on a group, we go to the group's questions (Active- or InactiveFragment)

        recyclerView = v.findViewById(R.id.groups_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        groups = new ArrayList<>();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference().child("groups");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Group group = ds.getValue(Group.class);
                    groups.add(group);
                }
                mAdapter = new GroupsAdapter(groups, context);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Button btn_create_group = v.findViewById(R.id.btn_create_group);
        btn_create_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et_group = v.findViewById(R.id.et_group_code);
                final String groupCode = et_group.getText().toString();

                if(groupCode.isEmpty()){
                    Toast.makeText(context, "Group Code is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = db.getReference().child("groups");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //check if groupCode exists
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Group group = ds.getValue(Group.class);
                            if(group.getGroupCode().equals(groupCode)){
                                Toast.makeText(context, "This group code already exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        //create and save new group
                        Group group = new Group(groupCode);
                        myRef.child(groupCode).setValue(group);

                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(getString(R.string.active_group), groupCode);
                        editor.apply();

                        //go to QuestionsFragment to add questions to the new group
                        FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                        frag_trans.replace(R.id.fragment_container, new QuestionsInactiveFragment(), "QuestionsInactiveFragment");
                        frag_trans.addToBackStack("PlanningPokerAdmin");
                        frag_trans.commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        View v = getView();

        recyclerView = v.findViewById(R.id.groups_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        groups = new ArrayList<>();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference().child("groups");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groups.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    Group group = ds.getValue(Group.class);
                    groups.add(group);
                }
                mAdapter = new GroupsAdapter(groups, context);
                recyclerView.setAdapter(mAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
