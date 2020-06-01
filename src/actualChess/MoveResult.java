/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

/**
 *
 * @author Client
 */
public class MoveResult {

    /**
     * Stores the type of move the piece is making
     */
    private MoveType type;

    /**
     * Stores the piece that is making the move
     */
    private Piece piece;

    /**
     * @return Returns the piece that is making the move
     */
    public Piece getPiece() {
        return piece;
    }

    /**
     * @return Returns the type of move that is being made
     */
    public MoveType getType() {
        return type;
    }

    /**
     * Creates a new MoveReult with the given type of move
     *
     * @param type Receives the type of move that is being made
     */
    public MoveResult(MoveType type) {
        this(type, null);
    }

    /**
     * Creates a new MoveReult with the given type of move and the piece that is
     * making the move
     *
     * @param type Receives the type of move that is being made
     * @param piece Receives the piece that is making the move
     */
    public MoveResult(MoveType type, Piece piece) {
        this.type = type;
        this.piece = piece;
    }

}
