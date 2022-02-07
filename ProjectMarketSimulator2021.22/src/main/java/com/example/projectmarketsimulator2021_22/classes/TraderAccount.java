package com.example.projectmarketsimulator2021_22.classes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

public class TraderAccount {
    private AssetsLocker availableassets;
    private MarketsLocker availablemarkets;
    private ArrayList<Fund> availablefunds;
    private volatile Hashtable<String, Asset> assets;

    /**
     * Creating trader account. By default, each investor has USD currency at the beginning.
     * @param budget
     */
    public TraderAccount(BigDecimal budget){
        this.assets = new Hashtable<>();
        this.assets.put("USD", new Currency("Dolar", "USD", "null"));
        this.assets.get("USD").setAmount(budget);
        this.availableassets = new AssetsLocker();
        this.availablemarkets = new MarketsLocker();
        this.availablefunds = new ArrayList<>();

    }

    /**
     * Main exchange method.
     * Select random asset from wallet. For given type of asset we select all available
     * assets from the markets, which this asset can be traded for. From all this available for trading assets
     * we are randomly selecting one.
     * We call appropriate function for trade between this two assets.
     *
     */
    public void trade() {
        Asset random = randomTraderAsset();// random asset from wallet
        ArrayList<Asset> possibles = new ArrayList<>(); //this array will hold all possible asset from markets
        if(random.getType().equals("currency")){
            possibles = possibleCurrencyTrades((Currency) random, availableassets.getTest());
            Asset a = possibles.get(new Random().nextInt(possibles.size())); //randomly selected asset from all available for trade
            if(a.getType().equals("currency")){
                System.out.println("I will trade my "+random.getName()+" for "+a.getName()+" on "+a.getMarket());
                tradeCurrforCurr((Currency)random, (Currency) a);
            }
            if(a.getType().equals("commodity")){
                System.out.println("I will trade my "+random.getName()+" for "+a.getName()+" on "+a.getMarket());
                tradeCurrforComm((Currency) random, (Commodity) a);
            }
            if(a.getType().equals("share")){
                System.out.println("I will buy "+a.getName()+" shares on "+a.getMarket()+" for my"+random.getName());
                tradeCurrforShare((Currency)random, (Share)a);
            }
        } else if(random.getType().equals("commodity")){
            Commodity choosen = (Commodity) random;
            ArrayList<CommodityMarket> avmarkets = possibleCommodityTrades(choosen);
            System.out.println("I trade "+choosen.getName());
            tradeCommodity(choosen, avmarkets);
        } else if(random.getType().equals("share")){
            Share choosen = (Share)random;
            System.out.println("I trade shares of "+choosen.getName());
            tradeShare(choosen); //share can be trade only for one defined currency, therefore we don't have to choose any other asset.
        }
    }

    /**
     * Iterate through all assets on the markets and select from them, these which can be exchange for our currency.
     * @param walletasset this currency from our wallet we want to trade
     * @param allassets all available assets on the markets
     * @return all possibles assets to trade (it might be currencies, commodities and shares)
     */
    public ArrayList<Asset> possibleCurrencyTrades(Currency walletasset, ArrayList<Asset> allassets){
        ArrayList<Asset> possibles = new ArrayList<>();
        String cur = walletasset.getShortname();
        for (Asset a : allassets) {
            if (a.getType().equals("commodity") && a.getCurrency().equals(cur)) {
                possibles.add(a);
//                System.out.println("I can trade "+walletasset.getName()+" for "+a.getName());
            }
            if (a.getType().equals("share") && a.getCurrency().equals(cur)) {
                possibles.add(a);
//                System.out.println("I can trade "+walletasset.getName()+" for "+a.getName()+" shares!");
            }
            if(a.getType().equals("currency") && !((Currency)a).getShortname().equals(cur)){
                CurrencyMarket ownMarket = (CurrencyMarket)availablemarkets.getMarket("Currency Market@"+a.getMarket());
                if(ownMarket.getCurrencies().containsKey(cur)){
                    possibles.add(a);
//                    System.out.println("I can trade "+walletasset.getName()+" for "+a.getName()+" on "+ownMarket.getName());
                }
            }
        }
        return possibles;
    }

    /**
     * Trading currency from investor wallet for other currency available on the market.
     * To limit the rapid spending of the investor budget, there is a limit that at one time
     * investor can trade at most 20% of his currency.
     * @param walletcurr currency from investor's wallet
     * @param marketcurrency currency from market
     */
    public void tradeCurrforCurr(Currency walletcurr, Currency marketcurrency){
        //ceny marketowych w gore
        //na markecie cena walletcurr w doł, cena marketcurrency w góre
        BigDecimal bound = walletcurr.getAmount().multiply(BigDecimal.valueOf(0.2));
        //trade max. 20% of current amount.
        BigDecimal amount = generateRandomBigDecimalFromRange(BigDecimal.valueOf(0), bound);
        CurrencyMarket market = (CurrencyMarket) availablemarkets.getMarket("Currency Market@"+marketcurrency.getMarket());
        BigDecimal fee = market.getOperation_fee().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN);
        BigDecimal ex_ratio_wallet_curr = market.getCurrencies().get(walletcurr.getShortname()).getPrice();
        BigDecimal ex_ratio_market_curr = marketcurrency.getPrice();
        BigDecimal universalCurr = amount.multiply(ex_ratio_wallet_curr).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal universal_to_marketcurr = universalCurr.divide(ex_ratio_market_curr, 2, RoundingMode.HALF_DOWN);
        BigDecimal margintosub = universal_to_marketcurr.multiply(fee);
        BigDecimal amtouser = universal_to_marketcurr.subtract(margintosub);
        amtouser = amtouser.setScale(2, RoundingMode.HALF_DOWN);
        if(universal_to_marketcurr.compareTo(marketcurrency.getAmount()) < 0){
            //operation available, since we want to buy currency from market less than there is available
            String shortname = marketcurrency.getShortname();
            String name = marketcurrency.getName();
            marketcurrency.subAmount(amtouser); // we substract amount from the market
            walletcurr.subAmount(amount);
            market.getCurrencies().get(walletcurr.getShortname()).addAmount(amount);
            if(this.assets.containsKey(shortname)){
                //user already have this currency in his wallet, so just add amount
                ((Currency)this.assets.get(shortname)).addAmount(amtouser);
            } else {
                this.assets.put(shortname, new Currency(name, shortname, marketcurrency.getMarket()));
                ((Currency)this.assets.get(shortname)).setAmount(amtouser);
            }
            market.getCurrencies().get(walletcurr.getShortname()).updatePrice(false, new BigDecimal(BigInteger.valueOf(new Random().nextInt(200)),4));
            marketcurrency.updatePrice(true, new BigDecimal(BigInteger.valueOf(new Random().nextInt(200)),4));
        }
    }

    /**
     * Method for exchange currency from an investor's wallet for a given commodity on the market.
     * @param walletcurr currency from an investor's wallet
     * @param marketcomm commodity from market, which investor will buy.
     */
    public void tradeCurrforComm(Currency walletcurr, Commodity marketcomm){
        //ceny marketowych w gore
        BigDecimal bound = walletcurr.getAmount().multiply(BigDecimal.valueOf(0.2));
        BigDecimal amount = generateRandomBigDecimalFromRange(BigDecimal.valueOf(0), bound);
        CommodityMarket market= (CommodityMarket) availablemarkets.getMarket("Commodity Market@"+marketcomm.getMarket());
        BigDecimal fee = market.getOperation_fee().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN);
        BigDecimal ex_ratio_market_curr = marketcomm.getPrice();
        BigDecimal withoutfee = amount.multiply(ex_ratio_market_curr).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal margintosub = withoutfee.multiply(fee).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal amountfrommarket = withoutfee.subtract(margintosub);
        String commname = marketcomm.getName();
        String unit = marketcomm.getUnit();
        String currency = marketcomm.getCurrency();
        if(amountfrommarket.compareTo(marketcomm.getAmount()) < 0){
            marketcomm.subVolume(amountfrommarket);
            walletcurr.subAmount(amount);
            if(this.assets.containsKey(commname)){
                ((Commodity)this.assets.get(commname)).addVolume(amountfrommarket);

            } else {
                this.assets.put(commname, new Commodity(commname, unit, market.getName(), currency));
                ((Commodity)this.assets.get(commname)).setVolume(amountfrommarket);
            }
            BigDecimal change = new BigDecimal(BigInteger.valueOf(new Random().nextInt(500)),4);
            marketcomm.updatePrice(true, change);
        }


    }

    /**
     * Method for buying shares by an investor.
     * @param walletcurr
     * @param marketshare
     */
    public void tradeCurrforShare(Currency walletcurr, Share marketshare){
        //ceny marketowych w gore
        StockMarket market = (StockMarket)availablemarkets.getMarket("Stock Market@"+marketshare.getMarket());
        BigDecimal bound = walletcurr.getAmount().multiply(BigDecimal.valueOf(0.4));
        BigDecimal shareprice = marketshare.getPrice();
        BigDecimal amount = generateRandomBigDecimalFromRange(shareprice, bound);
        int no_share_buy = amount.divide(shareprice, 2, RoundingMode.DOWN).intValue();
        BigDecimal moneyspent_out_fee = BigDecimal.valueOf(no_share_buy).multiply(shareprice).setScale(2, RoundingMode.HALF_UP);
        BigDecimal fee = moneyspent_out_fee.multiply(market.getOperation_fee());
        BigDecimal fullspentmoney = moneyspent_out_fee.add(fee).setScale(2, RoundingMode.HALF_UP);
        String sharename = marketshare.getName();
        if(no_share_buy < marketshare.getVolume() && walletcurr.getAmount().compareTo(fullspentmoney) > 0 && no_share_buy > 0){
            marketshare.subVolume(no_share_buy);
            Company company = market.getCompany(marketshare.getCompany());
            company.addCapital(moneyspent_out_fee);
            company.addTotalvolume(no_share_buy);
            company.addTotalsales(moneyspent_out_fee);
            if(this.assets.containsKey(sharename)){
                ((Share)assets.get(sharename)).addVolume(no_share_buy);
            } else {
                String curr = company.getCurrency();
                String marketname = market.getName();
                this.assets.put(sharename, new Share(company.getName(), null, no_share_buy, marketname, curr));
            }
            BigDecimal change = new BigDecimal(BigInteger.valueOf(new Random().nextInt(500)),4);
            marketshare.updatePrice(true, change);
        }
    }

    /**
     * Choose randomly one of markets, on which we can trade our commodity
     * Perform trade on that market
     * @param mycommodity commodity from trader wallet.
     */
    public void tradeCommodity(Commodity mycommodity, ArrayList<CommodityMarket> avmarkets){
        CommodityMarket market = avmarkets.get(new Random().nextInt(avmarkets.size()));
        Commodity marketcom = market.getCommodity(mycommodity.getName());
        BigDecimal bound = mycommodity.getAmount().multiply(BigDecimal.valueOf(0.2));
        // how many commodity we trade
        BigDecimal volume = generateRandomBigDecimalFromRange(BigDecimal.valueOf(0), bound);
        // value of our trade
        BigDecimal amount = marketcom.getPrice().multiply(volume);
        mycommodity.subVolume(volume);
        marketcom.addVolume(volume);
        String cur = marketcom.getCurrency();

        BigDecimal afterfee = amount.multiply(market.getOperation_fee()).setScale(2, RoundingMode.HALF_DOWN);
        if(this.assets.containsKey(cur)){
            ((Currency)this.assets.get(cur)).addAmount(afterfee);
        } else {
            this.assets.put(cur, new Currency(cur, cur, marketcom.getMarket()));
            ((Currency)assets.get(cur)).setAmount(afterfee);
        }
        BigDecimal change = new BigDecimal(BigInteger.valueOf(new Random().nextInt(200)),4);
        marketcom.updatePrice(false, change);
    }

    /**
     * Trade random volume of share from our wallet into currency, in which this share is representing.
     * Number of shares, which we sell, coming back to the market.
     * @param myshare share from wallet
     */
    public void tradeShare(Share myshare){
        StockMarket market = (StockMarket) availablemarkets.getMarket("Stock Market@"+myshare.getMarket());
        Company company = market.getCompany(myshare.getCompany());
        int tradeVolume = (int) ((Math.random() * (myshare.getVolume()-0)) + 0);
        myshare.subVolume(tradeVolume);
        company.getCompanyshares().addVolume(tradeVolume);
        BigDecimal amount = company.getCompanyshares().getPrice().multiply(BigDecimal.valueOf(tradeVolume));
        String cur = company.getCurrency();
        BigDecimal afterfee = amount.multiply(market.getOperation_fee()).setScale(2, RoundingMode.HALF_DOWN);
        ((Currency)this.assets.get(cur)).addAmount(afterfee);
        company.addTotalsales(amount);
        company.addTotalvolume(tradeVolume);
        BigDecimal change = new BigDecimal(BigInteger.valueOf(new Random().nextInt(200)),4);
        company.getCompanyshares().updatePrice(false, change);

    }

    /**
     * Iterate through all commodity markets and check which of them has our commodity.
     * @param mycommodity
     * @return all commodity markets on which we can trade our commodity
     */
    public ArrayList<CommodityMarket> possibleCommodityTrades(Commodity mycommodity){
        ArrayList<CommodityMarket> avmarkets = new ArrayList<>();
        Hashtable<String, Market> commarkets = availablemarkets.getSpecificType("Commodity Market");
        Enumeration<String> s = commarkets.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            if(((CommodityMarket)commarkets.get(key)).getCommodities().containsKey(mycommodity.getName())){
                avmarkets.add((CommodityMarket)commarkets.get(key));
            }
        }
        return avmarkets;
    }

    /**
     * Main trading method to perform trade between private investor and fund
     */
    public void tradeThroughFund(){
        Asset random = randomTraderAsset();// random asset from wallet
        ArrayList<Asset> possibleAssets = new ArrayList<>();

        if(random.getType().equals("currency")){
            ArrayList<Fund> avfunds = getPossibleFund(random.getName());
            if(avfunds.size()> 0){
                Fund choosen = avfunds.get(new Random().nextInt(avfunds.size()));
                possibleAssets = troughFundPossibleCurr(random.getName(), choosen);
                Asset randomasset = possibleAssets.get(new Random().nextInt(possibleAssets.size()));
                if(choosen.getType().equals("currency")){
                    throughFundCurCur((Currency) random, (Currency) randomasset, choosen);
                } else if(choosen.getType().equals("commodity")){
                    throughFundCurCom((Currency) random, (Commodity) randomasset, choosen);
                } else {
                    throughFundCurShare((Currency) random, (Share) randomasset, choosen);
                }
            }
        } else if(random.getType().equals("commodity")){
            ArrayList<Fund> avfunds = getPossibleFund(random.getCurrency());
            if(avfunds.size() > 0){
                Fund choosen = avfunds.get(new Random().nextInt(avfunds.size()));
                throughFundTradeComm((Commodity) random, choosen);
            }

        } else {
            ArrayList<Fund> avfunds = getPossibleFund(random.getCurrency());
            if(avfunds.size() > 0){
                Fund choosen = avfunds.get(new Random().nextInt(avfunds.size()));
                throughFundTradeShare((Share)random, choosen);
            }

        }
    }

    /**
     * We check via which funds, private investor can exchange his assets (we check if given fund has an asset in wallet)
     * @param curr we want to trade this asset -> check if fund has it
     * @return array of fund via which we can trade an asset
     */
    public ArrayList<Fund> getPossibleFund(String curr){
        ArrayList<Fund> avfunds = new ArrayList<>();
        for(Fund f : this.availablefunds){
            if(f.getAccount().getAssets().containsKey(curr)){
                avfunds.add(f);
            }
        }
        return avfunds;
    }

    /**
     * Checks for which assets from funds wallet, private investor can trade his currency.
     * @param cur name of currency investor wants to trade
     * @param f fund, whose wallet we check
     * @return array of possible assets from fund wallet
     */
    public ArrayList<Asset> troughFundPossibleCurr(String cur, Fund f){
        ArrayList<Asset> possible = new ArrayList<>();
            Hashtable<String, Asset> fassets = f.getAccount().getAssets();
            if(fassets.containsKey(cur)){ //if fund has that currency then we can trade every asset from fund wallet with our currency
                Enumeration<String> s = fassets.keys();
                while(s.hasMoreElements()){
                    String key = s.nextElement();
                    if(!fassets.get(key).getName().equals(cur)){
                        possible.add(fassets.get(key));
                    }
                }
            }
        return possible;
    }

    /**
     * Trading currencies between private investor and fund
     * @param walletcurr currency from private investor wallet
     * @param fundcurr currency from fund wallet
     * @param owner fund as a owner of fundcurr currency
     */
    public void throughFundCurCur(Currency walletcurr, Currency fundcurr, Fund owner){
        BigDecimal bound = walletcurr.getAmount().multiply(BigDecimal.valueOf(0.2));
        //trade max. 20% of current amount.
        BigDecimal amount = generateRandomBigDecimalFromRange(BigDecimal.valueOf(0), bound);
        CurrencyMarket market1 = (CurrencyMarket) availablemarkets.getMarket("Currency Market@"+walletcurr.getMarket());
        CurrencyMarket market2 = (CurrencyMarket) availablemarkets.getMarket("Currency Market@"+fundcurr.getMarket());
        BigDecimal price1 = market1.getCurrencies().get(walletcurr).getPrice();
        BigDecimal price2 = market2.getCurrencies().get(fundcurr).getPrice();
        BigDecimal walletcurtouniversal = amount.multiply(price1).setScale(2, RoundingMode.HALF_DOWN);
        BigDecimal amountafterchange = walletcurtouniversal.divide(price2, 2, RoundingMode.HALF_DOWN);
        if(amountafterchange.compareTo(fundcurr.getAmount()) < 0){
            //we can perform trade
            walletcurr.subAmount(amount);
            fundcurr.subAmount(amountafterchange);
            ((Currency)owner.getAccount().getAssets().get(walletcurr.getName())).subAmount(amount);
            System.out.println("Traded "+walletcurr.getName()+" for "+fundcurr.getName()+" via: "+owner.getFundname());

            if(this.assets.containsKey(fundcurr.getName())){
                ((Currency)assets.get(fundcurr.getName())).addAmount(amountafterchange);
            } else {
                assets.put(fundcurr.getName(), new Currency(fundcurr.getName(), fundcurr.getName(), fundcurr.getMarket()));
                ((Currency)assets.get(fundcurr.getName())).setAmount(amountafterchange);
            }
        }

    }

    /**
     * Trading currency from private investor's wallet with commodity from fund wallet
     * @param walletcurr currency from private investor's wallet
     * @param fundcomm commodity from fund's wallet
     * @param owner fund, whose commodity we trade
     */
    public void throughFundCurCom(Currency walletcurr, Commodity fundcomm, Fund owner){
        if(fundcomm.getCurrency().equals(walletcurr.getName())){
            BigDecimal bound = walletcurr.getAmount().multiply(BigDecimal.valueOf(0.2));
            BigDecimal amount = generateRandomBigDecimalFromRange(BigDecimal.valueOf(0), bound);
            CommodityMarket market= (CommodityMarket) availablemarkets.getMarket("Commodity Market@"+fundcomm.getMarket());
            BigDecimal price = market.getCommodity(fundcomm.getName()).getPrice();
            BigDecimal volume = amount.multiply(price).setScale(2, RoundingMode.HALF_DOWN);
            if(volume.compareTo(fundcomm.getAmount())< 0){
                walletcurr.subAmount(amount);
                fundcomm.subVolume(volume);
                ((Currency)owner.getAccount().getAssets().get(walletcurr.getName())).addAmount(amount);
                System.out.println("Traded "+walletcurr.getName()+" for "+fundcomm.getName()+" via: "+owner.getFundname());
                if(this.assets.containsKey(fundcomm.getName())){
                    ((Commodity)assets.get(fundcomm.getName())).addVolume(volume);
                } else {
                    assets.put(fundcomm.getName(), new Commodity(fundcomm.getName(), fundcomm.getUnit(), fundcomm.getMarket(), fundcomm.getCurrency()));
                    ((Commodity)this.assets.get(fundcomm.getName())).setVolume(volume);
                }
            }
        }

    }

    /**
     * Trading currency from private investor's wallet with fund's shares
     * @param walletcurr currency from private investor's wallet
     * @param fundshare share from fund's wallet
     * @param owner fund, whose shares are traded
     */
    public void throughFundCurShare(Currency walletcurr, Share fundshare, Fund owner){
        if(fundshare.getCurrency().equals(walletcurr.getName())){
            BigDecimal bound = walletcurr.getAmount().multiply(BigDecimal.valueOf(0.2));
            BigDecimal amount = generateRandomBigDecimalFromRange(BigDecimal.valueOf(0), bound);
            StockMarket market = (StockMarket)availablemarkets.getMarket("Stock Market@"+fundshare.getMarket());
            Company company = market.getCompany(fundshare.getCompany());
            BigDecimal price = company.getCompanyshares().getPrice();
            int no_share_buy = amount.divide(price, 2, RoundingMode.DOWN).intValue();
            BigDecimal moneyspent = BigDecimal.valueOf(no_share_buy).multiply(price).setScale(2, RoundingMode.HALF_UP);
            if(moneyspent.compareTo(walletcurr.getAmount()) < 0 && no_share_buy > 0 && no_share_buy < fundshare.getVolume()){
                fundshare.subVolume(no_share_buy);
                ((Currency)owner.getAccount().getAssets().get(walletcurr.getName())).addAmount(moneyspent);
                walletcurr.subAmount(moneyspent);
                company.addTotalvolume(no_share_buy);
                System.out.println("Traded "+walletcurr.getName()+" for "+fundshare.getName()+" via: "+owner.getFundname());

                if(this.assets.containsKey(fundshare.getName())){
                    ((Share)assets.get(fundshare.getName())).addVolume(no_share_buy);
                } else {
                    String curr = company.getCurrency();
                    String marketname = market.getName();
                    this.assets.put(fundshare.getName(), new Share(company.getName(), null, no_share_buy, marketname, curr));
                }
            }
        }

    }

    /**
     * Trading commodity from private investor's wallet for money via fund
     * @param fromwallet commodity from private investor's wallet
     * @param selected via this fund we trade our commodity into money
     */
    public void throughFundTradeComm(Commodity fromwallet, Fund selected) {
        BigDecimal bound = fromwallet.getAmount().multiply(BigDecimal.valueOf(0.2));
        // how many commodity we trade
        BigDecimal volume = generateRandomBigDecimalFromRange(BigDecimal.valueOf(0), bound);
        CommodityMarket firstmarket = (CommodityMarket) availablemarkets.getMarket("Commodity Market@" + fromwallet.getMarket());
        BigDecimal price = firstmarket.getCommodity(fromwallet.getName()).getPrice();
        BigDecimal amount = price.multiply(volume).setScale(2, RoundingMode.HALF_DOWN);
        if(amount.compareTo(((Currency) selected.getAccount().getAssets().get(fromwallet.getCurrency())).getAmount())< 0 ){
            fromwallet.subVolume(volume);
            if(selected.getAccount().getAssets().containsKey(fromwallet.getName())){
                ((Commodity) selected.getAccount().getAssets().get(fromwallet.getName())).addVolume(volume);

            } else {
                selected.getAccount().getAssets().put(fromwallet.getName(), new Commodity(fromwallet.getName(), fromwallet.getUnit(), fromwallet.getMarket(), fromwallet.getCurrency()));
                ((Commodity)selected.getAccount().getAssets().get(fromwallet.getName())).setVolume(volume);
            }
            ((Currency) selected.getAccount().getAssets().get(fromwallet.getCurrency())).subAmount(amount);
            ((Currency) this.assets.get(fromwallet.getCurrency())).addAmount(amount);
            System.out.println("Traded "+fromwallet.getName()+" for "+fromwallet.getCurrency()+" via: "+selected.getFundname());
        }


    }

    /**
     * Trading shares from private investor's waller for money via fund
     * @param myshare shares from private investor's wallet
     * @param selected via this fund private investor will trade his share for money
     */
    public void throughFundTradeShare(Share myshare, Fund selected){
        int tradeVolume = (int) ((Math.random() * (myshare.getVolume()-0)) + 0);
        myshare.subVolume(tradeVolume);
        StockMarket market = (StockMarket) availablemarkets.getMarket("Stock Market@"+myshare.getMarket());
        Company company = market.getCompany(myshare.getCompany());
        BigDecimal price = company.getCompanyshares().getPrice();
        BigDecimal amount = price.multiply(BigDecimal.valueOf(tradeVolume)).setScale(2, RoundingMode.HALF_DOWN);
        if(amount.compareTo(((Currency)selected.getAccount().getAssets().get(myshare.getCurrency())).getAmount()) < 0){
            if(selected.getAccount().getAssets().containsKey(myshare.getName())){
                ((Share)selected.getAccount().getAssets().get(myshare.getName())).addVolume(tradeVolume);

            }else{
                selected.getAccount().getAssets().put(myshare.getName(), new Share(company.getName(), null, tradeVolume, market.getName(), myshare.getCurrency() ));
            }
            ((Currency)assets.get(myshare.getCurrency())).addAmount(amount);
            ((Currency)selected.getAccount().getAssets().get(myshare.getCurrency())).subAmount(amount);
            company.addTotalvolume(tradeVolume); //trading through fund also has effect on company trading volume
            System.out.println("Traded "+myshare.getName()+" for "+myshare.getCurrency()+" via "+selected.getName());
        }
    }



    /**
     * Randomly selecting an asset from our wallet (this asset we will trade)
     * @return random Asset
     */
    public Asset randomTraderAsset(){
        Asset randomAsset = new ArrayList<>(assets.values()).get(new Random().nextInt(assets.size()));
        return randomAsset;
    }
    /**
     * Increase investor's budget of investor by random USD amount.
     */
    public synchronized void IncreaseBudget() {
        BigDecimal amount = new BigDecimal(BigInteger.valueOf(new Random().nextInt(100000)),2);
        BigDecimal act = this.GetBudget();
        BigDecimal new_budget = act.add(amount).setScale(2, RoundingMode.HALF_DOWN);
        this.assets.get("USD").setAmount(new_budget);
    }





    /**
     * Since every investor's budget is represented in USD, we return from wallet with dollar currency
     * @return budget asset
     */
    public synchronized BigDecimal GetBudget() {
        return this.assets.get("USD").getAmount();
    }
    public synchronized void SetBudget(BigDecimal budget) {
        this.assets.get("USD").setAmount(budget);
    }

    /**
     * Method for generating random bigdecimal value.
     * @param min minimal value
     * @param max maximal value
     * @return random generated bigdecimal number in range (min,max)
     */
    public static BigDecimal generateRandomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
        BigDecimal randomBigDecimal = min.add(new BigDecimal(Math.random()).multiply(max.subtract(min)));
        return randomBigDecimal.setScale(2,BigDecimal.ROUND_HALF_UP);
    }

    public AssetsLocker getAvailableassets(){return this.availableassets;}
    public void setAvailableassets(AssetsLocker al){this.availableassets = al;}
    public void setAvailablemarkets(MarketsLocker ml){this.availablemarkets = ml;}
    public void setAvailablefunds(ArrayList<Fund> funds){this.availablefunds = funds;}
    public Hashtable<String, Asset> getAssets(){return this.assets;}

}
