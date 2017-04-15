package server;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

//服务器类
public class Server {

	public static User ServerUser = new User("SYSTEM", null);
	private ArrayList<Message> MsgList;
	private ServerSocket SrvSocket;
	private InetSocketAddress SrvAddress;
	
	public Server(int port) throws IOException
	{
		this.MsgList = new ArrayList<Message>();
		this.SrvSocket = new ServerSocket();
		this.SrvAddress = new InetSocketAddress (port);
		this.SrvSocket.bind(this.SrvAddress, 50);
	}
	
	public int getPort()
	{
		return SrvAddress.getPort();
	}
	
	public String getHost()
	{
		return SrvAddress.getHostString();
	}
	
	public void Dispose()
	{
		try
		{
			SrvSocket.close();	
		}
		catch (Exception e) {}
		finally {}
	}
	
	//监听客户端并连接
	public void Listen()
	{
		while (true)
		{
			try 
			{
				Socket s = SrvSocket.accept();
				User u = new User("New User", s);
				
				//初始化一个线程然后启动它
				Thread t = new UserReceiveThread (u, MsgList);
				Thread tSend = new UserSendThread (u, MsgList);
				t.start();
				tSend.start();
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			
			
		}
	}

	

}
