package com.vincis.beTraDict;

import com.vincis.beTraDict.transactions;

import java.util.List;

public class Wallet {
    public float trollars;
    public float balance;
    public Integer cashoutCards;
    public List<transactions> lastTransactions;
    public Wallet()
    {
    }
    public Wallet(float trollars, float balance, Integer cashoutCards, List<transactions> lastTransactions) {
        this.trollars = trollars;
        this.balance = balance;
        this.cashoutCards = cashoutCards;
        this.lastTransactions = lastTransactions;
    }


}
