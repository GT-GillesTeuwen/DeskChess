/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package actualChess;

import chessjavafxmlbeta.User;
import deskchessMatchScreen.MatchScreenController;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.stage.Stage;

/**
 *
 * @author Client
 */
public class ChessApp extends Application {

    /**
     * Holds the pixel width and height of the tiles
     */
    public static final int TILE_SIZE = 70;
    /**
     * Holds the tile width of the board
     */
    public static final int WIDTH = 8;
    /**
     * Holds the tile height of the board
     */
    public static final int HEIGHT = 8;
    /**
     * Creates a 2d tile array of WIDTH and HEIGHT dimensions
     */
    public Tile[][] board = new Tile[WIDTH][HEIGHT];
    /**
     * Integer that stores the various states of check
     */
    private int checkMate = 0;
    /**
     * Integer that holds the score assigned to the current board
     */
    private double score = 0;
    /**
     * Group that contains all the tiles to display
     */
    private final Group tileGroup = new Group();
    /**
     * Group that contains all the pieces to display
     */
    final Group pieceGroup = new Group();
    /**
     * Group that contains all the legal indicator dots to display
     */
    final Group legalGroup = new Group();
    /**
     * Boolean that stores the current turn True=White False=Black
     */
    private boolean turn = true;
    /**
     * Recursive AI to make all the computer's moves
     */
    

    private MatchScreenController screen;

    /**
     * Creates a chess board with the setup of the brd string parsed in which is
     * to be read from the database
     *
     * @param brd Receives the string equivalent of the board which is to be
     * loaded in
     * @return Returns a pane with the correct setup according to the string
     * parsed in
     */
    public Pane loadContent(String brd) {

        String[] loadedBoard = brd.split("#");
        Pane root = new Pane();
        root.setPrefSize((WIDTH) * TILE_SIZE, (HEIGHT) * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup, legalGroup);

        //Loops through the board and creates the piece on the tile that corrosponds to the location in the brd string
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                Piece piece = null;
                switch (loadedBoard[y].charAt(x)) {
                    case 'p':
                        piece = makePiece(PieceColour.BLACK, PieceType.PAWN, x, y);
                        break;
                    case 'r':
                        piece = makePiece(PieceColour.BLACK, PieceType.ROOK, x, y);
                        break;
                    case 'n':
                        piece = makePiece(PieceColour.BLACK, PieceType.KNIGHT, x, y);
                        break;
                    case 'b':
                        piece = makePiece(PieceColour.BLACK, PieceType.BISHOP, x, y);
                        break;
                    case 'q':
                        piece = makePiece(PieceColour.BLACK, PieceType.QUEEN, x, y);
                        break;
                    case 'k':
                        piece = makePiece(PieceColour.BLACK, PieceType.KING, x, y);
                        break;
                    case 'P':
                        piece = makePiece(PieceColour.WHITE, PieceType.PAWN, x, y);
                        break;
                    case 'R':
                        piece = makePiece(PieceColour.WHITE, PieceType.ROOK, x, y);
                        break;
                    case 'N':
                        piece = makePiece(PieceColour.WHITE, PieceType.KNIGHT, x, y);
                        break;
                    case 'B':
                        piece = makePiece(PieceColour.WHITE, PieceType.BISHOP, x, y);
                        break;
                    case 'Q':
                        piece = makePiece(PieceColour.WHITE, PieceType.QUEEN, x, y);
                        break;
                    case 'K':
                        piece = makePiece(PieceColour.WHITE, PieceType.KING, x, y);
                        break;
                    default:
                        break;
                }

                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }

        }
        //printThreats();
        return root;
    }

    /**
     * Creates a standard chess board to begin a new game
     *
     * @return Returns a pane with the correct setup according to standard chess
     * rules
     */
    public Pane createContent() {

        Pane root = new Pane();
        root.setPrefSize((WIDTH) * TILE_SIZE, (HEIGHT) * TILE_SIZE);
        root.getChildren().addAll(tileGroup, pieceGroup, legalGroup);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                Piece piece = null;
                switch (y) {
                    case 1:
                        piece = makePiece(PieceColour.BLACK, PieceType.PAWN, x, y);
                        break;
                    case 0:
                        switch (x) {
                            case 0:
                            case 7:
                                piece = makePiece(PieceColour.BLACK, PieceType.ROOK, x, y);
                                break;
                            case 1:
                            case 6:
                                piece = makePiece(PieceColour.BLACK, PieceType.KNIGHT, x, y);
                                break;
                            case 2:
                            case 5:
                                piece = makePiece(PieceColour.BLACK, PieceType.BISHOP, x, y);
                                break;
                            case 3:
                                piece = makePiece(PieceColour.BLACK, PieceType.QUEEN, x, y);
                                break;
                            case 4:
                                piece = makePiece(PieceColour.BLACK, PieceType.KING, x, y);
                                break;
                            default:
                                break;
                        }
                        break;
                    case 6:
                        piece = makePiece(PieceColour.WHITE, PieceType.PAWN, x, y);
                        break;
                    case 7:
                        switch (x) {
                            case 0:
                            case 7:
                                piece = makePiece(PieceColour.WHITE, PieceType.ROOK, x, y);
                                break;
                            case 1:
                            case 6:
                                piece = makePiece(PieceColour.WHITE, PieceType.KNIGHT, x, y);
                                break;
                            case 2:
                            case 5:
                                piece = makePiece(PieceColour.WHITE, PieceType.BISHOP, x, y);
                                break;
                            case 3:
                                piece = makePiece(PieceColour.WHITE, PieceType.QUEEN, x, y);
                                break;
                            case 4:
                                piece = makePiece(PieceColour.WHITE, PieceType.KING, x, y);
                                break;
                            default:
                                break;
                        }
                        break;
                    default:
                        break;
                }
                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }

        }
        //printThreats();
        return root;
    }

    /**
     * Attempts to move a piece to the specified location
     *
     * @param piece Receives the piece that is attempting to move
     * @param newx Receives the x coordinate that the piece is attempting to
     * move to
     * @param newy Receives the y coordinate that the piece is attempting to
     * move to
     * @return Returns a MoveResult with the type of move that is occurring
     * (Kill for capturing, normal for standard movement and none for invalid
     * moves)
     */
    private MoveResult tryMove(Piece piece, int newx, int newy) {
        BoardLogic bl = new BoardLogic(board);
        int x0 = toBoard(piece.getOldX());
        int y0 = toBoard(piece.getOldY());

        if ((turn == true && piece.getColour() == PieceColour.BLACK) || (turn == false && piece.getColour() == PieceColour.WHITE)) {
            return new MoveResult(MoveType.NONE);
        } else {
            //Pawn movement
            if (piece.getType() == PieceType.PAWN) {
                if (bl.pawnMove(piece, newx, newy, x0, y0) == 1) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);
                    }

                } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);

                    }
                }
            }

            //Rook Movement
            if (piece.getType() == PieceType.ROOK) {
                if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);

                    }
                } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);

                    }
                }
            }

            //Knight Movement
            if (piece.getType() == PieceType.KNIGHT) {
                if (bl.knightMove(piece, newx, newy, x0, y0) == 1) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);

                    }
                } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);

                    }
                }
            }

            //Bishop Movement
            if (piece.getType() == PieceType.BISHOP) {
                if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);

                    }
                } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);

                    }
                }
            }

            //Queen Movement
            if (piece.getType() == PieceType.QUEEN) {
                if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);

                    }
                } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);
                    }

                }
            }

            //King Movement
            if (piece.getType() == PieceType.KING) {
                if (bl.kingMove(piece, newx, newy, x0, y0) == 1) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.NORMAL);

                    }
                } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2) {
                    if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);
                    } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                        return new MoveResult(MoveType.KILL);

                    }
                }
            }
        }
        return new MoveResult(MoveType.NONE);
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
    private int toBoard(double pixel) {
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
    private double toPixel(double place) {
        return ((TILE_SIZE) * place + (TILE_SIZE / 2)) - 1.5;
    }

    /**
     * Displays and populates the stage on which the board is created
     *
     * @param primaryStage Receives the stage on which all the content is
     * created
     */
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());

        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Creates a specific piece of a specific type and color at the specified
     * coordinates
     *
     * @param colour Receives the color of the piece that is to be created
     * @param type Receives the type of pieces that is to be created
     * @param x Receives the x coordinate on which the is is to be created
     * @param y Receives the y coordinate on which the is is to be created
     * @return Returns a piece of the specified type and color on the specified
     * coordinates
     */
    Piece makePiece(PieceColour colour, PieceType type, int x, int y) {
        BoardLogic bl = new BoardLogic(board);
        Piece piece = new Piece(colour, type, x, y, true);

        piece.setOnMousePressed((MouseEvent e) -> {
            legalGroup.getChildren().clear();
            showLegal(board, piece);

        });
        //Executes this block of code when the mouse is released
        //Snaps piece to nearest tile
        //Sets new threatened tiles
        //Triggers the AI to move
        piece.setOnMouseReleased((MouseEvent e) -> {
            if (turn) {
                RecursiveAI bot = new RecursiveAI(this,screen);
                int newX = toBoard(piece.getLayoutX());
                int newY = toBoard(piece.getLayoutY());
                MoveResult result = tryMove(piece, newX, newY);
                int x0 = toBoard(piece.getOldX());
                int y0 = toBoard(piece.getOldY());

                bot.setCurrentBoard(board);

                switch (result.getType()) {
                    case NONE:
                        piece.abortMove();
                        break;
                    case NORMAL:
                        piece.setMoved(true);
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);
                        board[newX][newY].setPiece(piece);
                        turn = false;
                        bl.setTilesThreat(board);
                        legalGroup.getChildren().clear();

                        if (newY == 0 && piece.getColour() == PieceColour.WHITE && piece.getType() == PieceType.PAWN) {
                            board[newX][newY].setPiece(null);
                            Piece promtePiece = makePiece(PieceColour.WHITE, PieceType.QUEEN, newX, newY);
                            pieceGroup.getChildren().remove(piece);
                            pieceGroup.getChildren().add(promtePiece);
                        }
                        break;
                    case KILL:
                        piece.setMoved(true);
                        Piece otherPiece = board[newX][newY].getPiece();
                        board[newX][newY].setPiece(null);
                        pieceGroup.getChildren().remove(otherPiece);
                        bl.setTilesThreat(board);
                        piece.move(newX, newY);
                        board[x0][y0].setPiece(null);
                        board[newX][newY].setPiece(piece);
                        turn = false;
                        legalGroup.getChildren().clear();

                        if (newY == 0 && piece.getColour() == colour.WHITE && piece.getType() == type.PAWN) {
                            board[newX][newY].setPiece(null);
                            Piece promtePiece = makePiece(PieceColour.WHITE, PieceType.QUEEN, newX, newY);
                            pieceGroup.getChildren().remove(piece);
                            pieceGroup.getChildren().add(promtePiece);
                        }
                        break;
                }
                try {
                    screen.checkCheckMate();
                } catch (SQLException ex) {
                    Logger.getLogger(ChessApp.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (turn == false) {
                    
                    bot.setCurrentBoard(board);
                    bot.setBrdPrint(printBoard());
                    System.out.println(bot.getState());
                    bot.start();

                    try {
                        screen.checkCheckMate();
                    } catch (SQLException ex) {
                        Logger.getLogger(ChessApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

        });

        return piece;
    }

    private Piece makePieceToDisplay(PieceColour colour, PieceType type, int x, int y) {

        Piece piece = new Piece(colour, type, x, y, false);

        //Executes this block of code when the mouse is released
        //Snaps piece to nearest tile
        //Sets new threatened tiles
        //Triggers the AI to move
        return piece;
    }

    /**
     * Returns a string representation of the 2d board array (which piece is on
     * which tile)
     *
     * @return Returns a string representation of the 2d board array (which
     * piece is on which tile)
     */
    public String printBoard() {
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

    /**
     * Displays all the legal moves for the selected piece
     *
     * @param board Receives a 2d tile array that is being assessed for all
     * possible legal moves of a particular piece
     * @param piece Receives the piece that is being assessed for all its legal
     * moves
     */
    private void showLegal(Tile[][] board, Piece piece) {
        BoardLogic bl = new BoardLogic(board);
        for (int newy = 0; newy < 8; newy++) {
            for (int newx = 0; newx < 8; newx++) {
                if (null != piece.getType()) {
                    int kingMoves = 0;
                    int x0 = toBoard(piece.getOldX());
                    int y0 = toBoard(piece.getOldY());
                    switch (piece.getType()) {
                        case PAWN:
                            if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.pawnMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.pawnMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            }
                            break;
                        case ROOK:
                            if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.rookMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.rookMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            }
                            break;
                        case KNIGHT:
                            if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.knightMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.knightMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            }
                            break;
                        case BISHOP:
                            if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.bishopMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.bishopMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            }
                            break;
                        case QUEEN:
                            if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.queenMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                } else if (bl.queenMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            }
                            break;
                        case KING:
                            if (bl.checkBlackCheck() == false && bl.checkWhiteCheck() == false && bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);

                                } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                }
                            } else if (bl.checkCheckBlock(piece, newx, newy, x0, y0, turn) == true) {
                                if (bl.kingMove(piece, newx, newy, x0, y0) == 1 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawEllipse(newx + 1.5, newy + 1.5);
                                    kingMoves++;
                                } else if (bl.kingMove(piece, newx, newy, x0, y0) == 2 && bl.isBishopBlocked(piece, newx, newy, x0, y0) == false && bl.isRookBlocked(piece, newx, newy, x0, y0) == false) {
                                    drawKillEllipse(newx + 1.5, newy + 1.5);
                                    kingMoves++;
                                }
                            }
                            if (kingMoves == 0 && bl.checkBlackCheck() == true) {
                                System.out.println("White Wins");
                            }
                            if (kingMoves == 0 && bl.checkWhiteCheck() == true) {
                                System.out.println("Black Wins");
                            }
                            break;
                        default:
                            break;
                    }
                }
            }

        }

    }

    /**
     * Creates a grey ellipse at the specified coordinates
     *
     * @param j Receives the x coordinate of the board where the ellipse has to
     * be drawn
     * @param i Receives the y coordinate of the board where the ellipse has to
     * be drawn
     */
    public void drawEllipse(double j, double i) {
        Ellipse dot = new Ellipse(toPixel(j), toPixel(i), 9, 9);
        dot.setFill(Color.GREY);
        legalGroup.getChildren().add(dot);

    }

    /**
     * Creates a red ellipse at the specified coordinates
     *
     * @param j Receives the x coordinate of the board where the ellipse has to
     * be drawn
     * @param i Receives the y coordinate of the board where the ellipse has to
     * be drawn
     *
     */
    public void drawKillEllipse(double j, double i) {
        Ellipse dot = new Ellipse(toPixel(j), toPixel(i), 9, 9);
        dot.setFill(Color.RED);
        legalGroup.getChildren().add(dot);

    }

    /**
     * Saves the current board to the boards table of the database and
     * associates it with the current user so that only that user can re-load
     * the saved board
     *
     * @param currentUser Receives the currentUser object to link to the board
     * being saved
     * @param name Receives the name to be associated with the board being saved
     * @throws SQLException Returns a SQL exception if there is an error with
     * the SQL being executed
     */
    public void save(User currentUser, String name) throws SQLException {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyy~hh:mm:ss");
        String strDate = dateFormat.format(date);
        String saveBoard = name + "~" + strDate + "~" + currentUser.getUserName() + "~";

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (null == board[j][i].getPiece()) {
                    saveBoard += ("0");
                } else {
                    switch (board[j][i].getPiece().getColour()) {
                        case BLACK:
                            if (null != board[j][i].getPiece().getType()) {
                                switch (board[j][i].getPiece().getType()) {
                                    case PAWN:
                                        saveBoard += ("p");
                                        break;
                                    case ROOK:
                                        saveBoard += ("r");
                                        break;
                                    case KNIGHT:
                                        saveBoard += ("n");
                                        break;
                                    case BISHOP:
                                        saveBoard += ("b");
                                        break;
                                    case QUEEN:
                                        saveBoard += ("q");
                                        break;
                                    case KING:
                                        saveBoard += ("k");
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        case WHITE:
                            if (null != board[j][i].getPiece().getType()) {
                                switch (board[j][i].getPiece().getType()) {
                                    case PAWN:
                                        saveBoard += ("P");
                                        break;
                                    case ROOK:
                                        saveBoard += ("R");
                                        break;
                                    case KNIGHT:
                                        saveBoard += ("N");
                                        break;
                                    case BISHOP:
                                        saveBoard += ("B");
                                        break;
                                    case QUEEN:
                                        saveBoard += ("Q");
                                        break;
                                    case KING:
                                        saveBoard += ("K");
                                        break;
                                    default:
                                        break;
                                }
                            }
                            break;
                        default:
                            saveBoard += ("0");
                            break;
                    }
                }
            }
            saveBoard += ("#");

        }
        saveBoard = saveBoard.substring(0, saveBoard.length() - 1);
        String[] boardInfo = saveBoard.split("~");
        DBManager db = new DBManager();
        db.addBoard(boardInfo[0], boardInfo[1], boardInfo[2], boardInfo[3], boardInfo[4]);

    }

    /**
     * @return Returns the current 2d tile array that represents the board
     */
    public Tile[][] getBoard() {
        return board;
    }

    public boolean isTurn() {
        return turn;
    }

    /**
     * Used when a game is being continued. Updates the board configuration of
     * the game with the parsed in user and name
     *
     * @param user the user currently playing
     * @param name the user-given name of the current game
     * @throws SQLException
     */
    public void overwrite(User user, String name) throws SQLException {
        DBManager db = new DBManager();
        db.updateBoard(name, user.getUserName(), printBoard().replace("\n", "#").substring(0, 71));
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
     * Using pieces left on the board, total dominance is calculated as a
     * percentage
     */
    public void calcWinnerScore() {
        int whiteScore = 0;
        int blackScore = 0;
        for (int y = 0; y < printBoard().length(); y++) {
            switch (printBoard().charAt(y)) {
                case 'P':
                    whiteScore += 1;
                    break;
                case 'R':
                    whiteScore += 10;
                    break;
                case 'N':
                    whiteScore += 8;
                    break;
                case 'B':
                    whiteScore += 10;
                    break;
                case 'Q':
                    whiteScore += 18;
                    break;
                case 'K':
                    whiteScore += 0;
                    break;
                case 'p':
                    blackScore += 1;
                    break;
                case 'r':
                    blackScore += 10;
                    break;
                case 'n':
                    blackScore += 8;
                    break;
                case 'b':
                    blackScore += 10;
                    break;
                case 'q':
                    blackScore += 18;
                    break;
                case 'k':
                    blackScore += 0;
                    break;
                default:
                    break;
            }
        }
        score = (((double) blackScore) / (whiteScore + blackScore));
    }

    /**
     * @return Returns the current state of checkmate on the board
     */
    public int getCheckMate() {
        return checkMate;
    }

    /**
     * @return Returns the score of the board as a decimal to decide who is
     * winning
     */
    public double getScore() {
        calcWinnerScore();
        return score;
    }

    public Pane display(String brd) {

        String[] loadedBoard = brd.split("#");
        Pane root = new Pane();
        root.setPrefSize(100, 100);
        //root.setStyle("-fx-background-color:red");
        root.relocate(450, 00);
        root.getChildren().addAll(tileGroup, pieceGroup, legalGroup);

        //Loops through the board and creates the piece on the tile that corrosponds to the location in the brd string
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x, y);
                board[x][y] = tile;
                tileGroup.getChildren().add(tile);

                Piece piece = null;
                switch (loadedBoard[y].charAt(x)) {
                    case 'p':
                        piece = makePieceToDisplay(PieceColour.BLACK, PieceType.PAWN, x, y);
                        break;
                    case 'r':
                        piece = makePieceToDisplay(PieceColour.BLACK, PieceType.ROOK, x, y);
                        break;
                    case 'n':
                        piece = makePieceToDisplay(PieceColour.BLACK, PieceType.KNIGHT, x, y);
                        break;
                    case 'b':
                        piece = makePieceToDisplay(PieceColour.BLACK, PieceType.BISHOP, x, y);
                        break;
                    case 'q':
                        piece = makePieceToDisplay(PieceColour.BLACK, PieceType.QUEEN, x, y);
                        break;
                    case 'k':
                        piece = makePieceToDisplay(PieceColour.BLACK, PieceType.KING, x, y);
                        break;
                    case 'P':
                        piece = makePieceToDisplay(PieceColour.WHITE, PieceType.PAWN, x, y);
                        break;
                    case 'R':
                        piece = makePieceToDisplay(PieceColour.WHITE, PieceType.ROOK, x, y);
                        break;
                    case 'N':
                        piece = makePieceToDisplay(PieceColour.WHITE, PieceType.KNIGHT, x, y);
                        break;
                    case 'B':
                        piece = makePieceToDisplay(PieceColour.WHITE, PieceType.BISHOP, x, y);
                        break;
                    case 'Q':
                        piece = makePieceToDisplay(PieceColour.WHITE, PieceType.QUEEN, x, y);
                        break;
                    case 'K':
                        piece = makePieceToDisplay(PieceColour.WHITE, PieceType.KING, x, y);
                        break;
                    default:
                        break;
                }

                if (piece != null) {
                    tile.setPiece(piece);
                    pieceGroup.getChildren().add(piece);
                }
            }

        }
        //printThreats();
        return root;
    }

    public void setScreen(MatchScreenController screen) {
        this.screen = screen;
    }

    public Group getPieceGroup() {
        return pieceGroup;
    }

    public void setCheckMate(int checkMate) {
        this.checkMate = checkMate;
    }

    public void setTurn(boolean turn) {
        this.turn = turn;
    }
    public void removePiece(Piece piece){
        System.out.println(pieceGroup.getChildren().indexOf(piece));
        pieceGroup.getChildren().remove(11);
    }

}