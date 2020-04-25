package com.vinciis.beTraDict;

import java.util.Date;

public class transactions {
    public float amt;
    public String Mid;
    public String poolId;
    public String poolType;
    public Date date;


    public transactions()
    {

    }

    public transactions(float amt, String mid, String poolId, String poolType, Date date) {
        this.amt = amt;
        Mid = mid;
        this.poolId = poolId;
        this.poolType = poolType;
        this.date = date;
    }
}













