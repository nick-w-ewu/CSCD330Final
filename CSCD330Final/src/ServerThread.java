import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerThread extends Thread
{
	public int player1;
	public int player2;
	private Client[] clients;
	private ArrayList<String> correctInput;

	public ServerThread(Client[] clients)
	{
		this.player1 = -1;
		this.player2 = -1;
		this.clients = clients;
		
		this.correctInput = new ArrayList<String>();
		correctInput.add("R");
		correctInput.add("P");
		correctInput.add("S");
	}
	

	public void run()
	{
		try
		{
			BufferedReader recive1 = new BufferedReader
					(new InputStreamReader(this.clients[player1].socket.getInputStream()));
			BufferedReader recive2 = new BufferedReader
					(new InputStreamReader(this.clients[player2].socket.getInputStream()));
			PrintWriter send1 = new PrintWriter(clients[player1].socket.getOutputStream(), true);
			PrintWriter send2 = new PrintWriter(clients[player2].socket.getOutputStream(), true);
			String p1Move = "";
			String p2Move = "";
			while(!this.correctInput.contains(p1Move) && !this.correctInput.contains(p2Move))
			{
				if(!this.correctInput.contains(p1Move))
				{
					send1.println("Please enter a move, R, P, or S");
					p1Move = recive1.readLine();
				}
				if(!this.correctInput.contains(p2Move))
				{
					send2.println("Please enter a move, R, P, or S");
					p2Move = recive2.readLine();
				}
			}
			writeToClients("Bye");
			//removeMe();
		} 
		catch (Exception e)
		{
			//removeMe();
		} 

	}

	private synchronized void writeToClients(String message)
	{
		try
		{
			PrintWriter send;
			for(int i = 0; i < this.clients.length; i++)
			{
				if(clients[i] != null && clients[i].checkConnected() == true)
				{
					send = new PrintWriter(clients[i].socket.getOutputStream(), true);
					send.println(message);
				}
			}
		}
		catch(Exception e)
		{

		}
	}
	
//	private synchronized void removeMe()
//	{
//		clients[this.index].setConnected(false);
//	}
}
