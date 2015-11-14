import java.net.Socket;

public class Client
{
	public Socket socket;
	public ServerThread clientThread;
	private boolean connected;
	
	public Client(Socket socket, ServerThread thread)
	{
		this.socket = socket;
		this.clientThread = thread;
		this.connected = true;
	}
	
	public void setConnected(boolean set)
	{
		this.connected = set;
	}
	
	public boolean checkConnected()
	{
		return this.connected;
	}
}
