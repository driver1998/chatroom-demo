package client;

import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
public class Program {

	
	public static void main(String[] args) throws Exception
	{
		
		BufferedReader stdinReader = new BufferedReader(new InputStreamReader(System.in));
		
		System.out.print("\nPlease input the server ip: ");
		String ip = stdinReader.readLine();
		
		System.out.print("\nPlease input your name: ");
		String name = stdinReader.readLine();
		
		InetSocketAddress add = new InetSocketAddress(ip, 5375);
		Socket socket = new Socket();
		
		socket.connect(add);
		socket.setKeepAlive(true);
		
		OutputStreamWriter socketWriter = new OutputStreamWriter (socket.getOutputStream());
		
		socketWriter.write("<-<-name->->\n".replace("name", name));
		socketWriter.flush();

		System.out.println("\nWelcome!\nNow you are logged in as " + name + ".");
		
		Thread t = new ClientThread(socket);
		t.start();
		
		String str = null;
		str = stdinReader.readLine();
		while (str != null)
		{
			socketWriter.write(str+"\n");
			socketWriter.flush();
			str = stdinReader.readLine();
		}
		
		System.out.println("\nLogging out...");
		socketWriter.write("->->exit<-<-\n");
		socketWriter.flush();
		
		Thread.sleep(1000);
		socket.close();
		
	}

}
