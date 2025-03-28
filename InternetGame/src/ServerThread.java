import org.json.JSONArray;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ServerThread extends Thread{
	Socket connSocket;
	private Player player;
	
	public ServerThread(Socket connSocket) throws IOException {
		this.connSocket = connSocket;
	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
			DataOutputStream client = new DataOutputStream(connSocket.getOutputStream());
			System.out.println("tråd oprettet");
			client.writeBytes("Hvad er dit navn?" + "\n");
			String navn = inFromClient.readLine();
			while(GameLogic.nameIsTaken(navn)){
				client.writeBytes("Navn allered brugt - vælg nyt navn" + "\n");
				navn = inFromClient.readLine();
			}
			System.out.println(navn + " Has joined");
			client.writeBytes("You have joined" + "\n");
			player = GameLogic.makePlayers(navn, client);

			// Do the work and the communication with the client here
			// The following two lines are only an example
			sleep(2500);
			while (connSocket.isConnected()) {
				GameLogic.updateClients();
				String moveCommand = inFromClient.readLine();
				if (moveCommand == null) break;

				String[] parts = moveCommand.split(" ");
				int dx = Integer.parseInt(parts[0]);
				int dy = Integer.parseInt(parts[1]);
				String direction = parts[2];
				GameLogic.updatePlayer(player, dx, dy, direction);
			}
		} catch (IOException e) {
			GameLogic.elements.remove(player);
            e.printStackTrace();
		} catch (InterruptedException e) {
			GameLogic.elements.remove(player);
            throw new RuntimeException(e);
        }
        // do the work here
	}
}
