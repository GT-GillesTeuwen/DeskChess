/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UnitTests;

import actualChess.ChessApp;
import deskchessMatchScreen.MatchScreenController;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author gteuw
 */
public class UnitTestController implements Initializable {

    private ChessApp app = new ChessApp();
    @FXML
    private AnchorPane anchorPane;

    /**
     * Initializes the controller_ class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        createBoard("rnbqkbnr#pppppppp#00QQ0000#00000000#00000000#00000000#PPPPPPPP#RNBQKBNR");
        changeBoard("rnbqkbnr#pppppppp#00000000#00000000#00000000#00000000#PPPPPPPP#RNBQKBNR");
        

    }

    public void createBoard(String board) {
        Pane pane = app.loadContent(board);
        anchorPane.getChildren().add(pane);
    }

    public void clearBoard() {
        anchorPane.getChildren().remove(0);
    }
    
    public void changeBoard(String board){
        clearBoard();
        createBoard(board);
    }

}
