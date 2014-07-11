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
	private int id;
	
	@Persistent 
	private boolean firstImage;
	
	@Persistent
    @Extension(vendorName="datanucleus", key="gae.unindexed", value="true")
    private String imageType;
	
	@Persistent
	private Blob image;
	
	public Photo(String name, boolean firstImg) {
		this.name = name;	
		this.firstImage = firstImg;
		this.id = hashCode();
	}
	
	public Long getKey() {
        return key.getId();
    }

    public String getName() {
        return this.name;
    }
    
    public int getId() {
    	return id;
    }
    
    public User getUser() {
    	return user;
    }

    public String getImageType() {
        return imageType;
    }
    
    public boolean isFirstImage(){
    	return firstImage;
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
    
    public void setFirstImage(boolean b) {
    	this.firstImage = b; 
    }
    
    public void setUser(User u) {
    	this.user = u;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Photo other = (Photo) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
    
}
