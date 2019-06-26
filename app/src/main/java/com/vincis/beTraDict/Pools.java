package com.vincis.beTraDict;


import java.util.List;

public class Pools {
    public String poolname;
    public Integer enteramount;
    public Integer poolUsers;
    public List<String> user;
    public Pools()
    {

    }

    public Pools(String poolname, Integer enteramount, Integer poolUsers, List<String> user) {
        this.poolname = poolname;
        this.enteramount = enteramount;
        this.poolUsers = poolUsers;
        this.user = user;
    }
}
