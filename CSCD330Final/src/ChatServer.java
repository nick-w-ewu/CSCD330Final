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
		Client[] gameRoom1 = new Client[2];
		
		try
		{
			ServerSocket server = new ServerSocket(port);
			PrintWriter clientOutput;
			BufferedReader clientRead;
			Socket client;
			ServerThread sender = new ServerThread(gameRoom1);
			int location;

			while(true)
			{
				try
				{
					client = server.accept();
					clientOutput = new PrintWriter(client.getOutputStream(), true);
					clientOutput.println("Please enter your name: ");
					clientRead = new BufferedReader
							(new InputStreamReader(client.getInputStream()));
					String name = clientRead.readLine();
					location = findSlot(gameRoom1);
					if(location != -1)
					{
						addClient(gameRoom1, client, sender, location, name);
						if(sender.player1 != -1 && sender.player2 != -1)
						{
							sender.start();
						}
					}
					else
					{
						clientOutput.println("Server Full");
						client.close();
					}

				}
				catch(Exception e)
				{
					e.printStackTrace(System.out);
					System.out.println("Something went wrong");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Server failure");

		}
	}

	private synchronized static int findSlot(Client[] clients)
	{
		for(int i = 0; i < clients.length; i++)
		{
			if(clients[i] == null || clients[i].checkConnected() == false)
			{
				return i;
			}
		}
		return -1;
	}

	private synchronized static boolean addClient(Client[] clients, Socket client, ServerThread sender, int i, String name)
	{
		clients[i] = new Client(client, name);
		if(sender.player1 == -1)
		{
			sender.player1 = i;
		}
		else if(sender.player2 == -1)
		{
			sender.player2 = i;
		}
		if(sender.player1 != -1 && sender.player2 != -1)
		{
			sender.start();
			return true;
		}
		return false;
	}

}