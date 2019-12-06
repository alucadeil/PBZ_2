package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;
import sample.User;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControllerDocuments implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> col2, col3;
    @FXML
    private TableColumn<User, Integer> col1;
    @FXML
    private TextField id;
    @FXML
    private DatePicker end_date;
    private ObservableList<User> usersData = FXCollections.observableArrayList();

    private void createTable() {
        usersData.clear();
        table.getItems().clear();
        col1.setCellValueFactory(new PropertyValueFactory<User, Integer>("id_document"));
        col2.setCellValueFactory(new PropertyValueFactory<User, String>("end_date"));
        col3.setCellValueFactory(new PropertyValueFactory<User, String>("name"));

        try (PreparedStatement preparedStatementInner = conn.prepareStatement("SELECT id_document,end_date,name FROM document");
             ResultSet rs = preparedStatementInner.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(1), rs.getDate(2).toLocalDate(), rs.getString(3)));
            }
            table.setItems(usersData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementSave() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE document SET end_date=? WHERE id_document=?");
        preparedStatement.setDate(1, Date.valueOf(end_date.getValue()));
        preparedStatement.setInt(2, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void save(ActionEvent actionEvent) {
        try (PreparedStatement preparedStatement = preparedStatementSave();) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        createTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTable();
    }
}
