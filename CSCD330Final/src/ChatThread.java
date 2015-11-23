import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatThread extends Thread
{
	private Socket destination;
	private volatile boolean stop = false;
	private BufferedReader recive;
	
	public ChatThread(Socket destination)
	{
		this.destination = destination;
	}
	
	public void requestStop()
	{
		stop = true;
		try
		{
			destination.close();
			recive.close();
		} 
		catch (IOException e)
		{
			
		}
		
	}
	
	@Override
	public void run()
	{
		try
		{
			recive = new BufferedReader
					(new InputStreamReader(this.destination.getInputStream()));
			while(!stop)
			{
				String message = recive.readLine();
				if(message == null)
				{
					break;
				}
				System.out.println(message);
			}

		} catch (IOException e)
		{
		}
	}
}
