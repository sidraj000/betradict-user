package com.vinciis.beTraDict;

import java.util.Date;

public class per_det {


    public String username;
    public String email;
    public String picId;
    public String uid;
    public Date dob;
    public String phoneNumber;
    public String gameName;
    public String referral;
    public per_det()
    {

    }
    public per_det(String username, String email, String picId, String uid, String referral) {
        this.username = username;
        this.email = email;
        this.picId = picId;
        this.uid = uid;
        this.referral = referral;
    }


    public per_det(String username, String email, String picId, String uid, Date dob, String phoneNumber, String gameName) {
        this.username = username;
        this.email = email;
        this.picId = picId;
        this.uid = uid;
        this.dob = dob;
        this.phoneNumber = phoneNumber;
        this.gameName = gameName;
    }
}
