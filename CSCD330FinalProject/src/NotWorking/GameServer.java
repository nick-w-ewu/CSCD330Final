package NotWorking;
import java.util.LinkedList;
import java.util.Queue;

public class GameServer
{

	public static void main(String[] args)
	{
		Queue<TempClient> clients = new LinkedList<TempClient>();
		ClientAccepter accepter = new ClientAccepter(clients);
		GameRunner runner = new GameRunner(accepter);
		accepter.run();
		runner.run();
	}

}
