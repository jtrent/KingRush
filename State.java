/**
 * This is a struct that keeps track of state information.  Previously this was 
 * the class that also had most of the game's methods, but those have been 
 * separated into their own class.
 * 
 * Please read README and COPY for instructions and license information.
 *
 * Version 1.0: This was basically the game, containing all of the states and 
 *              methods for the game.
 * Version 2.0: Separated the methods from this class.  The color field was 
 *              also changed from a boolean to an int.  This allows an easy 
 *              switch between negamax calls by passing in -color.
 * 
 * @author  Jon Trent (c) 2011
 * @version 2.0
 */

public class State {
    public  char    board[][];
    public  int     color;
    public  int     turns;
    
    public State(int width, int height) {
        board = new char[height][width];
        color = 1;
        turns = 1;
    }
    
    public String toString() {
        int     i;
        int     j;
        String  output;
        
        output =	""; //turns + " ";
        
        /*if (color == 1) {
            output +=   "W";
        } else {
            output +=   "B";
        }
        
        output +=   "\n";*/
        
        for (i = 0; i < board.length; i++) {
            for (j = 0; j < board[0].length; j++) {
                output +=   board[i][j];
            }
            
            output +=   "\n";
        }
        
        output +=   "\n";
        
        return output;
    }
}
