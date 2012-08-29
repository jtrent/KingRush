/**
 * Entries for the transition tables.  Storing this will help keep track of 
 * states that we have already calculated.
 * 
 * Please read README and COPY for instructions and license information.
 * 
 * @author  Jon Trent (c) 2011
 * @version 1.0
 */

public class TTableEntry {
	public	int		depth;
	public	int		a;
	public	int		b;
	public	int		value;
	public	boolean	valid;
	
	public TTableEntry() {
		valid =	false;
	}
	
	public TTableEntry(int depth, int a, int b, int value) {
		this.depth =	depth;
		this.a =		a;
		this.b =		b;
		this.value =	value;
		valid =			true;
	}
}
