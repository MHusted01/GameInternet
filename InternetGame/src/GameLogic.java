import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class GameLogic {
	public static List<Player> players = new ArrayList<Player>();
	//public static Player me;


	public static Player makePlayers(String name) {
		pair p = getRandomFreePosition();
		Player me = new Player(name, p, "up");
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

	public static void updatePlayer(Player me, int delta_x, int delta_y, String direction) {
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

}


