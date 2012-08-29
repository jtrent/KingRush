/**
 * A hashtable that stores the different states of the game.  It does not worry 
 * about having a perfect hash, so collisions result in the information being 
 * overwritten.
 * 
 * Please read README and COPY for instructions and license information.
 * 
 * @author  Jon Trent (c) 2011
 * @version 1.0
 */

import java.lang.Math;

public class TTable {
	private int			size;
    private TTableEntry	entries[];
    private Zobrist		zob;
    
    public TTable(char board[][]) {
    	int	i;
    	size =		256;
    	entries =	new TTableEntry[size];
    	zob =		new Zobrist(board);
    	
    	for (i = 0; i < size; i++) {
    		entries[i] =	new TTableEntry();
    	}
    }
    
    private int getIndex() {
    	//System.out.println("index = " + ((int)zob.getZob() % size));
    	return Math.abs((int)zob.getZob() % size);
    }
    
    public TTableEntry getEntry() {
    	TTableEntry	entry;
    	
    	entry =	entries[getIndex()];
    	
    	if (entry.valid) {
    		return entry;
    	} else {
    		return null;
    	}
    }
    
    public void setEntry(int depth, int a, int b, int value) {
    	TTableEntry	entry;
    	entry =	new TTableEntry(depth, a, b, value);
    	
    	entries[getIndex()] =	entry;
    }
    
    public void updateHash(Move move, char piece, char taken, int color) {
    	zob.makeZob(move, piece, taken, color);
    }
    
    public void updatePromote(Move move, char piece, char taken, int color) {
    	zob.makePromote(move, piece, taken, color);
    }
    
    public void unupdateHash(Move move, char piece, char taken, int color) {
    	zob.unmakeZob(move, piece, taken, color);
    }
    
    public void unupdatePromote(Move move, char piece, char taken, int color) {
    	zob.unmakePromote(move, piece, taken, color);
    }
}
