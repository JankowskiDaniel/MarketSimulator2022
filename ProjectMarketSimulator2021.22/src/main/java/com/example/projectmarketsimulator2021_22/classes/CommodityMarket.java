package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.util.Hashtable;

public class CommodityMarket extends Market {
    private final Hashtable<String, Commodity> commodities;
    private final String currency;

    public CommodityMarket(String name, String country, String city, String address, String currency, BigDecimal fee) {
        super(name, country, city, address, fee);
        this.type = "Commodity Market";
        this.commodities = new Hashtable<>();
        this.currency = currency;
    }

    /**
     * Adding commodity to the market
     * @param commodity
     */
    public void addCommodity(Commodity commodity){
        this.commodities.put(commodity.getName(), commodity);
    }



    public Hashtable<String, Commodity> getCommodities() {
        return commodities;
    }
    public String getCurrency(){return this.currency;}
    public Commodity getCommodity(String name){return this.commodities.get(name);}

}
