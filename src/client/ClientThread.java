package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

//把服务器的所有信件接收过来并且显示出来
public class ClientThread extends Thread
{
	Socket socket;
	BufferedReader socketReader;
	
	public ClientThread (Socket socket)
	{
		this.socket = socket;

		try
		{
			socketReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		} 
		catch (IOException e) {}
	}
	public void run()
	{
		String str = null;
		
		while(!socket.isClosed())
		{
			try { str = socketReader.readLine(); } 
			catch (IOException e) 
			{ try { socket.close(); } catch (IOException e1) {} }
			
			if (str!=null)
			{
				System.out.println("\n" + str + "\n");
			}
			
		}
	}
}
