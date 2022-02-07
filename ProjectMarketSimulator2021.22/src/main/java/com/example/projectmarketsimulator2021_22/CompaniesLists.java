package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Company;
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
 * Controller for CompaniesLists, which is response for properly displaying all companies available in the world.
 */
public class CompaniesLists extends Controller implements Initializable {
    TableView.TableViewSelectionModel<Company> selectionModel;
    @FXML
    private TableView<Company> table = new TableView<>();
    @FXML
    private TableColumn<Company, String> name = new TableColumn();
    @FXML
    private TableColumn<Company, String> market = new TableColumn();


    private ObservableList<Company> companies = FXCollections.observableArrayList();

    /**
     * Properly loads the data into table columns.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<Company, String>("name"));
        market.setCellValueFactory(new PropertyValueFactory<Company, String>("market"));
        table.setItems(companies);
        selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SINGLE);
    }

    /**
     * Switch to the CompanyInfo scene with selected company from the list
     * @param event
     * @throws IOException
     */
    public void switchCompanyInfo(ActionEvent event) throws IOException {
        ObservableList<Company> selected = selectionModel.getSelectedItems();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("CompanyInfo.fxml"));
        root = loader.load();
        CompanyInfo controller = loader.getController();
        controller.setWorld(this.world);
        controller.setCompany(selected);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Load companies from the world.
     */
    public void loadCompanies(){
        Hashtable<String, Company> storage = world.getCompaniesLocker().getStorage();
//        System.out.println(storage.size());
        Enumeration<String> s = storage.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            this.companies.add(storage.get(key));
        }
    }
}
