import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer
{

	public static void main(String[] args)
	{
		int port = 1238;
		Socket client;
		String name, gameType;
		Client player1 = null;
		Client player2 = null;
		Client2 player01 = null;
		Client2 player02 = null;
		
		try
		{
			ServerSocket server = new ServerSocket(port);
			PrintWriter send;
			BufferedReader recive;

			while(true)
			{
				try
				{
					client = server.accept();
					send = new PrintWriter(client.getOutputStream(), true);
					recive = new BufferedReader
							(new InputStreamReader(client.getInputStream()));
					send.println("Please enter a player name:");
					name = recive.readLine();
					gameType = getGameType(send, recive);
					if(gameType.equalsIgnoreCase("Rock-Paper-Scissors"))
					{
						gameRoom1(player1, player2, name, send, client);
					}
					else if(gameType.equalsIgnoreCase("Max Number"))
					{
						gameRoom2(player01, player02, name, send, client);
					}

				}
				catch(Exception e)
				{
					System.out.println("Something went wrong");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Server failure");

		}
	}

	private static String getGameType(PrintWriter send, BufferedReader recive)
	{
		
		try
		{
			send.println("Please choose a game type, the options are Rock-Paper-Scissors and Max Number");
			String gameType = recive.readLine();
			while(!gameType.equalsIgnoreCase("Rock-Paper-Scissors") || !gameType.equalsIgnoreCase("Max Number"))
			{
				send.println("Please choose a game type, the options are Rock-Paper-Scissors and Max Number");
				gameType = recive.readLine();
			}
			return gameType;
		} catch (IOException e)
		{
			return null;
		}
	}
	
	private static void gameRoom1(Client player1, Client player2, String name, PrintWriter send, Socket client) throws IOException
	{
		if(player1 == null || player1.checkConnected() == false)
		{
			player1 = new Client(client, name);
			if(player2 != null && player1.checkConnected() && player2.checkConnected())
			{
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				player1.start();
				player2.start();
			}
			else
			{
				send.println("waiting for an opponent");
			}
		}
		else if(player2 == null || player1.checkConnected() == false)
		{
			player2 = new Client(client, name);
			if(player1 !=null && player1.checkConnected() && player2.checkConnected())
			{
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				player1.start();
				player2.start();
			}
			else
			{
				send.println("waiting for an opponent");
			}
		}
		else if(player1 != null && player2 != null && !player1.getInGame() && !player2.getInGame())
		{
			if(player1.checkConnected() && player2.checkConnected())
			{
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				player1.start();
				player2.start();
			}
		}
		else
		{
			send.println("Server Full");
			client.close();
		}
	}
	
	private static void gameRoom2(Client2 player1, Client2 player2, String name, PrintWriter send, Socket client) throws IOException
	{
		if(player1 == null || player1.checkConnected() == false)
		{
			player1 = new Client2(client, name);
			if(player2 != null && player1.checkConnected() && player2.checkConnected())
			{
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				player1.start();
				player2.start();
			}
			else
			{
				send.println("waiting for an opponent");
			}
		}
		else if(player2 == null || player1.checkConnected() == false)
		{
			player2 = new Client2(client, name);
			if(player1 !=null && player1.checkConnected() && player2.checkConnected())
			{
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				player1.start();
				player2.start();
			}
			else
			{
				send.println("waiting for an opponent");
			}
		}
		else if(player1 != null && player2 != null && !player1.getInGame() && !player2.getInGame())
		{
			if(player1.checkConnected() && player2.checkConnected())
			{
				player1.setOpponent(player2);
				player2.setOpponent(player1);
				player1.start();
				player2.start();
			}
		}
		else
		{
			send.println("Server Full");
			client.close();
		}
	}

}