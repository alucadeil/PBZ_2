package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.Data;
import sample.Main;

import java.sql.Connection;

public class ControllerPerformed {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String> col2, col3,col4, col5, col7,col8 ,col9, col10,col12, col13, col14, col15;
    @FXML
    private TableColumn<Data, Integer> col1, col6, col11;
    @FXML
    private TextField email_executor, telephone_executor, name_executor, id_executor, email_author, telephone_author, id_author, name_author,
            email_controller, telephone_controller, name_controller, id_controller;
    @FXML
    private ChoiceBox position, position1, position2;
    private ObservableList<Data> DataAuto = FXCollections.observableArrayList();

    public void saveExecutor(ActionEvent actionEvent) {
    }

    public void deleteExecutor(ActionEvent actionEvent) {
    }

    public void searchExecutor(ActionEvent actionEvent) {
    }

    public void saveAuthor(ActionEvent actionEvent) {
    }

    public void deleteAuthor(ActionEvent actionEvent) {
    }

    public void searchAuthor(ActionEvent actionEvent) {
    }

    public void saveСontroller(ActionEvent actionEvent) {
    }

    public void deleteСontroller(ActionEvent actionEvent) {
    }

    public void searchСontroller(ActionEvent actionEvent) {
    }

}
