package com.example.planningpokeradmin;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

class QuestionsActiveAdapter extends RecyclerView.Adapter<QuestionsActiveAdapter.ViewQuestionsViewHolder> {
    private ArrayList<Question>  mDataset;
    private Context context;

    public QuestionsActiveAdapter(ArrayList<Question> questions, Context context) {
        this.context = context;
        mDataset = questions;
    }

    @Override
    public QuestionsActiveAdapter.ViewQuestionsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_questions_recyclerview, parent, false);
        return new QuestionsActiveAdapter.ViewQuestionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionsActiveAdapter.ViewQuestionsViewHolder holder, final int position) {
        final String question = mDataset.get(position).getQuestion();
        holder.btn_question.setText(question);
        holder.itemView.setClickable(true);

        holder.btn_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open votefragment
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(context.getString(R.string.active_question), question);
                editor.apply();

                FragmentTransaction frag_trans = ((MainActivity) context).getSupportFragmentManager().beginTransaction();
                frag_trans.replace(R.id.fragment_container, new VotesFragment());
                frag_trans.addToBackStack("PlanningPokerAdmin");
                frag_trans.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewQuestionsViewHolder extends RecyclerView.ViewHolder {
        public Button btn_question;

        public ViewQuestionsViewHolder(View itemView) {
            super(itemView);
            btn_question = itemView.findViewById(R.id.btn_question);
        }
    }
}

