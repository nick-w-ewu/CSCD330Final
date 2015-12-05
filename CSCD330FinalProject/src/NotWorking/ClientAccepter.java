package NotWorking;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;

public class ClientAccepter extends Thread
{
	Queue<TempClient> clients;

	public ClientAccepter(Queue<TempClient> clients)
	{
		this.clients = clients;
	}
	
	public void run()
	{
		int port = 1239;
		Socket client;
		TempClient temp;
		String name, gameType;
		
		try
		{
			ServerSocket server = new ServerSocket(port);
			PrintWriter send;
			BufferedReader recive;
			while(true)
			{
				client = server.accept();
				send = new PrintWriter(client.getOutputStream(), true);
				recive = new BufferedReader
						(new InputStreamReader(client.getInputStream()));
				send.println("Please enter a player name:");
				name = recive.readLine();
				gameType = getGameType(send, recive);
				temp = new TempClient(client, name, gameType);
				enqueue(temp);
			}
		}
		catch(Exception e)
		{
			System.out.println("Server failure");
		}
	}
	
	private String getGameType(PrintWriter send, BufferedReader recive)
	{

		try
		{
			send.println("Please choose a game type, the options are Rock-Paper-Scissors (rps) and Max Number");
			String gameType = recive.readLine();
			while(!gameType.equalsIgnoreCase("Rock-Paper-Scissors") && !gameType.equalsIgnoreCase("Max Number") && !gameType.equalsIgnoreCase("rps"))
			{
				send.println("Please choose a game type, the options are Rock-Paper-Scissors and Max Number");
				gameType = recive.readLine();
			}
			return gameType;
		}
		catch (IOException e)
		{
			return null;
		}
	}
	
	private synchronized void enqueue(TempClient t)
	{
		this.clients.add(t);
	}
	
	public synchronized TempClient dedueue()
	{
		return this.clients.peek();
	}
}
