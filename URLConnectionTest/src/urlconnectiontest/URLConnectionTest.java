package urlconnectiontest;

import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author skuarch
 */
public class URLConnectionTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        URL url = null;
        HttpURLConnection hurlc = null;
        DataOutputStream dataOutputStream = null;
        ObjectOutputStream objectOutputStream = null;
        OutputStream outputStream = null;
        HashMap hashMap = new HashMap();

        try {

            String urlParameters = "fName=" + URLEncoder.encode("algo", "UTF-8") + "&lName=" + URLEncoder.encode("algo", "UTF-8");

            url = new URL("http://192.168.208.9:8080/testSocket/connection");
            hurlc = (HttpURLConnection) url.openConnection();

            hurlc.setRequestMethod("POST");            
            hurlc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            hurlc.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
            hurlc.setRequestProperty("Content-Language", "en-US");
            hurlc.setRequestProperty("ehp", "1");

            hurlc.setDoOutput(true);
            outputStream = hurlc.getOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);

            hashMap.put("julio y ", "ricardo");
            hashMap.put("objecto", new ArrayList<Object>());
            objectOutputStream.writeObject(hashMap);
            objectOutputStream.flush();
            
            if (hurlc.getResponseCode() == HttpURLConnection.HTTP_OK) {
                System.out.println("se conecto");
            }else{
                System.out.println("no se conecto");
            }

            hurlc.disconnect();
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
