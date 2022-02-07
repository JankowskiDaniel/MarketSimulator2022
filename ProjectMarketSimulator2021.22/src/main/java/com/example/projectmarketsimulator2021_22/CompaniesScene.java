package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Company;
import com.example.projectmarketsimulator2021_22.classes.Share;
import com.example.projectmarketsimulator2021_22.classes.StockMarket;

/**
 * Controller for CompaniesScene.fxml response for creating companies on given stock market
 */
public class CompaniesScene extends Panel {


    /**
     * Crate company on selected market
     */
    public void createCompany() {
        String selectedmarket = (String) this.markets.getValue();
        String companyname = primitiveName.getText();
        String marketid = "Stock Market@" + selectedmarket;
        String curr = ((StockMarket) world.getMarketsStorage().getMarkets().get(marketid)).getCurrency();
        Company company = new Company(companyname, selectedmarket, world.getDt(), curr );
        ((StockMarket) world.getMarketsStorage().getMarkets().get(marketid)).AddCompany(company);
        Share a = company.getCompanyshares();
        world.getAssetsLocker().addAsset(a, company.getName());
        world.getCompaniesLocker().addCompany(company, company.toString());
    }
}
