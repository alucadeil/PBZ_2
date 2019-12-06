package sample.Controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.*;


import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class ControllerTask implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String>   col3;
    @FXML
    private TableColumn<User, Integer> col1,col2,col4;
    @FXML
    private TextField id, name,code;
    @FXML
    private ChoiceBox executor;

    private ObservableList<User> usersData = FXCollections.observableArrayList();

    private void createTable() throws SQLException{
        usersData.clear();
        executor.getItems().clear();
        col1.setCellValueFactory(new PropertyValueFactory<User, Integer>("id_task"));
        col2.setCellValueFactory(new PropertyValueFactory<User, Integer>("id_executor"));
        col3.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col4.setCellValueFactory(new PropertyValueFactory<User, Integer>("code"));
        table.getItems().clear();

        try (PreparedStatement preparedStatementInner = conn.prepareStatement("SELECT * FROM task");
             PreparedStatement preparedStatementExternal = conn.prepareStatement("SELECT * FROM employee WHERE role='исполнитель'");
             ResultSet rsInner = preparedStatementInner.executeQuery();
             ResultSet rsExternal = preparedStatementExternal.executeQuery();) {
            while (rsInner.next()) {
               usersData.add(new User(rsInner.getInt(1),rsInner.getInt(2),rsInner.getString(3),rsInner.getInt(4)));
            }
            table.setItems(usersData);

            while (rsExternal.next()) {
                executor.getItems().addAll(rsExternal.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementSearch() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM task WHERE id_task=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    @FXML
    private void SearchTask(){
        try(PreparedStatement preparedStatement = preparedStatementSearch();) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                name.setText(rs.getString(3));
                code.setText(rs.getString(4));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementDelete() throws SQLException{
        PreparedStatement   preparedStatement = conn.prepareStatement("DELETE FROM task CASCADE WHERE id_task=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    @FXML
    private void deleteInner() throws SQLException {
        try(PreparedStatement preparedStatement = preparedStatementDelete();) {
            preparedStatement.executeUpdate();
            createTable();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        createTable();
    }

    private PreparedStatement preparedStatementCount() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(id_task) FROM task WHERE id_task=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementUpdate() throws SQLException{
        int ID=preparedStatementIdExecutorSearch();
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE task SET code=?,name=?,id_executor=? WHERE id_task=?");
        preparedStatement.setInt(1, Integer.parseInt(code.getText()));
        preparedStatement.setString(2, name.getText());
        preparedStatement.setInt(3, ID);
        preparedStatement.setInt(4, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementSave(int id) throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO task(id_executor,name,code) VALUES (?, ?,?)");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2,name.getText());
        preparedStatement.setInt(3, Integer.parseInt(code.getText()));
        return preparedStatement;
    }


    private int preparedStatementIdExecutorSearch() {
        int idExecutor=0;
        try (PreparedStatement preparedStatement = preparedStatementIdExecutor();
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                idExecutor = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idExecutor;
    }

    private PreparedStatement preparedStatementIdExecutor() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT id_employee FROM employee WHERE name=?");
        preparedStatement.setString(1, executor.getValue().toString());
        return preparedStatement;
    }


    @FXML
    private void saveInner() throws SQLException {
        int count = 0;
        int idExecutor = 0;
        if (!id.getText().equals("")) {
            try (PreparedStatement preparedStatement = preparedStatementCount();
                 ResultSet rs = preparedStatement.executeQuery();) {
                while (rs.next()) {
                    count = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count == 1) {
            try (PreparedStatement preparedStatement = preparedStatementUpdate();) {
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (count == 0) {
            idExecutor=preparedStatementIdExecutorSearch();
            try (PreparedStatement preparedStatement = preparedStatementSave(idExecutor);) {
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        id.clear();
        createTable();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
