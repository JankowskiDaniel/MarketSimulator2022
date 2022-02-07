package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.World;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Main controller class, from which all other controllers inherits
 */
public class Controller {
    protected World world = new World();
    protected Parent root;
    protected Stage stage;
    protected Scene scene;
    public Controller() {
    }

    /**
     * Set instace of the world. this method is called when any scene is changed.
     * @param world
     */
    public void setWorld(World world) {
        this.world = world;
    }

    /**
     * Switch to the MainScene.fxml
     * @param event
     * @throws IOException
     */
    public void switchMain(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml"));
        root = loader.load();
        Controller controller = loader.getController();
        controller.setWorld(this.world);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into Control Panel
     * @param event
     * @throws IOException
     */
    public void switchPanel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Panel.fxml"));
        root = loader.load();
        Panel controller = loader.getController();
        controller.setWorld(this.world);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into market creation form
     * @param event
     * @throws IOException
     */
    public void switchMarketsScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MarketsScene.fxml"));
        root = loader.load();
        MarketsScene controller = loader.getController();
        controller.setWorld(this.world);
        controller.loadSlider();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into companies creation form
     * @param event
     * @throws IOException
     */
    public void switchCompaniesScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompaniesScene.fxml"));
        root = loader.load();
        CompaniesScene controller = loader.getController();
        controller.setWorld(this.world);
        ArrayList names = world.getMarketsStorage().getTypeNames("Stock Market");
        controller.setComboBox(names);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    /**
     * Switch into assets lists
     */
    public void switchAssetsLists(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssetsLists.fxml"));
        root = loader.load();
        AssetsLists controller = loader.getController();
        controller.setWorld(this.world);
        controller.loadAssets();
//        controller.logAssets();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into asset creation form
     * @param event
     * @throws IOException
     */
    public void switchAssetsScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssetsScene.fxml"));
        root = loader.load();
        AssetsScene controller = loader.getController();
        controller.setWorld(this.world);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into index creation form
     * @param event
     * @throws IOException
     */
    public void switchIndexesScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IndexesScene.fxml"));
        root = loader.load();
        IndexesScene controller = loader.getController();
        controller.setWorld(this.world);
        ArrayList names = world.getMarketsStorage().getTypeNames("Stock Market");
        controller.setComboBox(names);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into investor creation form
     * @param event
     * @throws IOException
     */
    public void switchInvestorsScene(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InvestorsScene.fxml"));
        root = loader.load();
        InvestorsScene controller = loader.getController();
        controller.setWorld(this.world);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into scene with list of investors
     * @param event
     * @throws IOException
     */
    public void switchInvestorsLists(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("InvestorsLists.fxml"));
        root = loader.load();
        InvestorsLists controller = loader.getController();
        controller.setWorld(this.world);
        controller.loadInvestors();
//        controller.logAssets();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into the scene with table of companies
     * @param event
     * @throws IOException
     */
    public void switchCompaniesLists(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompaniesLists.fxml"));
        root = loader.load();
        CompaniesLists controller = loader.getController();
        controller.setWorld(this.world);
        controller.loadCompanies();
//        controller.logAssets();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into scene with list of markets
     * @param event
     * @throws IOException
     */
    public void switchMarketsLists(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MarketsLists.fxml"));
        root = loader.load();
        MarketsLists controller = loader.getController();
        controller.setWorld(this.world);
        controller.loadMarkets();
//        controller.logAssets();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into scene, where user can start/stop simulation
     * @param event
     * @throws IOException
     */
    public void switchSimulation(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Simulation.fxml"));
        root = loader.load();
        Simulation controller = loader.getController();
        controller.setWorld(this.world);
//        controller.logAssets();
        controller.loadButtons();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Switch into scene with list of indexes.
     * @param event
     * @throws IOException
     */
    public void switchIndexesLists(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("IndexesLists.fxml"));
        root = loader.load();
        IndexesLists controller = loader.getController();
        controller.setWorld(this.world);
        controller.loadIndexesList();
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }





}

