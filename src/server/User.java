package server;

import java.net.Socket;

public class User {

	private String _name;   //用户名
	private Socket _socket; //用户对应的socket
	
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
