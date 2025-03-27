public abstract class Element {
    private pair location;

    public Element(pair location) {
        this.location = location;
    }

    public pair getLocation() {
        return location;
    }
    public int getXpos() {
        return location.x;
    }
    public String getName(){
        return null;
    }
    public String getDirection(){
        return null;
    }
    public int getPoint(){
        return 0;
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
    public void setLocation(pair location) {
        this.location = location;
    }

}

