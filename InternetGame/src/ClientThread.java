import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread{

    private Socket clientSocket;
    private static ArrayList<Element> elements = null;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            sleep(3000);
            elements = new ArrayList<>();
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String startMessage = inFromServer.readLine();
            System.out.println(startMessage);
            while(true){
                // Gennemløb JSON og skab Arrayliste med personer ud fra JSON
                String s = inFromServer.readLine();
                for (Element e : elements){
                    if (e instanceof Player){
                        Gui.removePlayerOnScreen(e.getLocation());
                    }
                    else { // element is treasure
                        Gui.removeTreasureOnScreen(e.getLocation());
                    }
                }
                elements.clear();
                JSONObject jo = new JSONObject(s);
                JSONArray jarr = jo.getJSONArray("liste");
                for (int i = 0; i < jarr.length(); i++) {

                    JSONObject js = (JSONObject) jarr.get(i);
                    Player p = new Player(js.getString("name"), new pair(js.getInt("x"), js.getInt("y")), js.getString("direction"), js.getInt("point"));
                    elements.add(p);
                };
                for (Element e : elements){{
                    if (e instanceof Player) {
                        Gui.placePlayerOnScreen(e.getLocation(),e.getDirection());
                    }
                }
                    Gui.updateScoreTable();
                }

            }
        }
        catch (Exception e){

        }
    }

    public static ArrayList<Player> getPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (Element e : elements){
            if (e instanceof Player){
                players.add((Player) e);
            }
        }
        return players;
    }
}
