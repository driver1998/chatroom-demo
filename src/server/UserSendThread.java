package server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

//每个用户对应的发信线程
public class UserSendThread extends Thread
{
	private User user;
	private Socket socket;
	private OutputStreamWriter socketWriter;
	private ArrayList<Message> msgList;      //总消息列表
	private int currentIndex;            //当前位于消息列表的位置
	
	public UserSendThread(User user, ArrayList<Message> msgList)
	{
		this.user = user;
		this.socket = user.getSocket();
		this.msgList = msgList;
		this.currentIndex = 0;
		
		try
		{
			socketWriter = new OutputStreamWriter(this.socket.getOutputStream());
		} 
		catch (IOException e) {}
	}
	
	public void run()
	{
		synchronized(msgList)
		{
			
			while (!socket.isClosed())
			{

					int size = msgList.size();
					
					//没新的信件了 等着吧
					if (size <= currentIndex)
					{
						try { msgList.wait(); } catch (InterruptedException e) {}
					}

					//把所有新信件发出去
					//新登陆的用户顺便把之前的聊天记录也发过去
					for (; currentIndex<size; currentIndex++)
					{
						Message msg = msgList.get(currentIndex);
						if (msg.sender != user)
						{
							try 
							{
								socketWriter.write(msg.sender.getName() + ": " + msg.msg + "\n");
								socketWriter.flush();
							}
							catch (IOException e) {}
						}

					}
					
					try { Thread.sleep(100); } catch (InterruptedException e) {}
				}
		
		}
	}
}
