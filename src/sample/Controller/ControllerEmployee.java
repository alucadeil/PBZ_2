package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import sample.Main;
import sample.User;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControllerEmployee implements Initializable {
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> col2, col3, col4, col5, col6;
    @FXML
    private TableColumn<User, Integer> col1;
    @FXML
    private TextField id, name, telephone, email, department;
    @FXML
    private ChoiceBox position, role;
    private ObservableList<User> usersData = FXCollections.observableArrayList();


    public void createTable() {
        String[] pos = {"директор", "зам", "начальник"};
        String[] ch = {"автор", "контроллер", "исполнитель"};
        position.getItems().clear();
        position.getItems().addAll(pos);

        role.getItems().clear();
        role.getItems().addAll(ch);

        table.getItems().clear();
        usersData.clear();

        col1.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        col2.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        col3.setCellValueFactory(new PropertyValueFactory<User, String>("position"));
        col4.setCellValueFactory(new PropertyValueFactory<User, String>("telephone"));
        col5.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
        col6.setCellValueFactory(new PropertyValueFactory<User, String>("role"));

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM employee");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getString(5), rs.getString(6)));
            }
            table.setItems(usersData);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private PreparedStatement preparedStatementCountController() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT COUNT(id_employee) FROM employee WHERE id_employee=? ");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementUpdateController() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("UPDATE employee SET name=?,position=?,telephone=?," +
                "email=?  WHERE id_employee=?");
        preparedStatement.setString(1, name.getText());
        preparedStatement.setString(2, position.getValue().toString());
        preparedStatement.setString(3, telephone.getText());
        preparedStatement.setString(4, email.getText());
        preparedStatement.setInt(5, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    private PreparedStatement preparedStatementSaveController() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO employee(name,position,telephone,email,role) VALUES (?, ?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, name.getText());
        preparedStatement.setString(2, position.getValue().toString());
        preparedStatement.setString(3, telephone.getText());
        preparedStatement.setString(4, email.getText());
        preparedStatement.setString(5, role.getValue().toString());
        return preparedStatement;
    }

    public void save(ActionEvent actionEvent) {
        int count = 0, id_inner = 0;
        if (!id.getText().equals("")) {
            try (PreparedStatement preparedStatement = preparedStatementCountController();
                 ResultSet rs = preparedStatement.executeQuery();) {
                while (rs.next()) {
                    count = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (count == 1) {
            try (PreparedStatement preparedStatement = preparedStatementUpdateController();) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (count == 0) {
            try (PreparedStatement preparedStatement = preparedStatementSaveController();) {
                preparedStatement.executeUpdate();
                if (role.getValue().toString().equals("автор")) {
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    while (rs.next()) {
                        id_inner = rs.getInt(1);
                    }
                    try (PreparedStatement preparedStatementInner = preparedStatementSaveInner(id_inner);) {
                        preparedStatementInner.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        createTable();
        id.clear();
    }

    private PreparedStatement preparedStatementSaveInner(Integer id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO inner_employee(id_employee,department) VALUES (?, ?)");
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, department.getText());
        return preparedStatement;
    }

    private PreparedStatement preparedStatementDeleteController() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM employee CASCADE WHERE id_employee=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void delete(ActionEvent actionEvent) {
        try (PreparedStatement preparedStatement = preparedStatementDeleteController();) {
            preparedStatement.executeUpdate();
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private PreparedStatement preparedStatementSearchController() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM employee WHERE id_employee=?");
        preparedStatement.setInt(1, Integer.parseInt(id.getText()));
        return preparedStatement;
    }

    public void search(ActionEvent actionEvent) {
        try (PreparedStatement preparedStatement = preparedStatementSearchController();) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                name.setText(rs.getString(2));
                position.setValue(rs.getString(3));
                telephone.setText(rs.getString(4));
                email.setText(rs.getString(5));
                role.setValue(rs.getString(6));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createTable();
    }
}
