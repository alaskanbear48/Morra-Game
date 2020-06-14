import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {
	
	//Utilities 
	TheServer server;
	ClientThread playerOne;
	ClientThread playerTwo;
	MorraInfo infoP1;
	MorraInfo infoP2;
	int numPlayers;
	
	int portNum;
	private Consumer<Serializable> callback;
	
	//Default Constructors 
	Server(Consumer<Serializable> call)
	{
		this.callback = call;
		portNum = 5555;
		this.server = new TheServer();
		this.server.start();
		playerOne = null;
		playerTwo = null;
		infoP1 = null;
		infoP2 = null;
		numPlayers = 0;
	}
	
	Server(Consumer<Serializable> call, int port)
	{
		this.callback = call;
		this.portNum = port;
		this.server = new TheServer();
		this.server.start();
		playerOne = null;
		playerTwo = null;
		numPlayers = 0;
	}
	
	//Used for Testing game Logic// Note: It is the same game logic code used in Client Thread
	//This was made for simplicity 
	void execGameLogic(MorraInfo one, MorraInfo two, MorraInfo newOne)
	{
		newOne.p1Plays = one.p1Plays;
		newOne.p2Plays = two.p1Plays;
		newOne.p1Guess = one.p1Guess;
		newOne.p2Guess = two.p1Guess;
		newOne.p1Points = one.p1Points;
		newOne.p2Points = two.p2Points;
		newOne.numPlayers = 2;
		
		int totalFin = newOne.p1Plays + newOne.p2Plays;
		
		//tie
		if(newOne.p1Guess == totalFin && newOne.p2Guess == totalFin) {
			newOne.serMess = "You both guessed correctly";
		}
		//player1 wins
		else if(newOne.p1Guess == totalFin) {
			newOne.p1Points = newOne.p1Points + 1;
			newOne.serMess = "Player One won";
		}
		//player2 wins
		else if(newOne.p2Guess == totalFin) {
			newOne.p2Points = newOne.p2Points + 1;
			newOne.serMess = "Player Two won";
			
		}
		//no one wins
		else {
			newOne.serMess = "No one guessed correctly";
		}
	}
	
	//Creates a Server Thread 
	public class TheServer extends Thread
	{
		public void run()
		{
			try(ServerSocket mysocket = new ServerSocket(portNum);)
			{
				//Looking for clients to connect 
				while(true)
				{
					int tempNum = 0;
					if(playerOne == null)
					{
						tempNum = 1;
					}
					else if(playerTwo == null) {
						tempNum = 2;
					}
					
					ClientThread c = new ClientThread(mysocket.accept(), tempNum);
					if(playerOne == null)
					{
						c.count = 1;
						System.out.println("open:p1");
						MorraInfo temp = new MorraInfo();
						temp.serMess = "new client " + c.count;
						numPlayers++;
						temp.numPlayers = numPlayers;
						callback.accept(temp);
						playerOne = c;
						c.start();
						System.out.println("recieved p1");	
					}
					else if(playerTwo == null) {
						c.count = 2;
						System.out.println("open:p2");
						MorraInfo temp = new MorraInfo();
						temp.serMess = "new client " + c.count;
						numPlayers++;
						temp.numPlayers = numPlayers;
						callback.accept(temp);
						playerTwo = c;
						c.start();
						System.out.println("recieved p2");
					}
				}
			}
			catch(Exception e) 
			{
				MorraInfo temp = new MorraInfo();
				temp.serMess = "Server socket didn't laucnh ";
				callback.accept(temp);
				
			}
		}
	}
	
	//Create a Client Thread
	public class ClientThread extends Thread
	{
		Socket connection;
		int count;
		ObjectInputStream in;
		ObjectOutputStream out;
		
		ClientThread(Socket s, int count)
		{
			this.connection = s;
			this.count = count;
		}
		
		//Updates Clients with a MorraInfo object
		public void updateClients(MorraInfo message)
		{
			if(playerOne != null) {
				try
				{
					message.curPlayer = 1;
					playerOne.out.writeObject(message);
				}
				catch(Exception e) {}
			}
			if(playerTwo != null) {
				try
				{
					message.curPlayer = 2;
					playerTwo.out.writeObject(message);
				}
				catch(Exception e) {}
			}
		}
		
		//Called when start() is called 
		public void run()
		{
			try
			{
				in = new ObjectInputStream(connection.getInputStream());
				out = new ObjectOutputStream(connection.getOutputStream());
				connection.setTcpNoDelay(true);
			}
			catch(Exception e)
			{
				System.out.println("Streams not open");
			}
			
			
			MorraInfo newClient = new MorraInfo();
			newClient.numPlayers = numPlayers;
			newClient.serMess = "new client on server: client #"+count;
			updateClients(newClient);
			
			
			//while loop to read in from clients and update others
			while(true)
			{
				MorraInfo resultsInfo = new MorraInfo();
				try 
				{
					if(count == 1)
					{
						infoP1 = (MorraInfo) in.readObject();
						System.out.println("playAGain: " + infoP1.playAgain);
						if(infoP1.playAgain == 1)
						{
							infoP1.p1Plays = -1;
							infoP1.playAgain = 0;
							infoP1.serMess = "Player 1 wants to play again";
							callback.accept(infoP1);
							updateClients(infoP1);
							infoP1 = null;
							System.out.println("play again p1");
						}
						else if(infoP1.playAgain == -1)
						{
							infoP1.p1Plays = -1;
							infoP1.playAgain = 0;
							infoP1.serMess = "Player 1 quit";
							callback.accept(infoP1);
							infoP1 = null;
							System.out.println("quit p1");
						}
						
					}
					else if(count == 2) 
					{
						infoP2 = (MorraInfo) in.readObject();
						System.out.println("playAGain: " + infoP2.playAgain);
						if(infoP2.playAgain == 1)
						{
							infoP2.p1Plays = -1;
							infoP2.playAgain = 0;
							infoP2.serMess = "Player 2 wants to play again";
							callback.accept(infoP2);
							updateClients(infoP2);
							infoP2 = null;
							System.out.println("play again p2");
						}
						else if(infoP2.playAgain == -1)
						{
							infoP2.p1Plays = -1;
							infoP2.playAgain = 0;
							infoP2.serMess = "Player 2 quit";
							callback.accept(infoP2);
							infoP2 = null;
							System.out.println("play again p2");
						}
						
					}
					
					//if both already sent it
					//Game Logic to determine who wins the round or ties 
					if(infoP1 != null && infoP2 != null)
					{
						
						resultsInfo.p1Plays = infoP1.p1Plays;
						resultsInfo.p2Plays = infoP2.p1Plays;
						resultsInfo.p1Guess = infoP1.p1Guess;
						resultsInfo.p2Guess = infoP2.p1Guess;
						resultsInfo.p1Points = infoP1.p1Points;
						resultsInfo.p2Points = infoP2.p2Points;
						resultsInfo.numPlayers = numPlayers;
						
						int totalFin = resultsInfo.p1Plays + resultsInfo.p2Plays;
						
						//tie
						if(resultsInfo.p1Guess == totalFin && resultsInfo.p2Guess == totalFin) {
							resultsInfo.serMess = "You both guessed correctly";
						}
						//player1 wins
						else if(resultsInfo.p1Guess == totalFin) {
							
							resultsInfo.p1Points = resultsInfo.p1Points + 1;
							
							resultsInfo.serMess = "Player One won";
							
						}
						//player2 wins
						else if(resultsInfo.p2Guess == totalFin) {
							
							resultsInfo.p2Points = resultsInfo.p2Points + 1;
							
							resultsInfo.serMess = "Player Two won";
						
						}
						//no one wins
						else {
							resultsInfo.serMess = "No one guessed correctly";
						}
						
						callback.accept(resultsInfo);
						
						updateClients(resultsInfo);
						infoP1 = null;
						infoP2 = null;
					}
				}
				//If someone Disconnects from the game
				catch(Exception e) 
				{
					
					MorraInfo connLoss = new MorraInfo();
					connLoss.serMess = "Client #"+count+" has left the server!";
					numPlayers--;
					connLoss.numPlayers = numPlayers;
					callback.accept(connLoss);
					updateClients(connLoss);
					if(count == 1) {
						playerOne = null;
						System.out.println("disconnect p1");
					}
					else if(count == 2) {
						playerTwo = null;
						System.out.println("disconnect p2");
					}
					break;
				}
			}
			
		}
	}
	
	
	
	
}
