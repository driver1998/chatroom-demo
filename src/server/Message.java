package server;

public class Message 
{
	User sender; //��Ϣ�ķ�����
	String msg;  //��Ϣ����
	
	public Message (User sender, String msg)
	{
		this.sender = sender;
		this.msg = msg;
	}
}
