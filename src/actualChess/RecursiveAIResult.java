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
public class RecursiveAIResult {

    /**
     * Holds the board that returned with the temporary movements having been
     * made
     */
    private Tile[][] retBrd = new Tile[8][8];
    /**
     * Holds the type of move that was made to achieve this board
     */
    private MoveType type;
    /**
     * Holds the previous x coordinate of the piece that was moved to achieve
     * this board
     */
    private int oldX;
    /**
     * Holds the previous y coordinate of the piece that was moved to achieve
     * this board
     */
    private int oldY;
    /**
     * Holds the x coordinate that the piece that was moved to to achieve this
     * board
     */
    private int newX;
    /**
     * Holds the y coordinate that the piece that was moved to to achieve this
     * board
     */
    private int newY;
   
    /**
     * Holds the color of the piece that was moved to achieve this board White =
     * true Black = false
     */
    private boolean turn;

    /**
     * Creates a new RecursiveAIResult with the returned board, old and new
     * coordinates, type of move that was made, turn it was made on, and score
     * it resulted in
     *
     * @param retBrd Receives the board that was returned with the temporary
     * movements having been made
     * @param oldX Receives the previous x coordinate of the piece that was
     * moved to achieve this board
     * @param newX Receives the x coordinate that the piece that was moved to to
     * achieve this board
     * @param oldY Receives the previous y coordinate of the piece that was
     * moved to achieve this board
     * @param newY Receives the y coordinate that the piece that was moved to to
     * achieve this board
     * @param type Receives the type of move that was made to achieve this board
     * @param turn Receives the color of the piece that was moved to achieve
     * this board White=true Black=false
     */
    public RecursiveAIResult(Tile[][] retBrd, int oldX, int newX, int oldY, int newY, MoveType type, boolean turn) {
        //Copies the parsed in board to the attribute board
        Piece piece = new Piece(PieceColour.BLACK, PieceType.KNIGHT, newX, newY,false);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (retBrd[j][i].hasPiece() == true) {
                    this.retBrd[j][i] = new Tile(true, j, i);
                    this.retBrd[j][i].setPiece(retBrd[j][i].getPiece());

                } else {
                    this.retBrd[j][i] = new Tile(true, j, i);
                    this.retBrd[j][i].setPiece(null);
                }

            }
        }
        
        this.oldX = oldX;
        this.newX = newX;
        this.oldY = oldY;
        this.newY = newY;
        this.type = type;
        
        this.turn = turn;

    }

    /**
     *
     * @return Returns the board with the temporary movements having been made
     */
    public Tile[][] getRetBrd() {
        return retBrd;
    }

    /**
     *
     * @return Returns the previous x coordinate of the piece that was moved to
     * achieve this board
     */
    public int getOldX() {
        return oldX;
    }

    /**
     *
     * @return Returns the x coordinate that the piece that was moved to to
     * achieve this board
     */
    public int getNewX() {
        return newX;
    }

    /**
     *
     * @return Returns the previous y coordinate of the piece that was moved to
     * achieve this board
     */
    public int getOldy() {
        return oldY;
    }

    /**
     *
     * @return Returns the y coordinate that the piece that was moved to to
     * achieve this board
     */
    public int getNewy() {
        return newY;
    }

    /**
     *
     * @return Returns the type of move that was made to achieve this board
     */
    public MoveType getType() {
        return type;
    }

    /**
     *
     * @return Returns the color of the piece that was moved to achieve this
     * board White = true Black = false
     */
    public boolean isTurn() {
        return turn;
    }

}