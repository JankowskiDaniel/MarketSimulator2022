package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;

abstract public class Investor{
    private static long idCounter = 0;
    protected TraderAccount Account;
    protected String investorID;
    protected String name;
    protected String lastname;
    protected boolean work;
    protected int workspeed;

    /**
     * This is set of common things for private investors and funds. It is only called by super() method.
     * @param id
     * @param budget
     * @param name
     * @param lastname
     */
    Investor(String id, BigDecimal budget, String name, String lastname) {
        this.Account = new TraderAccount(budget);
        this.investorID = id;
        this.name = name;
        this.lastname = lastname;
        this.work = false;
        this.workspeed = 0;
    }

    /**
     * Method for creating ID of the investor. With each subsequent investor, the ID increases by one.
     * @return
     */
    public static synchronized String createID()
    {
        return String.valueOf(idCounter++);
    }

    public void setWork(boolean c){this.work = c;}
    public String getName(){ return this.name;    }
    public String getLastname(){ return this.lastname;}
    public String getType(){ return "null";}
    public String getInvestorID(){return this.investorID;}
    public TraderAccount getAccount(){ return this.Account;}
    public String getFundname(){return "null";}
    public void setWorkspeed(int value){this.workspeed = value;}
    public int getWorkspeed(){return this.workspeed;}
}
