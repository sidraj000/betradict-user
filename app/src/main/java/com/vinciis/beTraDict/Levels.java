package com.vinciis.beTraDict;

public class Levels {
    public String id;
    public Pools battingPool;
    public Pools bowlingPool;
    public Integer enteramount;
    public Integer totalWinners;
    public Integer totalWinnings;
    public Levels()
    {

    }

    public Levels(String id, Pools battingPool, Pools bowlingPool, Integer enteramount, Integer totalWinners, Integer totalWinnings) {
        this.id = id;
        this.battingPool = battingPool;
        this.bowlingPool = bowlingPool;
        this.enteramount = enteramount;
        this.totalWinners = totalWinners;
        this.totalWinnings = totalWinnings;
    }
}
