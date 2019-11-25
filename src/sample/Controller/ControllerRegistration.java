package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import sample.Data;
import sample.Main;

import java.sql.Connection;

public class ControllerRegistration {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String>   col3,col4,  col6, col7, col8;
    @FXML
    private TableColumn<Data, Integer> col1,col2,col5;
    @FXML
    private TextField number, code,name;
    @FXML
    private DatePicker create_date, registration_date;
    @FXML
    private ChoiceBox type,executor;
    private ObservableList<Data> DataAuto = FXCollections.observableArrayList();

    public void save(ActionEvent actionEvent) {
    }
}
