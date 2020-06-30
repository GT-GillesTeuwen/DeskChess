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

    /**
     * A text field that receives the name the user has chosen for the game
     */
    @FXML
    private TextField nameFld;

    /**
     * Button that allows the user to confirm their save name and triggers the
     * write to database
     */
    @FXML
    private Button saveBtn;

    /**
     * Label that displays any errors encountered by the save process
     */
    @FXML
    private Label errLbl;

    /**
     * Database manager object that facilitates the linking and saving of the
     * current board configuration
     */
    private DBManager db = new DBManager();

    /**
     * The ChessApp the user was playing in Used to get position and save
     */
    private ChessApp game;

    /**
     * The user currently attempting to save
     */
    private User user;

    /**
     * Initializes the controller class.
     */
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Sets the game and user like a constructor Constructor could not be used
     * because the class implements initializable
     *
     * @param game The ChessApp the user was previously playing in
     * @param user The user who was playing
     */
    public void setAll(ChessApp game, User user) {
        this.game = game;
        this.user = user;
    }

    /**
     * Executed when saveBtn is clicked Does basic validation (Check for
     * presence and uniqueness) then saves the board to the database, finally
     * closing the screen
     *
     * @param event
     * @throws SQLException
     * @throws IOException
     */
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

            Stage homeStage = (Stage) saveBtn.getScene().getWindow();
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
