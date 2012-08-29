------------------------------------------------------------------------
Please see COPY for license information.
------------------------------------------------------------------------

PROJECT TITLE: 	King Rush
AUTHOR:		Jon Trent
VERSION:	3.0
DATE:		6/1/2011

FILES INCLUDED:
AIAdvancedPlayer.javaAIBeginnerPlayer.javaBool.javaCOPY.txtHumanPlayer.javaKingRush.javaMove.javaPlayer.javaREADME.txtSquare.javaState.javaTTable.javaTTableEntry.javaZobrist.java

HOW TO START THIS PROJECT:
To compile KingRush, navigate to the folder on your command line utility and type:
javac KingRush.java

To play the game, navigate to the folder on your command line utility and type:
java KingRush

USER INSTRUCTIONS:
The default game pits one AI opponent against another.  The white player is set to the higher setting, while the black player picks moves at random.

Calling the game with the number of players [0...2], the difficulty level of the white player [1...2], and the difficulty of the black player [1..2] will start a custom game, such as:
java KingRush 1 1 2
If you pick 0 for the number of players, then you have two AI players playing each other.
If you pick 1 for the number of players, then you have a white human player playing against a black AI player, and the second option does not matter.  Only the third option sets the AI level.
If you pick 2 for the number of players, then you have two human players playing against each other, and the second and third options are ignored.

To play a network came, call the game with an n:
java KingRush n