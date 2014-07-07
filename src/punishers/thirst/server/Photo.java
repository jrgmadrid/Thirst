package punishers.thirst.server;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable
public class Photo {
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Key key;
	
	@Persistent
	private User user;
	
	@Persistent
	private String name;
	
	@Persistent
    @Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
    private String imageType;
	
	@Persistent
	private Blob image;
	
	public Photo(String name) {
		this.name = name;		
	}
	
	public Long getId() {
        return key.getId();
    }

    public String getName() {
        return this.name;
    }

    public String getImageType() {
        return imageType;
    }
    
    
//    public byte[] getImage() {
//        if (image == null) {
//            return null;
//        }
//        return image.getBytes();
//    }
    
    public Blob getImage() {
    	return image;
    }
    
    public void rename(String name) {
        this.name = name;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

//    public void setImage(byte[] bytes) {
//        this.image = new Blob(bytes);
//    }
    
    public void setImage(Blob image) {
    	this.image = image;
    }
}
