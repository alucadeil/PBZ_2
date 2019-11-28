package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.Main;
import sample.User;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ControllerPerformed implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> col2, col3,col4, col5, col6;
    @FXML
    private TableColumn<User, Integer> col1;
    @FXML
    private TextField id,name,telephone,email;
    @FXML
    private ChoiceBox position,choice;
    private ObservableList<User> usersData = FXCollections.observableArrayList();


    public void createTable(){
        String[] pos={"директор","зам","начальник"};
        String[] ch={"автор","контроллер","исполнитель"};
        position.getItems().clear();
        position.getItems().addAll(pos);

        choice.getItems().clear();
        choice.getItems().addAll(ch);

        table.getItems().clear();
        usersData.clear();

        col1.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col3.setCellValueFactory(new PropertyValueFactory<User, String>("position"));
        col4.setCellValueFactory(new PropertyValueFactory<User, String>("telephone"));
        col5.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        col6.setCellValueFactory(new PropertyValueFactory<User, String>("choice"));

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM executor");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(1),rs.getString(2),rs.getString(3),
                        rs.getString(4),rs.getString(5),rs.getString(6)));
            }
            table.setItems(usersData);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private PreparedStatement preparedStatementCountController() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(id_executor) FROM executor WHERE id_executor=? " +
                "and choice='controller'");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementUpdateController() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE executor SET name_controller=?,position=?,telephone=?," +
                "email=?  WHERE id_executor=?");
        preparedStatement.setString(1, name.getText());
        preparedStatement.setString(2, position.getValue().toString());
        preparedStatement.setString(3, telephone.getText());
        preparedStatement.setString(4, email.getText());
        preparedStatement.setInt(5, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementSaveController() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO executor(fio,position,telephone,email,choice) VALUES (?, ?,?,?,?)");
        preparedStatement.setString(1, name.getText());
        preparedStatement.setString(2, position.getValue().toString());
        preparedStatement.setString(3, telephone.getText());
        preparedStatement.setString(4, email.getText());
        preparedStatement.setString(5, choice.getValue().toString());
        return preparedStatement;
    }

    public void save(ActionEvent actionEvent) {
        int count = 0;
        if (!id.getText().equals("")) {
            try(PreparedStatement preparedStatement = preparedStatementCountController();
                ResultSet rs = preparedStatement.executeQuery();){
                while (rs.next()) {
                    count = rs.getInt(1);
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count == 1) {
            try(PreparedStatement preparedStatement = preparedStatementUpdateController();){
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (count == 0) {
            try(PreparedStatement preparedStatement = preparedStatementSaveController();){
                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                e.printStackTrace();
            }
        }
        createTable();
        id.clear();
    }

    private PreparedStatement preparedStatementDeleteController() throws SQLException{
        PreparedStatement   preparedStatement = conn.prepareStatement("DELETE FROM executor CASCADE WHERE id_exexutor=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void delete(ActionEvent actionEvent) {
        try(PreparedStatement preparedStatement = preparedStatementDeleteController();) {
            preparedStatement.executeUpdate();
            createTable();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementSearchController() throws SQLException{
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM executer WHERE id_executor=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void search(ActionEvent actionEvent) {
        try(PreparedStatement preparedStatement = preparedStatementSearchController();) {
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                name.setText(rs.getString(2));
                position.getItems().addAll(rs.getString(3));
                telephone.setText(rs.getString(4));
                email.setText(rs.getString(5));
                choice.getItems().addAll(rs.getString(6));
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTable();
    }
}
