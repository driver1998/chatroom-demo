package server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.regex.*;

//ÿ���û���Ӧ�������߳�
public class UserReceiveThread extends Thread
{
	private User user;
	private Socket socket;
	private BufferedReader socketReader;
	private ArrayList<Message> msgList;      //ȫ����Ϣ�б�
	private ArrayList<Message> localMsgList; //������Ϣ�б�
	
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
				//������ʾ
				if (str.startsWith("->->exit<-<-"))
				{
					//�ȷŵ������б�
					Message msg = new Message (Server.ServerUser, user.getName() + " disconnected.");
					localMsgList.add(msg);
					
					System.out.println("\n" + msg.msg);
					
					try { socket.close(); } catch (IOException e) {}
				}
				//������ʾ ��ʽ��<-<-�ǳ�->->
				else if (str.startsWith("<-<-"))
				{
					Matcher match = Pattern.compile("<-<-(.*?)->->").matcher(str);
					if (match.matches())
					{
						user.setName(match.group(1));
					}
					
					//�ȷŵ������б�
					Message msg = new Message (Server.ServerUser,  match.group(1) + " connected.");
					localMsgList.add(msg);
					
					System.out.println("\n"+msg.msg);
				}
				//����Ϣ����ȥ
				else
				{
					//�ȷŵ������б�
					Message msg = new Message (user, str);
					localMsgList.add(msg);
					
					System.out.println("\n" + user.getName() + ": " + str);
				}
				
				//�����û�����һ����Ϣ�б�...
				synchronized(msgList)
				{
			
					//�����ص���Ϣȫ������ȥ
					for(Message msg : localMsgList)
					{
						msgList.add(msg);
					}
					localMsgList.clear();
					
					//���ѷ����� ����ȥ������
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

