import org.json.JSONArray;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ServerThread extends Thread{
	Socket connSocket;
	private Player player;
	private static ArrayList<Player> players = new ArrayList<>();
	private DataOutputStream outToClient;
	private static ArrayList<DataOutputStream> clients = new ArrayList<>();
	
	public ServerThread(Socket connSocket) throws IOException {
		this.connSocket = connSocket;
		this.outToClient = new DataOutputStream(connSocket.getOutputStream());
		clients.add(outToClient);
	}
	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));

			System.out.println("tråd oprettet");
			outToClient.writeBytes("Hvad er dit navn?" + "\n");
			String navn = inFromClient.readLine();
			System.out.println(navn + " Has joined");
			players.add(player = GameLogic.makePlayers(navn));
			System.out.println(player);
			// Do the work and the communication with the client here	
			// The following two lines are only an example
			while(connSocket.isConnected()) {
				System.out.println("Sender update til clients");
				updateClients();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}		
		// do the work here
	}
	public void updateClients() throws IOException {
		// lav en arraylist med 3 personer
		System.out.println("Updatere clients");
		// Pak indhold af arraylist ned i en JSON
		JSONArray jarr = new JSONArray();
		for (int i=0;i< GameLogic.players.size();i++) {
			JSONObject jo = new JSONObject();
			jo.put("name",GameLogic.players.get(i).getName());
			jo.put("x", GameLogic.players.get(i).getXpos());
			jo.put("y", GameLogic.players.get(i).getYpos());
			jo.put("direction",GameLogic.players.get(i).getDirection());
			jarr.put(jo);
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("liste",jarr);
		String s= jo2.toString();

		// Sysout af den skabte JSON
		System.out.println(s);

		// lav forbindelse til server og send den skabte JSON
		for (DataOutputStream c : clients){
			outToClient.writeBytes(s + '\n');
		}

	}
}
