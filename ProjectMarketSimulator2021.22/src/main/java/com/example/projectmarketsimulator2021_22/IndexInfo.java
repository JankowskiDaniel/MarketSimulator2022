package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Company;
import com.example.projectmarketsimulator2021_22.classes.Index;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.ResourceBundle;

/**
 * Controller for IndexInfo.fxml response for displaying information about selected index
 */
public class IndexInfo extends Controller implements Initializable {

    @FXML
    private Label title;
    private Index choosen;
    @FXML
    private Label noindexmsg;
    @FXML Label indexvalue;

    @FXML
    private TableView<Company> table = new TableView<>();
    @FXML
    private TableColumn<Company, String> name = new TableColumn();


    private ObservableList<Company> companies = FXCollections.observableArrayList();

    /**
     * Load data to the table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<Company, String>("name"));
        table.setItems(companies);
    }

    /**
     * Load index selected in the previous scene
     * @param selected
     */
    public void setIndex(ObservableList<Index> selected){
        if(selected.size() == 0){
            noindexmsg.setVisible(true);
        } else {
            this.choosen = selected.get(0);
            loadLabels();
            loadCompanies();

        }
    }

    /**
     * Load labels with proper text
     */
    public void loadLabels(){
        title.setVisible(true);
        title.setText(choosen.getName());
        table.setVisible(true);
        choosen.computeValue();
        indexvalue.setText("Index value: "+choosen.getValue());
        indexvalue.setVisible(true);
    }

    /**
     * Load companies for selected index
     */
    public void loadCompanies(){
        Hashtable<String, Company> companies = choosen.getCompanies();
        Enumeration<String> s = companies.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            this.companies.add(companies.get(key));
        }
    }
}
