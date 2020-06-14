import java.io.Serializable;

//Class is used to transfer game information to server and between clients 
public class MorraInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8073692471669315543L;
	int p1Points;
	int p2Points;
	int p1Plays;
	int p2Plays;
	int p1Guess;
	int p2Guess;
	int playAgain;
	
	String serMess;
	
	int curPlayer;
	int numPlayers = 0;
	
	MorraInfo()
	{
		p1Points = 0;
		p1Points = 0;
		
		p1Plays = -1;
		p2Plays = -1;
		
		p1Guess = -1;
		p2Guess = -1;
		
		curPlayer = -1;
		numPlayers = 0;
		playAgain = 0;
	}
	
	

}
