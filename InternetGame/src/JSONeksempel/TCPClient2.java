import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;


public class TCPClient2 {

	public static void main(String[] args) throws Exception, IOException {
		// lav en arraylist med 3 personer
		ArrayList<Person> list = new ArrayList<Person>();
		Person p1 = new Person("Ib","Viborg",250000);
		list.add(p1);
		Person p2 = new Person("Per","Gedsted",350000);
		list.add(p2);
		Person p3 = new Person("Hans","Skive",150000);
		list.add(p3);

		// Pak indhold af arraylist ned i en JSON
		JSONArray jarr = new JSONArray();
		for (int i=0;i< list.size();i++) {
			JSONObject jo = new JSONObject();
			jo.put("name",list.get(i).getNavn());
			jo.put("by",list.get(i).getBy());
			jo.put("loen",list.get(i).getLoen());
			jarr.put(jo);
		}
		JSONObject jo2 = new JSONObject();
		jo2.put("liste",jarr);
		String s= jo2.toString();

		// Sysout af den skabte JSON
		System.out.println(s);

		// lav forbindelse til server og send den skabte JSON
		Socket clientSocket = new Socket("localhost", 6789);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(s + '\n');
		clientSocket.close();
			
	}

}
