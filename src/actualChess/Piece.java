/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Ellipse;
import static actualChess.ChessApp.TILE_SIZE;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

/**
 *
 * @author Client
 */
public class Piece extends StackPane {

    /**
     * Holds the colour of the piece(black or white)
     */
    private PieceColour colour;
    /**
     * Holds the type of piece (pawn, rook, knight, bishop, king or queen)
     */
    private PieceType type;

    /**
     * Holds the piece’s previous x coordinate
     *
     * Holds the piece’s previous y coordinate
     *
     */
    private double oldX, oldY;

    /**
     * Holds whether or not the piece has moved
     */
    private boolean moved = false;

    /**
     * @return Returns the color of the piece
     */
    public PieceColour getColour() {
        return colour;
    }

    /**
     * @return Returns the piece's type (pawn, rook, knight, bishop, king or
     * queen)
     */
    public PieceType getType() {
        return type;
    }

    /**
     * @return Returns the old x coordinate of the piece in case it needs to be
     * moved back to that position due to an invalid move
     */
    public double getOldX() {
        return oldX;
    }

    /**
     * @return Returns the old y coordinate of the piece in case it needs to be
     * moved back to that position due to an invalid move
     *
     */
    public double getOldY() {
        return oldY;
    }

    /**
     * @return Returns true if the piece has moved in this game and false of not
     */
    public boolean getMoved() {
        return moved;
    }

    /**
     * @param moved Receives true when the piece moves to ensure that moves that
     * can only be done on the first move, are only done on the first move
     */
    public void setMoved(boolean moved) {
        this.moved = moved;
    }

    /**
     * Creates a specific piece of a specific type and color at the specified
     * coordinates
     *
     * @param colour Receives the color of the piece that is to be created
     * @param type Receives the type of pieces that is to be created
     * @param x Receives the x coordinate on which the is is to be created
     * @param y Receives the y coordinate on which the is is to be created
     */
    public Piece(PieceColour colour, PieceType type, int x, int y, boolean moveable) {
        this.colour = colour;
        this.type = type;
        move(x, y);

        //Imports piece icons from icon folder
        Image BlackPawn = new Image("/icons/BlackPawn.png");
        Image WhitePawn = new Image("/icons/WhitePawn.png");
        Image BlackRook = new Image("/icons/BlackRook.png");
        Image WhiteRook = new Image("/icons/WhiteRook.png");
        Image BlackKnight = new Image("/icons/BlackKnight.png");
        Image WhiteKnight = new Image("/icons/WhiteKnight.png");
        Image BlackBishop = new Image("/icons/BlackBishop.png");
        Image WhiteBishop = new Image("/icons/WhiteBishop.png");
        Image BlackQueen = new Image("/icons/BlackQueen.png");
        Image WhiteQueen = new Image("/icons/WhiteQueen.png");
        Image BlackKing = new Image("/icons/BlackKing.png");
        Image WhiteKing = new Image("/icons/WhiteKing.png");

        ImageView bg = new ImageView();
        bg.setFitHeight(0.785 * ChessApp.TILE_SIZE);
        bg.setFitWidth(0.785 * ChessApp.TILE_SIZE);

        //Sets the correct image to the piece when it is displayed
        if (colour == PieceColour.BLACK) {
            if (null != type) {
                switch (type) {
                    case PAWN:
                        bg.setImage(BlackPawn);
                        break;
                    case ROOK:
                        bg.setImage(BlackRook);
                        break;
                    case KNIGHT:
                        bg.setImage(BlackKnight);
                        break;
                    case BISHOP:
                        bg.setImage(BlackBishop);
                        break;
                    case QUEEN:
                        bg.setImage(BlackQueen);
                        break;
                    case KING:
                        bg.setImage(BlackKing);
                        break;
                    default:
                        break;
                }
            }

        } else {
            if (null != type) {
                switch (type) {
                    case PAWN:
                        bg.setImage(WhitePawn);
                        break;
                    case ROOK:
                        bg.setImage(WhiteRook);
                        break;
                    case KNIGHT:
                        bg.setImage(WhiteKnight);
                        break;
                    case BISHOP:
                        bg.setImage(WhiteBishop);
                        break;
                    case QUEEN:
                        bg.setImage(WhiteQueen);
                        break;
                    case KING:
                        bg.setImage(WhiteKing);
                        break;
                    default:
                        break;
                }
            }
        }

        bg.setTranslateX(TILE_SIZE * 0.1);
        bg.setTranslateY(TILE_SIZE * 0.17);

        getChildren().addAll(bg);
        if (moveable) {
            setOnMouseDragged(e -> {
                relocate(e.getSceneX() - TILE_SIZE * 0.42850, e.getSceneY() - TILE_SIZE * 0.42850);
            });
        }

    }



    /**
     * Relocates the piece to the specified coordinates (only called on valid
     * moves)
     *
     * @param x Receives the x coordinate that the piece is to be moved to
     * @param y Receives the y coordinate that the piece is to be moved to
     */
    public void move(int x, int y) {
        oldX = x * TILE_SIZE + (ChessApp.TILE_SIZE * 1.5);
        oldY = y * TILE_SIZE + (ChessApp.TILE_SIZE * 1.5);
        relocate(oldX, oldY);
    }

    /**
     * Moves the piece back to its previous location in case of an invalid move
     */
    public void abortMove() {
        relocate(oldX, oldY);
    }
    
    /**
     * Allows the piece to be dragged and relocated
     */
    public void allowMove(){
         setOnMouseDragged(e -> {
                relocate(e.getSceneX() - TILE_SIZE * 0.42850, e.getSceneY() - TILE_SIZE * 0.42850);
            });
    }
    
    /**
     * Prevents the piece to be dragged and relocated
     */
    public void disallowMove(){
        setOnMouseDragged(e -> {
               
            });
    }

}
