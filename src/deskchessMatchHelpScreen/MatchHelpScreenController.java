/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deskchessMatchHelpScreen;

import chessjavafxmlbeta.User;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Client
 */
public class MatchHelpScreenController implements Initializable {

    @FXML
    private ImageView viewImageBG;

    private User currentUser;
    
    Image backIconImage = new Image("/icons/BackBtn.png");
    ImageView backImageView = new ImageView(backIconImage);
    
    Image DbackIconImage = new Image("/icons/DarkBackBtn.png");
    ImageView DbackImageView = new ImageView(DbackIconImage);
    
    Image homeHelpBgImage = new Image("/icons/HomeScreenHelp.png");
    ImageView homeHelpBgImageView = new ImageView(homeHelpBgImage);
    
    
    @FXML
    private Button backBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image bgGameScreenImage = new Image("/icons/MatchScreenHelp.png");
        viewImageBG.setImage(bgGameScreenImage);
        
        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);
    }

    public void setCurretnUser(User user){
        this.currentUser=user;
    }
    @FXML
    private void lightB(MouseEvent event) {
        backImageView.setFitWidth(179);
        backImageView.setFitHeight(48);
        backBtn.setGraphic(backImageView);
    }

    @FXML
    private void darkB(MouseEvent event) {
        DbackImageView.setFitWidth(179);
        DbackImageView.setFitHeight(48);
        backBtn.setGraphic(DbackImageView);
    }

    @FXML
    private void backToMatch(ActionEvent event) {
        Stage homeStage = (Stage) backBtn.getScene().getWindow();
        homeStage.close();
        //Pane scene = new Pane(ChessApp.createContent());
        
        
        /*try {
            ChessApp brd = new ChessApp();
            Pane boardPane = brd.createContent();
            FXMLLoader loadWelcomeScreen = new FXMLLoader(getClass().getResource("/deskchessMatchScreen/MatchScreen.fxml"));

            Pane root = loadWelcomeScreen.load();
            MatchScreenController match = loadWelcomeScreen.getController();
            match.setGame(brd);
            match.setCurrentUser(currentUser);
            root.getChildren().add(boardPane);
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
        }*/
    }

    
}
