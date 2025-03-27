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
            startMessage = inFromServer.readLine();
            System.out.println(startMessage);
            while(!startMessage.equals("You have joined")){
                startMessage = inFromServer.readLine();
                System.out.println(startMessage);
            }

            while(true){
                // Gennemløb JSON og skab Arrayliste med personer ud fra JSON
                String s = inFromServer.readLine();
                for (Element e : elements){
                        Gui.removeElementOnScreen(e.getLocation());
                }
                elements.clear();
                JSONObject jo = new JSONObject(s);
                JSONArray jarrayP = jo.getJSONArray("Player");
                JSONArray jarrayT = jo.getJSONArray("Treasure");
                for (int i = 0; i < jarrayP.length(); i++) {
                    JSONObject js = (JSONObject) jarrayP.get(i);
                    Player p = new Player(js.getString("name"), new pair(js.getInt("x"), js.getInt("y")), js.getString("direction"), js.getInt("point"));
                    elements.add(p);
                };
                for (int i = 0; i < jarrayT.length(); i++) {
                    JSONObject js = (JSONObject) jarrayT.get(i);
                    Treasure t = new Treasure( new pair(js.getInt("x"), js.getInt("y")));
                    elements.add(t);
                };
                for (Element e : elements){{
                    if (e instanceof Player) {
                        Gui.placePlayerOnScreen(e.getLocation(),e.getDirection());
                    }
                    if (e instanceof Treasure){
                        Gui.placeTreasureOnScreen(e.getLocation());
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
