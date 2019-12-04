package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;
import sample.User;

import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Check implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<User> table,table2;
    @FXML
    private TableColumn<User, String> col2, col4,col3,col6,col7,col8,col9;
    @FXML
    private TableColumn<User, Integer> col1,col5;
    @FXML
    private DatePicker end_date,date;
    @FXML
    private TextField days;
    private ObservableList<User> usersData = FXCollections.observableArrayList();

    private void createTable(){
        usersData.clear();
        table.getItems().clear();
        col1.setCellValueFactory(new PropertyValueFactory<User, Integer>("number"));
        col2.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col3.setCellValueFactory(new PropertyValueFactory<User, String>("type"));
        col4.setCellValueFactory(new PropertyValueFactory<User, String>("end_date"));

        try (PreparedStatement preparedStatementInner = conn.prepareStatement("SELECT * FROM document");
             ResultSet rs = preparedStatementInner.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(2),rs.getString(7),
                        rs.getString(6),rs.getDate(9).toLocalDate()));
            }
            table.setItems(usersData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

   private PreparedStatement prepareStatementChek() throws SQLException {
       DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
       LocalDate localDate = LocalDate.now();
       PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM document WHERE end_date=? and end_date<?");
       preparedStatement.setDate(1, Date.valueOf(end_date.getValue()));
       preparedStatement.setDate(2, Date.valueOf(localDate));
       return preparedStatement;
   }

    public void chek(ActionEvent actionEvent) {
        usersData.clear();
        try (PreparedStatement preparedStatementInner = prepareStatementChek();
             ResultSet rs = preparedStatementInner.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(2),rs.getString(7),
                        rs.getString(6),rs.getDate(9).toLocalDate()));
            }
            table.setItems(usersData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTable();
    }

    public void checkDays(ActionEvent actionEvent) {
        usersData.clear();
        table2.getItems().clear();
        col5.setCellValueFactory(new PropertyValueFactory<User, Integer>("number"));
        col6.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col7.setCellValueFactory(new PropertyValueFactory<User, String>("task"));
        col8.setCellValueFactory(new PropertyValueFactory<User, String>("fio"));
        col9.setCellValueFactory(new PropertyValueFactory<User, String>("position"));

        try (PreparedStatement preparedStatement = preparedStatementCheckDate();
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(2),rs.getString(7),
                        rs.getString(10),rs.getString(14),rs.getString(15)));
            }
            table2.setItems(usersData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementCheckDate() throws SQLException {
        LocalDate d=date.getValue();
        d=d.plusDays(Integer.parseInt(days.getText()));
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM document\n" +
                "inner join executor e on document.id_executor = e.id_executor WHERE end_date>?");
        preparedStatement.setDate(1, Date.valueOf(d));
        return preparedStatement;
    }
}
