package server;

import java.io.IOException;

import server.Server;

public class Program {

	static Server srv;
	
	public static void main(String[] args) throws IOException 
	{
		
		System.out.println("ChatRoom Server Build 20170414");
		
		srv = new Server(5375);
		
		StringBuilder sb = new StringBuilder ();
		sb.append("Server Initialized. \nHost: ");
		sb.append(srv.getHost());
		sb.append("\nPort: ");
		sb.append(srv.getPort());
		System.out.println(sb.toString());
		
		srv.Listen();
		
		System.out.println("Server Terminated.");
		srv.Dispose();
	}
}
