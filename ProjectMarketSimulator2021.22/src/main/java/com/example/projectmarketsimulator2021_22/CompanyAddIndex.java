package com.example.projectmarketsimulator2021_22;

import com.example.projectmarketsimulator2021_22.classes.Company;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
 * Controller for CompanyAddIndex.fxml response for adding a company to the selected indexes.
 */
public class CompanyAddIndex extends Controller implements Initializable {
    private Company choosen;
    TableView.TableViewSelectionModel<Index> selectionModel;


    @FXML private Label title;
    @FXML
    private TableView<Index> index = new TableView<>();
    @FXML
    private TableColumn<Index, String> name = new TableColumn<>();
    @FXML private Label msg;
    @FXML private Button addInx;

    private ObservableList<Index> indexes = FXCollections.observableArrayList();


    /**
     * Load data into a table
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        name.setCellValueFactory(new PropertyValueFactory<Index, String>("name"));
        index.setItems(indexes);
        selectionModel = index.getSelectionModel();
        selectionModel.setSelectionMode(SINGLE);
    }

    /**
     * Set choosen company
     * @param choosen
     */
    public void setCompany(Company choosen){
        this.choosen = choosen;
    }

    /**
     * Load labels with proper text
     */
    public void loadLabels(){
        title.setText("Add "+choosen.getName()+" to the index(es)");
    }

    /**
     * Switch back into CompanyInfo scene.
     * @param event
     * @throws IOException
     */
    public void switchCompanyInfo(ActionEvent event) throws IOException {
        ObservableList<Company> selected = FXCollections.observableArrayList();
        selected.add(this.choosen);
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
     * Load indexes available for given company. Only those indices will be loaded that are available for the
     * stock exchange where the company is located and on which the company has not been added yet
     */
    public void loadIndexes(){
        StockMarket market = (StockMarket) world.getMarketsStorage().getMarket("Stock Market@"+choosen.getMarket());
        Hashtable<String, Index> indexes = market.getIndexes();
        Enumeration<String> s = indexes.keys();
        while(s.hasMoreElements()){
            String key = s.nextElement();
            if(!indexes.get(key).getCompanies().containsKey(choosen.getName())){
                this.indexes.add(indexes.get(key));
            }
        }
    }

    /**
     * Add company to selected index.
     */
    public void addToIndexes(){
        msg.setText("");
        ObservableList<Index> selected = selectionModel.getSelectedItems();
        if(selected.size() > 0){
            if(selected.get(0).getCompanies().containsKey(choosen.getName())){
                msg.setText("You've already added this company to this index!");
            } else {
                selected.get(0).AddCompany(choosen);
                msg.setText(choosen.getName()+" added to the index!");
            }
        }
    }
}
