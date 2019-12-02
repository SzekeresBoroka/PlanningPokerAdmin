package com.example.planningpokeradmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

class QuestionsInactiveAdapter extends RecyclerView.Adapter<QuestionsInactiveAdapter.QuestionsViewHolder> {
    private ArrayList<Question>  mDataset;
    private Context context;

    public QuestionsInactiveAdapter(ArrayList<Question> questions, Context context) {
        this.context = context;
        mDataset = questions;
    }

    @Override
    public QuestionsInactiveAdapter.QuestionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_questions_recyclerview, parent, false);
        return new QuestionsInactiveAdapter.QuestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionsInactiveAdapter.QuestionsViewHolder holder, final int position) {
        final String question = mDataset.get(position).getQuestion();
        holder.tv_question.setText(question);
        holder.itemView.setClickable(true);

        holder.btn_delete_questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                String groupCode = sharedPref.getString(context.getString(R.string.active_group),"");

                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference myRef = db.getReference();
                myRef.child("groups").child(groupCode).child("questions").child(question).removeValue();

                mDataset.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataset.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class QuestionsViewHolder extends RecyclerView.ViewHolder {
        public Button btn_delete_questions;
        public TextView tv_question;
        public QuestionsViewHolder(View itemView) {
            super(itemView);
            btn_delete_questions = itemView.findViewById(R.id.btn_delete_question);
            tv_question = itemView.findViewById(R.id.tv_question_label);
        }
    }
}
