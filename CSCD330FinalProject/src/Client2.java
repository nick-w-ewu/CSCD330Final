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
	
	public synchronized void setOpponent(Client2 o)
	{
		this.opponent = o;
	}
	
	public synchronized void opponentErrored()
	{
		this.inGame = false;
		this.opponent = null;
	}
	
	public synchronized boolean checkConnected()
	{
		return this.connected;
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
			send.println("Please enter a move, R,P, or S:");
			this.move = getInt(recive);
			moveStored();
			int myMove = getMove();
			send.println("Waiting for opponent to enter move");
			
			while(!opponent.isMoveStored())
			{
				
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
			
			send.println("Would you like to play again? Y or N");
			String playAgain = recive.readLine();
			while(!playAgain.equalsIgnoreCase("t") && !playAgain.equalsIgnoreCase("f") )
			{
				playAgain = recive.readLine();
			}
			if(playAgain.equalsIgnoreCase("t"))
			{
				setConnected(true);
				setInGame(false);
			}
			else
			{
				setConnected(false);
				setInGame(false);
			}
		} 
		catch (Exception e)
		{
			this.connected = false;
			this.inGame = false;
			this.opponent.opponentErrored();
		} 

	}

	private int getInt(BufferedReader read)
	{
		int ui;
		String temp;
		while (true)
		{
			try
			{
				System.out.println("Please enter a number");
				temp = read.readLine();
				if(temp.substring(0, 2).equals("/c"))
				{
					this.opponent.printMessage(temp);
				}
				else
				{
					ui = Integer.parseInt(temp);
					return ui;
				}
			}
			catch (Exception e)
			{
				System.out.println("There was an error with the input please try again.");
			}
		}
	}
}
