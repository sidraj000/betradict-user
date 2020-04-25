package com.vinciis.beTraDict;

public class Referral {
    public String referralCode;
    public String uid;
    public Integer money;
    public Referral(){

    }

    public Referral(String referralCode, String uid, Integer money) {
        this.referralCode = referralCode;
        this.uid = uid;
        this.money = money;
    }
}
