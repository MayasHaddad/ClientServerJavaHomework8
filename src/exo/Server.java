package exo;

import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	/**
	 * @param args
	 */
	public static void main(final String[] args) throws Exception{
		// TODO Auto-generated method stub
		if(args.length!=1){
			System.err.println("Usage : java "+Server.class.getName()+" port");
			System.exit(1);
		}
		final int port = Integer.parseInt(args[0]);

		final ServerSocket socket=new ServerSocket(port); // Le serveur bloque jusqu'à ce qu'un client se connecte

		while(true){
			Socket socketParClient=socket.accept();

			MyRunnable myRunnable =new MyRunnable(socketParClient);

			Thread newClientThread=new Thread(myRunnable);
			newClientThread.start();
		}
	}
}