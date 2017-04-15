package server;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

//ÿ���û���Ӧ�ķ����߳�
public class UserSendThread extends Thread
{
	private User user;
	private Socket socket;
	private OutputStreamWriter socketWriter;
	private ArrayList<Message> msgList;      //����Ϣ�б�
	private int currentIndex;            //��ǰλ����Ϣ�б��λ��
	
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
					
					//û�µ��ż��� ���Ű�
					if (size <= currentIndex)
					{
						try { msgList.wait(); } catch (InterruptedException e) {}
					}

					//���������ż�����ȥ
					//�µ�½���û�˳���֮ǰ�������¼Ҳ����ȥ
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
