package NotWorking;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

import good.Client;
import good.Client2;

public class GameRunner extends Thread
{
	ClientAccepter clientAccepter;
	
	public GameRunner(ClientAccepter c)
	{
		this.clientAccepter = c;
	}
	
	public void run()
	{
		Client player1 = null;
		Client player2 = null;
		Client2 player01 = null;
		Client2 player02 = null;
		Queue<Client> room1WaitList = new LinkedList<Client>();
		Queue<Client2> room2WaitList = new LinkedList<Client2>();
		TempClient temp;
		PrintWriter send;
		
		while(true)
		{
			try
			{
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
				
				temp = this.clientAccepter.dedueue();
				if(temp != null)
				{
					send = new PrintWriter(temp.socket.getOutputStream(), true);
					if(temp.gameType.equalsIgnoreCase("Rock-Paper-Scissors") || temp.gameType.equalsIgnoreCase("rps"))
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
											room1WaitList.add(new Client(temp.socket, temp.name));
											send.println("This game is currently full, you have been added to the wait list");
										}
										else
										{
											send.println("Server Full");
											temp.socket.close();
										}
									}
									else
									{
										player2 = new Client(temp.socket, temp.name);
									}
								}
								else if(room1WaitList.size() < 5)
								{
									room1WaitList.add(new Client(temp.socket, temp.name));
									send.println("This game is currently full, you have been added to the wait list");
								}
								else
								{
									send.println("Server Full, wait list full");
									temp.socket.close();
								}
							}
							else
							{
								player1 = new Client(temp.socket, temp.name);
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
											room1WaitList.add(new Client(temp.socket, temp.name));
											send.println("This game is currently full, you have been added to the wait list");
										}
										else
										{
											send.println("Server Full");
											temp.socket.close();
										}
									}
									else
									{
										player1 = new Client(temp.socket, temp.name);
									}
								}
								else if(room1WaitList.size() < 5)
								{
									room1WaitList.add(new Client(temp.socket, temp.name));
									send.println("This game is currently full, you have been added to the wait list");
								}
								else
								{
									send.println("Server Full, wait list full");
									temp.socket.close();
								}
							}
							else
							{
								player2 = new Client(temp.socket, temp.name);
							}
						}
						else
						{
							if(room1WaitList.size() < 5)
							{
								room1WaitList.add(new Client(temp.socket, temp.name));
								send.println("This game is currently full, you have been added to the wait list");
							}
							else
							{
								send.println("Server Full and Wait List Full");
								temp.socket.close();
							}
						}
					}
					else if(temp.gameType.equalsIgnoreCase("Max Number"))
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
											room2WaitList.add(new Client2(temp.socket, temp.name));
											send.println("This game is currently full, you have been added to the wait list");
										}
										else
										{
											send.println("Server Full");
											temp.socket.close();
										}
									}
									else
									{
										player02 = new Client2(temp.socket, temp.name);
									}
								}
								if(room2WaitList.size() < 5)
								{
									room2WaitList.add(new Client2(temp.socket, temp.name));
									send.println("This game is currently full, you have been added to the wait list");
								}
								else
								{
									send.println("Server Full, wait list full");
									temp.socket.close();
								}
							}
							else
							{
								player01 = new Client2(temp.socket, temp.name);
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
											room2WaitList.add(new Client2(temp.socket, temp.name));
											send.println("This game is currently full, you have been added to the wait list");
										}
										else
										{
											send.println("Server Full");
											temp.socket.close();
										}
									}
									else
									{
										player01 = new Client2(temp.socket, temp.name);
									}
								}
								if(room2WaitList.size() < 5)
								{
									room2WaitList.add(new Client2(temp.socket, temp.name));
									send.println("This game is currently full, you have been added to the wait list");
								}
								else
								{
									send.println("Server Full");
									temp.socket.close();
								}
							}
							else
							{
								player02 = new Client2(temp.socket, temp.name);
							}

							player02.printMessage("Waiting for opponent");
						}
						else
						{
							if(room2WaitList.size() < 5)
							{
								room2WaitList.add(new Client2(temp.socket, temp.name));
								send.println("This game is currently full, you have been added to the wait list");
							}
							else
							{
								send.println("Server Full and Wait List Full");
								temp.socket.close();
							}

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
}
