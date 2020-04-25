package com.vinciis.beTraDict;

import java.util.Date;


public class Match {
    public Date date;
    public String id;
    public String teamA;
    public String teamB;
    public String stadium;
    public Integer status;

    public Match()
    {
    }

    public Match(Date date, String id, String teamA, String teamB, String stadium, Integer status) {
        this.date = date;
        this.id = id;
        this.teamA = teamA;
        this.teamB = teamB;
        this.stadium = stadium;
        this.status = status;
    }
}

