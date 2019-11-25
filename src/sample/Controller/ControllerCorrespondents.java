package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import sample.Data;
import sample.Main;

import java.sql.Connection;

public class ControllerCorrespondents {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<Data> table;
    @FXML
    private TableColumn<Data, String>  col2, col3,col5, col6;
    @FXML
    private TableColumn<Data, Integer> col1, col4;
    @FXML
    private TextField id_inner, department, name, id_external, kod, name_org;
    @FXML
    private ObservableList<Data> DataAuto = FXCollections.observableArrayList();


    @FXML
    private void searchInner(){

    }

    @FXML
    private void deleteInner(){

    }

    @FXML
    private void saveInner(){

    }

    @FXML
    private void searchExternal(){

    }

    @FXML
    private void deleteExternal(){

    }

    @FXML
    private void saveExternal(){

    }
}
