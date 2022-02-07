package com.example.projectmarketsimulator2021_22.classes;

import java.util.Enumeration;
import java.util.Hashtable;

public class CompaniesLocker{
    private Hashtable<String, Company> storage;
    public CompaniesLocker(){
        this.storage = new Hashtable<>();
    }

    /**
     * Adding company to the global company locker
     * @param c
     * @param key
     */
    public void addCompany(Company c, String key){
        this.storage.put(key, c);
    }

    /**
     * This method is called when a simulation starts. Its starts each company as a thread.
     */
    public void startCompanies(int speed) {
        Enumeration<String> s = storage.keys();
        while (s.hasMoreElements()) {
            String key = s.nextElement();
            storage.get(key).setWork(true);
            storage.get(key).setWorkspeed(speed);
            Thread a  = new Thread(storage.get(key));
            a.setDaemon(true);
            a.start();
        }
    }

    /**
     * This method is called when simulation stops. Its stops each company
     */
    public void stopCompanies() {
        Enumeration<String> s = storage.keys();
        while (s.hasMoreElements()) {
            String key = s.nextElement();
            storage.get(key).setWork(false);
        }
    }
    public Hashtable<String, Company> getStorage(){ return this.storage;}
}

