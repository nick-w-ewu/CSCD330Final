package good;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

public class ChatServer
{

	public static void main(String[] args)
	{
		int port = 1237;
		Socket client;
		String name, gameType;
		Client player1 = null;
		Client player2 = null;
		Client2 player01 = null;
		Client2 player02 = null;
		Queue<Client> room1WaitList = new LinkedList<Client>();
		Queue<Client2> room2WaitList = new LinkedList<Client2>();

		try
		{
			ServerSocket server = new ServerSocket(port);
			PrintWriter send;
			BufferedReader recive;

			while(true)
			{
				try
				{
					if(player1 != null && player1.checkConnected() && player1.checkRestartRequest())
					{
						player1.stop();
						String tempName = player1.getPlayerId();
						Socket tempSocket = player1.getSocket();
						player1 = new Client(tempSocket, tempName);
					}
					if(player2 != null && player2.checkConnected() && player2.checkRestartRequest())
					{
						String tempName = player2.getPlayerId();
						Socket tempSocket = player2.getSocket();
						player2 = new Client(tempSocket, tempName);
					}
					if(player01 != null && player01.checkConnected() && player01.checkRestartRequest())
					{
						String tempName = player01.getPlayerId();
						Socket tempSocket = player01.getSocket();
						player01 = new Client2(tempSocket, tempName);
					}
					if(player02 != null && player02.checkConnected() && player02.checkRestartRequest())
					{
						String tempName = player02.getPlayerId();
						Socket tempSocket = player02.getSocket();
						player02 = new Client2(tempSocket, tempName);
					}
					if(player1 != null && player2 != null && !player1.getInGame() && !player2.getInGame())
					{
						if(player1.checkConnected() && player2.checkConnected())
						{
							player1.setOpponent(player2);
							player2.setOpponent(player1);
							player1.start();
							player2.start();
						}
					}
					if(player01 != null && player02 != null && !player01.getInGame() && !player02.getInGame())
					{
						if(player01.checkConnected() && player02.checkConnected())
						{
							player01.setOpponent(player02);
							player02.setOpponent(player01);
							player01.start();
							player02.start();
						}
					}
					client = server.accept();
					send = new PrintWriter(client.getOutputStream(), true);
					recive = new BufferedReader
							(new InputStreamReader(client.getInputStream()));
					send.println("Please enter a player name:");
					name = recive.readLine();
					gameType = getGameType(send, recive);
					if(gameType.equalsIgnoreCase("Rock-Paper-Scissors") || gameType.equalsIgnoreCase("rps"))
					{
						if(player1 == null || player1.checkConnected() == false)
						{
							if(room1WaitList.peek() != null)
							{
								player1 = room1WaitList.poll();
								if(player2 == null || player2.checkConnected() == false)
								{
									if(room1WaitList.peek() != null)
									{
										player2 = room1WaitList.poll();
										
										if(room1WaitList.size() < 5)
										{
											room1WaitList.add(new Client(client, name));
											send.println("This game is currently full, you have been added to the wait list");
										}
										else
										{
											send.println("Server Full");
											client.close();
										}
									}
									else
									{
										player2 = new Client(client, name);
									}
								}
								else if(room1WaitList.size() < 5)
								{
									room1WaitList.add(new Client(client, name));
									send.println("This game is currently full, you have been added to the wait list");
								}
								else
								{
									send.println("Server Full, wait list full");
									client.close();
								}
							}
							else
							{
								player1 = new Client(client, name);
							}
							player1.printMessage("Waiting for opponent");
						}
						else if(player2 == null || player2.checkConnected() == false)
						{
							if(room1WaitList.peek() != null)
							{
								player2 = room1WaitList.poll();
								if(player1 == null || player1.checkConnected() == false)
								{
									if(room1WaitList.peek() != null)
									{
										player1 = room1WaitList.poll();
										if(room1WaitList.size() < 5)
										{
											room1WaitList.add(new Client(client, name));
											send.println("This game is currently full, you have been added to the wait list");
										}
										else
										{
											send.println("Server Full");
											client.close();
										}
									}
									else
									{
										player1 = new Client(client, name);
									}
								}
								else if(room1WaitList.size() < 5)
								{
									room1WaitList.add(new Client(client, name));
									send.println("This game is currently full, you have been added to the wait list");
								}
								else
								{
									send.println("Server Full, wait list full");
									client.close();
								}
							}
							else
							{
								player2 = new Client(client, name);
							}
						}
						else
						{
							if(room1WaitList.size() < 5)
							{
								room1WaitList.add(new Client(client, name));
								send.println("This game is currently full, you have been added to the wait list");
							}
							else
							{
								send.println("Server Full and Wait List Full");
								client.close();
							}
						}
					}
					else if(gameType.equalsIgnoreCase("Max Number"))
					{
						if(player01 == null || player01.checkConnected() == false)
						{
							if(room2WaitList.peek() != null)
							{
								player01 = room2WaitList.poll();
								if(player02 == null || player02.checkConnected() == false)
								{
									if(room2WaitList.peek() != null)
									{
										player02 = room2WaitList.poll();
										if(room2WaitList.size() < 5)
										{
											room2WaitList.add(new Client2(client, name));
											send.println("This game is currently full, you have been added to the wait list");
										}
										else
										{
											send.println("Server Full");
											client.close();
										}
									}
									else
									{
										player02 = new Client2(client, name);
									}
								}
								if(room2WaitList.size() < 5)
								{
									room2WaitList.add(new Client2(client, name));
									send.println("This game is currently full, you have been added to the wait list");
								}
								else
								{
									send.println("Server Full, wait list full");
									client.close();
								}
							}
							else
							{
								player01 = new Client2(client, name);
							}
							player01.printMessage("waiting for an opponent");

						}
						else if(player02 == null || player02.checkConnected() == false)
						{
							if(room2WaitList.peek() != null)
							{
								player02 = room2WaitList.poll();
								if(player01 == null || player01.checkConnected() == false)
								{
									if(room2WaitList.peek() != null)
									{
										player01 = room2WaitList.poll();
										if(room2WaitList.size() < 5)
										{
											room2WaitList.add(new Client2(client, name));
											send.println("This game is currently full, you have been added to the wait list");
										}
										else
										{
											send.println("Server Full");
											client.close();
										}
									}
									else
									{
										player01 = new Client2(client, name);
									}
								}
								if(room2WaitList.size() < 5)
								{
									room2WaitList.add(new Client2(client, name));
									send.println("This game is currently full, you have been added to the wait list");
								}
								else
								{
									send.println("Server Full");
									client.close();
								}
							}
							else
							{
								player02 = new Client2(client, name);
							}

							player02.printMessage("Waiting for opponent");
						}
						else
						{
							if(room2WaitList.size() < 5)
							{
								room2WaitList.add(new Client2(client, name));
								send.println("This game is currently full, you have been added to the wait list");
							}
							else
							{
								send.println("Server Full and Wait List Full");
								client.close();
							}

						}
					}

				}
				catch(Exception e)
				{
					e.printStackTrace();
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
}