package com.vinciis.beTraDict;

public class UserProfile {

    public String UserId;
    public Integer acTroller;
    public String EmailId;

    public UserProfile() {
    }

    public UserProfile(String userId, Integer acTroller, String emailId) {
        UserId = userId;
        this.acTroller = acTroller;
        EmailId = emailId;
    }

}
