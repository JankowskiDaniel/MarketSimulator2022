package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Random;

public class Share extends Asset {
    private volatile BigDecimal current_price;
    private volatile int volume;
    private String company;
    private String currency;

    /**
     * Creating a share. Share is always created during a company creation.
     * @param name
     * @param start_price
     * @param volume
     * @param market
     * @param currency
     */
    Share(String name, BigDecimal start_price, int volume, String market, String currency) {
        super(name.toUpperCase(Locale.ROOT), start_price, market, "share");
        this.volume = volume;
        this.current_price = start_price;
        this.company = name;
        this.currency = currency;
        this.prices.add(current_price);
    }

    /**
     * Method for changing price of an asset, saving changes in arraylist prices and percentchanges
     * this method is called every time after exchange asset on the market.
     * @param action true = we increase price of an asset, false = decrease price of an asset
     * @param bound randomly generated difference, by which price of the asset is changing
     */
    @Override
    public synchronized void updatePrice(boolean action, BigDecimal bound){
        BigDecimal ratiodiff = this.current_price.multiply(bound);
        ratiodiff.setScale(2,RoundingMode.HALF_DOWN);
        if(action){
            //we increase price of an asset
            this.current_price = this.current_price.add(ratiodiff);
            this.current_price = this.current_price.setScale(2, RoundingMode.HALF_UP);
            this.prices.add(current_price);
            if(this.current_price.compareTo(this.maximal_price) > 0){
                this.maximal_price = this.current_price;
            }
        } else {
            //we decrease price of an asset
            this.current_price = this.current_price.subtract(ratiodiff);
            this.current_price = this.current_price.setScale(2, RoundingMode.HALF_UP);
            this.prices.add(current_price);
            if(this.current_price.compareTo(this.minimal_price) < 0){
                this.minimal_price = this.current_price;
            }
        }
        // now compute % changes of currency
        BigDecimal percent;
        if(this.current_price.compareTo(this.started_price) > 0){
            percent = this.current_price.divide(this.started_price, 2, RoundingMode.HALF_DOWN);
        } else {
            percent = this.started_price.divide(this.current_price, 2, RoundingMode.HALF_DOWN);
        }
        percent = percent.subtract(BigDecimal.valueOf(1));
        percent = percent.multiply(BigDecimal.valueOf(100));
        this.percentchanges.add(percent);
    }


    @Override
    public synchronized BigDecimal getPrice(){return this.current_price;}
    @Override
    public String getCurrency(){return this.currency;}
    @Override
    public synchronized BigDecimal getAmount(){return BigDecimal.valueOf(this.volume);}
    public synchronized int getVolume() {
        return this.volume;
    }
    public void setVolume(int volume) {
        this.volume = volume;
    }
    public String getCompany() {
        return this.company;
    }
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Increase volume of shares by volume parameter
     * @param volume
     */
    public synchronized void addVolume(int volume){this.volume += volume;}

    /**
     * Decrease volume of share by volume parameter
     * @param volume
     */
    public synchronized void subVolume(int volume){this.volume -= volume;}
}
