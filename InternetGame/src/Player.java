public class Player {
	String name;
	pair location;
	int point;
	String direction;

	public Player(String name, pair loc, String direction, int point) {
		this.name = name;
		this.location = loc;
		this.direction = direction;
		this.point = point;
	}
	public pair getLocation() {
		return this.location;
	}

	public void setLocation(pair p) {
		this.location = p;
	}

	public int getXpos() {
		return location.x;
	}

	public void setXpos(int xpos) {
		this.location.x = xpos;
	}

	public int getYpos() {
		return location.y;
	}

	public void setYpos(int ypos) {
		this.location.y = ypos;
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
}

