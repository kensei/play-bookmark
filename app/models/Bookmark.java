package models;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
 
import play.data.binding.As;
import play.data.validation.MaxSize;
import play.data.validation.Required;
import play.data.validation.URL;
import play.db.jpa.*;

@Entity
public class Bookmark extends Model {

	@Required
	@As("yyyy-MM-dd")
    public Date markedAt;
	
	@Required
    @MaxSize(2000)
    @URL
    public String url;
	@Required
    public String title;
	
    public String comment;

    @Required
    @ManyToOne
    public User author;
    
    @ManyToMany(cascade=CascadeType.PERSIST)
    public Set<Tag> tags;
    
    public Bookmark(User author, String url, String title, String comment) {
        this.author = author;
        this.url = url;
        this.title = title;
        this.comment = comment;
        this.tags = new TreeSet<Tag>();
        
        this.markedAt = new Date();
    }
    
    public String hasTag(Tag tag) {
    	return "";
    }
    
    public Bookmark tagItWith(String name) {
        tags.add(Tag.findOrCreateByName(name));
        return this;
    }
    
    public static List<Bookmark> findTaggedWith(String tag) {
        return Bookmark.find(
            "select distinct b from Bookmark b join b.tags as t where t.name = ?", tag
        ).fetch();
    }
    
    public static List<Bookmark> findTaggedWith(String... tags) {
        return Bookmark.find(
            "select distinct b from Bookmark b join b.tags as t " +
            "where t.name in (:tags) group by b.id, b.author, b.url, b.title, " +
            "b.comment, b.markedAt having count(t.id) = :size"
        ).bind("tags", tags).bind("size", tags.length).fetch();
    }
}
