package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.util.ArrayList;

abstract public class Asset {
    protected volatile ArrayList<BigDecimal> prices;
    protected volatile ArrayList<BigDecimal> percentchanges;
    protected String name;
    protected volatile BigDecimal minimal_price, maximal_price, started_price;
    protected String market, type;

    Asset(String name, BigDecimal start_price, String market, String type) {
        this.name = name;
        this.minimal_price = start_price;
        this.maximal_price = start_price;
        this.started_price = start_price;
        this.market = market;
        this.type = type;
        this.percentchanges = new ArrayList<>();
        this.prices = new ArrayList<>();
        this.percentchanges.add(BigDecimal.valueOf(0));
    }

    public synchronized void updatePrice(boolean action, BigDecimal bound){
        this.prices.add(BigDecimal.valueOf(0));
    }
    public final String getName() {
        return this.name;
    }
    public final String getMarket(){ return this.market;}
    public BigDecimal getAmount(){ return null;}
    public BigDecimal getPrice(){return null;}
    public final BigDecimal GetMaxPrice() {
        return this.maximal_price;
    }
    public final BigDecimal GetMinPrice() {
        return this.minimal_price;
    }
    public final ArrayList<BigDecimal> getPrices(){return this.prices;}
    public final ArrayList<BigDecimal> getPercentchanges(){return this.percentchanges;}
    public void setAmount(BigDecimal am){};
    public String getType(){return this.type;}
    public String getCurrency(){return null;}






}
