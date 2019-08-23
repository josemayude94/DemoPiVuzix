package com.vuzix.sample.barcodefromintent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLBasicAuth {
    String webPage ;
    InputStream is;
    int numCharsRead;
    char[] charArray;
    String authStringEnc;

    public URLBasicAuth(String URL, String credentials){
        webPage = URL;
        authStringEnc = credentials;
    }

    public URLBasicAuth(String webid){
        webPage = "https://devdata.osisoft.com/piwebapi/streams/"+webid+"/value";

        /*User y pass encodeado en Base64 "webapiuser:!try3.14webapi!"*/
        authStringEnc = "d2ViYXBpdXNlcjohdHJ5My4xNHdlYmFwaSE=";
    }

    public String conectar(){
        try{
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            return getPage(isr);
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public String getPage(InputStreamReader isr){
        char[] charArray = new char[2056];
        StringBuffer sb = new StringBuffer();
        String result = null;
        try{
            while ((numCharsRead = isr.read(charArray)) > 0){
                sb.append(charArray, 0, numCharsRead);
            }
            result = sb.toString();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return result;
    }
}
