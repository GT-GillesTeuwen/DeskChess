/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import deskchessMatchScreen.MatchScreenController;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;

/**
 *
 * @author Client
 */
public class RecursiveAI extends Thread {

    /**
     * Holds the 2d tile array that represents the current state of the board
     */
    private Tile[][] currentBoard;
    /**
     * Holds a String representation of the current board
     */
    private String stringVersionOfBoard;
    /**
     * Holds the score of the current board
     */
    private int score;

    private RecursiveAIResult result;
    private int mainDepth = 3;
    private ArrayList<RecursiveAIResult> viableMoves;
    private ChessApp chessApp;
    private MatchScreenController screen;
    private int threadsComplted;

    /**
     * Constructor with no parameters Creates a new RecursiveAI (used if the
     * board has not been created yet)
     */
    public RecursiveAI(ChessApp chessApp, MatchScreenController screen) {
        this.chessApp = chessApp;
        this.screen = screen;
        threadsComplted = 0;
    }

    /**
     * Creates a new RecusiveAI with the current board set to be evaluated and a
     * printed board to assign a score to
     *
     * @param currentBoard Receives the current board to be evaluated for the
     * best move
     * @param brdPrint Receives a String version of the board to assign a score
     * to
     */
    public RecursiveAI(Tile[][] currentBoard, String brdPrint, ChessApp chessApp) {
        this.currentBoard = currentBoard;
        this.stringVersionOfBoard = brdPrint.replace("\n", "");
        this.score = calculateScore(this.stringVersionOfBoard);
        this.chessApp = chessApp;
        threadsComplted = 0;
    }

    /**
     *
     * @return Returns the 2d tile array that represents the current state of
     * the board
     */
    public Tile[][] getCurrentBoard() {
        return currentBoard;
    }

    /**
     * Overrides the current board in favor of an updated one
     *
     * @param currentBoard Receives the current board to be evaluated for the
     * best move
     */
    public void setCurrentBoard(Tile[][] currentBoard) {
        this.currentBoard = currentBoard;
    }

    /**
     *
     * @return Returns a String representation of the current board
     */
    public String getBrdPrint() {
        return stringVersionOfBoard;
    }

    /**
     * Overrides the current String representation of the current board with an
     * up to date one
     *
     * @param brdPrint Receives a String version of the board to assign a score
     * to
     */
    public void setBrdPrint(String brdPrint) {
        this.stringVersionOfBoard = brdPrint.replace("\n", "");
        this.score = calculateScore(this.stringVersionOfBoard);
    }

    /**
     *
     * @return Returns a numerical value to indicate how good or bad the current
     * board is for the computer player
     */
    public int getScore() {
        return score;
    }

    /**
     * Finds the best move looking n moves ahead for black
     *
     * @param position receives the board currently being assessed for moves
     * @param depth receives how many moves ahead the AI must look
     * @param alpha receives theoretical min for pruning
     * @param beta receives theoretical max for pruning
     * @param turn receives the player the move is being predicted for
     * @return Returns the location for the best board for either white or black
     */
    public int minimax(Tile[][] position, int depth, int alpha, int beta, boolean turn) {
        if (depth == 0) {
            return calculateScore(printBoard(position));
        }
        ArrayList<RecursiveAIResult> moves = thinkMove(position, turn);
        if (turn) {
            int maxEval = -10000;
            for (int i = 0; i < moves.size(); i++) {

                int eval = minimax(moves.get(i).getRetBrd(), depth - 1, alpha, beta, true);
                maxEval = max(maxEval, eval);
                alpha = max(alpha, eval);
                if (beta <= alpha) {

                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = 100000;
            for (int i = 0; i < moves.size(); i++) {

                int eval = minimax(moves.get(i).getRetBrd(), depth - 1, alpha, beta, false);
                if (depth == mainDepth && eval < minEval) {
                    result = moves.get(i);

                }
                minEval = min(minEval, eval);
                beta = min(beta, eval);

                if (beta <= alpha) {

                    break;
                }
            }

            return minEval;
        }
    }

    /**
     * Finds the smaller of the two given inputs
     *
     * @param num1
     * @param num2
     * @return the smaller of the two given inputs
     */
    private int min(int num1, int num2) {
        if (num1 < num2) {
            return num1;
        }
        return num2;
    }

    /**
     * Finds the larger of the two given inputs
     *
     * @param num1
     * @param num2
     * @return the larger of the two given inputs
     */
    private int max(int num1, int num2) {
        if (num1 > num2) {
            return num1;
        }
        return num2;
    }

    /**
     * Called by the AI player on the current board stored in order to decide
     * which move is best to make
     *
     * @return Returns a RecursiveAIResult which contains the coordinates of the
     * piece to be moved and where it is being moved to
     */
    public void makeMove() {
        System.out.println("1");
        // Populates an array list with all the valid moves
        viableMoves = thinkMove(currentBoard, false);
        ArrayList<Integer> good = new ArrayList<>();
        int[] moveScores = new int[viableMoves.size()];

        int bestMove = 0;

        int minScore = 10000000;
        double progress = 0.0;
        for (int i = 0; i < viableMoves.size(); i++) {
            System.out.println("MiniMax " + i + "/" + viableMoves.size());
            MinimaxThread temp = new MinimaxThread(viableMoves.get(i).getRetBrd(), i, this, moveScores);
            temp.start();
            System.out.println("ID " + temp.getId() + " State " + temp.getState());
        }
        while (threadsComplted != viableMoves.size()) {
            System.out.println("Thinking\t Threads Complete:" + threadsComplted + "/" + viableMoves.size());
        }
        for (int i = 0; i < viableMoves.size(); i++) {
            System.out.println("Sweep " + i + "/" + viableMoves.size());
            if (moveScores[i] < minScore) {
                bestMove = i;
                minScore = moveScores[i];
            }
        }
        for (int i = 0; i < viableMoves.size(); i++) {
            System.out.println("Set " + i + "/" + viableMoves.size());
            if (moveScores[i] == minScore) {
                good.add(i);
            }
        }
        if (good.size() > 0) {
            int rand = good.get((int) ((Math.random()) * (good.size())));
            result = viableMoves.get(rand);
            //return viableMoves.get(rand);
        } else {
            result = new RecursiveAIResult(currentBoard, 0, 0, 0, 0, MoveType.NONE, true);
        }

        //return new RecursiveAIResult(currentBoard, 0, 0, 0, 0, MoveType.NONE, true);
        System.out.println("Here");
        Tile[][] checkBrd = new Tile[8][8];
        BoardLogic bl = new BoardLogic(arrayCopy(currentBoard, checkBrd));
        System.out.println("here 1");
        Piece oppPiece = chessApp.getBoard()[result.getOldX()][result.getOldy()].getPiece();
        System.out.println("here 2");
        if (null != result.getType()) {
            switch (result.getType()) {
                case KILL:

                    oppPiece.setMoved(true);
                    Piece otherPiece2 = chessApp.getBoard()[result.getNewX()][result.getNewy()].getPiece();
                    chessApp.pieceGroup.getChildren().lastIndexOf(otherPiece2);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            chessApp.pieceGroup.getChildren().remove(otherPiece2);
                        }
                    });

                    oppPiece.move(result.getNewX(), result.getNewy());
                    chessApp.getBoard()[result.getNewX()][result.getNewy()].setPiece(oppPiece);
                    chessApp.getBoard()[result.getOldX()][result.getOldy()].setPiece(null);

                    if (result.getNewy() == 7 && oppPiece.getColour() == PieceColour.BLACK && oppPiece.getType() == PieceType.PAWN) {
                        chessApp.getBoard()[result.getNewX()][result.getNewy()].setPiece(null);
                        Piece promtePiece = chessApp.makePiece(PieceColour.BLACK, PieceType.QUEEN, result.getNewX(), result.getNewy());
                        chessApp.pieceGroup.getChildren().remove(oppPiece);
                        chessApp.pieceGroup.getChildren().add(promtePiece);
                    }
                    bl.setTilesThreat(chessApp.board);
                    break;
                case NORMAL:

                    oppPiece.setMoved(true);
                    oppPiece.move(result.getNewX(), result.getNewy());
                    chessApp.getBoard()[result.getNewX()][result.getNewy()].setPiece(oppPiece);
                    chessApp.getBoard()[result.getOldX()][result.getOldy()].setPiece(null);

                    if (result.getNewy() == 7 && oppPiece.getColour() == PieceColour.BLACK && oppPiece.getType() == PieceType.PAWN) {
                        chessApp.getBoard()[result.getNewX()][result.getNewy()].setPiece(null);
                        Piece promtePiece = chessApp.makePiece(PieceColour.BLACK, PieceType.QUEEN, result.getNewX(), result.getNewy());
                        chessApp.pieceGroup.getChildren().remove(oppPiece);
                        chessApp.pieceGroup.getChildren().add(promtePiece);
                    }
                    bl.setTilesThreat(chessApp.board);
                    break;
                case NONE:
                    chessApp.setCheckMate(1);
                    break;
                default:
                    break;
            }

        }
        chessApp.setTurn(true);

    }

    /**
     * Called to find all the valid moves that the AI player can make
     *
     * @param workingBrd Receives the board that the computer is evaluating for
     * all valid moves
     * @param aheadTurn Receives whose turn it is so that the computer can
     * predict all the possible moves
     * @return Returns an array list containing all the valid moves as well as
     * their scores for the computer to determine which is the best for it to
     * make
     */
    public ArrayList<RecursiveAIResult> thinkMove(Tile[][] workingBrd, boolean aheadTurn) {
        boolean thinkAheadturn = aheadTurn;
        int compMoveOldX = 0;
        int compMoveOldY = 0;
        int compMoveNewX = 0;
        int compMoveNewY = 0;

        Tile[][] bestBrd = new Tile[8][8];
        BoardLogic bl = new BoardLogic(workingBrd);

        Piece piece = null;
        //Initialises a new array liost of Recursive AI results 
        ArrayList<RecursiveAIResult> moves2 = new ArrayList<>();
        //Loops through all the tiles to find pieces on them
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (workingBrd[x][y].hasPiece()) {
                    piece = workingBrd[x][y].getPiece();

                } else {
                    piece = null;
                }

                // Loops through all the tiles to see if the selected piece can move there
                //If the piece can move there the AI moves it on a temporoary board and evauluates the score 
                //Then it is stored as a RecursiveAIResult (with the correct type of move, locations etc) in the array list
                for (int newy = 0; newy < 8; newy++) {

                    for (int newx = 0; newx < 8; newx++) {
                        int x0 = x;
                        int y0 = y;
                        Tile[][] tempBrd = new Tile[8][8];

                        if (piece != null && piece.getColour() == PieceColour.BLACK && thinkAheadturn == false) {
                            if (piece != null) {
                                tempBrd = arrayCopy(workingBrd, tempBrd);
                                tempBrd[x][y].setPiece(null);
                                tempBrd[newx][newy].setPiece(null);
                                tempBrd[newx][newy].setPiece(piece);

                            }
                            switch (piece.getType()) {

                                case PAWN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }

                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case ROOK:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case KNIGHT:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));

                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {

                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case BISHOP:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case QUEEN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }
                                    break;
                                case KING:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, true));
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, true));
                                        }
                                    }

                                    break;
                                default:
                                    break;
                            }
                        }
                        if (piece != null && piece.getColour() == PieceColour.WHITE && thinkAheadturn == true) {
                            if (piece != null) {
                                tempBrd = arrayCopy(workingBrd, tempBrd);
                                tempBrd[x][y].setPiece(null);
                                tempBrd[newx][newy].setPiece(null);
                                tempBrd[newx][newy].setPiece(piece);

                            }
                            switch (piece.getType()) {
                                case PAWN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case ROOK:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == false) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case KNIGHT:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));

                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case BISHOP:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case QUEEN:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    }
                                    break;
                                case KING:
                                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
                                        }
                                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, false) == true) {
                                        if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.NORMAL, false));
                                        } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                            moves2.add(new RecursiveAIResult(tempBrd, x0, newx, y0, newy, MoveType.KILL, false));
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
        return moves2;
    }

    /**
     * Using pieces left on the board, total dominance is calculated according
     * to the value of the pieces remaining
     *
     * @param brd Receives a string representation of a board
     * @return Returns the integer value of the calculated score for the board
     * that was received as a parameter
     */
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

    /**
     * Prints out a graphical representation of a game board with pieces in
     * their respective places
     *
     * @param board Receives a 2d tile array representation of a board
     * @return Returns a string representation of the 2d tile array parsed in as
     * a parameter
     */
    public String printBoard(Tile[][] board) {
        String brdPrint = "";
        //Loops through all the tiles on the board and adds the specfied character denoting which piece is on the tile if any 
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
            brdPrint += ("");
        }
        brdPrint += ("");
        return brdPrint;
    }

    /**
     * Duplicates a 2d tile array in order to manipulate the duplicate without
     * altering the original
     *
     * @param aSource Receives the original array
     * @param aDestination Receives the empty array that will store the values
     * of the source array
     * @return Returns a 2d Tile array identical to the input
     */
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

    /**
     *
     * @return Returns the best move for black as a RecursiveAIResult
     */
    public RecursiveAIResult getResult() {
        return result;
    }

    public void incrementCompletedThreads() {
        threadsComplted++;
    }

    @Override
    public void run() {
        makeMove();
        try {
            if(screen!=null){
                 screen.checkCheckMate();
            }
           
        } catch (SQLException ex) {
            Logger.getLogger(RecursiveAI.class.getName()).log(Level.SEVERE, null, ex);
        }
        chessApp.allowMoves();
    }

}
