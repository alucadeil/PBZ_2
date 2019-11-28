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

public class ControllerRegistration implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> col3,col4,col6, col7, col8,col9,col10;
    @FXML
    private TableColumn<User, Integer> col1 ,col2,col5;
    @FXML
    private TextField number, code,name,task;
    @FXML
    private DatePicker create_date, registration_date, end_date;
    @FXML
    private ChoiceBox type,executor;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private String[] t={"распоряжение","приказ"};

    private void createTable() {
        table.getItems().clear();

        type.getItems().addAll(t);

        col1.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<User, Integer>("number"));
        col3.setCellValueFactory(new PropertyValueFactory<User, String>("create_date"));
        col4.setCellValueFactory(new PropertyValueFactory<User, String>("registration_date"));
        col5.setCellValueFactory(new PropertyValueFactory<User, Integer>("code"));
        col6.setCellValueFactory(new PropertyValueFactory<User, String>("type"));
        col7.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col8.setCellValueFactory(new PropertyValueFactory<User, String>("executor"));
        col9.setCellValueFactory(new PropertyValueFactory<User, String>("end_date"));
        col10.setCellValueFactory(new PropertyValueFactory<User, String>("task"));

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM document");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(1), rs.getInt(2), rs.getDate(3).toLocalDate(),
                        rs.getDate(4).toLocalDate(), rs.getInt(5), rs.getString(6),
                        rs.getString(7), rs.getString(8), rs.getDate(9).toLocalDate(),rs.getString(10)));
            }
            table.setItems(usersData);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM executor");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                executor.getItems().addAll(rs.getString(2));
            }
            table.setItems(usersData);
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
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO document(number,create_date,registration_date,code,type,name,id_executor,end_date,task) VALUES (?,?,?,?,?,?,?,?,?)");
        preparedStatement.setInt(1, Integer.parseInt(number.getText()));
        preparedStatement.setDate(2,  Date.valueOf(create_date.getValue()));
        preparedStatement.setDate(3,  Date.valueOf(registration_date.getValue()));
        preparedStatement.setInt(4, Integer.parseInt(code.getText()));
        preparedStatement.setString(5, type.getValue().toString());
        preparedStatement.setString(6, name.getText());
        preparedStatement.setInt(7, ID);
        preparedStatement.setDate(8,  Date.valueOf(end_date.getValue()));
        preparedStatement.setString(9, task.getText());
        return preparedStatement;
    }

    public void save(ActionEvent actionEvent) {
        try(PreparedStatement preparedStatement = preparedStatementSave();){
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        createTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTable();
    }
}
