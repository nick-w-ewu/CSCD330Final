package good;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;


public class Client extends Thread
{
	private Socket socket;
	private boolean connected;
	private String playerId;
	private String move;
	private Client opponent;
	private boolean inGame;
	private boolean isStored;
	private boolean restart;
	private String validMoves = "([R,P,S,r,p,s])";
	private ArrayList<String> iWin;
	private ArrayList<String> opponentWins;
	PrintWriter send;
	BufferedReader recive;
	
	public Client(Socket socket, String name)
	{
		this.socket = socket;
		this.connected = true;
		this.isStored = false;
		this.playerId = name;
		this.restart = false;
		
		try
		{
			this.send = new PrintWriter(this.socket.getOutputStream(), true);
		}
		catch (IOException e)
		{
			
		}
		
		this.iWin = new ArrayList<String>();
		this.iWin.add("P R");
		this.iWin.add("R S");
		this.iWin.add("S P");
		
		this.opponentWins = new ArrayList<String>();
		this.opponentWins.add("R P");
		this.opponentWins.add("S R");
		this.opponentWins.add("P S");
	}
	
	public Socket getSocket()
	{
		return this.socket;
	}
	
	public synchronized void setInGame(boolean b)
	{
		this.inGame = b;
	}
	
	public synchronized boolean getInGame()
	{
		return this.inGame;
	}
	
	public synchronized void setConnected(boolean set)
	{
		this.connected = set;
	}
	
	public synchronized void setOpponent(Client o)
	{
		this.opponent = o;
	}
	
	public synchronized boolean checkConnected()
	{
		return this.connected;
	}
	
	public synchronized boolean checkRestartRequest()
	{
		return this.restart;
	}
	
	public synchronized String getMove()
	{
		return this.move;
	}
	
	public synchronized boolean isMoveStored()
	{
		return this.isStored;
	}
	
	public synchronized void moveStored()
	{
		this.isStored = true;
	}
	
	public void printMessage(String message)
	{
		send.println(message);
	}
	
	public String getPlayerId()
	{
		return this.playerId;
	}
	
	public synchronized void opponentErrored()
	{
		this.inGame = false;
		this.opponent = null;
		printMessage("Your opponent experienced an error and disconnected, your game will be restarted with a new opponent shortly.");
		printMessage("You will need to press enter and then your move when you are reconnected");
		this.restart = true;
	}
	
	public void run()
	{
		try
		{
			this.inGame = true;
			this.recive = new BufferedReader
					(new InputStreamReader(this.socket.getInputStream()));
			send.println("Please enter a move, R,P, or S:");
			this.move = recive.readLine();
			while(!this.move.matches(validMoves))
			{
				if(this.move.length() >=2 && this.move.substring(0, 2).equals("/c"))
				{
					this.opponent.printMessage(move.substring(3));
				}
				send.println("Please enter a move, R,P, or S:");
				this.move = recive.readLine();
			}
			
			moveStored();
			String myMove = getMove();
			send.println("Waiting for opponent to enter move");
			
			while(!opponent.isMoveStored() && opponent.getInGame())
			{
				
			}
			if(!opponent.getInGame())
			{
				throw new Exception();
			}
			String opponentMove = this.opponent.getMove();
			String combinedMoves = myMove + " " + opponentMove;
			if(this.iWin.contains(combinedMoves))
			{
				send.println("Player: " + this.playerId + " wins this match");
			}
			else if(opponentWins.contains(combinedMoves))
			{
				send.println("Player: " + opponent.getPlayerId() + " wins this match");
			}
			else
			{
				send.println("Match was a draw");
			}
			
			send.println("Would you like to play again, press enter and then your choice? y or n");
			String playAgain = recive.readLine();
			while(!playAgain.equals("y") && !playAgain.equals("n") )
			{
				playAgain = recive.readLine();
			}
			if(playAgain.equals("y"))
			{
				send.println("Preparing to play agin");
				setConnected(true);
				setInGame(false);
				this.restart = true;
			}
			else
			{
				send.println("exiting...");
				setConnected(false);
				setInGame(false);
				this.socket.close();
			}
			return;
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			this.connected = false;
			this.inGame = false;
			if(opponent != null)
			{
				this.opponent.opponentErrored();
			}
		} 
		return;
	}

}
