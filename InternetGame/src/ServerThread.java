import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ServerThread extends Thread{
	Socket connSocket;
	Player player;
	
	public ServerThread(Socket connSocket) {
		this.connSocket = connSocket;
	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connSocket.getOutputStream());
			outToClient.writeBytes("Hvad er dit navn?" + "\n");
			String navn = inFromClient.readLine();
			System.out.println(navn + " Has joined");
			Player player = GameLogic.makePlayers(navn);

			
			// Do the work and the communication with the client here	
			// The following two lines are only an example
			while(connSocket.isConnected()) {
				String clientSentence = inFromClient.readLine();
				System.out.println(clientSentence); // modtaget update fra client
				System.out.println("Sender update til clients");


				outToClient.writeBytes(/*GameLogic.players*/); // update til clients
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}		
		// do the work here
	}
}
