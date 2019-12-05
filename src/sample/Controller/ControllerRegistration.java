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
    private TableColumn<User, String> col4,col5,col6, col7, col8,col9,col10;
    @FXML
    private TableColumn<User, Integer> col1 ,col2,col3;
    @FXML
    private TextField name,id_tasks;
    @FXML
    private DatePicker create_date, registration_date, end_date;
    @FXML
    private ChoiceBox type,author,controller;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private String[] t={"распоряжение","приказ"};

    private void createTable() {
        table.getItems().clear();

        type.getItems().addAll(t);

        col1.setCellValueFactory(new PropertyValueFactory<User, Integer>("id_document"));
        col2.setCellValueFactory(new PropertyValueFactory<User, Integer>("id_author"));
        col3.setCellValueFactory(new PropertyValueFactory<User, Integer>("id_controller"));
        col4.setCellValueFactory(new PropertyValueFactory<User, String>("id_tasks"));
        col5.setCellValueFactory(new PropertyValueFactory<User, String>("create_date"));
        col6.setCellValueFactory(new PropertyValueFactory<User, String>("registration_date"));
        col7.setCellValueFactory(new PropertyValueFactory<User, String>("end_date"));
        col8.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col9.setCellValueFactory(new PropertyValueFactory<User, String>("type"));
       // col10.setCellValueFactory(new PropertyValueFactory<User, String>("task"));

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM document");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(1), rs.getInt(2),rs.getInt(3),rs.getString(4),
                        rs.getDate(5).toLocalDate(), rs.getDate(6).toLocalDate(), rs.getDate(7).toLocalDate(),
                        rs.getString(8), rs.getString(9)));
            }
            table.setItems(usersData);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try (PreparedStatement preparedStatementA = conn.prepareStatement("SELECT * FROM employee WHERE role='автор'");
             PreparedStatement preparedStatementC = conn.prepareStatement("SELECT * FROM employee WHERE role='контроллер'");
             ResultSet rsA = preparedStatementA.executeQuery();
             ResultSet rsC = preparedStatementC.executeQuery();) {
            while (rsA.next()) {
                author.getItems().addAll(rsA.getString(2));
            }
            while (rsC.next()) {
                controller.getItems().addAll(rsC.getString(2));
            }
            table.setItems(usersData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementIdAuthor() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT id_employee FROM employee WHERE name=? and role='автор'");
        preparedStatement.setString(1, author.getValue().toString());
        System.out.println(preparedStatement);
        return  preparedStatement;
    }

    private PreparedStatement preparedStatementIdController() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT id_employee FROM employee WHERE name=? and role='контроллер'");
        preparedStatement.setString(1, controller.getValue().toString());
        System.out.println(preparedStatement);
        return  preparedStatement;
    }

    private PreparedStatement preparedStatementSave() throws SQLException {
        int IdA=0, IdC=0;
        try(PreparedStatement preparedStatementA = preparedStatementIdAuthor();
            PreparedStatement preparedStatementC = preparedStatementIdController();

            ResultSet rsA=preparedStatementA.executeQuery();
            ResultSet rsC=preparedStatementC.executeQuery();){

            while (rsA.next()){
                IdA=rsA.getInt(1);
            }
            while (rsC.next()){
                IdC=rsC.getInt(1);
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        String txt=id_tasks.getText();
        int number=txt.length();
        System.out.println(number);
        boolean end = txt.endsWith(",");
        if(end=true){
            txt=txt.substring(0,number-1);
        }

        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO document(id_author,id_controller,id_task,create_date,registration_date,end_date,name,type) VALUES (?,?,?,?,?,?,?,?)");
        preparedStatement.setInt(1, IdA);
        preparedStatement.setInt(2,  IdC);
        preparedStatement.setString(3, txt);
        preparedStatement.setDate(4, Date.valueOf(create_date.getValue()));
        preparedStatement.setDate(5, Date.valueOf(registration_date.getValue()));
        preparedStatement.setDate(6, Date.valueOf(end_date.getValue()));
        preparedStatement.setString(7, name.getText());
        preparedStatement.setString(8,  type.getValue().toString());
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
