package controllers;
 
import java.util.Date;
import java.util.List;

import models.Bookmark;
import models.Tag;
import models.User;
import play.Logger;
import play.mvc.After;
import play.mvc.Before;
import play.mvc.Controller;
import play.mvc.With;
import util.URLUtils;
 
@With(Secure.class)
public class Admin extends Controller {
    
	@Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user.fullname);
        }
    }
    
	public static void index() {
		Application.index();
	}
	
    public static void putBookmark() {
    	Date entrydate = new Date();
    	List<Tag> tags = Tag.findAll();
    	render("@bookmark", entrydate, tags);
    }
     
    public static void setBookmark(Long id) {
    	if(id != null) {
            Bookmark bookmark = Bookmark.findById(id);
            List<Tag> tags = Tag.findAll();
            render("@bookmark", bookmark, tags);
        }
        Application.index();
    }
    
    public static void delBookmark(Long id) {
    	if(id != null) {
            Bookmark bookmark = Bookmark.findById(id);
            List<Tag> tags = Tag.findAll();
            Boolean delflg = true;
            render("@bookmark", bookmark, tags, delflg);
    	}
    	Application.index();
    }
    
    public static void delete(Long id) {
    	Bookmark bookmark = Bookmark.findById(id);
    	bookmark.delete();
        Application.index();
    }
    
    public static void save(String url, String title, String comment, String tags) {
    	// Create Bookmark
    	User author = User.find("byEmail", Security.connected()).first();
    	Bookmark bookmark = new Bookmark(author, url, title, comment);
    	
    	// Set tags list
	    if (tags != null) {
	        for(String tag : tags.split("\\s+")) {
	            if(tag.trim().length() > 0) {
	            	bookmark.tags.add(Tag.findOrCreateByName(tag));
	            }
	        }
	    }
        
	    // Add Title
	    if ("".equals(bookmark.title)) {
	    	try {
				bookmark.title = URLUtils.getTitleByURI(url);
			} catch (Exception e) {
				Logger.error(e, "");
			}
	    }
	    
    	// Validate
        validation.valid(bookmark);
        if(validation.hasErrors()) {
            render("@bookmark", bookmark);
        }
        
        // Save
        bookmark.save();
        Application.index();
    }
}