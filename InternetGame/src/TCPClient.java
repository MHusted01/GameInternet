import javafx.application.Application;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

// Denne er kun medtaget til Test-formål, skal IKKE anvendes.
public class TCPClient {
	private static ArrayList<Player> players = new ArrayList<>();
	public static DataOutputStream outToServer;
	public static void main(String argv[]) throws Exception{
		String sentence;
		String modifiedSentence;
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket= new Socket("localhost",6789);
		outToServer = new DataOutputStream(clientSocket.getOutputStream());

		new ClientThread(clientSocket).start();

		String navn = inFromServer.readLine();
		outToServer.writeBytes(navn + "\n");

		Application.launch(Gui.class);

		while(clientSocket.isConnected()){
			System.out.println("Sendt venstre til server");
			sendDirection("Venstre", outToServer);
			int direction = inFromServer.read();
			sentence = inFromServer.readLine();
			outToServer.write(direction);
			outToServer.writeBytes(sentence + '\n');
		}

		//clientSocket.close();
	}
	public static void sendDirection (String direction, DataOutputStream outToServer){
		try {
			outToServer.writeBytes(direction + '\n');
		}
		catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
	public static ArrayList<Player> getPlayers() {
		return players;
	}

	public static void setPlayers(ArrayList<Player> players) {
		TCPClient.players = players;
	}
}


