package server;

public class Message 
{
	User sender; //消息的发送者
	String msg;  //消息内容
	
	public Message (User sender, String msg)
	{
		this.sender = sender;
		this.msg = msg;
	}
}
