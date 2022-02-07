package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Investor;
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
 * Controller for InvestorsLists.fxml response for displaying table with all investor
 */
public class InvestorsLists extends Controller implements Initializable {
    TableView.TableViewSelectionModel<Investor> selectionModel;
    @FXML
    private TableView<Investor> table = new TableView<>();
    @FXML
    private TableColumn<Investor, String> name = new TableColumn();
    @FXML
    private TableColumn<Investor, String> lastname = new TableColumn();
    @FXML
    private TableColumn<Investor, String> type = new TableColumn();


    private ObservableList<Investor> investors = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<Investor, String>("name"));
        lastname.setCellValueFactory(new PropertyValueFactory<Investor, String>("lastname"));
        type.setCellValueFactory(new PropertyValueFactory<Investor, String>("type"));

        table.setItems(investors);
        selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SINGLE);
    }

    /**
     * Switch into scene, which will display information about selected investor
     * @param event
     * @throws IOException
     */
    public void switchInvestorInfo(ActionEvent event) throws IOException {
        ObservableList<Investor> selected = selectionModel.getSelectedItems();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("InvestorInfo.fxml"));
        root = loader.load();
        InvestorInfo controller = loader.getController();
        controller.setWorld(this.world);
        controller.setInvestor(selected);
        if(selected.size() > 0){
            controller.loadAssets();
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Load investors from the world.
     */
    public void loadInvestors(){
        Hashtable<String, Investor> storage = world.getInvestorsLocker().getStorage();
//        System.out.println(storage.size());
        Enumeration<String> s = storage.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            this.investors.add(storage.get(key));
        }
    }
}
