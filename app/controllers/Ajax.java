package controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

import play.Logger;
import play.data.validation.Required;
import play.mvc.Controller;
import play.mvc.results.RenderJson;
import util.URLUtils;

public class Ajax extends Controller {

	public static void url(String uri) {
		if (StringUtils.isEmpty(uri))
			return;
        Logger.info("uri:" + uri);
        
		String title = "";
        try {
            title = URLUtils.getTitleByURI(uri);
            Logger.info("title:" + title);
        } catch (Exception e) {
            Logger.error(e, "convUri2Title");
        }
        renderJSON(title);
	}
	
    
}
