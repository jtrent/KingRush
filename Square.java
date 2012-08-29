/**
 * A class to keep track of coordinate information.
 * 
 * Please read README and COPY for instructions and license information.
 * 
 * @author  Jon Trent (c) 2011
 * @version 1.0
 */

public class Square {
    public  int x;
    public  int y;
    
    public Square(int x, int y) {
        this.x =    x;
        this.y =    y;
    }
    
    public String toString() {
    	String	output;
    	output =	"(" + x + ", " + y + ")";
    	
    	return output;
    }
}
