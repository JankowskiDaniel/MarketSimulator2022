package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

public class Index {
    private String name;
    private String parentMarket;
    private BigDecimal value;
    private Hashtable<String, Company> companies;

    /**
     * Create a index
     * @param name
     * @param parent name of the stock market, on which that index exists.
     */
    Index(String name, String parent) {
        this.name = name;
        this.parentMarket = parent;
        this.companies = new Hashtable<>();
    }

    /**
     * Add company to the index
     * @param company
     */
    public void AddCompany(Company company) {
        this.companies.put(company.getName(), company);
    }

    /**
     * Compute acutal value of an index
     */
    public void computeValue(){
        this.value = BigDecimal.valueOf(0);
        Enumeration<String> s = this.companies.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            value = value.add(this.companies.get(key).getCapital());
        }
    }

    @Override
    public String toString(){
        return this.parentMarket+"@"+this.name;
    }

    public String getName(){return this.name;}
    public BigDecimal getValue(){return this.value;}
    public Hashtable<String, Company> getCompanies(){return this.companies;}
    public String getMarket(){return this.parentMarket;}



}
