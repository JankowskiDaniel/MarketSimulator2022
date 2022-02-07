package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

public class Currency extends Asset {
    private String shortname;
    private volatile BigDecimal amount;
    private volatile BigDecimal exchange_ratio;

    public Currency(String name, String symbol, String market) {
        super(name, new BigDecimal(BigInteger.valueOf(new Random().nextInt(1500)),2), market, "currency");
        this.shortname = symbol;
        this.amount = new BigDecimal(BigInteger.valueOf(new Random().nextInt(100000000)),2);
        this.exchange_ratio = this.maximal_price;
        this.prices.add(exchange_ratio);
    }

    /**
     * Increase amount of currency
     * @param amount
     */
    public synchronized void addAmount(BigDecimal amount){this.amount = this.amount.add(amount);}

    /**
     * decrease amount of currency
     * @param amount
     */
    public synchronized void subAmount(BigDecimal amount){this.amount = this.amount.subtract(amount);}
    @Override
    public synchronized BigDecimal getAmount(){ return this.amount;}
    @Override
    public void setAmount(BigDecimal am){this.amount = am;}
    @Override
    public synchronized BigDecimal getPrice(){return this.exchange_ratio;}

    public String getShortname(){return this.shortname;}


    /**
     * Method for changing price of an asset, saving changes in arraylist prices and percentchanges
     * this method is called every time after exchange asset on the market.
     * @param action true = we increase price of an asset, false = decrease price of an asset
     * @param bound randomly generated difference, by which price of the asset is changing
     */
    @Override
    public synchronized void updatePrice(boolean action, BigDecimal bound){
        //price of asset will change in range (-2%;2%), its quite large, but I want to clearly see changes on the market
        BigDecimal ratiodiff = this.exchange_ratio.multiply(bound);
        ratiodiff.setScale(2,RoundingMode.HALF_DOWN);
        if(action){
            //we increase price of an asset
            this.exchange_ratio = this.exchange_ratio.add(ratiodiff).setScale(2, RoundingMode.HALF_DOWN);
            this.prices.add(exchange_ratio);
            if(this.exchange_ratio.compareTo(this.maximal_price) > 0){
                this.maximal_price = this.exchange_ratio;
            }
        } else {
            //we decrease price of an asset
            this.exchange_ratio = this.exchange_ratio.subtract(ratiodiff).setScale(2, RoundingMode.HALF_DOWN);
            this.prices.add(exchange_ratio);
            if(this.exchange_ratio.compareTo(this.minimal_price) < 0){
                this.minimal_price = this.exchange_ratio;
            }
        }
        // now compute % changes of currency
        BigDecimal percent;
        if(this.exchange_ratio.compareTo(this.started_price) > 0){
            percent = this.exchange_ratio.divide(this.started_price, 2, RoundingMode.HALF_UP);
        } else {
            percent = this.started_price.divide(this.exchange_ratio, 2, RoundingMode.HALF_UP);
        }
        percent = percent.subtract(BigDecimal.valueOf(1));
        percent = percent.multiply(BigDecimal.valueOf(100));
        this.percentchanges.add(percent);
    }

}
