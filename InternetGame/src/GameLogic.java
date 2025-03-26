import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class GameLogic {
	public static List<Player> players = new ArrayList<Player>();
	//public static Player me;


	public static Player makePlayers(String name, DataOutputStream client) {
		pair p = getRandomFreePosition();
		Player me = new Player(name, p, "up", 0);
		me.setOutToClient(client);
		players.add(me);
		return me;
	}

	public static pair getRandomFreePosition()
	// finds a random new position which is not wall
	// and not occupied by other players
	{
		int x = 1;
		int y = 1;
		boolean foundfreepos = false;
		while (!foundfreepos) {
			Random r = new Random();
			x = Math.abs(r.nextInt() % 18) + 1;
			y = Math.abs(r.nextInt() % 18) + 1;
			if (Generel.board[y].charAt(x) == ' ') // er det gulv ?
			{
				foundfreepos = true;
				for (Player p : players) {
					if (p.getXpos() == x && p.getYpos() == y) //pladsen optaget af en anden
						foundfreepos = false;
				}

			}
		}
		pair p = new pair(x, y);
		return p;
	}

	public static synchronized void updatePlayer(Player me, int delta_x, int delta_y, String direction) {
		for (Player player : players) {
			if (player.equals(me)) {
				player.direction = direction;
				int x = player.getXpos();
				int y = player.getYpos();

				if (Generel.board[y + delta_y].charAt(x + delta_x) == 'w') {
					player.addPoints(-1);
				} else {
					// Collision detection
					Player p = getPlayerAt(x + delta_x, y + delta_y);
					if (p != null) {
						player.addPoints(10);
						p.addPoints(-10);
						pair pa = getRandomFreePosition();
						p.setLocation(pa);
						// Optional: Gui.movePlayerOnScreen(new pair(x + delta_x, y + delta_y), pa, p.direction);
					} else {
						player.addPoints(1);
						pair newpos = new pair(x + delta_x, y + delta_y);
						player.setLocation(newpos);
						// Optional: Gui.movePlayerOnScreen(me.getLocation(), newpos, direction);
					}
				}
				break;
			}
		}
	}


	public static Player getPlayerAt(int x, int y) {
		for (Player p : players) {
			if (p.getXpos() == x && p.getYpos() == y) {
				return p;
			}
		}
		return null;
	}
	public static void updateClients() throws IOException {
		// Pak indhold af arraylist ned i en JSON
		JSONArray jarr = new JSONArray();
		for (int i=0;i< players.size();i++) {
			JSONObject jo = new JSONObject();
			jo.put("name", players.get(i).getName());
			jo.put("x", players.get(i).getXpos());
			jo.put("y", players.get(i).getYpos());
			jo.put("direction",players.get(i).getDirection());
			jo.put("point", players.get(i).getPoint());
			jarr.put(jo);
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("liste",jarr);
		String s= jo2.toString();

		// lav forbindelse til server og send den skabte JSON
		for (Player p : players){
			p.Update(s);
		}
	}

}


