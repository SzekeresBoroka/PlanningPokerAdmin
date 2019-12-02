package com.example.planningpokeradmin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Question implements Serializable {
    private String question, status;
    private Map<String, String> votes;

    public Question() {
    }

    public Question(String question) {
        this.question = question;
        status = "Inactive";
        votes = new HashMap<>();
    }

    public String getQuestion() {
        return question;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<String>  getVotes() {
        ArrayList<String> votesList = new ArrayList<>();
        for (Map.Entry<String,String> entry : votes.entrySet()) {
            votesList.add(entry.getKey() + " - " + entry.getValue());
        }
        return votesList;
    }
}


