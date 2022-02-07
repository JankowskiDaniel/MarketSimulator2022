package com.example.projectmarketsimulator2021_22.classes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class MarketsLocker<T extends Market> {
    private Hashtable<String, T> storage;
    public MarketsLocker(){
        this.storage = new Hashtable<>();
    }

    /**
     * Add market to the locker
     * @param Market
     * @param key
     */
    public void addMarket(T Market, String key){
        this.storage.put(key, Market);
    }

    /**
     *
     * @param type type of market we want to extract from storage. Input should be exactly like names of classes
     * for markets, BUT with space character between words, e.g. "Stock Market" or "Currency Market"
     * @return hashtable of markets of given type extracted from storage of all markets
     */
    public Hashtable<String, Market> getSpecificType(String type){
        String[] marketType;
        Hashtable<String, Market> markets = new Hashtable<>();
        Enumeration<String> s = this.storage.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            marketType = this.storage.get(key).toString().split("@");
            if (marketType[0].equals(type)){
               markets.put(this.storage.get(key).toString(),this.storage.get(key));
            }
        }
        return markets;
    }

    /**
     * Method return names for specific, given type of markets. It's helpful in case of creating choicebox/combobox with specific markets names.
     * An argument type is always equal to any of market type, e.g. "Stock Market".
     * This method uses above method to get hashtable of markets of type we want to get. From that hashtable this method
     * only reads names of markets.
     * @param type like for method above.
     * @return
     */
    public ArrayList<String> getTypeNames(String type){
        Hashtable<String, Market> markets = getSpecificType(type);
        ArrayList<String> specificTypeMarkets = new ArrayList<>();
        Enumeration<String> s = markets.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            specificTypeMarkets.add(markets.get(key).getName());
        }
        return specificTypeMarkets;
    }

    public Market getMarket(String key){return this.storage.get(key);}
    public Hashtable<String, T> getMarkets(){
        return this.storage;
    }
}
