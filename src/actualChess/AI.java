/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import javafx.stage.Stage;

/**
 *
 * @author Client
 */
public class AI {

    private Tile[][] currentBoard;
    private String brdPrint;
    private int score;

    public AI() {
    }

    public AI(Tile[][] currentBoasrd) {
        this.currentBoard = currentBoasrd;
    }

    public AI(Tile[][] currentBoard, String brdPrint) {
        this.currentBoard = currentBoard;
        this.brdPrint = brdPrint.replace("\n", "");
        this.score = calculateScore(this.brdPrint);
    }

    public Tile[][] getCurrentBoard() {
        return currentBoard;
    }

    public void setCurrentBoard(Tile[][] currentBoard) {
        this.currentBoard = currentBoard;
    }

    public String getBrdPrint() {
        return brdPrint;
    }

    public void setBrdPrint(String brdPrint) {
        this.brdPrint = brdPrint.replace("\n", "");
        this.score = calculateScore(this.brdPrint);
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "AI{" + "currentBoard=" + currentBoard + ", brdPrint=" + brdPrint + ", score=" + score + '}';
    }

    public AIResult makeMove() {
        AIResult[] allMoves = new AIResult[100];
        int total = 0;
        int compMoveOldX = 0;
        int compMoveOldY = 0;
        int compMoveNewX = 0;
        int compMoveNewY = 0;
        BoardLogic bl = new BoardLogic(currentBoard);
        int currentScore = score;
        int bestScore = score;
        Tile[][] bestBrd = new Tile[8][8];
        Tile[][] tempBrd = new Tile[8][8];
        AIResult result = new AIResult(bestBrd, score, score, score, score, MoveType.NONE);
        Piece piece = null;
        int viableMoves = 0;
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {

                if (currentBoard[x][y].hasPiece()) {
                    piece = currentBoard[x][y].getPiece();

                } else {
                    piece = null;
                }

                for (int newy = 0; newy < 8; newy++) {
                    for (int newx = 0; newx < 8; newx++) {

                        if (piece != null && piece.getColour() == PieceColour.BLACK) {

                            int x0 = x;
                            int y0 = y;

                            switch (piece.getType()) {
                                case PAWN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    }
                                    break;
                                case ROOK:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    }
                                    break;
                                case KNIGHT:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {

                                           tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;

                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    }
                                    break;
                                case BISHOP:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    }
                                    break;
                                case QUEEN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                           tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    }
                                    break;
                                case KING:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NORMAL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            tempBrd = arrayCopy(currentBoard, tempBrd);
                                            tempBrd[newx][newy].setPiece(piece);
                                            tempBrd[x0][y0].setPiece(null);
                                            compMoveNewX = newx;
                                            compMoveNewY = newy;
                                            compMoveOldX = x0;
                                            compMoveOldY = y0;
                                            bestScore = calculateScore(printBoard(tempBrd));
                                            bestBrd = arrayCopy(tempBrd, bestBrd);
                                            result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.KILL);
                                            allMoves[total] = result;
                                            total++;
                                            viableMoves++;
                                        }
                                    }

                                    break;
                                default:
                                    break;
                            }
                            
                            if (viableMoves == 0 && bl.checkBlackCheck() == true) {
                                System.out.println("White Wins");
                                result = new AIResult(bestBrd, x0, newx, y0, newy, MoveType.NONE);
                            }
                            if (viableMoves == 0 && bl.checkWhiteCheck() == true) {
                                System.out.println("Black Wins");
                            }
                        }
                    }
                }
            }
        }
        
        result = allMoves[(int )(Math.random() * total)];
        return result;
    }

    private int calculateScore(String brd) {
        int calculatedScore = 0;
        for (int y = 0; y < brd.length(); y++) {
            switch (brd.charAt(y)) {
                case 'P':
                    calculatedScore += 1;
                    break;
                case 'R':
                    calculatedScore += 5;
                    break;
                case 'N':
                    calculatedScore += 4;
                    break;
                case 'B':
                    calculatedScore += 5;
                    break;
                case 'Q':
                    calculatedScore += 8;
                    break;
                case 'K':
                    calculatedScore += 50;
                    break;
                case 'p':
                    calculatedScore += -1;
                    break;
                case 'r':
                    calculatedScore += -5;
                    break;
                case 'n':
                    calculatedScore += -4;
                    break;
                case 'b':
                    calculatedScore += -5;
                    break;
                case 'q':
                    calculatedScore += -8;
                    break;
                case 'k':
                    calculatedScore += -50;
                    break;
                default:
                    break;
            }
        }
        return calculatedScore;
    }

    public String printBoard(Tile[][] board) {
        String brdPrint = "";
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (null == board[j][i].getPiece()) {
                    brdPrint += ("0");
                } else {
                    switch (board[j][i].getPiece().getColour()) {
                        case BLACK:
                            if (board[j][i].getPiece().getType() == PieceType.PAWN) {
                                brdPrint += ("p");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.ROOK) {
                                brdPrint += ("r");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.KNIGHT) {
                                brdPrint += ("n");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.BISHOP) {
                                brdPrint += ("b");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.QUEEN) {
                                brdPrint += ("q");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.KING) {
                                brdPrint += ("k");
                            }
                            break;
                        case WHITE:
                            if (board[j][i].getPiece().getType() == PieceType.PAWN) {
                                brdPrint += ("P");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.ROOK) {
                                brdPrint += ("R");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.KNIGHT) {
                                brdPrint += ("N");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.BISHOP) {
                                brdPrint += ("B");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.QUEEN) {
                                brdPrint += ("Q");
                            }
                            if (board[j][i].getPiece().getType() == PieceType.KING) {
                                brdPrint += ("K");
                            }
                            break;
                        default:
                            brdPrint += ("0");
                            break;
                    }
                }
            }
            brdPrint += ("\n");
        }
        brdPrint += ("\n");
        return brdPrint;
    }

    public static Tile[][] arrayCopy(Tile[][] aSource, Tile[][] aDestination) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (aSource[j][i].hasPiece()) {
                    aDestination[j][i] = new Tile(true, j, i);
                    aDestination[j][i].setPiece(aSource[j][i].getPiece());

                } else {
                    aDestination[j][i] = new Tile(true, j, i);
                    aDestination[j][i].setPiece(null);
                }

            }
        }
        return aDestination;
    }

}
