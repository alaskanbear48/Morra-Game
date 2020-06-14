import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.stage.Stage;

//import org.junit.jupiter.api.DisplayName;

//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.ValueSource;

class MorraTest {

	MorraInfo p1;
	MorraInfo p2;
	MorraInfo total;
	
	MorraInfo serverInfo;
	MorraInfo clientInfo;
	
	Server serverConnection;
	Client clientConnection;
	
	@BeforeEach
	void defaultN()
	{
		p1 = new MorraInfo();
		p2 = new MorraInfo();
		total = new MorraInfo();
		
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
	
	//---Tests that the default constructor is called for the Server---//
	@Test
	void testPlayerOneDefault() 
	{
		assertNull(serverConnection.playerOne, "Did Not Set playerOne to Null");
	}
	
	@Test 
	void testPlayerTwoDefault()
	{
		assertNull(serverConnection.playerTwo, "Did Not Set playerTwo to Null");
	}
	
	@Test
	void testPlayer1Info()
	{
		assertNull(serverConnection.infoP1, "Did Not Set infoP1 to Null");
	}
	
	@Test
	
	void testPlayer2Info()
	{
		assertNull(serverConnection.infoP2, "Did Not Set infoP2 to Null");
	}

	@Test
	void testNumOfPlayers()
	{
		assertEquals(0, serverConnection.numPlayers, "The numPlayers is not 0");
	}
	
	@Test
	void testPortNum()
	{
		assertEquals(5555, serverConnection.portNum, "The portNum is not 5555");
	}
	
	@Test
	void getClientName()
	{
		assertEquals("Server", serverConnection.getClass().getName());
	}
	
	//---Test Game Logic---//
	
	@Test 
	void testPlayer1Win()
	{
		p1.p1Plays = 3;
		p2.p1Plays = 4;
		
		p1.p1Guess = 7;
		p2.p1Guess = 10;
		
		serverConnection.execGameLogic(p1, p2, total);
	
		assertEquals(1, total.p1Points, "Player 1 Did not get a Point");
	}
	
	@Test 
	void testPlayer2Win()
	{
		p1.p1Plays = 2;
		p2.p1Plays = 4;
		
		p1.p1Guess = 8;
		p2.p1Guess = 6;
		
		serverConnection.execGameLogic(p1, p2, total);
		
		assertEquals(1, total.p2Points, "Player 2 Did not get a Point");
	}
	
	@Test
	void testWrongGuess()
	{
		p1.p1Plays = 2;
		p2.p1Plays = 4;
		
		p1.p1Guess = 8;
		p2.p1Guess = 10;
		
		serverConnection.execGameLogic(p1, p2, total);
		
		int value = total.p1Points + total.p2Points;
		
		assertEquals(0, value, "One of the players guessed right");
	}
	
	@Test 
	void testBothRightGuess()
	{
		p1.p1Plays = 2;
		p2.p1Plays = 4;
		
		p1.p1Guess = 6;
		p2.p1Guess = 6;
		
		serverConnection.execGameLogic(p1, p2, total);
		
		int value = total.p1Points + total.p2Points;
		
		assertEquals(0, value, "One of the players guessed something different");
	}
	
	
}
	

