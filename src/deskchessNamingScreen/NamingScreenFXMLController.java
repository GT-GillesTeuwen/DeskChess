/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessNamingScreen;

import actualChess.ChessApp;
import actualChess.DBManager;

import chessjavafxmlbeta.User;
import deskchessHomeScreen.HomeScreenFXMLDocumentController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Client
 */
public class NamingScreenFXMLController implements Initializable {

    @FXML
    private TextField nameFld;
    @FXML
    private Button saveBTN;
    @FXML
    private Label errLbl;

    private DBManager db = new DBManager();
    private ChessApp game;
    private User user;

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setAll(ChessApp game, User user) {

        this.game = game;
        this.user = user;
    }

    @FXML
    private void saveGame(ActionEvent event) throws SQLException, IOException {
        ObservableList<String> info = db.getBoardNames(user.getUserName());
        boolean saved = false;

        if (errLbl.getText().equals("")) {
            errLbl.setText("Board must have a name");
        }
        if (info.contains(nameFld.getText())) {
            errLbl.setText("Board name must be unique");
        } else {
            game.save(user, nameFld.getText());

            Stage homeStage = (Stage) saveBTN.getScene().getWindow();
            homeStage.close();
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessHomeScreen/HomeScreenFXML.fxml"));
            Parent root = (Parent) loadWelcomeScreen.load();
            HomeScreenFXMLDocumentController match = loadWelcomeScreen.getController();
            match.setCurrentUser(user);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();

        }

    }

}
