import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;

//import org.junit.jupiter.api.DisplayName;

//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;

class MorraTest {

	MorraInfo test;
	
	MorraInfo serverInfo;
	MorraInfo clientInfo;
	
	Server serverConnection;
	Client clientConnection;
	
	@BeforeEach
	void defaultN()
	{
		test = new MorraInfo();
		
		serverConnection = new Server(data -> 
		{
			  try{
			    Platform.runLater(()->
				{
					serverInfo = (MorraInfo) data;
				});
				}
				catch(Exception e){}
		}, 5555);
		
		clientInfo = new MorraInfo();
		
		clientConnection = new Client(data -> 
		{
			Platform.runLater(()->
			{
				clientInfo = (MorraInfo) data;
			});
		}, "127.0.0.1", 5555);
		
	}

	//---Test Default Constructor---//
	
	@Test
	void IPAdress()
	{
		assertEquals("127.0.0.1", clientConnection.IPAddress, "Wrong IP adress");
	}
	
	@Test 
	void portNumber()
	{
		assertEquals(5555, clientConnection.portNumber, "Wrong Port Number");
	}
	
	@Test
	void getClientName()
	{
		assertEquals("Client", clientConnection.getClass().getName());
	}
	
	//---Test Morra Info---//
	
	@Test
	void p1PlaysTest()
	{
		assertEquals(-1, test.p1Plays, "Not the right Play (p1)");
	}
	
	@Test
	void p2PlaysTest()
	{
		assertEquals(-1, test.p2Plays, "Not the right Play (p2)");
	}
	
	@Test
	void p1GuessTest()
	{
		assertEquals(-1, test.p1Guess, "Not the right Guess (p1)");
	}
	
	@Test
	void p2GuessTest()
	{
		assertEquals(-1, test.p2Guess, "Not the right Guess (p2)");
	}
	
	@Test
	void CurrentPlayer()
	{
		assertEquals(-1, test.curPlayer, "Not the right Current Player");
	}
	
	@Test 
	void NumberOfPlayers()
	{
		assertEquals(0, test.numPlayers, "Not the right Number of players");
	}
	
	@Test
	void playAgain()
	{
		assertEquals(0, test.playAgain, "Not the right Play Again");
	}

}
