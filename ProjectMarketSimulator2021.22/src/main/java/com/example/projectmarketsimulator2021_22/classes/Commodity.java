package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Random;

public class Commodity extends Asset {
    private String unit, trading_currency;
    private volatile BigDecimal current_price, volume;

    public Commodity(String name, String unit, String market, String currency) {
        super(name,new BigDecimal(BigInteger.valueOf(new Random().nextInt(2000)),2),  market, "commodity");
        this.unit = unit;
        this.current_price = this.maximal_price;
        this.volume = new BigDecimal(BigInteger.valueOf(new Random().nextInt(500000000)),2);
        this.trading_currency = currency;
        this.prices.add(current_price);
    }
    @Override
    public synchronized BigDecimal getAmount(){return this.volume;}

    @Override
    public synchronized BigDecimal getPrice(){return this.current_price;}

    @Override
    public String getCurrency(){return this.trading_currency;}

    /**
     * increase available volume of commodity
     * @param volume
     */
    public synchronized void addVolume(BigDecimal volume){this.volume = this.volume.add(volume);}

    /**
     * decrease available volume of commodity
     * @param volume
     */
    public synchronized void subVolume(BigDecimal volume){this.volume = this.volume.subtract(volume);}

    /**
     * Method for changing price of an asset, saving changes in arraylist prices and percentchanges
     * this method is called every time after exchange asset on the market.
     * @param action true = we increase price of an asset, false = decrease price of an asset
     * @param bound randomly generated difference, by which price of the asset is changing
     */
    @Override
    public synchronized void updatePrice(boolean action, BigDecimal bound){
        //price of asset will change in range (-2%;2%), its quite large, but I want to clearly see changes on the market
        BigDecimal ratiodiff = this.current_price.multiply(bound).setScale(2, RoundingMode.HALF_DOWN);
        if(action){
            //we increase price of an asset
            this.current_price = this.current_price.add(ratiodiff).setScale(2, RoundingMode.HALF_DOWN);
            this.prices.add(current_price);
            if(this.current_price.compareTo(this.maximal_price) > 0){
                this.maximal_price = this.current_price;
            }
        } else {
            //we decrease price of an asset
            this.current_price = this.current_price.subtract(ratiodiff).setScale(2, RoundingMode.HALF_DOWN);
            this.prices.add(current_price);
            if(this.current_price.compareTo(this.minimal_price) < 0){
                this.minimal_price = this.current_price;
            }
        }
        // now compute % changes of currency
        BigDecimal percent;
        if(this.current_price.compareTo(this.started_price) > 0){
            percent = this.current_price.divide(this.started_price, 2, RoundingMode.HALF_UP);
        } else {
            percent = this.started_price.divide(this.current_price, 2, RoundingMode.HALF_UP);
        }
        percent = percent.subtract(BigDecimal.valueOf(1));
        percent = percent.multiply(BigDecimal.valueOf(100));
        this.percentchanges.add(percent);
    }

    public String getUnit(){return this.unit;}
    public void setVolume(BigDecimal volume){this.volume = volume;}


}
