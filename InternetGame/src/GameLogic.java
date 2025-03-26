import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class GameLogic {
	public static List<Element> elements = new ArrayList<Element>();
	//public static Player me;


	public static Player makePlayers(String name, DataOutputStream client) {
		pair p = getRandomFreePosition();
		Player me = new Player(name, p, "up", 0);
		me.setOutToClient(client);
		elements.add(me);
		return me;
	}
	public static void makeTreasure(){
		Treasure t = new Treasure()
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
				for (Element e : elements) {
					if (e.getXpos() == x && e.getYpos() == y) //pladsen optaget af en anden
						foundfreepos = false;
				}

			}
		}
		pair p = new pair(x, y);
		return p;
	}

	public static synchronized void updatePlayer(Player me, int delta_x, int delta_y, String direction) {
		for (Element element: elements) {
			if (element instanceof Player){
				Player player = (Player) element;
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
		}}
	}


	public static Player getPlayerAt(int x, int y) {
		for (Element p : elements) {
			if (p instanceof Player){
			if (p.getXpos() == x && p.getYpos() == y) {
				return (Player) p;
			}
			}
		}
		return null;
	}
	public static void updateClients() throws IOException {
		// Pak indhold af arraylist ned i en JSON
		JSONArray jarrayP = new JSONArray();
		JSONArray jarrayT = new JSONArray();

		for (int i=0;i< elements.size();i++) {
			if (elements.get(i) instanceof Player) {
				JSONObject joP = new JSONObject();
				joP.put("name", elements.get(i).getName());
				joP.put("x", elements.get(i).getXpos());
				joP.put("y", elements.get(i).getYpos());
				joP.put("direction", elements.get(i).getDirection());
				joP.put("point", elements.get(i).getPoint());
				jarrayP.put(joP);
			}
			else { // if element is a treasure
				JSONObject joT = new JSONObject();
				joT.put("x", elements.get(i).getXpos());
				joT.put("y", elements.get(i).getYpos());
				jarrayT.put(joT);
			}
		}
		JSONObject jo = new JSONObject();
		jo.put("Player",jarrayP);
		jo.put("Treasure", jarrayT);
		String s= jo.toString();

		// lav forbindelse til server og send den skabte JSON
		for (Element e : elements){
			if (e instanceof Player){
				Player player = (Player) e;
				player.Update(s);
			}
		}
	}

}


