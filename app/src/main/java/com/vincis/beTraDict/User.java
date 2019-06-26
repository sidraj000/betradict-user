package com.vincis.beTraDict;


public class User {

   public per_det per;
   public Wallet wallet;


    public User(per_det per, Wallet wallet) {
        this.per = per;
        this.wallet = wallet;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}