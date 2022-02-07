package com.example.projectmarketsimulator2021_22.classes;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

public class AssetsLocker<T extends Asset> {
    private Hashtable<String, T> storage;
    private ArrayList<T> test;
    public AssetsLocker(){
        this.storage = new Hashtable<>();
        this.test = new ArrayList<>();
    }

    /**
     * Add asset to the locker.
     * @param Asset
     * @param key
     */
    public void addAsset(T Asset, String key){
        this.storage.put(key, Asset);
        this.test.add(Asset);
    }

    public Hashtable<String, T> getStorage(){ return this.storage;}
    public ArrayList<T> getTest(){return this.test;}
}
