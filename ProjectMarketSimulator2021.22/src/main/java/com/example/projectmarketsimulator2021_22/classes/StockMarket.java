package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class StockMarket extends Market {
    private Hashtable<String, Company> companies;
    private Hashtable<String, Index> indexes;
    private String currency;

    /**
     * Create a stock market
     * @param name
     * @param country
     * @param city
     * @param address
     * @param currency
     * @param fee
     */
    public StockMarket(String name, String country, String city, String address, String currency, BigDecimal fee) {
        super(name, country, city, address, fee);
        this.type = "Stock Market";
        this.companies = new Hashtable<>();
        this.indexes = new Hashtable<>();
        this.currency = currency;
    }

    /**
     * Add index to the market
     * @param name
     */
    public void addIndex(String name) {
        Index index = new Index(name, this.name);
        indexes.put(index.toString(), index);

    }

    /**
     * Add company to the market
     * @param company
     */
    public void AddCompany(Company company) {
        this.companies.put(company.getName(), company);
    }

    public Hashtable<String, Index> getIndexes(){
        return this.indexes;
    }
    public String getCurrency(){return this.currency;}
    public Company getCompany(String key){return this.companies.get(key);}

}
