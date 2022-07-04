/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import static actualChess.RecursiveAI.arrayCopy;
import java.util.ArrayList;

/**
 *
 * @author gteuw
 */
public class MinimaxThread extends Thread {

    /**
     * The position in the movesScores array of this thread and position
     */
    private final int i;

    /**
     * The position that is being assessed by the minimax algorithm to get a
     * score
     */
    private final Tile[][] position;

    /**
     * The moveScores array linked to the main RecursiveAI
     */
    private final int[] moveScores;

    /**
     * How many moves ahead the algorithm is thinking
     */
    private final int depth;

    /**
     * Thread constructor
     *
     * @param position The position that is being assessed by the minimax
     * algorithm to get a score
     * @param i The position in the movesScores array of this thread and
     * position
     * @param moveScores The moveScores array linked to the main RecursiveAI
     * @param depth How many moves ahead the algorithm is thinking
     */
    public MinimaxThread(Tile[][] position, int i, int[] moveScores, int depth) {
        this.position = position;
        this.i = i;
        this.moveScores = moveScores;
        this.depth = depth;
    }

    /**
     * Begins running the thread
     * Begins the minimax operation
     */
    @Override
    public void run() {
        moveScores[i] = minimax(position, depth, -99999, 99999, true);

    }

    /**
     * Minimax algorithm which uses itself recursively to assign a score to each
     * position that is fed in. The lower the score the better for black the
     * higher the better for white. It assumes each player makes the most
     * logical move to maximise or minimise the score
     *
     * @param positionBrd The position being assigned the score
     * @param depth how many moves the algorithm is thinking ahead
     * @param alpha A potential maximum value used to prune the decision tree
     * @param beta A potential minimum value used to prune the decision tree
     * @param turn The colour being assessed (black - min) (white - max)
     * @return Returns the potential maximum, minimum or final score of a given
     * position
     */
    private int minimax(Tile[][] positionBrd, int depth, int alpha, int beta, boolean turn) {
        Tile[][] position = new Tile[8][8];
        position = arrayCopy(positionBrd, position);
        if (depth == 0) {
            // System.out.println("ID: "+this.getId()+" Score: "+calculateScore(printBoard(position)));
            return calculateScore(printBoard(position));
        }
        ArrayList<RecursiveAIResult> moves = thinkMove(position, turn);
        if (turn) {
            int maxEval = -10000;
            for (int j = 0; j < moves.size(); j++) {

                int eval = minimax(moves.get(j).getRetBrd(), depth - 1, alpha, beta, true);
                maxEval = max(maxEval, eval);
                alpha = max(alpha, eval);
                if (beta <= alpha) {

                    break;
                }
            }

            return maxEval;
        } else {
            int minEval = 100000;
            for (int j = 0; j < moves.size(); j++) {
                int eval = minimax(moves.get(j).getRetBrd(), depth - 1, alpha, beta, false);

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
     * Returns a string equivalent of the current board state
     *
     * @param board the current board state to be printed
     * @return
     */
    public String printBoard(Tile[][] board) {
        String brdPrint = "";
        //Loops through all the tiles on the board and adds the specfied character denoting which piece is on the tile if any 
        for (int y = 0; y < 8; y++) {
            for (int j = 0; j < 8; j++) {
                if (null == board[j][y].getPiece()) {
                    brdPrint += ("0");
                } else {
                    switch (board[j][y].getPiece().getColour()) {
                        case BLACK:
                            if (null != board[j][y].getPiece().getType()) {
                                switch (board[j][y].getPiece().getType()) {
                                    case PAWN:
                                        brdPrint += ("p");
                                        break;
                                    case ROOK:
                                        brdPrint += ("r");
                                        break;
                                    case KNIGHT:
                                        brdPrint += ("n");
                                        break;
                                    case BISHOP:
                                        brdPrint += ("b");
                                        break;
                                    case QUEEN:
                                        brdPrint += ("q");
                                        break;
                                    case KING:
                                        brdPrint += ("k");
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        case WHITE:
                            if (null != board[j][y].getPiece().getType()) {
                                switch (board[j][y].getPiece().getType()) {
                                    case PAWN:
                                        brdPrint += ("P");
                                        break;
                                    case ROOK:
                                        brdPrint += ("R");
                                        break;
                                    case KNIGHT:
                                        brdPrint += ("N");
                                        break;
                                    case BISHOP:
                                        brdPrint += ("B");
                                        break;
                                    case QUEEN:
                                        brdPrint += ("Q");
                                        break;
                                    case KING:
                                        brdPrint += ("K");
                                        break;
                                    default:
                                        break;
                                }
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
     * @param brd Receives the position being evaluated for a score
     * @return Returns an integer to represent the which colour is winning and
     * by how much
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
     * Generates an arrayList of all the possible positions that van come from a
     * particular position
     *
     * @param workingBrd Receives the board being extrapolated for possible
     * moves
     * @param aheadTurn Receives the turn on the next move
     * @return
     */
    private ArrayList<RecursiveAIResult> thinkMove(Tile[][] workingBrd, boolean aheadTurn) {
        boolean thinkAheadturn = aheadTurn;
        int compMoveOldX = 0;
        int compMoveOldY = 0;
        int compMoveNewX = 0;
        int compMoveNewY = 0;
        Tile[][] check = new Tile[8][8];
        check = arrayCopy(workingBrd, check);
        Tile[][] bestBrd = new Tile[8][8];
        BoardLogic bl = new BoardLogic(check);

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

}
