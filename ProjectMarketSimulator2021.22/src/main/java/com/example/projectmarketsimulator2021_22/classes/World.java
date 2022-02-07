package com.example.projectmarketsimulator2021_22.classes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Class, which holds whole created objects in the world (markets, investors, assets, indexes etc..)
 * State of this class is passed between the scenes.
 */
public class World{

    private String[] marketsTypes = new String[]{"Stock Market","Currency Market","Commodity Market"};
    private String[] investorTypes = new String[]{"Private Investor", "Fund"};
    private MarketsLocker marketsStorage;
    private InvestorsLocker investorsLocker;
    private AssetsLocker assetsLocker;
    private CompaniesLocker companiesLocker;
    private String dt = "2022-01-01";
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private boolean ifsimulating;
    private int speed;


    /**
     * Create a new instance of the world.
     */
    public World(){
        this.marketsStorage = new MarketsLocker();
        this.investorsLocker = new InvestorsLocker();
        this.assetsLocker = new AssetsLocker();
        this.companiesLocker = new CompaniesLocker();
        this.ifsimulating = false;
        this.speed = 0;
    }

    public void setSpeed(int value){
        if(value == 0){
            this.speed = 5000;
        } else if(value == 25){
            this.speed = 3500;
        } else if(value == 50){
            this.speed = 2500;
        } else if(value == 75){
            this.speed = 1200;
        } else {
            this.speed = 600;
        }

    }



    public String[] getMarketTypes(){
        return this.marketsTypes;
    }
    public String[] getInvestorTypes(){
        return this.investorTypes;
    }
    public AssetsLocker getAssetsLocker(){ return this.assetsLocker;}
    public MarketsLocker getMarketsStorage(){ return this.marketsStorage;}
    public InvestorsLocker getInvestorsLocker(){ return this.investorsLocker;}
    public CompaniesLocker getCompaniesLocker(){return this.companiesLocker;}
    public String getDt(){return this.dt;}
    public void setIfsimulating(boolean sim){this.ifsimulating = sim;}
    public boolean getIfsimulating(){return this.ifsimulating;}
    public int getSpeed(){return this.speed;}



}
