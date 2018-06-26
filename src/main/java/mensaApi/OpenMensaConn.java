package mensaApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenMensaConn {

    private static OpenMensaConn openMensaConn = null;

    private OpenMensaConn() {}

    public static OpenMensaConn getInstance() {
        if(openMensaConn == null) {
            openMensaConn = new OpenMensaConn();
        }
        return openMensaConn;
    }

    public HttpURLConnection openConnection(String requestUrl) throws Exception {

        URL url = new URL(requestUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setRequestProperty("Content-Type", "application/json");

        return con;
    }

    public String getResponseString(HttpURLConnection con)  throws Exception {

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();

        while((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        return content.toString();
    }

}
