package server;

import java.net.Socket;

public class User {

	private String _name;   //�û���
	private Socket _socket; //�û���Ӧ��socket
	
	public User(String name, Socket socket)
	{
		_name = name;
		_socket = socket;
	}
	
	public String getName()
	{
		return _name;
	}
	
	public void setName(String name)
	{
		_name = name;
	}
	
	public Socket getSocket()
	{
		return _socket;
	}
}
