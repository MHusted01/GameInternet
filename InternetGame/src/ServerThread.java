import org.json.JSONArray;
import org.json.JSONObject;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class ServerThread extends Thread {
	private Socket connSocket;
	private Player player;
	private BufferedReader inFromClient;
	private DataOutputStream outToClient;
	public static ArrayList<DataOutputStream> allClients = new ArrayList<>();
	public static ArrayList<Player> players = new ArrayList<>();

	public ServerThread(Socket connSocket) throws IOException {
		this.connSocket = connSocket;
		this.inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
		this.outToClient = new DataOutputStream(connSocket.getOutputStream());
		synchronized (allClients) { allClients.add(outToClient); }
	}

	public void run() {
		try {
			outToClient.writeBytes("Hvad er dit navn?\n");
			String navn = inFromClient.readLine();
			System.out.println(navn + " har tilsluttet sig.");
			player = GameLogic.makePlayers(navn);
			players.add(player);

			while (connSocket.isConnected()) {
				String clientSentence = inFromClient.readLine();
				if (clientSentence == null) break;
				System.out.println("Modtaget: " + clientSentence);
				player.setDirection(clientSentence);
				updateClients();
			}
		} catch (IOException e) {
			System.out.println("Forbindelse mistet til klient.");
		} finally {
			synchronized (allClients) { allClients.remove(outToClient); }
			players.remove(player);
			try { connSocket.close(); } catch (IOException ignored) {}
		}
	}

	public void updateClients() {
		JSONArray jarr = new JSONArray();
		for (Player p : players) {
			JSONObject jo = new JSONObject();
			jo.put("name", p.getName());
			jo.put("x", p.getXpos());
			jo.put("y", p.getYpos());
			jo.put("direction", p.getDirection());
			jarr.put(jo);
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("liste", jarr);
		String jsonStr = jo2.toString();

		synchronized (allClients) {
			for (DataOutputStream out : allClients) {
				try {
					out.writeBytes(jsonStr + '\n');
				} catch (IOException ignored) {}
			}
		}
	}

	public static pair getRandomFreePosition()
	// finds a random new position which is not wall
	// and not occupied by other players
	{
		int x = 1;
		int y = 1;
		boolean foundfreepos = false;
		while  (!foundfreepos) {
			Random r = new Random();
			x = Math.abs(r.nextInt()%18) +1;
			y = Math.abs(r.nextInt()%18) +1;
			if (Generel.board[y].charAt(x)==' ') // er det gulv ?
			{
				foundfreepos = true;
				for (Player p: players) {
					if (p.getXpos()==x && p.getYpos()==y) //pladsen optaget af en anden
						foundfreepos = false;
				}

			}
		}
		pair p = new pair(x,y);
		return p;
	}
}