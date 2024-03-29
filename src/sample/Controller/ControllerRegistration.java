package sample.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import sample.Main;
import sample.User;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class ControllerRegistration implements Initializable {
    @FXML
    public Text text;
    private Connection conn = Main.returnCon();
    @FXML
    private TableView<User> table;
    @FXML
    private TableColumn<User, String> col4, col5, col6, col7, col8, col9, col10;
    @FXML
    private TableColumn<User, Integer> col1, col2, col3;
    @FXML
    private TextField name, id_tasks;
    @FXML
    private DatePicker create_date, registration_date, end_date;
    @FXML
    private ChoiceBox type, author, controller;
    private ObservableList<User> usersData = FXCollections.observableArrayList();
    private String[] t = {"распоряжение", "приказ"};



    private void createTable() {
        type.getItems().clear();
        author.getItems().clear();
        controller.getItems().clear();
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

        try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT * FROM document");
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                usersData.add(new User(rs.getInt(1), rs.getInt(2), rs.getInt(3), searchIdTask(rs.getInt(1)),
                        rs.getDate(4).toLocalDate(), rs.getDate(5).toLocalDate(), rs.getDate(6).toLocalDate(),
                        rs.getString(7), rs.getString(8)));
            }
            table.setItems(usersData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String a="Задачи:";
        boolean bool=false;
        try (PreparedStatement preparedStatementA = conn.prepareStatement("SELECT * FROM employee WHERE role='автор'");
             PreparedStatement preparedStatementC = conn.prepareStatement("SELECT * FROM employee WHERE role='контроллер'");
             PreparedStatement preparedStatementTask = conn.prepareStatement("SELECT id_task FROM task");
             ResultSet rsA = preparedStatementA.executeQuery();
             ResultSet rsC = preparedStatementC.executeQuery();
             ResultSet rsTask = preparedStatementTask.executeQuery();) {
            while (rsA.next()) {
                author.getItems().addAll(rsA.getString(2));
            }
            while (rsC.next()) {
                controller.getItems().addAll(rsC.getString(2));
            }
            while (rsTask.next()) {
                if(bool==false){
                    a+=rsTask.getString(1);
                    bool=true;
                }else{
                    a+=","+rsTask.getString(1);
                }
            }
            text.setText(a);
            table.setItems(usersData);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    String searchIdTask(int id) {
        String txt="";
        boolean bool=false;
        try (PreparedStatement preparedStatement = preparedStatementSearchTask(id);
             ResultSet rs = preparedStatement.executeQuery();) {
            while (rs.next()) {
                if (bool==false) {
                    txt += rs.getString(1);
                    bool=true;
                }else{
                    txt +=","+rs.getString(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return txt;
    }

    private PreparedStatement preparedStatementSearchTask(int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT id_task FROM doc_task WHERE id_document=?");
        preparedStatement.setInt(1,id);
        return preparedStatement;
    }

    private PreparedStatement preparedStatementIdAuthor() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT id_employee FROM employee WHERE name=? and role='автор'");
        preparedStatement.setString(1, author.getValue().toString());
        System.out.println(preparedStatement);
        return preparedStatement;
    }

    private PreparedStatement preparedStatementIdController() throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("SELECT id_employee FROM employee WHERE name=? and role='контроллер'");
        preparedStatement.setString(1, controller.getValue().toString());
        System.out.println(preparedStatement);
        return preparedStatement;
    }

    private PreparedStatement preparedStatementSave() throws SQLException {
        int IdA = 0, IdC = 0;
        try (PreparedStatement preparedStatementA = preparedStatementIdAuthor();
             PreparedStatement preparedStatementC = preparedStatementIdController();
             ResultSet rsA = preparedStatementA.executeQuery();
             ResultSet rsC = preparedStatementC.executeQuery();) {

            while (rsA.next()) {
                IdA = rsA.getInt(1);
            }
            while (rsC.next()) {
                IdC = rsC.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO document(id_author,id_controller,create_date,registration_date,end_date,name,type) VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setInt(1, IdA);
        preparedStatement.setInt(2, IdC);
        preparedStatement.setDate(3, Date.valueOf(create_date.getValue()));
        preparedStatement.setDate(4, Date.valueOf(registration_date.getValue()));
        preparedStatement.setDate(5, Date.valueOf(end_date.getValue()));
        preparedStatement.setString(6, name.getText());
        preparedStatement.setString(7, type.getValue().toString());
        System.out.println(preparedStatement);
        return preparedStatement;
    }

    public void save(ActionEvent actionEvent) {
        int id_doc=0;
        try (PreparedStatement preparedStatement = preparedStatementSave();) {

            preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getGeneratedKeys();
            while (rs.next()) {
                id_doc = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String txt[]=id_tasks.getText().split(",");
        for (int i=0;i<txt.length;i++) {
            try (PreparedStatement preparedStatement = preparedStatementSaveTask(id_doc,txt[i]);) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        createTable();
    }

    private PreparedStatement preparedStatementSaveTask(int id_doc, String txt) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO doc_task(id_document,id_task) VALUES (?,?)");
        preparedStatement.setInt(1, id_doc);
        preparedStatement.setInt(2, Integer.parseInt(txt));
        System.out.println(preparedStatement);
        return preparedStatement;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        type.setTooltip(new Tooltip("Тип документа"));
        author.setTooltip(new Tooltip("Автор"));
        controller.setTooltip(new Tooltip("Контроллер"));
        createTable();
    }
}


