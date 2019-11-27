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


public class ControllerCorrespondents implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<User> table,table2;
    @FXML
    private TableColumn<User, String>  col2, col3, col6;
    @FXML
    private TableColumn<User, Integer> col1, col4,col5;
    @FXML
    private TextField id_inner, department, name, id_external, code, name_org;

    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private ObservableList<User> usersData2 = FXCollections.observableArrayList();


    private void createTable() throws SQLException{
        usersData.clear();
        usersData2.clear();
        col1.setCellValueFactory(new PropertyValueFactory<User, Integer>("id_inner"));
        col2.setCellValueFactory(new PropertyValueFactory<User, String>("department"));
        col3.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col4.setCellValueFactory(new PropertyValueFactory<User, Integer>("id_external"));
        col5.setCellValueFactory(new PropertyValueFactory<User, Integer>("code"));
        col6.setCellValueFactory(new PropertyValueFactory<User, String>("name_org"));
        table.getItems().clear();

        try (PreparedStatement preparedStatementInner = conn.prepareStatement("SELECT * FROM inner_guys");
             PreparedStatement preparedStatementExternal = conn.prepareStatement("SELECT * FROM external_guys");
             ResultSet rsInner = preparedStatementInner.executeQuery();
             ResultSet rsExternal = preparedStatementExternal.executeQuery();) {
            while (rsInner.next()) {
               usersData.add(new User(rsInner.getInt(1),rsInner.getString(2),rsInner.getString(3)));
            }
            table.setItems(usersData);

            while (rsExternal.next()) {
                usersData2.add(new User(rsExternal.getInt(1),rsExternal.getInt(2),rsExternal.getString(3)));
            }
            table2.setItems(usersData2);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementSearch() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM inner_guys WHERE id_inner=?");
        preparedStatement.setInt(1, Integer.parseInt(id_inner.getText()));
        return preparedStatement;
    }

    @FXML
    private void searchInner(){
        try(PreparedStatement preparedStatement = preparedStatementSearch();) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                department.setText(rs.getString(2));
                name.setText(rs.getString(3));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementDelete() throws SQLException{
        PreparedStatement   preparedStatement = conn.prepareStatement("DELETE FROM inner_guys CASCADE WHERE id_inner=?");
        preparedStatement.setInt(1, Integer.parseInt(id_inner.getText()));
        return preparedStatement;
    }

    @FXML
    private void deleteInner(){
        try(PreparedStatement preparedStatement = preparedStatementDelete();) {
            preparedStatement.executeUpdate();
            createTable();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementCount() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(id_inner) FROM inner_guys WHERE id_inner=?");
        preparedStatement.setInt(1, Integer.parseInt(id_inner.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementUpdate() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE inner_guys SET department=?,name=? WHERE id_inner=?");
        preparedStatement.setString(1, department.getText());
        preparedStatement.setString(2, name.getText());
        preparedStatement.setInt(3, Integer.parseInt(id_inner.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementSave() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO inner_guys(department,name) VALUES (?, ?)");
        preparedStatement.setString(1, department.getText());
        preparedStatement.setString(2, name.getText());
        return preparedStatement;
    }

    @FXML
    private void saveInner() throws SQLException {
        int count = 0;
        if (!id_inner.getText().equals("")) {
            try(PreparedStatement preparedStatement = preparedStatementCount();
                ResultSet rs = preparedStatement.executeQuery();){
                while (rs.next()) {
                    count = rs.getInt(1);
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count == 1) {
            try(PreparedStatement preparedStatement = preparedStatementUpdate();){
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (count == 0) {
            try(PreparedStatement preparedStatement = preparedStatementSave();){
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        createTable();
        id_inner.clear();
    }

    private PreparedStatement preparedStatementSearchExternal() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM external_guys WHERE id_external=?");
        preparedStatement.setInt(1, Integer.parseInt(id_external.getText()));
        return preparedStatement;
    }

    @FXML
    private void searchExternal(){
        try(PreparedStatement preparedStatement = preparedStatementSearchExternal();) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                code.setText(rs.getString(2));
                name_org.setText(rs.getString(3));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementDeleteExternal() throws SQLException{
        PreparedStatement   preparedStatement = conn.prepareStatement("DELETE FROM external_guys CASCADE WHERE id_external=?");
        preparedStatement.setInt(1, Integer.parseInt(id_external.getText()));
        return preparedStatement;
    }

    @FXML
    private void deleteExternal(){
        try(PreparedStatement preparedStatement = preparedStatementDeleteExternal();) {
            preparedStatement.executeUpdate();
            createTable();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementCountExternal() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(id_external) FROM external_guys WHERE id_external=?");
        preparedStatement.setInt(1, Integer.parseInt(id_external.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementUpdateExternal() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE external_guys SET code=?,name_org=? WHERE id_external=?");
        preparedStatement.setString(1, code.getText());
        preparedStatement.setString(2, name_org.getText());
        preparedStatement.setInt(3, Integer.parseInt(id_external.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementSaveExternal() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO external_guys(code,name_org) VALUES (?, ?)");
        preparedStatement.setInt(1, Integer.parseInt(code.getText()));
        preparedStatement.setString(2, name_org.getText());
        return preparedStatement;
    }

    @FXML
    private void saveExternal() throws SQLException {

        int count = 0;
        if (!id_external.getText().equals("")) {
            try(PreparedStatement preparedStatement = preparedStatementCountExternal();
                ResultSet rs = preparedStatement.executeQuery();){
                while (rs.next()) {
                    count = rs.getInt(1);
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count == 1) {
            try(PreparedStatement preparedStatement = preparedStatementUpdateExternal();){

                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (count == 0) {
            try(PreparedStatement preparedStatement = preparedStatementSaveExternal();){
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        createTable();
        id_external.clear();
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
