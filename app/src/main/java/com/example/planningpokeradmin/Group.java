package com.example.planningpokeradmin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group implements Serializable {
    private String groupCode, status, questionTime;
    //private ArrayList<Question> questions;
    private Map<String, Question> questions;

    public Group() {
    }

    public Group(String groupCode) {
        this.groupCode = groupCode;
        status = "Inactive";
        questionTime = "0";
        //questions = new ArrayList<>();
        questions = new HashMap<>();
    }

    public String getGroupCode() {
        return groupCode;
    }

    public String getStatus() {
        return status;
    }

    public String getQuestionTime() {
        return questionTime;
    }


    public ArrayList<Question> getQuestions() {
        ArrayList<Question> questions = new ArrayList<>();
        if(this.questions == null){
            return questions;
        }
        for (Map.Entry<String, Question> entry : this.questions.entrySet()) {
            questions.add(entry.getValue());
        }
        return questions;
    }


}
