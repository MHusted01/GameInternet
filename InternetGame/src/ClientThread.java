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
            players = new ArrayList<>();
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String startMessage = inFromServer.readLine();
            System.out.println(startMessage);
            while(true){
                // Gennemløb JSON og skab Arrayliste med personer ud fra JSON
                String s = inFromServer.readLine();
                for (Player p : players){
                    Gui.removePlayerOnScreen(p.location);
                }
                players.clear();
                JSONObject jo = new JSONObject(s);
                JSONArray jarr = jo.getJSONArray("liste");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject js = (JSONObject) jarr.get(i);
                    Player p = new Player(js.getString("name"), new pair(js.getInt("x"), js.getInt("y")), js.getString("direction"));
                    players.add(p);
                };
                for (Player p : players){
                    Gui.placePlayerOnScreen(p.location,p.direction);
                    Gui.updateScoreTable();
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
