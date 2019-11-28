package sample.Controller;

import javafx.event.ActionEvent;
import sample.Windows.*;

import java.io.IOException;

public class ControllerWinMain {


    public void OpenRegistration(ActionEvent actionEvent) throws IOException {
        WinRegistration registration = new WinRegistration();
    }

    public void OpenCorrespondents(ActionEvent actionEvent) throws IOException {
        WinCorrespondent correspondent = new WinCorrespondent();
    }

    public void OpenPerformed(ActionEvent actionEvent) throws IOException {
        WinPerformed performed = new WinPerformed();
    }


    public void OpenListDocuments(ActionEvent actionEvent) throws IOException {
        WinDocuments documents = new WinDocuments();
    }

    public void OpenChek(ActionEvent actionEvent) throws IOException {
        WinChek documents = new WinChek();
    }
}
