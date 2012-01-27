package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import org.cyberneko.html.parsers.DOMParser;
import org.w3c.dom.html.HTMLDocument;
import org.xml.sax.InputSource;

import org.h2.util.MathUtils;

import models.Bookmark;
import play.Logger;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

public class Application extends Controller {

    private static final int LIST_CNT = 10;
    
    public static void index() {
    	Logger.info("sessionAppli:" + session.get("name"));
        int current = 1;
        int total = getTotalPage();
        List<Bookmark> frontBookmarks = getFrontData(current);
        render(frontBookmarks, total, current);
    }

    public static void index(int page) {
    	Logger.info("sessionAppli:" + session.get("name"));
        int current = page;
        int total = getTotalPage();
        List<Bookmark> frontBookmarks = getFrontData(current);
        
        render(frontBookmarks, current, total);
    }

    public static void listTagged(String tag) {
    	int current = 1;
        int total = 1;
        List<Bookmark> frontBookmarks = Bookmark.findTaggedWith(tag);
        Logger.info("count:" + frontBookmarks.size());
        render("@index", tag, frontBookmarks, current, total);
    }
    
    private static List<Bookmark> getFrontData(int page) {
        int position = ((page > 0) ? (page - 1) * LIST_CNT : 0);
        return Bookmark.find("order by markedAt desc").from(position).fetch(LIST_CNT);
    }
    
    private static int getTotalPage() {
        int total = (int) Math.ceil(Bookmark.count() / LIST_CNT);
        return (total > 0) ? total : 1;
    }
    
    @Before
    public static void addDefaults() {
        renderArgs.put("bookmark_owner", Play.configuration.getProperty("appli.owner"));
    }
}