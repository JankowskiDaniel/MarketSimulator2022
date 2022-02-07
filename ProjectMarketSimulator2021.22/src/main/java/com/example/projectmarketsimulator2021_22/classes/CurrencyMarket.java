package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.util.Hashtable;

public class CurrencyMarket extends Market {
    private Hashtable<String, Currency> currencies;

    /**
     *
     * @param name
     * @param country
     * @param city
     * @param address
     * @param fee applicable margin on the market
     */
    public CurrencyMarket(String name, String country, String city, String address, BigDecimal fee) {
        super(name, country, city, address, fee);
        this.type = "Currency Market";
        this.currencies = new Hashtable<>();
    }

    /**
     * Add currency to the market
     * @param currency
     */
    public void addCurrency(Currency currency){
        this.currencies.put(currency.getShortname(), currency);
    }

    public Hashtable<String, Currency> getCurrencies() {
        return this.currencies;
    }
}
