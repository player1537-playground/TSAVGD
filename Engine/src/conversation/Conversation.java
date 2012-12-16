import java.util.*;
import java.io.*;

class Conversation {
    static void printConv(String conv) {
	System.out.println("> " + conv);
    }
    static void getResponse(Response ... responses) {
	HashMap<String, Response> map = new HashMap<String, Response>();
	for (Response r : responses) {
	    System.out.println("[" + r.getText() + "]");
	    map.put(r.getText(), r);
	}
	String key = "";
	BufferedReader bufRead = new BufferedReader(new InputStreamReader(System.in));
	do {
	    System.out.print("Your response: ");
	    System.out.flush();
	    try {
		String temp = bufRead.readLine();
		//key = temp.substring(0, temp.length());
		key = temp;
	    } catch (Exception e) { }
	    System.out.print("<" + key + ">");
	} while (!map.containsKey(key));
	map.get(key).getAction().doAction();
    }
}
