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
	}

	public void run() {
		try {
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));

			System.out.println("tråd oprettet");
			outToClient.writeBytes("Hvad er dit navn?" + "\n");
			String navn = inFromClient.readLine();
			System.out.println(navn + " Has joined");

			players.add(player = GameLogic.makePlayers(navn));
			clients.add(outToClient);
			System.out.println(player);

			while(connSocket.isConnected()) {
				sleep(3000);
				System.out.println("Sender update til clients");
				updateClients();
			}
		} catch (IOException | InterruptedException e) {
			System.out.println("Forbindelse tabt til spiller: " + player.getName());
			e.printStackTrace();
		} finally {
			// Fjern spilleren og forbindelsen uanset hvad der sker
			if (player != null) players.remove(player);
			if (outToClient != null) clients.remove(outToClient);
			try {
				if (connSocket != null && !connSocket.isClosed()) {
					connSocket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateClients() throws IOException {
		// lav en arraylist med 3 personer
		System.out.println("Updatere clients");
		// Pak indhold af arraylist ned i en JSON
		JSONArray jarr = new JSONArray();
		for (int i=0;i< players.size();i++) {
			JSONObject jo = new JSONObject();
			jo.put("name",players.get(i).getName());
			jo.put("x", players.get(i).getXpos());
			jo.put("y", players.get(i).getYpos());
			jo.put("direction",players.get(i).getDirection());
			jo.put("point", players.get(i).getPoint());
			jarr.put(jo);
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("liste",jarr);
		String s= jo2.toString();

		// Sysout af den skabte JSON
		System.out.println(s);

		// lav forbindelse til server og send den skabte JSON
		for (DataOutputStream c : clients){
			try {
				c.writeBytes(s + '\n');
			} catch (IOException e) {
				System.out.println("En klient kunne ikke modtage data – fjernes.");
				clients.remove(c);
			}
		}

	}
}
