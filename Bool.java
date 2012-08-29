/**
 * The Java Boolean object's value cannot be passed into a method and changed 
 * with the results visible outside of the method.  So it does not appear to be 
 * passed by reference.  This is an object that would do the same thing, but 
 * would ensure that the boolean would be passed by reference.
 * 
 * Please read README and COPY for instructions and license information.
 * 
 * @author  Jon Trent (c) 2011
 * @version 1.0
 */

public class Bool {
	public	boolean	bool;
	
	public Bool(boolean bool) {
		this.bool =	bool;
	}
	
	public String toString() {
		String	output;
		output =	"This boolean is ";
		
		if (bool) {
			output +=	"true.";
		} else {
			output +=	"false";
		}
		
		return output;
	}
}
