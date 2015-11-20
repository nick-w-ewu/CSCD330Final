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
	private String name;
	private String move;
	private Client opponent;
	private boolean inGame;
	private boolean isStored;
	private String validMoves = "([R,P,S,r,p,s])";
	private ArrayList<String> iWin;
	private ArrayList<String> opponentWins;
	PrintWriter send;
	
	public Client(Socket socket, String name)
	{
		this.socket = socket;
		this.connected = true;
		this.isStored = false;
		try
		{
			this.send = new PrintWriter(this.socket.getOutputStream());
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
	
	public synchronized void setConnected(boolean set)
	{
		this.connected = set;
	}
	
	public synchronized void setOpponent(Client o)
	{
		this.opponent = o;
	}
	
	public boolean checkConnected()
	{
		return this.connected;
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
	
	public void run()
	{
		try
		{
			this.inGame = true;
			BufferedReader recive = new BufferedReader
					(new InputStreamReader(this.socket.getInputStream()));
			send.println("Please enter a move, R,P, or S:");
			this.move = recive.readLine();
			while(!this.move.matches(validMoves))
			{
				if(this.move.substring(0, 2).equals("/c"))
				{
					this.opponent.printMessage(move);
				}
				send.println("Please enter a move, R,P, or S:");
				this.move = recive.readLine();
			}
			
			moveStored();
			String myMove = getMove();
			
			while(!opponent.isMoveStored())
			{
				send.println("Waiting for opponent to enter move");
			}
			String opponentMove = this.opponent.getMove();
			String combinedMoves = myMove + " " + opponentMove;
			if(this.iWin.contains(combinedMoves))
			{
				send.println("Player: " + this.name + " wins this match");
			}
			else if(opponentWins.contains(combinedMoves))
			{
				send.println("Player: " + opponent.getName() + " wins this match");
			}
			else
			{
				send.println("Match was a draw");
			}
		} 
		catch (Exception e)
		{
			
		} 

	}

}
