import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class ClientThread extends Thread{

    private Socket clientSocket;
    private static ArrayList<Player> players = null;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            sleep(3000);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String startMessage = inFromServer.readLine();
            System.out.println(startMessage);
            while(true){
                // Gennemløb JSON og skab Arrayliste med personer ud fra JSON
                players = new ArrayList<Player>();
                JSONObject jo = new JSONObject(inFromServer.readLine());
                JSONArray jarr = jo.getJSONArray("liste");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject js = (JSONObject) jarr.get(i);
                    Player p = new Player(js.getString("name"), new pair(js.getInt("x"), js.getInt("y")), js.getString("direction"));
                    p.addPoints(js.getInt("point"));
                    players.add(p);
                };

                for (Player player : players) {
                    Gui.placePlayerOnScreen(player.getLocation(), player.getDirection());
                }
                Gui.updateScoreTable();
                System.out.println("Modtaget JSON fra server:");
                Gui.updateScoreTable();
                // testudskrigt af den skabte Arraylist
                for (int i = 0; i < players.size(); i++) {
                    System.out.println(players.get(i).getName() + "   " + players.get(i).getLocation()+ "    "
                            + players.get(i).getDirection() +  "   " + players.get(i).getPoint());
                }
            }
        }
        catch (Exception e){

        }
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }
}
