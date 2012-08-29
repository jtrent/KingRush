/**
 * This is a struct that is used to keep track of the move information.  It has 
 * the cooridinates that we moved from and the ones that we are moving to.
 * 
 * Please read README and COPY for instructions and license information.
 * 
 * @author  Jon Trent (c) 2011
 * @version 1.0
 */

public class Move {
    public  Square  from;
    public  Square  to;
    
    public Move(Square from, Square to) {
        this.from = from;
        this.to =   to;
    }
    
    public String toString() {
    	String	output;
    	output =	"from" + from + " - to" + to;
    	
    	return output;
    }
}
