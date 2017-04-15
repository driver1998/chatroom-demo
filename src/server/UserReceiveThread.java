package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.*;

//每个用户对应的收信线程
public class UserReceiveThread extends Thread
{
	private User user;
	private Socket socket;
	private BufferedReader socketReader;
	private ArrayList<Message> msgList;      //全局消息列表
	private ArrayList<Message> localMsgList; //本地消息列表
	
	public UserReceiveThread (User user, ArrayList<Message> msgList)
	{
		this.user = user;
		this.socket = user.getSocket();
		this.msgList = msgList;
		this.localMsgList = new ArrayList<Message>();
		
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
			
			System.out.println(str.length());
			
			if (str!=null)
			{
				//下线提示
				if (str.startsWith("->->exit<-<-"))
				{
					//先放到本地列表
					Message msg = new Message (Server.ServerUser, user.getName() + " disconnected.");
					localMsgList.add(msg);
					
					System.out.println("\n" + msg.msg);
					
					try { socket.close(); } catch (IOException e) {}
				}
				//上线提示 格式是<-<-昵称->->
				else if (str.startsWith("<-<-"))
				{
					Matcher match = Pattern.compile("<-<-(.*?)->->").matcher(str);
					if (match.matches())
					{
						user.setName(match.group(1));
					}
					
					//先放到本地列表
					Message msg = new Message (Server.ServerUser,  match.group(1) + " connected.");
					localMsgList.add(msg);
					
					System.out.println("\n"+msg.msg);
				}
				//把消息发出去
				else
				{
					//先放到本地列表
					Message msg = new Message (user, str);
					localMsgList.add(msg);
					
					System.out.println("\n" + user.getName() + ": " + str);
				}
				
				//所有用户共享一个消息列表...
				synchronized(msgList)
				{
			
					//将本地的消息全部推上去
					for(Message msg : localMsgList)
					{
						msgList.add(msg);
					}
					localMsgList.clear();
					
					//提醒发送者 可以去发信了
					try
					{	
						msgList.notifyAll();
					}
					catch (Exception e) {e.printStackTrace();}
					

				}
			}
			
		}
	}
}

