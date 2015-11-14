import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ServerThread extends Thread
{
	private Socket client;
	private int index;
	private Client[] clients;

	public ServerThread(Socket client, int ix, Client[] clients)
	{
		this.client = client;
		this.index = ix;
		this.clients = clients;
	}



	public void run()
	{
		try
		{
			BufferedReader recive = new BufferedReader
					(new InputStreamReader(this.client.getInputStream()));
			PrintWriter send;
			String message = recive.readLine();
			while(message != null && !message.equals("Bye"))
			{
				writeToClients(message);
				message = recive.readLine();
			}
			writeToClients("Bye");
			removeMe();
		} 
		catch (Exception e)
		{
			removeMe();
		} 

	}

	private synchronized void writeToClients(String message)
	{
		try
		{
			PrintWriter send;
			for(int i = 0; i < this.clients.length; i++)
			{
				if(clients[i] != null && i != this.index && clients[i].checkConnected() == true)
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
	
	private synchronized void removeMe()
	{
		clients[this.index].setConnected(false);
	}
}
