import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread{

    private Socket clientSocket;
    static ArrayList<Player> players = null;

    public ClientThread(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            sleep(3000);
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            while(true){
                System.out.println(inFromServer.readLine());
                TCPClient.setPlayers();


                String clientSentence = inFromServer.readLine();

                // Gennemløb JSON og skab Arrayliste med personer ud fra JSON
                players = new ArrayList<Player>();
                JSONObject jo = new JSONObject(clientSentence);
                JSONArray jarr = jo.getJSONArray("liste");
                for (int i = 0; i < jarr.length(); i++) {
                    JSONObject js = (JSONObject) jarr.get(i);
                    Person p = new Person(js.getString("navn"), js.getString("by"), js.getInt("loen"));
                    list.add(p);
                };

                // testudskrigt af den skabte Arraylist
                for (int i = 0; i < list.size(); i++) {
                    System.out.println(list.get(i).getNavn() + "   " + list.get(i).getBy() + "    " + list.get(i).getLoen());
                }
            }
        }
        catch (Exception e){

        }
    }
}
