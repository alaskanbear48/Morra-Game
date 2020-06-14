
import java.util.HashMap;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TheGameOfMorra extends Application {

	//--Utilities--//
	ListView<String> serverMessagesGuess;
	ListView<String> serverMessages;
	HashMap<String, Scene> sceneMap;
	Client clientConnection;
	MorraInfo turnInfo;
	String clientNum;
	int portNum;
	String IPAddress;
	
	//--------------------------
	TextField userGuess;
	Button enterGuess;
	
	Label lab48;
	Label lab49;
	Label lab50;
	Label lab51;
	Label lab52;
	
	Label lab2;
	Label lab3;
	Label lab5;
	Label lab6;
	
	Label label2;
	Label label3;
	ImageView pic1;
    //-------------------------
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		//Scene Labels 
		//----------------------------------------------
		lab2 = new Label("Opponents: -.-");
		lab3 = new Label("Yours: -.-");
		lab5 = new Label("Played: ");
		lab6 = new Label("Guessed: -.-");

		lab48 = new Label("Your Game Info");
		lab48.setStyle("-fx-font-weight: bold");
		lab49 = new Label("Played: -.-");
		lab50 = new Label("Guessed: -.-");
		lab51 = new Label("Number of Players: -.-");
		lab52 = new Label("Current Player: -.-");
		
	    userGuess = new TextField();
		enterGuess = new Button("Enter");
		enterGuess.setDisable(true);
		
		label2 = new Label("Opponent: ");
		label3 = new Label("Your: ");
		
		//Store Scenes in Hash Map 
		primaryStage.setTitle("(Client) Let's Play Morra!!!");
		sceneMap = new HashMap<String, Scene>();
		turnInfo = new MorraInfo();
		sceneMap.put("start",  createStart(primaryStage));
		sceneMap.put("guess",  createGuess(primaryStage));
		sceneMap.put("results",  createResults(primaryStage));
		
		primaryStage.setScene(sceneMap.get("start"));
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
            	
            	try 
            	{
            	clientConnection.disconnect();
            	}
            	catch(Exception e){}
            	
                Platform.exit();
                System.exit(0);
            }
        });
	}
	
	//Create a Start Scene for the client 
	public Scene createStart(Stage primaryStage) {
		
		Label labIP = new Label("IP Address");
		Label labPort = new Label("Port Number");
		
		TextField enterIP = new TextField();
		TextField enterPort = new TextField();
		
		Button btnConnect = new Button("Connect");
		serverMessages = new ListView<String>();
		
		VBox col1 = new VBox(labIP, enterIP, labPort, enterPort);
		VBox col2 = new VBox(btnConnect, serverMessages);
		HBox clientBox = new HBox(col1, col2);
		clientBox.setStyle("-fx-background-color: orange");
		
		//Event Handlers 
		btnConnect.setOnAction(e->{
			primaryStage.setScene(sceneMap.get("guess"));
			
			IPAddress =enterIP.getText();
			try {
			portNum = Integer.parseInt(enterPort.getText());
			}
			catch(Exception a){}
			
			primaryStage.show();
			
			clientConnection = new Client(data->
			{
				Platform.runLater(()->
				{
							turnInfo = (MorraInfo) data;
							
							serverMessagesGuess.getItems().add("servMess: " + turnInfo.serMess);
							
							lab51.setText("Number of Players: " + turnInfo.numPlayers);
							lab52.setText("Current Player: " + turnInfo.curPlayer);
							
							if(turnInfo.curPlayer == 1)
							{
								lab2.setText("Opponents: " + turnInfo.p2Points);
								lab3.setText("Yours: " + turnInfo.p1Points);
							
								if(turnInfo.p2Plays >= 0 && turnInfo.p2Plays <= 5)
								{
									pic1.setImage(new Image(turnInfo.p2Plays + ".jpg"));
									setImageSize(pic1);
								}
								else {
									pic1.setImage(null);
								}
								
								
								if(turnInfo.p2Guess == -1) {
									lab6.setText("Guessed: -.-");
								}
								else {
									lab6.setText("Guessed: " + turnInfo.p2Guess);
								}
								
							
							}
							else if(turnInfo.curPlayer == 2)
							{
								lab2.setText("Opponents: " + turnInfo.p1Points);
								lab3.setText("Yours: " + turnInfo.p2Points);
								
								if(turnInfo.p1Plays >= 0 && turnInfo.p1Plays <= 5)
								{
									pic1.setImage(new Image(turnInfo.p1Plays + ".jpg"));
									setImageSize(pic1);
								}
								else {
									pic1.setImage(null);
								}
								
								if(turnInfo.p1Guess == -1) {
									lab6.setText("Guessed: -.-");
								}
								else {
									lab6.setText("Guessed: " + turnInfo.p1Guess);
								}
								
							}
							
							if(turnInfo.p1Points == 2 || turnInfo.p2Points == 2)
							{
								int tot = turnInfo.p1Plays + turnInfo.p2Plays;
								String mess;
								if(turnInfo.curPlayer == 1)
								{
									if(turnInfo.p1Points == 2) {
										mess = "You won";
									}
									else {
										mess = "You lost";
									}
									label2.setText("Opponent: " + turnInfo.p2Points);
									label3.setText("Your: " + turnInfo.p1Points + "\n\nFinal Round\n" +
											       "You Guessed: " + turnInfo.p1Guess + 
											       "\nOpponent Guessed: " + turnInfo.p2Guess +
											       "\nTotal Was " + tot + 
											       "\n" + mess);
								}
								else if(turnInfo.curPlayer == 2)
								{
									if(turnInfo.p2Points == 2) {
										mess = "You won";
									}
									else {
										mess = "You lost";
									}
									label2.setText("Opponent: " + turnInfo.p1Points);
									label3.setText("Your: " + turnInfo.p2Points + "\n\nFinal Round\n" +
										       "You Guessed: " + turnInfo.p2Guess + 
										       "\nOpponent Guessed: " + turnInfo.p1Guess +
										       "\nTotal Was: " + tot + 
										       "\n" + mess);
								}
								primaryStage.setScene(sceneMap.get("results"));
								primaryStage.show();
								
							}		
				});
			}, IPAddress, portNum);
			clientConnection.start();
			
		});
		
		return new Scene(clientBox, 600, 500);
	}
	
	public void setImageSize(ImageView cur)
	{
		cur.setFitHeight(60);
		cur.setFitWidth(60);
		cur.setPreserveRatio(true);
	}
	
	//Create the primary Scene were the client guesses and makes his move 
	public Scene createGuess(Stage primaryStage) {
		
		//---------------------------------------------------------
		Label enterGuessInfo = new Label("Make a Guess:");
		HBox userGuessSubmit = new HBox(5, userGuess, enterGuess);
		VBox guessTotal = new VBox(enterGuessInfo, userGuessSubmit);
		VBox userInfo= new VBox(lab48, lab49, lab50, lab51, lab52);		
		//----------------------------------------------------------
		
		Label lab1 = new Label("Score");
		lab1.setStyle("-fx-font-weight: bold");
		VBox score = new VBox(lab1, lab2, lab3);
		
		Label lab4 = new Label("Opponent");
		lab4.setStyle("-fx-font-weight: bold");
		pic1 = new ImageView();
		HBox opPlayed = new HBox(lab5, pic1);
		ImageView pic2 = new ImageView();
		HBox opGuessed = new HBox(lab6, pic2);
		VBox opponent = new VBox(lab4, opPlayed, opGuessed);
		
		
		Label lab7 = new Label("Choose a number: ");
		
		ImageView fin0 = new ImageView(new Image("0.jpg"));
		ImageView fin1 = new ImageView(new Image("1.jpg"));
		ImageView fin2 = new ImageView(new Image("2.jpg"));
		ImageView fin3 = new ImageView(new Image("3.jpg"));
		ImageView fin4 = new ImageView(new Image("4.jpg"));
		ImageView fin5 = new ImageView(new Image("5.jpg"));
		
		setImageSize(fin0);
		setImageSize(fin1);
		setImageSize(fin2);
		setImageSize(fin3);
		setImageSize(fin4);
		setImageSize(fin5);
		
		HBox row1 = new HBox(5,fin0, fin1, fin2);
		HBox row2 = new HBox(5, fin3, fin4, fin5, guessTotal);
		VBox player = new VBox(5, lab7, row1, row2);
		
		serverMessagesGuess = new ListView<String>();
		HBox temp = new HBox(90, score, opponent, userInfo);
		VBox clientBox = new VBox(10, temp, player, serverMessagesGuess);
	    clientBox.setStyle("-fx-background-color: orange");
		
		//Pictures to press to make a play 
		fin0.setOnMousePressed(e->
		{
			
			turnInfo.p1Plays = 0;
			lab49.setText("Played: " + turnInfo.p1Plays);
				enterGuess.setDisable(false);

		});
		fin1.setOnMousePressed(e->
		{
				turnInfo.p1Plays = 1;
				lab49.setText("Played: " + turnInfo.p1Plays);
				enterGuess.setDisable(false);

		});
		fin2.setOnMousePressed(e->
		{
				turnInfo.p1Plays = 2;
				lab49.setText("Played: " + turnInfo.p1Plays);
				enterGuess.setDisable(false);

		});
		fin3.setOnMousePressed(e->
		{
				turnInfo.p1Plays = 3;
				lab49.setText("Played: " + turnInfo.p1Plays);
				enterGuess.setDisable(false);

		});
		fin4.setOnMousePressed(e->
		{
			
			    turnInfo.p1Plays = 4;
			    lab49.setText("Played: " + turnInfo.p1Plays);
				enterGuess.setDisable(false);

		
		});
		fin5.setOnMousePressed(e->
		{
		
				turnInfo.p1Plays = 5;
				lab49.setText("Played: " + turnInfo.p1Plays);
				enterGuess.setDisable(false);
		});
		
		//User Enters Guess 
		enterGuess.setOnAction(new EventHandler<ActionEvent> (){
			public void handle(ActionEvent action)
			{
				  try
				  {
			      turnInfo.p1Guess = Integer.parseInt(userGuess.getText());
				  }
				  catch(Exception b){}
				  
			      userGuess.clear();
			      
			      lab50.setText("Guessed: " + turnInfo.p1Guess);
			      enterGuess.setDisable(true);
			      clientConnection.send(turnInfo);
			      
			}
		});  
		
		return new Scene(clientBox, 600, 500);
	}
	
	//Create a result scene. This is used when someone wins the game
	public Scene createResults(Stage primaryStage) {
		
		Label label1 = new Label("Score");
		Button btnAgain = new Button("Play Again");
		Button btnQuit = new Button("Quit");
		serverMessages = new ListView<String>();
		
		
		VBox clientBox = new VBox(10, label1, label2, label3, btnAgain, btnQuit);
		clientBox.setStyle("-fx-background-color: orange");
		clientBox.setAlignment(Pos.CENTER);
		
		btnAgain.setOnAction(e->
		{
			lab2.setText("Opponents: -.-");
			lab3.setText("Yours: -.-");
			lab5.setText("Played: ");
			lab6.setText("Guessed: -.-");

			lab48.setText("Your Game Info");
			lab48.setStyle("-fx-font-weight: bold");
			lab49.setText("Played: -.-");
			lab50.setText("Guessed: -.-");
			lab51.setText("Number of Players: -.-");
			lab52.setText("Current Player: -.-");
			serverMessagesGuess.getItems().clear();
			
			if(turnInfo.numPlayers == 1) {
				serverMessagesGuess.getItems().add("Starting new game, waiting for opponent");
			}
			turnInfo = new MorraInfo();
			turnInfo.playAgain = 1;
			clientConnection.send(turnInfo);
			turnInfo.playAgain = 0;
			
			primaryStage.setScene(sceneMap.get("guess"));
			primaryStage.show();
			
		});
		btnQuit.setOnAction(e->
		{
			 turnInfo.playAgain = -1;
			 clientConnection.send(turnInfo);
			 clientConnection.disconnect();
			 Platform.exit();
             System.exit(0);
		});
		
		return new Scene(clientBox, 600, 500);
	}
}

