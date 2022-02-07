package com.example.projectmarketsimulator2021_22.classes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class InvestorsLocker<T extends Investor>{
    private Hashtable<String, T> storage;
    public InvestorsLocker(){
        this.storage = new Hashtable<>();
    }

    /**
     * Add investor to the global locker
     * @param Investor
     * @param key
     */
    public void addInvestor(T Investor, String key){
        this.storage.put(key, Investor);
    }

    /**
     * Method which starts investor's work as a threads.
     * @param al available assets in the world
     * @param ml available markets in the world
     * @param funds available funds in the world.
     */
    public void startInvestors(AssetsLocker al, MarketsLocker ml, ArrayList<Fund> funds, int speed) {
        Enumeration<String> s = storage.keys();
        while (s.hasMoreElements()) {
            String key = s.nextElement();
            storage.get(key).getAccount().setAvailableassets(al);
            storage.get(key).getAccount().setAvailablemarkets(ml);
            storage.get(key).getAccount().setAvailablefunds(funds);

            storage.get(key).setWork(true);
            storage.get(key).setWorkspeed(speed);
            if (storage.get(key).getType().equals("Private Investor")) {
                (new Thread((PrivateInvestor) storage.get(key))).start();
            } else {
                (new Thread((Fund) storage.get(key))).start();
            }
        }
    }

    /**
     * Method which stops investors work.
     */
    public void stopInvestors() {
        Enumeration<String> s = storage.keys();
        while (s.hasMoreElements()) {
            String key = s.nextElement();
            storage.get(key).setWork(false);
        }
    }

    /**
     * Method which creates new, random investor. There is 70% chance that new investor will be a private investor
     * and 30% that it will be a fund. To make our world more real, name and lastname is randomly selected from the files
     * which contains real, different names and surnames of people from around the world.
     */
    public void createRandomInvestor(){
        int prob = (int)((Math.random()*(101-0))+0);
        String name = getRandomName();
        String lastname = getRandomSurname();
        if(prob<70){
            PrivateInvestor pi = new PrivateInvestor(name, lastname);
            addInvestor((T) pi, pi.toString());
        } else{
            Fund f = new Fund(name, lastname, name+lastname+"Fund");
            addInvestor((T)f, f.toString());
        }
    }

    /**
     * Extract funds from all investors
     * @return arraylist with all funds in the world.
     */
    public ArrayList<Fund> getFunds(){
        ArrayList<Fund>  funds= new ArrayList<>();
        Enumeration<String> s = storage.keys();
        while (s.hasMoreElements()) {
            String key = s.nextElement();
            if(storage.get(key).getType().equals("Fund")){
                funds.add((Fund)storage.get(key));
            }
        }
        return funds;
    }

    /**
     * Randomly select line from a given file
     * @param path path to our file
     * @return randomly selected String line from our file
     */
    private static String getRandomLine(String path) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        Random random = new Random();
        return lines.get(random.nextInt(lines.size()));
    }

    /**
     * Method for getting randomly selected name from name.txt file
     * @return String with random name
     */
    public static String getRandomName() {
        String path = new File("").getAbsolutePath() + "/src/names.txt";
        String randomLine = getRandomLine(path);
        return randomLine;
    }
    /**
     * Method for getting randomly selected surname from name.txt file
     * @return String with random surname
     */
    public static String getRandomSurname() {
        String path = new File("").getAbsolutePath() + "/src/surnames.txt";
        String randomLine = getRandomLine(path);
        return randomLine;
    }

    public Hashtable<String, T> getStorage(){ return this.storage;}
}
