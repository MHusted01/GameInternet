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
		Treasure t = new Treasure(getRandomFreePosition());
		elements.add(t);
	}
	public static boolean nameIsTaken(String name){
		Boolean bool = false;
		for (Element p : elements){
			if (p instanceof Player){
				if (((Player) p).name.equals(name)){
					bool = true;
				};
			}
		}
		return bool;
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

	public static synchronized void updatePlayer(Player me, int delta_x, int delta_y, String direction) throws IOException {
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
						Element e = getElementAt(x + delta_x, y + delta_y);
						if (e  instanceof Player) {
							player.addPoints(10);
							((Player) e).addPoints(-10);
							pair pa = getRandomFreePosition();
							e.setLocation(pa);
							// Optional: Gui.movePlayerOnScreen(new pair(x + delta_x, y + delta_y), pa, p.direction);
						} else if (e instanceof Treasure){
							player.addPoints(50);
							elements.remove(e);
							makeTreasure();
							pair newpos = new pair(x + delta_x, y + delta_y);
							player.setLocation(newpos);
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
	}


	public static Element getElementAt(int x, int y) throws IOException {
		for (Element p : elements) {
			if (p instanceof Player) {
				if (p.getXpos() == x && p.getYpos() == y) {
					return (Player) p;
				}
			}
			if (p instanceof Treasure){
				if (p.getXpos() == x && p.getYpos() == y) {
					return (Treasure) p;
				}
			}
		}
        return null;
    }


	public static synchronized void updateClients() {
		JSONArray jarrayP = new JSONArray();
		JSONArray jarrayT = new JSONArray();
		ArrayList<Element> toRemove = new ArrayList<>();

		for (Element e : elements) {
			if (e instanceof Player) {
				Player p = (Player) e;
				JSONObject joP = new JSONObject();
				joP.put("name", p.getName());
				joP.put("x", p.getXpos());
				joP.put("y", p.getYpos());
				joP.put("direction", p.getDirection());
				joP.put("point", p.getPoint());
				jarrayP.put(joP);
			} else if (e instanceof Treasure) {
				JSONObject joT = new JSONObject();
				joT.put("x", e.getXpos());
				joT.put("y", e.getYpos());
				jarrayT.put(joT);
			}
		}

		JSONObject jo = new JSONObject();
		jo.put("Player", jarrayP);
		jo.put("Treasure", jarrayT);
		String s = jo.toString();

		for (Element e : new ArrayList<>(elements)) {
			if (e instanceof Player) {
				try {
					((Player) e).Update(s);
				} catch (IOException ex) {
					System.out.println("Fjerner spiller: " + e.getName());
					toRemove.add(e);
				}
			}
		}

		elements.removeAll(toRemove);
	}
}


