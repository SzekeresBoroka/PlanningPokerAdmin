package com.example.planningpokeradmin;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
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

import java.util.ArrayList;

import static java.lang.Integer.parseInt;

public class QuestionsInactiveFragment extends Fragment {
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

        //get the chosen group's id
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        groupCode = sharedPref.getString(getString(R.string.active_group),"");
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_questions_inactive, container, false);

        //put the groupCode on the screen
        TextView tv_group_code = v.findViewById(R.id.tv_group_code);
        tv_group_code.setText(groupCode);

        //set datachangelistener to textview_minutes
        setTimePicker(v);

        //set the recyclerview that conatins the chosen group's questions
        recyclerView = v.findViewById(R.id.questions_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);

        questions = new ArrayList<>();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference().child("groups");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Group group = ds.getValue(Group.class);
                    if(group.getGroupCode().equals(groupCode)){
                        questions = group.getQuestions();

                        int questionTime = parseInt(group.getQuestionTime());
                        if(questionTime > 0){
                            String minutes = questionTime + "";
                            EditText et_minute = v.findViewById(R.id.et_minute);
                            et_minute.setText(minutes);
                        }
                        break;
                    }
                }
                mAdapter = new QuestionsInactiveAdapter(questions, context);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //set onclicklistener to add new question
        Button btn_add_question = v.findViewById(R.id.btn_new_question);
        btn_add_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container, new AddQuestionFragment());
                frag_trans.addToBackStack("PlanningPokerAdmin");
                frag_trans.commit();
            }
        });

        //set onclicklistener to "start"
        Button btn_start = v.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start(v);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        final View v = getView();

        recyclerView = v.findViewById(R.id.questions_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);

        questions = new ArrayList<>();

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference myRef = db.getReference().child("groups");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Group group = ds.getValue(Group.class);
                    if(group.getGroupCode().equals(groupCode)){
                        questions = group.getQuestions();

                        int questionTime = parseInt(group.getQuestionTime());
                        if(questionTime > 0){
                            String minutes = questionTime + "";
                            EditText et_minute = v.findViewById(R.id.et_minute);
                            et_minute.setText(minutes);
                        }
                        break;
                    }
                }
                mAdapter = new QuestionsInactiveAdapter(questions, context);
                recyclerView.setAdapter(mAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void start(View view){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        final String groupCode = sharedPref.getString(getString(R.string.active_group),"");

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference().child("groups");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    final Group group = ds.getValue(Group.class);
                    if(group.getGroupCode().equals(groupCode)){
                        if(group.getGroupCode().equals(groupCode)){
                            ArrayList<Question> questions = group.getQuestions();
                            if(questions.size() == 0){
                                Toast.makeText(context, "No questions!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if(group.getQuestionTime().equals("0")){
                                Toast.makeText(context, "Question time missing!", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            myRef.child(groupCode).child("status").setValue("Active");
                            Handler timerHandler = new Handler();
                            activateQuestions(myRef, timerHandler, group,0, "");

                            FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                            frag_trans.replace(R.id.fragment_container, new QuestionsActiveFragment());
                            frag_trans.addToBackStack("PlanningPokerAdmin");
                            frag_trans.commit();
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void activateQuestions(final DatabaseReference myRef, final Handler timerHandler, final Group group, final int i, String previousQuestion){
        //deactivate previous Question
        if(!previousQuestion.isEmpty()) {
            myRef.child(group.getGroupCode()).child("questions").child(previousQuestion).child("status").setValue("Voted");
        }

        int minutes = parseInt(group.getQuestionTime());

        final ArrayList<Question> questions = group.getQuestions();

        if(i <= questions.size()){
            String question = "";
            long millis = minutes * 60 * 1000;
            //if question is inactive, activate it
            if(i < questions.size() && questions.get(i).getStatus().equals("Inactive")){
                question = questions.get(i).getQuestion();
                //activateQuestion
                myRef.child(group.getGroupCode()).child("questions").child(question).child("status").setValue("Active");
            }
            else{
                millis = 0;
            }

            final String currentQuestion = question;
            //delay the next question
            timerHandler.postDelayed( new Runnable() {
                @Override
                public void run() {
                    activateQuestions(myRef, timerHandler, group, i+1, currentQuestion);
                }
            }, millis);
        }
        else{
            //after each question's time is over, deactivate the group
            myRef.child(group.getGroupCode()).child("status").setValue("Voted");
        }
    }

    private void setTimePicker(View v){
        final EditText et_minute = v.findViewById(R.id.et_minute);

        et_minute.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
        et_minute.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(final Editable editable) {
                if(editable.toString().length() == 1) {
                    int n = parseInt(editable.toString());
                    if(n > 5){
                        String number = "0" + n;
                        et_minute.setText(number);
                        et_minute.setSelection(et_minute.getText().length());
                        et_minute.clearFocus();
                    }
                }
                if(editable.toString().length() == 2){
                    et_minute.clearFocus();
                }
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference myRef = db.getReference().child("groups");
                if(editable.toString().length() > 0) {
                    myRef.child(groupCode).child("questionTime").setValue(editable.toString());
                }
                else{
                    myRef.child(groupCode).child("questionTime").setValue("0");
                }
            }
        });

    }


}
