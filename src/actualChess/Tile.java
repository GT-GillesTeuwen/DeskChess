/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Client
 */
public class Tile extends Rectangle {

    //Holds the piece that is on the tile (null if none)
    private Piece piece;
    //Holds true if black can attack this tile on the next move and false if not
    private boolean Bthreat = false;
    //Holds true if white can attack this tile on the next move
    private boolean Wthreat = false;

    /**
     *
     * @return Returns true if there is a piece on the tile and false if not
     */
    public boolean hasPiece() {
        return piece != null;
    }

    /**
     * Sets the piece on the tile to the piece that is parsed in
     *
     * @param piece Receives the piece that is now being placed on the tile
     */
    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    /**
     * @return Returns true if black can attack this tile on the next move and
     * false if not
     */
    public boolean getBThreat() {
        return Bthreat;
    }

    /**
     * Overrides the current black threat with the parsed in parameter
     *
     * @param Bthreat Receives true if black can attack this tile on the next
     * move and false if not
     */
    public void setBThreat(boolean Bthreat) {
        this.Bthreat = Bthreat;
    }

    /**
     * @return Returns true if white can attack this tile on the next move and
     * false if not
     */
    public boolean getWThreat() {
        return Wthreat;
    }

    /**
     *
     * Overrides the current white threat with the parsed in parameter
     *
     * @param Wthreat Receives true if White can attack this tile on the next
     * move and false if not
     *
     */
    public void setWThreat(boolean Wthreat) {
        this.Wthreat = Wthreat;
    }

    /**
     * Creates a tile of the specified color at the specified coordinates
     *
     * @param light Receives true if the tile has to be colored light and false
     * if it must be colored dark
     * @param x Receives the x coordinate at which the tile must be created
     * @param y Receives the y coordinate at which the tile must be created
     */
    public Tile(boolean light, int x, int y) {
        setWidth(ChessApp.TILE_SIZE);
        setHeight(ChessApp.TILE_SIZE);

        relocate((x * ChessApp.TILE_SIZE + (ChessApp.TILE_SIZE * 1.5)), (y * ChessApp.TILE_SIZE + (ChessApp.TILE_SIZE * 1.5)));

        setFill(light ? Color.valueOf("#ffce9eff") : Color.valueOf("#d18b47ff"));
    }

    /**
     *
     * @return Returns the piece that is currently on the tile
     */
    public Piece getPiece() {
        return piece;
    }
}
