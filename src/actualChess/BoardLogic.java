/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import static actualChess.ChessApp.TILE_SIZE;

/**
 *
 * @author Client
 */
public class BoardLogic {

    /**
     * Holds the board that is currently being assessed for legal moves
     */
    private Tile[][] board;

    /**
     * Constructor, stores the given board parameter as the board being assessed
     *
     * @param board This is the board state as a 2d Tile array that is being
     * assessed for legal moves
     *
     */
    public BoardLogic(Tile[][] board) {
        this.board = board;
    }

    /**
     * Returns the tile position of where the mouse is (derived from pixel
     * location)
     *
     * @param pixel This receives the pixel location of the mouse to be
     * converted to a tile position on the board
     * @return The equivalent tile position of the parameter pixel is returned
     * as an integer
     */
    public int toBoard(double pixel) {
        return (int) (((pixel + TILE_SIZE / 2) / TILE_SIZE) - 1.5);
    }

    /**
     * Returns the pixel position of the current tile a piece is placed on.
     *
     * @param place This receives the position on the board to be converted to a
     * pixel coordinate
     * @return The equivalent pixel coordinate of the parameter place is
     * returned as a double
     */
    public double toPixel(double place) {
        return ((TILE_SIZE) * place + (TILE_SIZE / 2)) - 1.5;
    }

    /**
     * Creates a tile array to show which tiles can be attacked by each color on
     * the next move. Used to prevent illegally moving into check
     *
     * @param board This receives the current state of the game board to be
     * assessed.
     * @return This returns the board parsed as a parameter with the correct
     * tiles set as being threatened
     */
    public Tile[][] setTilesThreat(Tile[][] board) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                board[x][y].setBThreat(false);
                board[x][y].setWThreat(false);
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i].hasPiece()) {
                    Piece piece = board[j][i].getPiece();
                    for (int newy = 0; newy < 8; newy++) {
                        for (int newx = 0; newx < 8; newx++) {
                            if (null != piece.getType()) {
                                int x0 = toBoard(piece.getOldX());
                                int y0 = toBoard(piece.getOldY());
                                if (piece.getColour() == PieceColour.BLACK) {
                                    switch (piece.getType()) {
                                        case PAWN:
                                            if (Math.abs(newx - x0) == 1 && newy - y0 == piece.getColour().moveDir) {
                                                board[newx][newy].setBThreat(true);
                                            }
                                            break;
                                        case ROOK:
                                            if (Math.abs(newx - x0) == 0 || Math.abs(newy - y0) == 0) {
                                                if (isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                                    board[newx][newy].setBThreat(true);
                                                }

                                            }
                                            break;
                                        case KNIGHT:
                                            if (board[newx][newy].hasPiece() == false) {
                                                if ((Math.abs(newx - x0) == 2 && Math.abs(newy - y0) == 1) || (Math.abs(newx - x0) == 1 && Math.abs(newy - y0) == 2)) {
                                                    board[newx][newy].setBThreat(true);
                                                }
                                            }
                                            if (board[newx][newy].hasPiece() == true && board[newx][newy].getPiece().getColour() != piece.getColour()) {
                                                if ((Math.abs(newx - x0) == 2 && Math.abs(newy - y0) == 1) || (Math.abs(newx - x0) == 1 && Math.abs(newy - y0) == 2)) {
                                                    board[newx][newy].setBThreat(true);
                                                }
                                            }
                                            break;
                                        case BISHOP:
                                            if (Math.abs(newx - x0) == Math.abs(newy - y0) && isBishopBlocked(piece, newx, newy, x0, y0) == false) {
                                                board[newx][newy].setBThreat(true);
                                            }
                                            break;
                                        case QUEEN:
                                            if ((Math.abs(newx - x0) == Math.abs(newy - y0) || Math.abs(newx - x0) == 0 || Math.abs(newy - y0) == 0)) {
                                                if (isBishopBlocked(piece, newx, newy, x0, y0) == false && isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                                    board[newx][newy].setBThreat(true);
                                                }
                                            }
                                            break;
                                        case KING:
                                            if (piece.getType() == PieceType.KING) {
                                                if (Math.abs(newx - x0) < 2 && Math.abs(newy - y0) < 2) {
                                                    board[newx][newy].setBThreat(true);
                                                }
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                } else if (piece.getColour() == PieceColour.WHITE) {
                                    switch (piece.getType()) {
                                        case PAWN:
                                            if (Math.abs(newx - x0) == 1 && newy - y0 == piece.getColour().moveDir) {
                                                board[newx][newy].setWThreat(true);
                                            }
                                            break;
                                        case ROOK:
                                            if (Math.abs(newx - x0) == 0 || Math.abs(newy - y0) == 0) {
                                                if (isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                                    board[newx][newy].setWThreat(true);
                                                }

                                            }
                                            break;
                                        case KNIGHT:
                                            if (board[newx][newy].hasPiece() == false) {
                                                if ((Math.abs(newx - x0) == 2 && Math.abs(newy - y0) == 1) || (Math.abs(newx - x0) == 1 && Math.abs(newy - y0) == 2)) {
                                                    board[newx][newy].setWThreat(true);
                                                }
                                            }
                                            if (board[newx][newy].hasPiece() == true && board[newx][newy].getPiece().getColour() != piece.getColour()) {
                                                if ((Math.abs(newx - x0) == 2 && Math.abs(newy - y0) == 1) || (Math.abs(newx - x0) == 1 && Math.abs(newy - y0) == 2)) {
                                                    board[newx][newy].setWThreat(true);
                                                }
                                            }
                                            break;
                                        case BISHOP:
                                            if (Math.abs(newx - x0) == Math.abs(newy - y0) && isBishopBlocked(piece, newx, newy, x0, y0) == false) {
                                                board[newx][newy].setWThreat(true);
                                            }
                                            break;
                                        case QUEEN:
                                            if ((Math.abs(newx - x0) == Math.abs(newy - y0) || Math.abs(newx - x0) == 0 || Math.abs(newy - y0) == 0)) {
                                                if (isBishopBlocked(piece, newx, newy, x0, y0) == false && isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                                    board[newx][newy].setWThreat(true);
                                                }
                                            }
                                            break;
                                        case KING:
                                            if (piece.getType() == PieceType.KING) {
                                                if (Math.abs(newx - x0) < 2 && Math.abs(newy - y0) < 2) {
                                                    board[newx][newy].setWThreat(true);
                                                }
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        return board;
    }

    /**
     * Returns whether a pawn is capturing, moving or making an invalid move by
     * returning 1, 2 or 0 respectively
     *
     * @param piece Receives the piece being assessed for valid moves (only
     * called on pawns)
     * @param newx Receives the x coordinate that the piece is trying to move to
     * @param newy Receives the y coordinate that the piece is trying to move to
     * @param x0 Receives the x coordinate that the piece is moving from
     * @param y0 Receives the y coordinate that the piece is moving from
     * @return Returns whether a pawn is capturing, moving or making an invalid
     * move by returning 1, 2 or 0 respectively
     *
     */
    public int pawnMove(Piece piece, int newx, int newy, int x0, int y0) {
        
        if (board[newx][newy].hasPiece() == false) {
            if (Math.abs(newx - x0) == 0 && (newy - y0) == piece.getColour().moveDir) {
                return 1;
            }
            if (Math.abs(newx - x0) == 0 && (newy - y0) == piece.getColour().moveDir * 2 && piece.getMoved() == false) {
                if ((piece.getColour() == PieceColour.WHITE && y0 == 6) || (piece.getColour() == PieceColour.BLACK && y0 == 1)) {
                    return 1;
                }

            }

        }
        if (board[newx][newy].hasPiece() == true && board[newx][newy].getPiece().getColour() != piece.getColour()) {
            if (Math.abs(newx - x0) == 1 && newy - y0 == piece.getColour().moveDir) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * Returns whether a rook is capturing, moving or making an invalid move by
     * returning 1, 2 or 0 respectively
     *
     * @param piece Receives the piece being assessed for valid moves (only
     * called on rooks)
     * @param newx Receives the x coordinate that the piece is trying to move to
     * @param newy Receives the y coordinate that the piece is trying to move to
     * @param x0 Receives the x coordinate that the piece is moving from
     * @param y0 Receives the y coordinate that the piece is moving from
     * @return Returns whether a rook is capturing, moving or making an invalid
     * move by returning 1, 2 or 0 respectively
     *
     */
    public int rookMove(Piece piece, int newx, int newy, int x0, int y0) {

        if (Math.abs(newx - x0) == 0 || Math.abs(newy - y0) == 0) {

            if (board[newx][newy].hasPiece() == false) {
                return 1;
            }
            if (board[newx][newy].hasPiece() == true && board[newx][newy].getPiece().getColour() != piece.getColour()) {

                return 2;
            }
        }
        return 0;
    }

    /**
     * Returns whether a knight is capturing, moving or making an invalid move
     * by returning 1, 2 or 0 respectively
     *
     * @param piece Receives the piece being assessed for valid moves (only
     * called on knights)
     * @param newx Receives the x coordinate that the piece is trying to move to
     * @param newy Receives the y coordinate that the piece is trying to move to
     * @param x0 Receives the x coordinate that the piece is moving from
     * @param y0 Receives the y coordinate that the piece is moving from
     * @return Returns whether a knight is capturing, moving or making an
     * invalid move by returning 1, 2 or 0 respectively
     *
     */
    public int knightMove(Piece piece, int newx, int newy, int x0, int y0) {
        if (board[newx][newy].hasPiece() == false) {
            if ((Math.abs(newx - x0) == 2 && Math.abs(newy - y0) == 1) || (Math.abs(newx - x0) == 1 && Math.abs(newy - y0) == 2)) {
                return 1;
            }
        }
        if (board[newx][newy].hasPiece() == true && board[newx][newy].getPiece().getColour() != piece.getColour()) {
            if ((Math.abs(newx - x0) == 2 && Math.abs(newy - y0) == 1) || (Math.abs(newx - x0) == 1 && Math.abs(newy - y0) == 2)) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * Returns whether a bishop is capturing, moving or making an invalid move
     * by returning 1, 2 or 0 respectively
     *
     * @param piece Receives the piece being assessed for valid moves (only
     * called on bishops)
     * @param newx Receives the x coordinate that the piece is trying to move to
     * @param newy Receives the y coordinate that the piece is trying to move to
     * @param x0 Receives the x coordinate that the piece is moving from
     * @param y0 Receives the y coordinate that the piece is moving from
     * @return Returns whether a bishop is capturing, moving or making an
     * invalid move by returning 1, 2 or 0 respectively
     *
     */
    public int bishopMove(Piece piece, int newx, int newy, int x0, int y0) {

        if (Math.abs(newx - x0) == Math.abs(newy - y0)) {
            if (board[newx][newy].hasPiece() == false) {
                return 1;
            }
            if (board[newx][newy].hasPiece() == true && board[newx][newy].getPiece().getColour() != piece.getColour()) {

                return 2;
            }
        }

        return 0;
    }

    /**
     * Returns whether a queen is capturing, moving or making an invalid move by
     * returning 1, 2 or 0 respectively
     *
     * @param piece Receives the piece being assessed for valid moves (only
     * called on queens)
     * @param newx Receives the x coordinate that the piece is trying to move to
     * @param newy Receives the y coordinate that the piece is trying to move to
     * @param x0 Receives the x coordinate that the piece is moving from
     * @param y0 Receives the y coordinate that the piece is moving from
     * @return Returns whether a queen is capturing, moving or making an invalid
     * move by returning 1, 2 or 0 respectively
     *
     */
    public int queenMove(Piece piece, int newx, int newy, int x0, int y0) {
        if (Math.abs(newx - x0) == Math.abs(newy - y0) || Math.abs(newx - x0) == 0 || Math.abs(newy - y0) == 0) {
            if (board[newx][newy].hasPiece() == false) {
                return 1;
            }
            if (board[newx][newy].hasPiece() == true && board[newx][newy].getPiece().getColour() != piece.getColour()) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * Returns whether a king is capturing, moving or making an invalid move by
     * returning 1, 2 or 0 respectively
     *
     * @param piece Receives the piece being assessed for valid moves (only
     * called on king)
     * @param newx Receives the x coordinate that the piece is trying to move to
     * @param newy Receives the y coordinate that the piece is trying to move to
     * @param x0 Receives the x coordinate that the piece is moving from
     * @param y0 Receives the y coordinate that the piece is moving from
     * @return Returns whether a king is capturing, moving or making an invalid
     * move by returning 1, 2 or 0 respectively
     *
     */
    public int kingMove(Piece piece, int newx, int newy, int x0, int y0) {
        if (Math.abs(newx - x0) < 2 && Math.abs(newy - y0) < 2) {
            if (board[newx][newy].hasPiece() == false) {
                return 1;
            }
            if (board[newx][newy].hasPiece() == true && board[newx][newy].getPiece().getColour() != piece.getColour()) {
                return 2;
            }
        }
        return 0;
    }

    /**
     * Returns true if a vertically moving piece encounters a piece which
     * prevents it from moving further.
     *
     * @param piece Receives the piece being assessed for valid moves (only
     * called on rooks, queens and kings)
     * @param newx Receives the x coordinate that the piece is trying to move to
     * @param newy Receives the y coordinate that the piece is trying to move to
     * @param x0 Receives the x coordinate that the piece is moving from
     * @param y0 Receives the y coordinate that the piece is moving from
     * @return Returns true if a vertically moving piece encounters a piece
     * which prevents it from moving further and false if not.
     */
    public boolean isRookBlocked(Piece piece, int newx, int newy, int x0, int y0) {
        if (Math.abs(newx - x0) == 0 || Math.abs(newy - y0) == 0) {
            int dirx = (int) Math.signum(newx - x0);
            int diry = (int) Math.signum(newy - y0);
            if (dirx != 0) {
                for (int i = 1; i < Math.abs(newx - x0); i++) {
                    if (board[x0 + i * dirx][y0].hasPiece() == true) {
                        return true;
                    }
                }
            }
            if (diry != 0) {
                for (int i = 1; i < Math.abs(newy - y0); i++) {
                    if (board[x0][y0 + i * diry].hasPiece() == true) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if a diagonally moving piece encounters a piece which
     * prevents it from moving further.
     *
     * @param piece Receives the piece being assessed for valid moves (only
     * called on bishops, queens and kings)
     * @param newx Receives the x coordinate that the piece is trying to move to
     * @param newy Receives the y coordinate that the piece is trying to move to
     * @param x0 Receives the x coordinate that the piece is moving from
     * @param y0 Receives the y coordinate that the piece is moving from
     * @return Returns true if a diagonally moving piece encounters a piece
     * which prevents it from moving further and false if not.
     */
    public boolean isBishopBlocked(Piece piece, int newx, int newy, int x0, int y0) {
        if (Math.abs(newx - x0) == Math.abs(newy - y0)) {
            int dirx = newx > x0 ? 1 : -1;
            int diry = newy > y0 ? 1 : -1;
            for (int i = 1; i < Math.abs(newx - x0); ++i) {
                if (board[x0 + i * dirx][y0 + i * diry].hasPiece() == true) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Prints a representation of the board in terms of who can attack which
     * tile
     */
    public void printThreats() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i].getBThreat() == true && board[j][i].getWThreat() == true) {
                    System.out.print("#");
                } else if (board[j][i].getBThreat() == true && board[j][i].getWThreat() == false) {
                    System.out.print("B");
                } else if (board[j][i].getBThreat() == false && board[j][i].getWThreat() == true) {
                    System.out.print("W");
                } else {
                    System.out.print("*");
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    /**
     * Used to check if the black king is currently in check
     *
     * @return Returns true if the black king is on a threatened square and
     * false if not
     */
    public boolean checkBlackCheck() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i].hasPiece() == true) {
                    if (board[j][i].getPiece().getType() == PieceType.KING) {
                        if (board[j][i].getPiece().getColour() == PieceColour.BLACK) {
                            if (board[j][i].getWThreat() == true) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Used to check if the white king is currently in check
     *
     * @return Returns true if the white king is on a threatened square and
     * false if not
     */
    public boolean checkWhiteCheck() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[j][i].hasPiece() == true) {
                    if (board[j][i].getPiece().getType() == PieceType.KING) {
                        if (board[j][i].getPiece().getColour() == PieceColour.WHITE) {
                            if (board[j][i].getBThreat() == true) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns false if the piece parsed in is preventing the king from being in
     * check
     *
     * @param piece Receives the piece being assessed for valid moves (only
     * called on bishops, queens and kings)
     * @param newx Receives the x coordinate that the piece is trying to move to
     * @param newy Receives the y coordinate that the piece is trying to move to
     * @param x0 Receives the x coordinate that the piece is moving from
     * @param y0 Receives the y coordinate that the piece is moving from
     * @param turn True is parsed in if it is white's turn and false if it is
     * black's turn
     * @return Returns false if the piece parsed in is preventing the king from
     * being in check and true if not
     */
    public boolean checkCheckBlock(Piece piece, int newx, int newy, int x0, int y0, boolean turn) {
        Piece tempPiece = null;
        if (board[newx][newy].hasPiece() == true) {
            tempPiece = board[newx][newy].getPiece();
        }
        //piece.move(newx, newy);
        board[x0][y0].setPiece(null);
        board[newx][newy].setPiece(piece);
        setTilesThreat(board);

        if ((checkBlackCheck() == true && turn == false) || (checkWhiteCheck() == true && turn == true)) {
            //piece.move(x0, y0);
            board[newx][newy].setPiece(tempPiece);
            board[x0][y0].setPiece(piece);
            setTilesThreat(board);
            return false;

        } else {
            //piece.move(x0, y0);
            board[newx][newy].setPiece(tempPiece);
            board[x0][y0].setPiece(piece);
            setTilesThreat(board);
            return true;
        }
    }

}
