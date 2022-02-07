package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Index;
import com.example.projectmarketsimulator2021_22.classes.StockMarket;
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

import static javafx.scene.control.SelectionMode.SINGLE;

/**
 * Controller for IndexesLists.fxml response for displaying table with all indexes
 */
public class IndexesLists extends Controller implements Initializable {
    TableView.TableViewSelectionModel<Index> selectionModel;
    @FXML
    private TableView<Index> table = new TableView<>();
    @FXML
    private TableColumn<Index, String> name = new TableColumn();
    @FXML
    private TableColumn<Index, String> market = new TableColumn();


    private ObservableList<Index> indexes = FXCollections.observableArrayList();

    /**
     * Load data into the table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<Index, String>("name"));
        market.setCellValueFactory(new PropertyValueFactory<Index, String>("market"));
        table.setItems(indexes);
        selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SINGLE);
    }

    /**
     * Load indexes from the world.
     */
    public void loadIndexesList(){
        Hashtable<String, StockMarket> storage = world.getMarketsStorage().getSpecificType("Stock Market");
        Enumeration<String> s = storage.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            Hashtable<String, Index> marketindexes= storage.get(key).getIndexes();
            Enumeration<String> indexS = marketindexes.keys();
            while(indexS.hasMoreElements()){
                String key1 = indexS.nextElement();
                this.indexes.add(marketindexes.get(key1));
            }
        }
    }

    /**
     * Switch into scene with information about selected index.
     * @param event
     * @throws IOException
     */
    public void switchIndexInfo(ActionEvent event) throws IOException {
        ObservableList<Index> selected = selectionModel.getSelectedItems();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("IndexInfo.fxml"));
        root = loader.load();
        IndexInfo controller = loader.getController();
        controller.setWorld(this.world);
        controller.setIndex(selected);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
