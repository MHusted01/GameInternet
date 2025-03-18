import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Iterator;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer {
	static ArrayList<Person> list = null;

	public static void main(String[] args) throws Exception {

		String clientSentence;
		ServerSocket welcomSocket = new ServerSocket(6789);

		while (true) {
			// accepter forbindelse fra klient og modtag JSON fra klient
			Socket connectionSocket = welcomSocket.accept();
			BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = inFromClient.readLine();

			// Gennemløb JSON og skab Arrayliste med personer ud fra JSON
			list = new ArrayList<Person>();
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

}
