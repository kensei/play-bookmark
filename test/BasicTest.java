import java.util.List;
import java.util.Map;

import models.Bookmark;
import models.Tag;
import models.User;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;
import play.test.UnitTest;

public class BasicTest extends UnitTest {

	@Before
    public void setup() {
        Fixtures.deleteAllModels();
    }
	
	@Test
	public void createAndRetrieveUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "secret", "Bob").save();
	    
	    // Retrieve the user with e-mail address bob@gmail.com
	    User bob = User.find("byEmail", "bob@gmail.com").first();
	    
	    // Test 
	    assertNotNull(bob);
	    assertEquals("Bob", bob.fullname);
	}
	
	@Test
	public void tryConnectAsUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "secret", "Bob").save();
	    
	    // Test 
	    assertNotNull(User.connect("bob@gmail.com", "secret"));
	    assertNull(User.connect("bob@gmail.com", "badpassword"));
	    assertNull(User.connect("tom@gmail.com", "secret"));
	}

	@Test
	public void createBookmark() {
	    // Create a new user and save it
	    User bob = new User("bob@gmail.com", "secret", "Bob").save();
	    
	    // Create a new Bookmark
	    new Bookmark(bob, "http://playdocja.appspot.com/documentation/1.2.3/home", "playdoc", "comment").save();
	    
	    // Test that the Bookmark has been created
	    assertEquals(1, Bookmark.count());
	    
	    // Retrieve all Bookmarks created by Bob
	    List<Bookmark> bobBookmarks = Bookmark.find("byAuthor", bob).fetch();
	    
	    // Tests
	    assertEquals(1, bobBookmarks.size());
	    Bookmark firstBookmark = bobBookmarks.get(0);
	    assertNotNull(firstBookmark);
	    assertEquals(bob, firstBookmark.author);
	    assertEquals("http://playdocja.appspot.com/documentation/1.2.3/home", firstBookmark.url);
	    assertEquals("playdoc", firstBookmark.title);
	    assertEquals("comment", firstBookmark.comment);
	    assertNotNull(firstBookmark.markedAt);
	}
	
	@Test
	public void testTags() {
	    // Create a new user and save it
	    User bob = new User("bob@gmail.com", "secret", "Bob").save();
	 
	    // Create a new post
	    Bookmark bobBookmark = new Bookmark(bob, "http://playdocja.appspot.com/documentation/1.2.3/home", "playdoc", "comment").save();
	    Bookmark anotherBobBookmark = new Bookmark(bob, "http://playdocja.appspot.com/documentation/1.2.3/home", "playdoc", "comment").save();
	    
	    // Well
	    assertEquals(0, Bookmark.findTaggedWith("Red").size());
	    
	    // Tag it now
	    bobBookmark.tagItWith("Red").tagItWith("Blue").save();
	    anotherBobBookmark.tagItWith("Red").tagItWith("Green").save();
	    
	    // Check
	    assertEquals(2, Bookmark.findTaggedWith("Red").size());
	    assertEquals(1, Bookmark.findTaggedWith("Blue").size());
	    assertEquals(1, Bookmark.findTaggedWith("Green").size());
	    assertEquals(1, Bookmark.findTaggedWith("Red", "Blue").size());   
	    assertEquals(1, Bookmark.findTaggedWith("Red", "Green").size());   
	    assertEquals(0, Bookmark.findTaggedWith("Red", "Green", "Blue").size());  
	    assertEquals(0, Bookmark.findTaggedWith("Green", "Blue").size());
	    
	    List<Map> cloud = Tag.getCloud();
	    assertEquals(
	        "[{tag=Blue, pound=1}, {tag=Green, pound=1}, {tag=Red, pound=2}]", 
	        cloud.toString()
	    );
	}
	
	@Test
	public void fullTest() {
	    Fixtures.loadModels("data.yml");
	 
	    // Count things
	    assertEquals(2, User.count());
	    assertEquals(3, Bookmark.count());
	 
	    // Try to connect as users
	    assertNotNull(User.connect("bob@gmail.com", "secret"));
	    assertNotNull(User.connect("jeff@gmail.com", "secret"));
	    assertNull(User.connect("jeff@gmail.com", "badpassword"));
	    assertNull(User.connect("tom@gmail.com", "secret"));
	 
	    // Find all of Bob's Bookmarks
	    List<Bookmark> bobBookmarks = Bookmark.find("author.email", "bob@gmail.com").fetch();
	    assertEquals(2, bobBookmarks.size());
	 
	    // Find the most recent Bookmark
	    Bookmark frontBookmark = Bookmark.find("order by MarkedAt desc").first();
	    assertNotNull(frontBookmark);
	    assertEquals("http://playdocja.appspot.com/documentation/1.2.3/home", frontBookmark.url);
	}
}
