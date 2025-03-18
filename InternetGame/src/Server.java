import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;

public class Server {
	
	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {

		ArrayList<Player> players= new ArrayList<>();


		ServerSocket welcomeSocket = new ServerSocket(6789);
		while (true) {
			Socket connectionSocket = welcomeSocket.accept();
			(new ServerThread(connectionSocket)).start();
			System.out.println("tråd oprettet");
		}
	}

}
