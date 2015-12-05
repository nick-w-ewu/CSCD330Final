package NotWorking;
import java.net.Socket;

public class TempClient
{
	Socket socket;
	String name;
	String gameType;
	
	public TempClient(Socket socket, String name, String gameType)
	{
		this.socket = socket;
		this.name = name;
		this.gameType = gameType;
	}
}
