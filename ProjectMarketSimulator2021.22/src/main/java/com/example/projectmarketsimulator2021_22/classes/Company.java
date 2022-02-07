package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Company implements Runnable{
    private volatile String ipo_date, name, parentMarket, currency;
    private volatile BigDecimal ipoShareValue, open_price, current_price, revenue, capital, total_sale;
    private volatile Integer trading_volume;
    private volatile Integer no_shares;
    private boolean work;
    private volatile Share companyshares;
    private int workspeed;

    public Company(String name, String marketname, String date, String currency) {
        this.name = name;
        this.parentMarket = marketname;
        this.ipo_date = date;
        this.ipoShareValue = new BigDecimal(BigInteger.valueOf(new Random().nextInt(20000)),2);
        this.open_price = ipoShareValue;
        this.current_price = ipoShareValue;
        this.no_shares = (int) ((Math.random() * (1000 - 50)) + 50);
        this.capital = ipoShareValue.multiply(BigDecimal.valueOf(no_shares)).add(new BigDecimal(BigInteger.valueOf((int)(Math.random() * (2000000000 - 50000000 )) +50000000 ),2));
        this.trading_volume = 0;
        this.total_sale = new BigDecimal(BigInteger.valueOf(0));
        this.revenue = new BigDecimal(BigInteger.valueOf(0));
        this.currency = currency;
        this.work = false;
        companyshares = new Share(name, this.ipoShareValue, this.no_shares, marketname, currency);
        this.workspeed = 0;

    }

    public String getName() {
        return this.name;
    }

    /**
     * Main function which monitor work of company. In each iteration, there is a list of several operation,
     * which company can do with given probability.
     */
    public void run(){
        System.out.println("Company "+name+" starts!");
        while(work){
            int prob = (int)((Math.random()*(101-0))+0);
            if(prob<20){
                increaseShares();
            } else if(prob>=20 && prob <45){
                increaseRevenue();
            } else if(prob>=45 && prob<70){
                increaseCapital();
            } else if(prob>=70 && prob<85){
                decreaseRevenue();
            } else{
                decreaseCapital();
            }
            try {
                Thread.sleep(this.workspeed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Private: "+name+" stopped");
    }


    /**
     * Method for increasing company shares, which are available on the market.
     */
    public synchronized void increaseShares(){
        synchronized (this){
            int addshares = (int) ((Math.random() * (100 - 0)) + 0);
            this.companyshares.addVolume(addshares);
            this.no_shares += addshares;
            System.out.println("Company"+name+"increases their shares by "+addshares);
        }
    }

    /**
     * Method for increasing revenue company's value. This results in an increase in share prices about 0-10%
     */
    public synchronized void increaseRevenue(){
        synchronized (this){
            BigDecimal revenue = new BigDecimal(BigInteger.valueOf((int)(Math.random() * (100000000 - 10000000 )) +10000000 ),2);
            this.revenue = this.revenue.add(revenue);
            this.companyshares.updatePrice(true, new BigDecimal(BigInteger.valueOf(new Random().nextInt(1000)),4));
            System.out.println("Company "+name+" increases revenue by "+capital);
        }
    }

    /**
     * Method for decreasing revenue company's value. This results in a decrease in share prices about 0-8%
     */
    public synchronized void decreaseRevenue(){
        synchronized (this){
            BigDecimal revenue = new BigDecimal(BigInteger.valueOf((int)(Math.random() * (100000000 - 10000000 )) +10000000 ),2);
            if(this.revenue.compareTo(revenue) > 0){
                this.revenue = this.revenue.subtract(revenue);
                this.companyshares.updatePrice(false, new BigDecimal(BigInteger.valueOf(new Random().nextInt(800)),4));
                System.out.println("Company "+name+" decreases revenue by "+capital);
            }
        }
    }

    /**
     * Method for increasing company's capital. This results in an increase in share prices about 0-5%
     */
    public synchronized void increaseCapital(){
        synchronized (this){
            BigDecimal capital = new BigDecimal(BigInteger.valueOf((int)(Math.random() * (1000000000 - 100000000 )) +100000000 ),2);
            this.capital = this.capital.add(capital);
            this.companyshares.updatePrice(true, new BigDecimal(BigInteger.valueOf(new Random().nextInt(500)),4));
            System.out.println("Company "+name+" increases capital by "+capital);
        }



    }
    /**
     * Method for decreasing company's capital. This results in a decrease in share prices about 0-4%
     */
    public synchronized void decreaseCapital(){
        synchronized (this){
            BigDecimal capital = new BigDecimal(BigInteger.valueOf((int)(Math.random() * (1000000000 - 100000000 )) +100000000 ),2);
            if(this.capital.compareTo(capital) > 0){
                this.capital = this.capital.subtract(capital);
                this.companyshares.updatePrice(false, new BigDecimal(BigInteger.valueOf(new Random().nextInt(400)),4));
                System.out.println("Company "+name+" decreases capital by "+capital);
            }
        }
    }

    /**
     * Method for performing buy out operation, which is force by a user.
     * @param value - volume of shares, which will be buy out by company
     */
    public synchronized void buyout(Integer value){
        synchronized (this){
            companyshares.subVolume(value);
            BigDecimal ac_price = companyshares.getPrice();
            BigDecimal add_to_capital = ac_price.multiply(BigDecimal.valueOf(value)).setScale(2, RoundingMode.HALF_DOWN);
            this.capital = this.capital.add(add_to_capital);
        }
    }

    @Override
    public String toString(){
        return this.parentMarket+"@"+this.name;
    }
    public synchronized void addTotalvolume(int volume){this.trading_volume += volume;}
    public synchronized void addTotalsales(BigDecimal sales){this.total_sale = this.total_sale.add(sales);}
    public void setWork(boolean c){this.work = c; }
    public String getMarket() { return this.parentMarket;}
    public String getIpo_date(){ return this.ipo_date;}
    public BigDecimal getIpoShareValue(){return this.ipoShareValue;}
    public BigDecimal getOpen_price(){return this.open_price;}
    public BigDecimal getCurrent_price(){return this.getCompanyshares().getPrice();}
    public BigDecimal getRevenue(){return this.revenue;}
    public BigDecimal getCapital(){return this.capital;}
    public BigDecimal getTotal_sale(){return this.total_sale;}
    public Integer getNo_shares(){return this.companyshares.getVolume();}
    public String getCurrency(){return this.currency;}
    public synchronized Share getCompanyshares() {
        return companyshares;
    }
    public void setCompanyshares(Share companyshares) {
        this.companyshares = companyshares;
    }
    public synchronized void addCapital(BigDecimal capital){this.capital = this.capital.add(capital);}
    public void setWorkspeed(int value){this.workspeed = value;}
    public int getWorkspeed(){return this.workspeed;}
}
