import java.io.DataOutputStream;
import java.io.IOException;

public class Player extends Element {
	String name;
	int point;
	private DataOutputStream outToClient;
	String direction;

	public Player(String name, pair loc, String direction, int point) {
		super(loc);
		this.name = name;
		this.direction = direction;
		this.point = point;
	}

	public void setOutToClient(DataOutputStream outToClient) {
		this.outToClient = outToClient;
	}


	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getName() {
		return name;
	}

	public int getPoint() {
		return point;
	}



	public void addPoints(int p) {
		point += p;
	}

	public String toString() {
		return name + ":   " + point;
	}

	public synchronized void Update(String s) throws IOException {
		try {
			outToClient.writeBytes(s + "\n");
		} catch (IOException e) {
			System.out.println("Klient: " + name + " afbrød forbindelse.");
			GameLogic.elements.remove(this);
			throw e;
		}
	}
}

