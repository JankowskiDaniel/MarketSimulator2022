package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Asset;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

import static javafx.scene.control.SelectionMode.MULTIPLE;

/**
 * Controller for AssetsLists.fxml file response for displaying table with all assets available in the world.
 */
public class AssetsLists extends Controller implements Initializable {
    TableView.TableViewSelectionModel<Asset> selectionModel;
    @FXML
    private TableView<Asset> table = new TableView<>();
    @FXML
    private TableColumn<Asset, String> name = new TableColumn();
    @FXML
    private TableColumn<Asset, Float> amount = new TableColumn();
    @FXML
    private TableColumn<Asset, String> market = new TableColumn();
    @FXML
    private TableColumn<Asset, Float> price = new TableColumn();
    private ObservableList<Asset> assets = FXCollections.observableArrayList();

    /**
     * Initialize method, which loads data to our tableview with assets
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<Asset, String>("name"));
        market.setCellValueFactory(new PropertyValueFactory<Asset, String>("market"));
        amount.setCellValueFactory(new PropertyValueFactory<Asset, Float>("amount"));
        price.setCellValueFactory(new PropertyValueFactory<Asset, Float>("price"));

        table.setItems(assets);
        selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(MULTIPLE);
    }

    /**
     * Load assets from the world
     */
    public void loadAssets(){
        Hashtable<String, Asset> storage = world.getAssetsLocker().getStorage();
        Enumeration<String> s = storage.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            this.assets.add(storage.get(key));
        }
    }

    /**
     * Switching scene into AssetsGraph.fxml and passing selected assets from the list.
     * @param event
     * @throws IOException
     */
    public void switchGraph(ActionEvent event) throws IOException {
        ObservableList<Asset> selectedAssets = selectionModel.getSelectedItems();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("AssetsGraph.fxml"));
        root = loader.load();
        AssetsGraph controller = loader.getController();
        controller.setWorld(this.world);
        controller.setGraph(selectedAssets);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
