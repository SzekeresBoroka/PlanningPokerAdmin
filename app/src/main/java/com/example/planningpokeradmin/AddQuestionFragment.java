package com.example.planningpokeradmin;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddQuestionFragment extends Fragment {
    private Context context;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v =  inflater.inflate(R.layout.fragment_add_question, container, false);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String groupCode = sharedPref.getString(getString(R.string.active_group),"");

        TextView tv_group_code = v.findViewById(R.id.tv_group_code);
        tv_group_code.setText(groupCode);

        Button btn_add_question = v.findViewById(R.id.btn_add_question);
        btn_add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                final String groupCode = sharedPref.getString(context.getString(R.string.active_group),"Active Group");

                EditText et_new_question = v.findViewById(R.id.et_new_question);
                final String question = et_new_question.getText().toString();

                if(question.isEmpty()){
                    Toast.makeText(context, "Question is empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                final DatabaseReference myRef = db.getReference().child("groups").child(groupCode).child("questions");

                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren())
                        {
                            Question q = ds.getValue(Question.class);
                            if(q.getQuestion().equals(question)){
                                Toast.makeText(context, "This question already exists", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        myRef.child(question).setValue(new Question(question));

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

}
