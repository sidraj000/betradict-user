package com.vinciis.beTraDict;

public class StadiumData {
    String t1Name, t2Name, t1Date, t2Date, t1Score, t2Score;
    Integer t1WicketTaken, t2WicketTaken, t16s, t26s, t14s, t24s, t1Catches, t2Catches;
    String t1Result, t2Result;

    public StadiumData() {

    }

    public StadiumData(String t1Name, String t2Name, String t1Date, String t2Date, String t1Score, String t2Score, Integer t1WicketTaken, Integer t2WicketTaken, Integer t16s, Integer t26s, Integer t14s, Integer t24s, Integer t1Catches, Integer t2Catches, String t1Result, String t2Result) {
        this.t1Name = t1Name;
        this.t2Name = t2Name;
        this.t1Date = t1Date;
        this.t2Date = t2Date;
        this.t1Score = t1Score;
        this.t2Score = t2Score;
        this.t1WicketTaken = t1WicketTaken;
        this.t2WicketTaken = t2WicketTaken;
        this.t16s = t16s;
        this.t26s = t26s;
        this.t14s = t14s;
        this.t24s = t24s;
        this.t1Catches = t1Catches;
        this.t2Catches = t2Catches;
        this.t1Result = t1Result;
        this.t2Result = t2Result;
    }
}