/**
 * This is a special key that represents the current state for the game.  It is 
 * designed so that it only needs to be called by TTable, and not by 
 * KingRush.  It has methods so that the hash can be updated as the game is 
 * played.
 * 
 * Please read README and COPY for instructions and license information.
 * 
 * @author  Jon Trent (c) 2011
 * @version 1.0
 */

import java.util.Random;
import java.lang.Math;

public class Zobrist {
	private	long	rands[][];
	private long	white;
	private	long	black;
	private	long	zob;
	
	public Zobrist(char board[][]) {
		Random	rand;
		int		i;
		int		j;
		int		isize;
		int		jsize;
		
		rand =	new Random();
		rands =	new long[12][30];
		isize =	12;
		jsize =	30;
		
		for (i = 0; i < isize; i++) {
			for (j = 0; j < jsize; j++) {
				rands[i][j] =	Math.abs(rand.nextLong());
			}
		}
		
		white =	Math.abs(rand.nextLong());
		black =	Math.abs(rand.nextLong());
		zob =	Math.abs(rand.nextLong());
		
		initializeRands(board);
	}
	
	private void initializeRands(char board[][]) {
		int i;
		int j;
		int isize;
		int jsize;
		
		isize =	board.length;
		jsize =	board[0].length;
		
		for (i = 0; i < isize; i++) {
			for (j = 0; j < jsize; j++) {
				
			}
		}
	}
	
	public long getZob() {
		return zob;
	}
	
	public void makeZob(Move move, char piece, char taken, int color) {
		zob =	zob ^ getRand(move.from, piece);
		
		if (taken != '.') {
			zob =	zob ^ getRand(move.to, taken);
		}
		
		zob =	zob ^ getRand(move.to, piece);
		
		if (color == 1) {
			zob =	zob ^ black ^ white;
		} else {
			zob =	zob ^ white ^ black;
		}
	}
	
	public void makePromote(Move move, char piece, char taken, int color) {
		zob =	zob ^ getRand(move.from, piece);
		
		if (taken != '.') {
			zob =	zob ^ getRand(move.to, taken);
		}
				
		if (color == 1) {
			zob =	zob ^ getRand(move.to, 'Q') ^ black ^ white;
		} else {
			zob =	zob ^ getRand(move.to, 'q') ^ white ^ black;
		}
	}
	
	public void unmakeZob(Move move, char piece, char taken, int color) {
		if (color == 1) {
			zob =	zob ^ white ^ black;
		} else {
			zob =	zob ^ black ^ white;
		}
		
		zob =	zob ^ getRand(move.to, piece);
		
		if (taken != '.') {
			zob = zob ^ getRand(move.to, taken);
		}
		
		zob =	zob ^ getRand(move.from, piece);
	}
	
	public void unmakePromote(Move move, char piece, char taken, int color) {
		if (color == 1) {
			zob =	zob ^ getRand(move.to, 'Q') ^ white ^ black;
		} else {
			zob =	zob ^ getRand(move.to, 'q') ^ black ^ white;
		}
		
		if (taken != '.') {
			zob =	zob ^ getRand(move.to, taken);
		}
		
		zob =	zob ^ getRand(move.from, piece);
	}
	
	private	long getRand(Square position, char piece) {
		int	x;
		int	y;
		
		x =	convertPiece(piece);
		y =	(5 * position.y) + position.x;
		
		return rands[x][y];
	}
	
	private int convertPiece(char piece) {
		int	number;
		number =	0;
		
		switch (piece) {
			case 'B':
				number = 	0;
				break;
			case 'K':
				number = 	1;
				break;
			case 'N':
				number = 	2;
				break;
			case 'P':
				number = 	3;
				break;
			case 'Q':
				number =	4;
				break;
			case 'R':
				number = 	5;
				break;
			case 'b':
				number =	6;
				break;
			case 'k':
				number =	7;
				break;
			case 'n':
				number =	8;
				break;
			case 'p':
				number =	9;
				break;
			case 'q':
				number =	10;
				break;
			case 'r':
				number =	11;
				break;
		}
		
		return number;
	}
}
