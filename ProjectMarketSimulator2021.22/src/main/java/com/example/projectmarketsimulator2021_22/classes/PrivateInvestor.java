package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

public class PrivateInvestor extends Investor implements Runnable{
    public PrivateInvestor(String first_name, String last_name) {
        super(createID(), new BigDecimal(BigInteger.valueOf(new Random().nextInt(1000000)),2), first_name, last_name);
    }
    @Override
    public String getType(){return "Private Investor";}

    /**
     * Main method monitoring work of the private investor. At each iteration, there is set of several available actions
     * to do for the private investor:
     * 80% for make a trade
     * 20% for increasing his budget.
     */
    public void run(){
        System.out.println("Private investor "+name+" "+lastname+" appears on the market!");
        while(work){
            int prob = (int)((Math.random()*(101-0))+0);
            if(prob < 40) {
                this.getAccount().trade();
//                System.out.println(name+" traded");
            }else if(prob >=40 && prob <80){
                this.getAccount().tradeThroughFund();
            } else if(prob >= 80 && prob <100){
                this.getAccount().IncreaseBudget();
                System.out.println(name+" increases his budget to "+this.getAccount().GetBudget().toString()+"!");
            }
            try {
                Thread.sleep(this.workspeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Private: "+name+" stopped");
    }


}
