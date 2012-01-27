package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

public final class URLUtils {

	private URLUtils() {
	}
	
    public static String getTitleByURI(String uri) throws Exception {
        
        URL url = new URL(uri);
        HttpURLConnection.setFollowRedirects(true);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        
        StringBuffer html = new StringBuffer();
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "JISAutoDetect"));
        String tmp;
        while ((tmp = br.readLine()) != null) {
            html.append(tmp);
        }
        br.close();
        
        String title = "";
        try {
            DOMParser parser = new DOMParser();
            StringReader sr = new StringReader(html.toString());
            parser.parse(new InputSource(sr));
            HTMLDocument doc = (HTMLDocument) parser.getDocument();
            title = doc.getTitle();
        } catch (Exception e) {
            return "";
        }
        
        return title;
    }
}
