import java.net.Socket;

public class Client
{
	public Socket socket;
	public String name;
	private boolean connected;
	public boolean inGame;
	
	public Client(Socket socket, String name)
	{
		this.name = name;
		this.socket = socket;
		this.connected = true;
		this.inGame = false;
	}
	
	public void setConnected(boolean set)
	{
		this.connected = set;
	}
	
	public void setInGame(boolean set)
	{
		this.inGame = set;
	}
	
	public boolean checkConnected()
	{
		return this.connected;
	}

}
