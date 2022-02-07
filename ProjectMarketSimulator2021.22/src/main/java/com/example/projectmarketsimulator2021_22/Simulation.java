package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Fund;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.ArrayList;

/**
 * Controller for Simulation.fxml response for starting and stopping simulation
 */
public class Simulation extends Controller{
    @FXML
    private Button start;
    @FXML
    private Button stop;

    /**
     * Properly displaying buttons for starting, stopping a simulation.
     */
    public void loadButtons(){
        if(world.getIfsimulating()){
            this.stop.setVisible(true);
            this.start.setVisible(false);
        } else {
            this.start.setVisible(true);
            this.stop.setVisible(false);
        }
    }

    /**
     * Method which starts our simulation.
     */
    public void start(){
        while(world.getAssetsLocker().getStorage().size() > world.getInvestorsLocker().getStorage().size()){
            world.getInvestorsLocker().createRandomInvestor();
        }
        world.getCompaniesLocker().startCompanies(world.getSpeed());
        ArrayList<Fund> funds = world.getInvestorsLocker().getFunds();

        world.getInvestorsLocker().startInvestors(world.getAssetsLocker(), world.getMarketsStorage(), funds, world.getSpeed());
        this.world.setIfsimulating(true);
        this.stop.setVisible(true);
        this.start.setVisible(false);

    }

    /**
     * Method which stops our simulation.
     */
    public void stop(){
        world.getInvestorsLocker().stopInvestors();
        world.getCompaniesLocker().stopCompanies();
        world.setIfsimulating(false);
        this.stop.setVisible(false);
        this.start.setVisible(true);
    }

}
