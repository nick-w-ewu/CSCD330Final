import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer
{

	public static void main(String[] args)
	{
		int port = 1237;
		Socket client;
		String name;
		Client player1 = null;
		Client player2 = null;
		
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
					send = new PrintWriter(client.getOutputStream());
					recive = new BufferedReader
							(new InputStreamReader(client.getInputStream()));
					send.println("Please enter a player name:");
					name = recive.readLine();
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
					else
					{
						send.println("Server Full");
						client.close();
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

}