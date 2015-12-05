package good;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client2 extends Thread
{
	private Socket socket;
	private boolean connected;
	private String playerId;
	private int move;
	private Client2 opponent;
	private boolean inGame;
	private boolean isStored;
	private boolean restart;
	private boolean restarted;
	PrintWriter send;
	
	public Client2(Socket socket, String name)
	{
		this.socket = socket;
		this.connected = true;
		this.isStored = false;
		this.playerId = name;
		try
		{
			this.send = new PrintWriter(this.socket.getOutputStream(), true);
		}
		catch (IOException e)
		{
			
		}
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
	
	public void setRestarted(boolean b)
	{
		this.restarted = b;
	}
	
	public synchronized void setConnected(boolean set)
	{
		this.connected = set;
	}
	
	public synchronized void setOpponent(Client2 o)
	{
		this.opponent = o;
	}
	
	public synchronized void opponentErrored()
	{
		this.inGame = false;
		this.opponent = null;
		printMessage("Your opponent experienced an error and disconnected, your game will be restarted with a new opponent shortly.");
		printMessage("You will need to press enter and then your number when you are reconnected");
		this.restart = true;
	}
	
	public synchronized boolean checkConnected()
	{
		return this.connected;
	}
	
	public synchronized boolean checkRestartRequest()
	{
		return this.restart;
	}
	
	public synchronized int getMove()
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
	
	public void run()
	{
		try
		{
			this.inGame = true;
			BufferedReader recive = new BufferedReader
					(new InputStreamReader(this.socket.getInputStream()));
			this.move = getInt(recive);
			moveStored();
			int myMove = getMove();
			send.println("Waiting for opponent to enter move");
			
			while(!opponent.isMoveStored() && opponent.getInGame())
			{
				
			}
			if(!opponent.getInGame())
			{
				throw new Exception();
			}
			int opponentMove = this.opponent.getMove();
			
			if(myMove > opponentMove)
			{
				send.println("Player: " + this.playerId + " wins this match");
			}
			else if(myMove < opponentMove)
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
		} 
		catch (Exception e)
		{
			this.connected = false;
			this.inGame = false;
			if(opponent != null)
			{
				this.opponent.opponentErrored();
			}
		} 

	}

	private int getInt(BufferedReader read) throws IOException
	{
		int ui;
		String temp;
		while (true)
		{
			try
			{
				send.println("Please enter a number");
				temp = read.readLine();
				if(temp.length() >= 2 && temp.substring(0, 2).equals("/c"))
				{
					this.opponent.printMessage(temp.substring(3));
				}
				else
				{
					ui = Integer.parseInt(temp);
					return ui;
				}
			}
			catch (NumberFormatException e )
			{
				send.println("There was an error with the input please try again.");
			}
		}
	}
}
