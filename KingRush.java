/**
 * Currently, this is the class that holds the majority of the states.  Because 
 * of the amount of state that it keeps track of, the game is played from here.
 * 
 * Please read README and COPY for instructions and license information.
 * 
 * Version 1.0: Got the game working where it picks random moves.
 * Version 2.0: The computer now performs a negamax search and interative 
 *              deepening to make a better move.
 * 
 * @author  Jon Trent (c) 2011
 * @version 2.0
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Random;

public class KingRush {
    /**
     * Main method is where the game object is created and run is called on it. 
     * It also passes in any variables that the game is started with to allow 
     * the user to start the game with between 0 and 2 human players, and 
     * network play once it is implemented.
     * 
     * @param args An array of strings.  It is used to pass in variables that 
     *             the game is called with to the game object.
     */
    public static void main(String[] args) {
    	boolean	error;
		int			nPlayers;
		int			comp1Lvl;
		int			comp2Lvl;
		KingRush	kr;
		
		error =	false;
		kr =	null;
		
        // Verify correct number of players.
		if (args.length == 3) {
			try {
				nPlayers =	Integer.parseInt(args[0]);
				comp1Lvl =	Integer.parseInt(args[1]);
				comp2Lvl =	Integer.parseInt(args[2]);
                
                // Make sure inputs are within range.
                if (((nPlayers < 0) || (2 < nPlayers)) ||
                    ((comp1Lvl < 0) || (2 < comp1Lvl)) ||
                    ((comp2Lvl < 0) || (2 < comp2Lvl))) {
                    error = true;
                } else {
                    // Create KingRush object with parameters.
                    kr =	new KingRush(nPlayers, comp1Lvl, comp2Lvl);
                }
			} catch (Exception e) {
				error =	true;
			}
		} else if (args.length == 0) {
            // Call default KingRush object.
			kr =	new KingRush();
		} else {
			error =	true;
		}
		
        // If we have no error, run the game, else print a helpful message.
		if (!error) {
			kr.run();
		} else {
			printHelp();
		}
        /*KingRush    kr;
        kr =    new KingRush();
        
        kr.run();*/
    }
    
    /**
     * A helpful message.
     */
    public static void printHelp() {
		System.out.print("***************************************************");
		System.out.println("*****************************");
		System.out.println("You have entered the wrong parameters.");
		System.out.println("- Enter 0 parameters for KingRush to play itself");
		System.out.print("- Enter the number of players [0 through 2] and ");
		System.out.println("the difficulty levels for white");
		System.out.println("  and black");
		System.out.print("-- If 2 is entered for number of players, then ");
		System.out.println("enter 0 for both difficulties.");
		System.out.print("-- If 1 is entered for number of players, then ");
		System.out.println("enter 0 for the first (white) ");
		System.out.print("   difficulty, and [1..2] for the second (black) ");
		System.out.println("difficulty");
		System.out.print("-- If 0 is entered for number of players, then ");
		System.out.println("enter the difficulty [1..2] for ");
		System.out.println("   both players.");
		System.out.print("***************************************************");
		System.out.println("*****************************");
	}
    
    private State   state;          // The states for the game.
    private int		numOfPlayers;   // Number of human players [0..2].
    private int     computer1Level; // Difficulty level for computer player 1.
    private int     computer2Level; // Difficulty level for computer player 2.
    private boolean whiteKing;      // Whether player 1 has a king or not.
    private boolean blackKing;      // Whether player 2 has a king or not.
    private boolean	timeOver;
    private long	timeAllowed;
    private TTable	ttable;
    private boolean draw;			// Keeps track of whether there is a draw.

/*******************************************************************************
 *******************************************************************************
 INITIALIZATION METHODS
 *******************************************************************************
 ******************************************************************************/
    /**
     * Default KingRush constructor.  It currently assumes 2 human players when 
     * it is called.
     */
    public KingRush() {
        this(0, 1, 0);
    }
    
    public KingRush(int nPlayers, int comp1Lvl, int comp2Lvl) {
        numOfPlayers =		nPlayers;
        computer1Level =    comp1Lvl;
        computer2Level =    comp2Lvl;
        whiteKing =         true;
        blackKing =         true;
        timeAllowed =		900;
        draw =              false;
        state =             new State(5, 6);
        ttable =			new TTable(state.board);
        
        initializeBoard();
    }
    
    /**
     * This method initializes the board so that the pieces are all in their 
     * beginning positions.
     */
    private void initializeBoard() {
        /*
         * This board is:
         * kqbnr
         * ppppp
         * .....
         * .....
         * PPPPP
         * RNBQK
         */
        
        // Black pieces.
        state.board[0][0] = 'k';
        state.board[0][1] = 'q';
        state.board[0][2] = 'b';
        state.board[0][3] = 'n';
        state.board[0][4] = 'r';
        state.board[1][0] = 'p';
        state.board[1][1] = 'p';
        state.board[1][2] = 'p';
        state.board[1][3] = 'p';
        state.board[1][4] = 'p';
        
        // Open squares.
        state.board[2][0] = '.';
        state.board[2][1] = '.';
        state.board[2][2] = '.';
        state.board[2][3] = '.';
        state.board[2][4] = '.';
        state.board[3][0] = '.';
        state.board[3][1] = '.';
        state.board[3][2] = '.';
        state.board[3][3] = '.';
        state.board[3][4] = '.';
        
        // White pieces.
        state.board[4][0] = 'P';
        state.board[4][1] = 'P';
        state.board[4][2] = 'P';
        state.board[4][3] = 'P';
        state.board[4][4] = 'P';
        state.board[5][0] = 'R';
        state.board[5][1] = 'N';
        state.board[5][2] = 'B';
        state.board[5][3] = 'Q';
        state.board[5][4] = 'K';
    }
    
/*******************************************************************************
 *******************************************************************************
 GAME RUNNING METHODS
 *******************************************************************************
 ******************************************************************************/
    /**
     * This method is the game loop.  It is called to start and play the game.
     */
    public void run() {
        boolean gameOver;
        int     count;
        
        gameOver =  false;
        count =     0;
        
        while (gameOver == false) {
            print();
            gameOver =  update();
            count++;
        }
        
        printResults();
    }
    
    private boolean update() {
        boolean gameOver;
        gameOver =  false;
        
        // Determine whose turn it is.
        if (state.color == 1) {
        	// If there are 1 or 2 players, it is a human move, else computer 
        	// move.
        	if (numOfPlayers > 0) {
        		humanMove();
        	} else {
        		whiteMove();
        	}
        } else {
        	// If there are 2 players, it is a human move, else computer move.
        	if (numOfPlayers == 2) {
        		humanMove();
        	} else {
        		blackMove();
        	}
        }
        //computerMove();
        checkDraw();
        
        if (draw) {
            gameOver =  true;
        }
        
        if (!whiteKing || !blackKing) {
            gameOver =      true;
            state.color =   -state.color;
            
            if (state.color == -1) {
                state.turns--;
            }
        }
        
        return gameOver;
    }
    
    /**
     * Method also updates the draw field, setting it to true if it is a draw.
     */
    private void checkDraw() {
        if (state.turns > 40) {
            draw = true;
        }
    }
    
    /**
     * Checks for a win based on whose turn it is and what the state of the 
     * blackKing and whiteKing variables.
     */
    private boolean checkWin() {
        if ((state.color == 1) && !blackKing) {
            return true;
        } else if ((state.color == -1) && !whiteKing) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * A convenience method for getting the current time in milliseconds.
     * 
     * @return The current time in milliseconds as a long.
     */
    private long getTime() {
    	return new Date().getTime();
    }
    
    /**
     * Determines whether this piece is a friend or foe based on whose turn it 
     * is.
     * 
     * @param  piece Piece to be tested whether it is a friend or foe.
     * 
     * @return True if piece is a friend and false if not.
     */
    private boolean isFriend(char piece) {
        int decode;
        decode =    (int)piece;
        
        // chars for white are between 65 and 83, black is between 97 and 115.
        // If white, char needs to be below 83.  Above 83 is black.
        if (state.color == 1) {
            if ((decode > 65) && (decode < 83)) {
                return true;
            } else {
                return false;
            }
        } else {
            if ((decode > 97) && (decode < 115)) {
                return true;
            } else {
                return false;
            }
        }
    }
    
/*******************************************************************************
 *******************************************************************************
 PERFORMING MOVE METHODS
 *******************************************************************************
 ******************************************************************************/
    /**
     * This method is called for the human move.  It gets the input from the 
     * command line, verifies the move, and then performs the move.
     */
    private void humanMove() {
        InputStreamReader   isReader;
        BufferedReader      bReader;
        String				input;
        Move				move;
        ArrayList<Move>		moves;
        
        isReader =  new InputStreamReader(System.in);
        bReader =   new BufferedReader(isReader);
        moves =		new ArrayList<Move>();
        
        try {
        	input =	bReader.readLine();
        	move =	convertInput(input);
        	moves =	getMoves();
        	
        	if ((move != null) && (checkMove(move, moves))) {
        		doMove(move);
        	}
        } catch (Exception e) {}
    }
    
    private Move convertInput(String input) {
    	String	inputs[];
    	Square	to;
    	Square	from;
    	
    	// Split the string into smaller strings.
    	inputs =	input.split("-");
    	// Verify that there are two substrings now.
    	if (inputs.length != 2) {
    		return null;
    	}
    	
    	to =	convertSquare(inputs[0]);
    	from =	convertSquare(inputs[1]);
    	
    	// Verify the two strings are within range.
    	if (!verifyInputRange(to) || !verifyInputRange(from)) {
    		return null;
    	}
    	
    	// Verify the move isn't to itself.
    	if (sameSquare(to, from)) {
    		return null;
    	}
    	
    	return new Move(to, from);
    }
    
    private Square convertSquare(String input) {
    	int		x;
    	int		y;
    	Square	position;
    	
    	if (input.length() != 2) {
    		position =	null;
    	} else {
    		x =	(int)input.charAt(0) - 96;
    		y =	(int)input.charAt(1) - 48;
    	
    		position =	new Square(x, y);
    	}
    	
    	return position;
    }
    
    private boolean verifyInputRange(Square square) {
    	boolean	valid;
    	
    	if ((square.x < 0) || (5 < square.x)) {
    		valid =	false;
    	} else if ((square.y < 0) || (6 < square.y)) {
    		valid =	false;
    	} else {
    		valid =	true;
    	}
    	
    	return valid;
    }
    
    private boolean sameSquare(Square to, Square from) {
    	boolean	valid;
    	
    	if ((to.x == from.x) && (to.y == from.y)) {
    		valid =	true;
    	} else {
    		valid =	false;
    	}
    	
    	return valid;
    }
    
    private boolean sameMove(Move aMove, Move bMove) {
    	boolean	valid;
    	if (sameSquare(aMove.to, bMove.to) && 
    			sameSquare(aMove.from, bMove.from)) {
    		valid =	true;
    	} else {
    		valid =	false;
    	}
    	
    	return valid;
    }
    
    private boolean checkMove(Move aMove, ArrayList<Move> moves) {
    	//Square	to;
    	//Square	from;
    	boolean	valid;
    	int		i;
    	int		size;
    	//Square	mTo;
    	//Square	mFrom;
    	
    	//to =	aMove.to;
    	//from =	aMove.from;
    	valid =	false;
    	size =	moves.size();
    	
    	for (i = 0; i < size; i++) {
    		//mTo =	moves.get(i).to;
    		//mFrom =	moves.get(i).from;
    		
    		if (sameMove(aMove, moves.get(i))) {
    			valid =	true;
    		}
    	}
    	
    	return valid;
    }
    
    /*
     * This is where the computer players are called from.  It calls whiteMove 
     * or blackMove depending on who's turn it is.  This is so the difficulty 
     * levels of the two players can be set individually.
     *
    private void computerMove() {
        if (state.color == 1) {
            whiteMove();
        } else {
            blackMove();
        }
    }*/
    
    /**
     * Calls the appropriate methods to get the next move for white.
     */
    private void whiteMove() {
    	if (computer1Level == 0) {
    		randomMove();
    	} else if (computer1Level == 1) {
    		// untimedMove();
    		iterativeMove();
    	}
    }
    
    /**
     * Calls the appropriate methods to get the next move for black.
     */
    private void blackMove() {
    	if (computer2Level == 0) {
    		// randomMove();
    		timedMove();
    	} else if (computer2Level == 1) {
    		untimedMove();
    	}
    }
    
    /**
     * A method to get the next move based on picking a move at random.
     */
    private void randomMove() {
    	Move	move;
    	move =	getRandomMove();
    	
    	doMove(move);
    }
    
    /**
     * A method to get the next move based on picking the bes move from the 
     * negamax algorithm.  The move is made as soon as it is decided.
     */
    private void untimedMove() {
    	Move	move;
    	move =	runAlphabeta();
    	
    	doMove(move);
    }
    
    private void timedMove() {
    	long	startTime;    	
    	long	elapsedTime;
    	startTime =	getTime();
    	
    	randomMove();
    	elapsedTime =	getTime() - startTime;
    	
    	while(elapsedTime < 1000) {
    		elapsedTime =	getTime() - startTime;
    	}
    }
    
    private void iterativeMove() {
    	Move	move;
    	long	startTime;
    	//long	elapsedTime;
    	
    	startTime =	getTime();
    	move =		runTablemax(startTime); //runIterative(startTime);
    	
    	doMove(move);
    }
    
    /**
     * Given a Move object this method:
     * - Saves any piece that will be taken
     * - Performs the move
     * - Updates "king" boolean if a king was taken
     * - Switches the side on move
     * - Then returns the piece taken so it can be restored
     *
     * @param  move The move to be performed.
     * @return The piece that was taken during the move.
     */
    private char doMove(Move move, Bool promotion) {
    	Square	from;
    	Square	to;
    	int		height;
        char    piece;
        char    taken;
        
        from =							move.from;
        to =							move.to;
        height =						state.board.length;
        piece = 						state.board[from.y][from.x];
        taken = 						state.board[to.y][to.x];
        state.board[to.y][to.x] =		piece;
        state.board[from.y][from.x] =	'.';
        
        // If the taken piece was a king, we need to update the king booleans.
        if (taken == 'k') {
            blackKing = false;
        } else if (taken == 'K') {
            whiteKing = false;
        }
        
        // If it is a pawn that has advanced to the end, set the promotion
        // flag.
        if (((piece == 'P') && (to.y == 0)) || 
        		((piece == 'p') && (to.y == height))) {
        	promotion.bool =	true;
        }
        
        // Switch color on play.  If color was black/is white, add a number to 
        // the turn count.
        state.color =   -state.color;
        if (state.color == 1) {
            state.turns++;
        }
        
        return taken;
    }
    
    /**
     * This wraps the doMove function, so that when called to make the actual 
     * move, it will not require a Bool object to be passed in.  It also does 
     * not need to pass a char.
     * 
     * @param move The move to be performed.
     */
    private void doMove(Move move) {
    	Bool	promotion;
    	char	piece;
    	char	taken;
    	
    	promotion =	new Bool(false);
    	piece =		state.board[move.from.y][move.from.x];
    	taken =		doMove(move, promotion);
    	
    	if (promotion.bool) {
    		promotePawn(move);
    		ttable.updatePromote(move, piece, taken, state.color);
    	} else {
    		ttable.updateHash(move, piece, taken, state.color);
    	}
    }
    
    /**
     * Given the move and piece taken from doMove, this method undoes what was 
     * done in doMove by:
     * - Switching back the side on move
     * - Restores the "king" boolean if a king was taken
     * - Restores the piece taken from the move
     * - Places the piece moved in its original location
     *
     * @param move      The move to be undone.
     * @param taken     The piece that was taken from the move.
     * @param promotion Whether the move resulted in a pawn promotion.
     */
    private void undoMove(Move move, char taken, Bool promotion) {
    	Square	from;
    	Square	to;
        char    piece;
        
        from =	move.from;
        to =	move.to;
        piece = state.board[to.y][to.x];
        
        // Switch color on play back.  If color was white/is black, subtract a 
        // number from the turn count.
        state.color =   -state.color;
        
        if (state.color == -1) {
            state.turns--;
        }
        
        if (promotion.bool == true) {
        	promotion.bool =	false;
        }
        
        // If the taken piece was a king, we need to reset the king booleans.
        if (taken == 'k') {
            blackKing = true;
        } else if (taken == 'K') {
            whiteKing = true;
        }
        
        state.board[to.y][to.x] =		taken;
        state.board[from.y][from.x] = 	piece;
    }
    
    /**
     * Promotes the pawn to a queen.
     * 
     * @param move The move that put the pawn in the promotion position.
     */
    private void promotePawn(Move move) {
    	Square	to;
    	char	queen;
    	
    	to =	move.to;
    	
    	if (state.color == 1) {
    		queen =	'Q';
    	} else {
    		queen = 'q';
    	}
    	
    	state.board[to.y][to.x] =	queen;
    }
    
    /**
     * Un-promotes the queen back into a pawn.
     * 
     * @param move The move that put the pawn in the promotion position.
     */
    private void unpromotePawn(Move move) {
    	Square	to;
    	char	pawn;
    	
    	to =	move.to;
    	
    	if (state.color == 1) {
    		pawn =	'P';
    	} else {
    		pawn =	'p';
    	}
    	
    	state.board[to.y][to.x] =	pawn; 
    }
    
/*******************************************************************************
 *******************************************************************************
 PIECE EVALUATION METHODS
 *******************************************************************************
 ******************************************************************************/
    /**
     * A wrapper method that calls a particular eval function.
     * 
     * @return The value of the board.
     */
    private int eval() {
        return evalPieces();
    }
    
    /**
     * A method to determine the value of the board based on the pieces on it.
     * 
     * @return The value of the board.
     */
    private int evalPieces() {
        int     i;
        int     j;
        int     height;
        int     width;
        char    piece;
        int     friend;
        int     foe;
        
        height =    state.board.length;
        width =     state.board[0].length;
        friend =    0;
        foe =       0;
        
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                piece = state.board[i][j];
                
                // Make sure the piece is not a blank square.
                if (piece != '.') {
                    if (isFriend(piece)) {
                        // Calculate the value of white.
                        friend +=   pieceValue(piece);
                    } else {
                        // Calculate the value of black.
                        foe +=      pieceValue(piece);
                    }
                }
            }
        }
        
        return friend - foe;
    }
    
    /**
     * Determines the value for a particular piece.
     * 
     * @param  piece The piece that you need the value for.
     * 
     * @return The value for the piece in question.
     */
    private int pieceValue(char piece) {
        //bknpqr
        int     value;
        char    current;
        
        current =   Character.toLowerCase(piece);
        
        switch(current) {
            case 'b':
                value = 300;
                break;
            case 'k':
                value = 2500;
                break;
            case 'n':
                value = 200;
                break;
            case 'p':
                value = 175;
                break;
            case 'q':
                value = 900;
                break;
            case 'r':
                value = 500;
                break;
            default:
                value = 0;
        }
        
        return value;
    }
    
/*******************************************************************************
 *******************************************************************************
 AI FOR DETERMINING NEXT MOVE METHODS
 *******************************************************************************
 ******************************************************************************/
    /**
     * Generates a list of possible moves and then picks one at random.
     * 
     * @return The move chosen randomly.
     */
    private Move getRandomMove() {
        ArrayList<Move> moves;
        Random          rand;
        int				size;
        int				i;
        
        moves = getMoves();
        rand =  new Random();
        size =	moves.size();
        i =		rand.nextInt(size);
        
        return moves.get(i);
    }
    
    /*
     * Generates a list of possible moves and picks the one that looks to be 
     * next best based on what the board looks like now.  If no move is better 
     * than any other, it picks a random move.
     * 
     * @return The move chosen.
     *
    private Move getEvalMove() {
        ArrayList<Move> moves;
        Move            bestMove;
        char			taken;
        Bool			promotion;
        int				bestValue;
        int             value;
        int             i;
        int				size;
        Move			move;
        
        promotion =	new Bool(false);
        moves =     getMoves();
        size =		moves.size();
        
        Collections.shuffle(moves);
        
        i =			0;
        bestMove =  moves.get(i);
        taken =     doMove(bestMove, promotion);
        if (promotion.bool) {
        	promotePawn(bestMove);
        }
        bestValue = eval();
        if (promotion.bool){
        	unpromotePawn(bestMove);
        }
        undoMove(bestMove, taken, promotion);
        
        for (i = 1; i < size; i++) {
        	move =	moves.get(i);
        	
            taken = doMove(move, promotion);
            if (promotion.bool) {
            	promotePawn(move);
            }
            value = eval();
            if (promotion.bool) {
            	unpromotePawn(move);
            }
            
            undoMove(move, taken, promotion);
            
            if (bestValue < value) {
                bestValue = value;
                bestMove =  move;
            }
        }
        
        return bestMove;
    }*/
    
    /*
     * This method runs the negamax algorithm, and uses the result to find the 
     * next best move.  It returns the best move available, or null if there is 
     * no move available.
     *
     * @return The next best move, or null if no move possible.
     *
    private Move runNegamax() {
        int             bestValue;
        int             depth;
        ArrayList<Move> moves;
        char            taken;
        Bool			promotion;
        int             value;
        Move            bestMove;
        int				i;
        int				size;
        Move			move;
        
        bestValue = -1000;
        depth =     4;
        moves =     getMoves();
        promotion =	new Bool(false);
        bestMove =  null;
        size =		moves.size();
        
        Collections.shuffle(moves);
        
        if (moves.size() == 1) {
            return moves.get(0);
        }
        
        for (i = 0; i < moves.size(); i++) {
        	move =	moves.get(i);
        	
            taken = doMove(move, promotion);
            if (promotion.bool) {
            	promotePawn(move);
            }
            value = -negamax(depth - 1);
            if (promotion.bool) {
            	unpromotePawn(move);
            }
            undoMove(move, taken, promotion);
            
            if (bestValue < value) {
                bestValue = value;
                bestMove =  move;
            }
        }
        
        return bestMove;
    }*/
    
    /*
     * Negamax algorithm to determine the best available move based on the 
     * results from calling eval on the board state.
     * 
     * @param  depth The depth levels that we have left.
     * 
     * @return The value of this part of the tree.
     *
    private int negamax(int depth) {
        int             bestValue;
        ArrayList<Move> moves;
        char            taken;
        Bool			promotion;
        int             value;
        int				i;
        int				size;
        Move			move;
        
        bestValue = -1000;
        promotion =	new Bool(false);
        
        if (state.turns > 40) {
            return 999;
        }
        
        if (checkWin()) {
            return bestValue;
        }
        
        if (depth == 0) {
            return eval();
        }
        
        moves = getMoves();
        size =	moves.size();
        
        for (i = 0; i < moves.size(); i++) {
        	move =	moves.get(i);
        	
            taken = doMove(move, promotion);
            if (promotion.bool) {
            	promotePawn(move);
            }
            value = -negamax(depth - 1);
            if (promotion.bool) {
            	unpromotePawn(move);
            } 
            undoMove(move, taken, promotion);
            
            if (value > bestValue) {
                bestValue = value;
            }
        }
        
        return bestValue;
    }*/
    
    /**
     * This method runs the alpha-beta version of the negamax algorithm, and 
     * uses the result to find the next best move.  It returns the best move 
     * available, or null if there is no move available.
     * 
     * @return The next best move, or null if no move possible.
     */
    private Move runAlphabeta() {
    	int				depth;
    	Move			bestMove;
    	int				bestValue;
    	int				alpha;
    	ArrayList<Move>	moves;
    	char			taken;
    	Bool			promotion;
    	int				value;
    	int				size;
    	int				i;
    	Move			move;
    	
    	depth =		4;
    	bestMove =	null;
    	bestValue =	-1000;
    	alpha =		-1000;
    	promotion =	new Bool(false);
    	moves =		getMoves();
    	size =		moves.size();
    	
    	Collections.shuffle(moves);
    	
    	for (i = 0; i < size; i++) {
    		move =	moves.get(i);
    		
    		taken =	doMove(move, promotion);
    		if (promotion.bool) {
    			promotePawn(move);
    		}
    		value =	-alphabeta(depth - 1, -1000, -alpha);
    		if (promotion.bool) {
    			unpromotePawn(move);
    		}
    		undoMove(move, taken, promotion);
    		
    		if (value < bestValue) {
    			value =	bestValue;
    		}
    		
    		if (bestValue < value) {
    			bestValue =	value;
    			bestMove =	move;
    		}
    	}
    	
    	return bestMove;
    }
    
    /**
     * The alpha-beta version of the negamax algorithm to determine the best 
     * available move based on the results from calling eval on the board 
     * state.
     * 
     * @param  depth The depth levels that we have left.
     * @param  alpha The alpha cutoff
     * @param  beta  The beta cutoff
     * 
     * @return The value of this part of the tree.
     */
    private int alphabeta(int depth, int alpha, int beta) {
    	int				bestValue;
    	int				newAlpha;
    	ArrayList<Move>	moves;
    	char			taken;
    	Bool			promotion;
    	int				value;
    	int				i;
    	int				size;
    	Move			move;
    	
    	bestValue = -1000;
        promotion =	new Bool(false);
        
        if (state.turns > 40) {
            return -999;
        }
        
        if (checkWin()) {
            return bestValue;
        }
        
    	if (depth == 0) {
    		return eval();
    	}
    	
    	bestValue =	-1000;
    	newAlpha =	alpha;
    	moves =		getMoves();
    	size =		moves.size();
    	promotion =	new Bool(false);
    	
    	for (i = 0; i < size; i++) {
    		move =	moves.get(i);
    		
    		taken =	doMove(move, promotion);
    		if (promotion.bool) {
    			promotePawn(move);
    		}
    		value =	-alphabeta(depth - 1, -beta, -newAlpha);
    		if (promotion.bool) {
    			unpromotePawn(move);
    		}
    		undoMove(move, taken, promotion);
    		
    		if (bestValue < value) {
    			bestValue =	value;
    		}
    		if (newAlpha < value) {
    			newAlpha = value;
    		}
    		if (bestValue >= beta) {
    			return bestValue;
    		}
    	}
    	
    	return bestValue;
    }
    
    /**
     * Runs the iterative deepening negamax algorithm.  Returns the next best 
     * move from a particular depth.  It is used to fill the time left over 
     * after finding an initial move.  Returns null if there is no move 
     * possible.
     * 
     * @param  startTime The time when the computer's turn had started.
     * 
     * @return The next best move, or null if no move possible.
     */
    private Move runIterative(long startTime) {
    	int				depth;
    	Move			bestMove;
    	int				bestValue;
    	int				alpha;
    	Bool			promotion;
    	ArrayList<Move>	moves;
    	char			taken;
    	int				value;
    	int				size;
    	int				i;
    	Move			move;
    	long			elapsedTime;
    	
    	depth =		1;
    	bestMove =	null;
    	timeOver =	false;
    	
    	while (!timeOver) {
    		bestValue =	-1000;
    		alpha =		-1000;
    		promotion =	new Bool(false);
    		moves =		getMoves();
    		size =		moves.size();
    		
    		for (i = 0; i < size; i++) {
    			move =	moves.get(i);
    			
    			taken =	doMove(move, promotion);
    			if (promotion.bool) {
    				promotePawn(move);
    			}
    			value =	-iterative(depth, -1000, -alpha, startTime);
    			if (promotion.bool) {
    				unpromotePawn(move);
    			}
    			undoMove(move, taken, promotion);
    			if (!timeOver) {
    				if (value < bestValue) {
    					value = bestValue;
    				}
    				
    				if (bestValue < value) {
    					bestValue =	value;
    					bestMove =	move;
    				}
    				
    				elapsedTime =	getTime() - startTime;
    				
    				if (elapsedTime > timeAllowed) {
    					timeOver =	true;
    					break;
    				}
    			}
    		}
    	}
    	
    	return bestMove;
    }
    
    private int iterative(int depth, int alpha, int beta, long startTime) {
    	int				bestValue;
    	int				newAlpha;
    	ArrayList<Move>	moves;
    	long			elapsedTime;
    	char			taken;
    	Bool			promotion;
    	int				value;
    	int				i;
    	int				size;
    	Move			move;
    	
    	bestValue =	-1000;
    	
    	if (state.turns > 40) {
    		return -999;
    	}
    	
    	if (checkWin()) {
    		return bestValue;
    	}
    	
    	if (depth == 0) {
    		return eval();
    	}
    	
    	newAlpha =	alpha;
    	moves =		getMoves();
    	size =		moves.size();
    	promotion =	new Bool(false);
    	
    	for (i = 0; i < size; i++) {
    		elapsedTime =	getTime() - startTime;
    		
    		if (elapsedTime > timeAllowed) {
    			timeOver =	true;
    			break;
    		} else {
	    		move =	moves.get(i);
	    		
	    		taken =	doMove(move, promotion);
	    		if (promotion.bool) {
	    			promotePawn(move);
	    		}
	    		value =	-iterative(depth - 1, -beta, -newAlpha, startTime);
	    		if (promotion.bool) {
	    			unpromotePawn(move);
	    		}
	    		undoMove(move, taken, promotion);
	    		
	    		if (bestValue < value) {
	    			bestValue =	value;
	    		}
	    		if (newAlpha < value) {
	    			newAlpha = value;
	    		}
	    		if (bestValue >= beta) {
	    			return bestValue;
	    		}
    		}
    	}
    	
    	return bestValue;
    }
    
    /**
     * Runs the iterative deepening alpha-beta algorithm that uses a transition 
     * table to help improve search efficiency.  Returns the next best move 
     * from a particular depth.  It is used to fill the time left over after 
     * finding an initial move.  Returns null if there is no move possible.
     * 
     * @param  startTime The time when the computer's turn had started.
     * 
     * @return The next best move, or null if no move possible.
     */
    private Move runTablemax(long startTime) {
    	int				depth;
    	Move			bestMove;
    	int				bestValue;
    	int				alpha;
    	Bool			promotion;
    	ArrayList<Move>	moves;
    	char			piece;
    	char			taken;
    	int				value;
    	int				i;
    	int				size;
    	Move			move;
    	long			elapsedTime;
    	
    	depth =		1;
    	bestMove =	null;
    	timeOver =	false;
    	
    	while (!timeOver) {
    		bestValue =	-1000;
    		alpha =		-1000;
    		promotion =	new Bool(false);
    		moves =		getMoves();
    		size =		moves.size();
    		
    		for (i = 0; i < size; i++) {
    			move =	moves.get(i);
    			
    			piece =	state.board[move.from.y][move.to.x];
    			taken =	doMove(move, promotion);
    			if (promotion.bool) {
    				promotePawn(move);
    				ttable.updatePromote(move, piece, taken, state.color);
    			} else {
    				ttable.updateHash(move, piece, taken, state.color);
    			}
    			value =	-tablemax(depth, -1000, -alpha, startTime);
    			if (promotion.bool) {
    				unpromotePawn(move);
    				ttable.unupdatePromote(move, piece, taken, state.color);
    			} else {
    				ttable.unupdateHash(move, piece, taken, state.color);
    			}
    			undoMove(move, taken, promotion);
    			
    			if (!timeOver) {
    				if (value < bestValue) {
    					value =	bestValue;
    				}
    				
    				if (bestValue < value) {
    					bestValue =	value;
    					bestMove =	move;
    				}
    				
    				elapsedTime =	getTime() - startTime;
    				
    				if (elapsedTime > timeAllowed) {
    					timeOver =	true;
    					break;
    				}
    			}
    		}
    	}
    	
    	return bestMove;
    }
    
    private int tablemax(int depth, int alpha, int beta, long startTime) {
    	TTableEntry		te;
    	Bool			promotion;
    	ArrayList<Move>	moves;
    	int				bestValue;
    	int				bestAlpha;
    	long			elapsedTime;
    	char			piece;
    	char			taken;
    	int				value;
    	int				i;
    	int				size;
    	Move			move;
    	
    	bestValue =	-1000;
    	
    	if (state.turns > 40) {
    		return -999;
    	}
    	
    	if (checkWin()) {
    		return bestValue;
    	}
    	
    	if (depth == 0) {
    		return eval();
    	}
    	
    	te =	ttable.getEntry();
    	
    	if ((te != null) && ((((te.a < te.value) && (te.value < te.b)) || 
    			((te.a <= alpha) && (beta <= te.b))) && (te.depth >= depth))) {
    		return te.value;
    	}
    	
    	promotion =	new Bool(false);
    	moves =		getMoves();
    	bestAlpha =	alpha;
    	size =		moves.size();
    	
    	for (i = 0; i < size; i++) {
    		move =			moves.get(i);
    		elapsedTime =	getTime() - startTime;
    		
    		if (elapsedTime > timeAllowed) {
    			timeOver =	true;
    			break;
    		} else {
    			piece =	state.board[move.from.y][move.from.x];
    			taken =	doMove(move, promotion);
    			if (promotion.bool) {
    				promotePawn(move);
    				ttable.updatePromote(move, piece, taken, state.color);
    			} else {
    				ttable.updateHash(move, piece, taken, state.color);
    			}
    			value =	-tablemax((depth - 1), -beta, -bestAlpha, startTime);
    			if (promotion.bool) {
    				unpromotePawn(move);
    				ttable.unupdatePromote(move, piece, taken, state.color);
    			} else {
    				ttable.unupdateHash(move, piece, taken, state.color);
    			}
    			undoMove(move, taken, promotion);
    			
    			if (bestValue < value) {
    				bestValue =	value;
    			}
    			
    			if (bestAlpha < value) {
    				bestAlpha = value;
    			}
    			
    			if (bestValue >= beta) {
    				return bestValue;
    			}
    		}
    	}
    	
    	return bestValue;
    }
    
/*******************************************************************************
 *******************************************************************************
 SCAN FOR MOVES METHODS
 *******************************************************************************
 ******************************************************************************/
    /**
     * Populates the moves list with all available moves for a side.
     * 
     * @return An ArrayList of Move.
     */
    private ArrayList<Move> getMoves() {
        ArrayList<Move>     moves;
        int                 i;
        int                 j;
        int					height;
        int					width;
        char                piece;
        
        height =	state.board.length;
        width =		state.board[0].length;
        moves = 	new ArrayList<Move>();
        
        // Loop through the pieces.
        for (i = 0; i < height; i++) {
            for (j = 0; j < width; j++) {
                piece = state.board[i][j];
                
                if ((piece != '.') && isFriend(piece)) {
                    moves.addAll(scan(new Square(j, i)));
                }
            }
        }
        
        return moves;
    }
    
    /**
     * This method is called to gather the moves for the king
     * 
     * @param  from  The square the piece is moving from.
     * @param  white True if piece is white and false if piece is black.
     * 
     * @return An ArrayList of Move
     */
    private ArrayList<Move> scanKing(Square from, boolean white) {
    	ArrayList<Move>	moves;
    	moves =	new ArrayList<Move>();
    	
    	moves.addAll(scanAll(from, false, white));
    	
    	return moves;
    }
    
    /**
     * This method is called to gather the moves for the queen.
     * 
     * @param from  The square the piece is moving from.
     * @param  white True if piece is white and false if piece is black.
     * 
     * @return An ArrayList of Move
     */
    private ArrayList<Move> scanQueen(Square from, boolean white) {
    	ArrayList<Move>	moves;
    	moves =	new ArrayList<Move>();
    	
    	moves.addAll(scanAll(from, true, white));
    	
    	return moves;
    }
    
    /**
     * This method is called to gather the moves for the rook.
     * 
     * @param from  The square the piece is moving from.
     * @param  white True if piece is white and false if piece is black.
     * 
     * @return An ArrayList of Move
     */
    private ArrayList<Move> scanRook(Square from, boolean white) {
    	ArrayList<Move>	moves;
    	moves =	new ArrayList<Move>();
    	
    	moves.addAll(scanOrthogonal(from, true, white));
    	
    	return moves;
    }
    
    /**
     * This method is called to gather the moves for the bishop.
     * 
     * @param  from  The square the piece is moving from.
     * @param  white True if piece is white and false if piece is black.
     * 
     * @return An ArrayList of Move.
     */
    private ArrayList<Move> scanBishop(Square from, boolean white)  {
        ArrayList<Move> moves;
        moves = new ArrayList<Move>();
        
        moves.addAll(scanDiagonal(from, true, white));
        moves.addAll(scanOrthogonalBishop(from, white));
        
        return moves;
    }
    
    /**
     * Scans the knight's coordinates (e.g. 1 out and 2 over, or 2 out and 1 
     * over) from the starting position.
     * 
     * @param  from  The square the piece is moving from.
     * @param  white True if piece is white and false if piece is black.
     * 
     * @return An ArrayList of Move.
     */
    private ArrayList<Move> scanKnight(Square from, boolean white) {
        ArrayList<Move> moves;
        int             width;
        int             height;
        int             x;
        int             y;
        
        moves =     new ArrayList<Move>();
        width =     state.board[0].length;
        height =    state.board.length;
        
        // (x-2, y-1)
        y = from.y - 1;
        
        if (y > -1) {
            x = from.x - 2;
            
            if (x > -1) {
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                } else if (!isFriend(state.board[y][x])) {
                    moves.add(new Move(from, new Square(x, y)));
                }
            }
            
            // (x+2, y-1)
            x = from.x + 2;
            
            if (x < width) {
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                } else if (!isFriend(state.board[y][x])) {
                    moves.add(new Move(from, new Square(x, y)));
                }
            }
        }
        
        // (x-1, y-2)
        y = from.y - 2;
        
        if (y > -1) {
            x = from.x - 1;
            
            if (x > -1) {
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                } else if (!isFriend(state.board[y][x])) {
                    moves.add(new Move(from, new Square(x, y)));
                }
            }
            
            // (x+1, y-2)
            x = from.x + 1;
            
            if (x < width) {
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                } else if (!isFriend(state.board[y][x])) {
                    moves.add(new Move(from, new Square(x, y)));
                }
            }
        }
        
        // (x-2, y+1)
        y = from.y + 1;
        
        if (y < height) {
            x = from.x - 2;
            
            if (x > -1) {
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                } else if (!isFriend(state.board[y][x])) {
                    moves.add(new Move(from, new Square(x, y)));
                }
            }
            
            // (x+2, y+1)
            x = from.x + 2;
            
            if (x < width) {
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                } else if (!isFriend(state.board[y][x])) {
                    moves.add(new Move(from, new Square(x, y)));
                }
            }
        }
        
        // (x-1, y+2)
        y = from.y + 2;
        
        if (y < height) {
            x = from.x - 1;
            
            if (x > -1) {
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                } else if (!isFriend(state.board[y][x])) {
                    moves.add(new Move(from, new Square(x, y)));
                }
            }
            
            // (x+1, y+2)
            x = from.x + 1;
            
            if (x < width) {
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                } else if (!isFriend(state.board[y][x])) {
                    moves.add(new Move(from, new Square(x, y)));
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Scans the square either up or down from the starting position, depending 
     * on the pieces color.  Pawns can also attack in the two diagonals in the 
     * direction that the pawns move, but only if the square is occupied by an 
     * enemy.  Additionally, if the pawn reaches the end, it is promoted to a 
     * queen.
     * 
     * @param  from  The square the piece is moving from.
     * @param  white True if piece is white and false if piece is black.
     * 
     * @return An ArrayList of Move.
     */
    private ArrayList<Move> scanPawn(Square from, boolean white) {
        int             width;
        int             height;
        int             x;
        int             y;
        int             ldX;
        int             rdX;
        ArrayList<Move> moves;
        
        width =     state.board[0].length;
        height =    state.board.length;
        x =         from.x;
        ldX =       from.x - 1;
        rdX =       from.x + 1;
        moves =     new ArrayList<Move>();
        
        
        if (white) {
            y = from.y - 1;
            
            // Wall collision detection.
            if (y > -1) {
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                }
                
                // Check diagonals.
                if (ldX > -1) {
                    if (state.board[y][ldX] != '.') {
                        if (!isFriend(state.board[y][ldX])) {
                            moves.add(new Move(from, new Square(ldX, y)));
                        }
                    }
                }
                
                if (rdX < width) {
                    if (state.board[y][rdX] != '.') {
                        if (!isFriend(state.board[y][rdX])) {
                            moves.add(new Move(from, new Square(rdX, y)));
                        }
                    }
                }
            }
        } else {
            y = from.y + 1;
            
            // Wall collision detection.
            if (y < height) {
                // Piece collision detection.
                if (state.board[y][x] == '.') {
                    moves.add(new Move(from, new Square(x, y)));
                }
                
                // Check diagonals.
                if (ldX > -1) {
                    if (state.board[y][ldX] != '.') {
                        if (!isFriend(state.board[y][ldX])) {
                            moves.add(new Move(from, new Square(ldX, y)));
                        }
                    }
                }
                
                if (rdX < width) {
                    if (state.board[y][rdX] != '.') {
                        if (!isFriend(state.board[y][rdX])) {
                            moves.add(new Move(from, new Square(rdX, y)));
                        }
                    }
                }
            }
        }
        
        return moves;
    }
    
    /**
     * Given a piece on the board, the scan function determines which scan to 
     * perform for the piece.
     * 
     * @param  from The square the piece is moving from.
     * 
     * @return An ArrayList of Move.
     */
    private ArrayList<Move> scan(Square from) {
        ArrayList<Move> moves;
        
        // The pieces in alphabetical order.
        // bknpqr
        switch (state.board[from.y][from.x]) {
                // White pieces.
            case 'B':
                moves = scanBishop(from, true);
                break;
            case 'K':
                moves = scanKing(from, true); //scanAll(from, false, true);
                break;
            case 'N':
                moves = scanKnight(from, true);
                break;
            case 'P':
                moves = scanPawn(from, true);
                break;
            case 'Q':
                moves = scanQueen(from, true); //scanAll(from, true, true);
                break;
            case 'R':
                moves = scanRook(from, true); //scanOrthogonal(from, true, true);
                break;
                
                // Black pieces.
            case 'b':
                moves = scanBishop(from, false);
                break;
            case 'k':
                moves = scanKing(from, false); //scanAll(from, false, false);
                break;
            case 'n':
                moves = scanKnight(from, false);
                break;
            case 'p':
                moves = scanPawn(from, false);
                break;
            case 'q':
                moves = scanQueen(from, false); //scanAll(from, true, false);
                break;
            case 'r':
                moves = scanRook(from, false); //scanOrthogonal(from, true, false);
                break;
            default:
                moves = new ArrayList<Move>();
        }
        
        return moves;
    }
    
    /**
     * Scans in all directions on the board from the starting position.
     * 
     * @param  from   The square the piece is moving from.
     * @param  repeat Whether the scan is only one square in a direction, or 
     *                continuous.
     * @param  white  True if piece is white and false if piece is black.
     * 
     * @return An ArrayList of Move.
     */
    private ArrayList<Move> scanAll(Square from, boolean repeat, 
                                    boolean white) {
        ArrayList<Move> moves;
        moves = new ArrayList<Move>();
        
        moves.addAll(scanDiagonal(from, repeat, white));
        moves.addAll(scanOrthogonal(from, repeat, white));
        
        return moves;
    }
    
    /**
     * Scans in the diagonal directions from the starting position.  If this is 
     * a bishop we also check the orthogonals 1 square out.  If they are 
     * unoccupied, we can move there.
     * 
     * @param  from   The square the piece is moving from.
     * @param  repeat Whether the scan is only one square in a direction, or 
     *                continuous.
     * @param  white  True if piece is white and false if piece is black.
     * @param  bishop Whether this pieces is a bishop or not.
     * 
     * @return An ArrayList of Move.
     */
    private ArrayList<Move> scanDiagonal(Square from, boolean repeat, 
                                         boolean white) {
        int             height;
        int             width;
        int             x;
        int             y;
        int             minX;
        int             minY;
        int             maxX;
        int             maxY;
        ArrayList<Move> moves;
        
        height =    state.board.length;
        width =     state.board[0].length;
        moves =     new ArrayList<Move>();
        
        //**********************************************************************
        // Setting up the min and max values like this allows me to use the 
        // same loop for a repeating move and a non-repeating one.
        if (repeat) {
            minX =  -1;
            minY =  -1;
            maxX =  width;
            maxY =  height;
        } else {
            minX =  from.x - 2;
            minY =  from.y - 2;
            maxX =  from.x + 2;
            maxY =  from.y + 2;
            
            // Clamp values to make sure they are not out of bounds.
            if (minX < -1) {
                minX =  -1;
            }
            if (minY < -1) {
                minY =  -1;
            }
            if (maxX > width) {
                maxX =  width;
            }
            if (maxY > height) {
                maxY =  height;
            }
        }
        
        //**********************************************************************
        // The moves for the different directions.
        // NW (x-1, y-1)
        for (x = from.x - 1, y = from.y - 1; x > minX && y > minY; x--, y--) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            } else if (!isFriend(state.board[y][x])) {
                moves.add(new Move(from, new Square(x, y)));
                break;
            } else {
                break;
            }
        }
        // NE (x+1, y-1)
        for (x = from.x + 1, y = from.y - 1; x < maxX && y > minY; x++, y--) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            } else if (!isFriend(state.board[y][x])) {
                moves.add(new Move(from, new Square(x, y)));
                break;
            } else {
                break;
            }
        }
        // SE (x+1, y+1)
        for (x = from.x + 1, y = from.y + 1; x < maxX && y < maxY; x++, y++) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            } else if (!isFriend(state.board[y][x])) {
                moves.add(new Move(from, new Square(x, y)));
                break;
            } else {
                break;
            }
        }
        // SW (x-1, y+1)
        for (x = from.x - 1, y = from.y + 1; x > minX && y < maxY; x--, y++) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            } else if (!isFriend(state.board[y][x])) {
                moves.add(new Move(from, new Square(x, y)));
                break;
            } else {
                break;
            }
        }
        
        return moves;
    }
    
    /**
     * Scans in the orthogonal directions from the starting position.
     * 
     * @param  from   The square the piece is moving from.
     * @param  repeat Whether the scan is only one square in a direction, or 
     *                continuous.
     * @param  white  True if piece is white and false if piece is black.
     * 
     * @return An ArrayList of Move.
     */
    private ArrayList<Move> scanOrthogonal(Square from, boolean repeat, 
                                           boolean white) {
        int             height;
        int             width;
        int             x;
        int             y;
        int             minX;
        int             minY;
        int             maxX;
        int             maxY;
        ArrayList<Move> moves;
        
        height =    state.board.length;
        width =     state.board[0].length;
        moves =     new ArrayList<Move>();
        
        if (repeat) {
            minX =  -1;
            minY =  -1;
            maxX =  width;
            maxY =  height;
        } else {
            minX =  from.x - 2;
            minY =  from.y - 2;
            maxX =  from.x + 2;
            maxY =  from.y + 2;
            
            // clamp values to make sure they are not out of bounds.
            if (minX < -1) {
                minX =  -1;
            }
            if (minY < -1) {
                minY =  -1;
            }
            if (maxX > width) {
                maxX =  width;
            }
            if (maxY > height) {
                maxY =  height;
            }
        }
        
        // N (x, y-1)
        for (x = from.x, y = from.y - 1; y > minY; y--) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            } else if (!isFriend(state.board[y][x])) {
                moves.add(new Move(from, new Square(x, y)));
                break;
            } else {
                break;
            }
        }
        // E (x+1, y)
        for (x = from.x + 1, y = from.y; x < maxX; x++) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            } else if (!isFriend(state.board[y][x])) {
                moves.add(new Move(from, new Square(x, y)));
                break;
            } else {
                break;
            }
        }
        // S (x, y+1)
        for (x = from.x, y = from.y + 1; y < maxY; y++) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            } else if (!isFriend(state.board[y][x])) {
                moves.add(new Move(from, new Square(x, y)));
                break;
            } else {
                break;
            }
        }
        // W (x-1, y)
        for (x = from.x - 1, y = from.y; x > minX; x--) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            } else if (!isFriend(state.board[y][x])) {
                moves.add(new Move(from, new Square(x, y)));
                break;
            } else {
                break;
            }
        }
        
        return moves;
    }
    
    /**
     * This function scans the orthogal spaces for the bishop, and will allow 
     * the bishop to move there if the space is unoccupied.
     * 
     * @param  from  The square the piece is moving from.
     * @param  white True if piece is white and false if piece is black.
     * 
     * @return An ArrayList of Move.
     */
    private ArrayList<Move> scanOrthogonalBishop(Square from, boolean white) {
        int             x;
        int             y;
        int             height;
        int             width;
        ArrayList<Move> moves;
        
        x =         from.x;
        height =    state.board.length;
        width =     state.board[0].length;
        moves =     new ArrayList<Move>();
        
        // N
        y = from.y - 1;
        
        if (y > -1) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            }
        }
        
        // S
        y = from.y + 1;
        
        if (y < height) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            }
        }
        
        y = from.y;
        
        // W
        x = from.x - 1;
        
        if (x > -1) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            }
        }
        
        // E
        x = from.x + 1;
        
        if (x < width) {
            if (state.board[y][x] == '.') {
                moves.add(new Move(from, new Square(x, y)));
            }
        }
        
        return moves;
    }
    
/*******************************************************************************
 *******************************************************************************
 PRINT METHODS
 *******************************************************************************
 ******************************************************************************/
    /**
     * Prints the ply number, whose turn it is, and the status of the board.
     */
    private void print() {
    	System.out.print(state.turns + " ");
    	
    	if (state.color == 1) {
            System.out.println("W");
        } else {
            System.out.println("B");
        }
    	
        System.out.println(state.toString());
    }
    
    /**
     * Prints who won and in how many moves, or if it was a draw.  It then 
     * prints the status of the board.
     */
    private void printResults() {
        if (draw) {
        	System.out.println("Game is a draw");
        } else {
        	if (state.color == 1) {
        		System.out.print("W");
        	} else {
        		System.out.print("B");
        	}
        	
        	System.out.println(" Wins in " + state.turns + " Moves");
        }
        
        System.out.println(state.toString());
    }
}
