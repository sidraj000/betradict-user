package com.vinciis.beTraDict;



import java.util.List;

public class Pools {
    public String poolname;
    public Integer enteramount;
    public Integer poolUsers;
    public Integer totalWinners;
    public Integer totalWinnings;
    public Integer totalParticipants;
    public List<String> user;
    public Integer status;
    public Pools()
    {

    }

    public Pools(String poolname, Integer enteramount, Integer poolUsers, Integer totalWinners, Integer totalWinnings, Integer totalParticipants, List<String> user, Integer status) {
        this.poolname = poolname;
        this.enteramount = enteramount;
        this.poolUsers = poolUsers;
        this.totalWinners = totalWinners;
        this.totalWinnings = totalWinnings;
        this.totalParticipants = totalParticipants;
        this.user = user;
        this.status = status;
    }
}
