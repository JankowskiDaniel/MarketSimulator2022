package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class Fund extends Investor implements Runnable {
    private String namefund;

    /**
     * Creating a fund
     * @param first_name name of the manager
     * @param last_name lastname of the manager
     * @param fname name of the fund
     */
    public Fund(String first_name, String last_name, String fname) {
        super(createID(), new BigDecimal(BigInteger.valueOf(new Random().nextInt(3000000)),2), first_name, last_name);
        this.namefund = fname;
    }

    /**
     * Main function, which monitor work of the fund.
     */
    public void run(){
        while(work){
            int prob = (int)((Math.random()*(101-0))+0);
            if(prob < 100){
                this.getAccount().trade();
            }
            try {
                Thread.sleep(this.workspeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("fund: "+name+" stopped");
    }
    public String getType(){return "Fund";}
    public String getFundname(){ return this.namefund;}
}
