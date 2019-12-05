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
    private TableColumn<User, String> col2, col4;
    @FXML
    private TableColumn<User, Integer> col1, col3;
    @FXML
    private TextField id, id_executor;
    @FXML
    private ChoiceBox executor;
    @FXML
    private DatePicker end_date;
    private ObservableList<User> usersData = FXCollections.observableArrayList();

    private void createTable(){
        usersData.clear();
        table.getItems().clear();
        col1.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<User, String>("end_date"));
        col3.setCellValueFactory(new PropertyValueFactory<User, Integer>("id_executor"));
        col4.setCellValueFactory(new PropertyValueFactory<User, String>("name"));

        try (PreparedStatement preparedStatementInner = conn.prepareStatement("SELECT * FROM document\n" +
                "inner join executor e on document.id_executor = e.id_executor");
             PreparedStatement preparedStatementExternal = conn.prepareStatement("SELECT * FROM executor");
             ResultSet rs = preparedStatementInner.executeQuery();
             ResultSet rsExecutor = preparedStatementExternal.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(1),rs.getDate(9).toLocalDate(),rs.getInt(8),
                        rs.getString(11)));
            }
            table.setItems(usersData);

            while (rsExecutor.next()) {
                executor.getItems().addAll(rsExecutor.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private PreparedStatement preparedStatementID() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT id_executor FROM executor WHERE fio=?");
        preparedStatement.setString(1, executor.getValue().toString());
        return  preparedStatement;
    }

    private PreparedStatement preparedStatementSave() throws SQLException {
        int ID=0;
        try(PreparedStatement preparedStatement = preparedStatementID();
            ResultSet rs=preparedStatement.executeQuery();){
            while (rs.next()){
                ID=rs.getInt(1);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE document SET end_date=?,id_executor=? WHERE id_document=?");
        preparedStatement.setDate(1, Date.valueOf(end_date.getValue()));
        preparedStatement.setInt(2, ID);
        preparedStatement.setInt(3, Integer.parseInt(id.getText()));
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
