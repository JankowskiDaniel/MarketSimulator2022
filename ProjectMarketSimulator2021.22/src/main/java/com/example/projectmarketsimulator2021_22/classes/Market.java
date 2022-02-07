package com.example.projectmarketsimulator2021_22.classes;


import java.math.BigDecimal;

abstract public class Market{
    protected String name, country, city, address, type;
    protected BigDecimal operation_fee;

    Market(String name, String country, String city, String address, BigDecimal fee) {
        this.name = name;
        this.country = country;
        this.city = city;
        this.address = address;
        this.operation_fee = fee;
    }

    public String getName(){
        return this.name;
    }
    public String getType(){return this.type;}
    public String getCountry(){return this.country;}
    public String getCity(){return this.city;}
    public String getAddress(){return this.address;}
    public BigDecimal getOperation_fee(){return this.operation_fee;}
    public String getCurrency(){return "null";}

    /**
     * I decided that each market will be represented by the following rule "type@name",
     * e.g. we have Stock Market named 'Global Exchange', then toString method will return "Stock Market@Global Exchange'
     * @return String representation of the market
     */
    @Override
    public String toString(){
        return this.type+"@"+this.name;
    }
}