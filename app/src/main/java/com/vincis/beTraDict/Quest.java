package com.vincis.beTraDict;

import android.util.Pair;

import java.util.List;

public class Quest {
    public String ques;
    public String heading;
    public String type;
    public String opt1;
    public String opt2;
    public String opt3;
    public String qid;
    public int status;
    public float mybid;
    public float myrate;
    public String myans;
    public String cans;
    public List<Answer> answers;
    public Float amtearned;
    public Quest()
    {  }

    public Quest(String ques, String heading, String type, String opt1, String opt2, String opt3, String qid, int status, float mybid, float myrate, String myans, String cans, List<Answer> answers, Float amtearned) {
        this.ques = ques;
        this.heading = heading;
        this.type = type;
        this.opt1 = opt1;
        this.opt2 = opt2;
        this.opt3 = opt3;
        this.qid = qid;
        this.status = status;
        this.mybid = mybid;
        this.myrate = myrate;
        this.myans = myans;
        this.cans = cans;
        this.answers = answers;
        this.amtearned = amtearned;
    }
}