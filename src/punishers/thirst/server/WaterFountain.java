package punishers.thirst.server;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.users.User;

@PersistenceCapable
public class WaterFountain {
	@Persistent
	private double lat;
	
	@Persistent
	private double lon;
	
	@Persistent
	private String location;
	
	@Persistent
	private String maintainer;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private long id;
	
	@Persistent
	private Set<User> users;
	
	/*
	 * Constructor for the Water Fountain for all of its information
	 * Takes latitude, longitude, location, and maintainer as parameters
	 * Id is also a field that belongs with a water fountain
	 */
	public WaterFountain(Double lat, Double lon, String location, String maintainer){
		this.lat = lat;
		this.lon = lon;
		this.location = location;
		this.maintainer = maintainer;
		this.users = new HashSet<User>();
	}
	
	/*
	 * Sets the Id of the water fountain (done during the parsing process
	 */
	public void setId() {
		long temp = Long.valueOf(hashCode());
		if (temp < 0)
			this.id = temp * -1;
		else
			this.id = temp;
	}
	
	/*
	 * Adds a user to the set of users associated with the water fountain
	 */
	public void addUser(User u) {
		users.add(u);
	}
	
	
	/*
	 * Gets the set of users
	 */
	public Set<User> getUsers() {
		return this.users;
	}
	
	/*
	 * Gets the id value for the water fountain
	 */
	public long getId() {
		return this.id;
	}
	
	/*
	 * Gets the latitude value of the water fountain
	 */
	public double getLatitude() {
		return this.lat;
	}
	
	/*
	 * Gets the longitude of the water fountain
	 */
	public double getLongitude() {
		return this.lon;
	}
	
	/*
	 * Gets the location of the water fountain
	 */
	public String getLocation() {
		return this.location;
	}
	
	/*
	 * Gets the maintainer of the water fountain
	 */
	public String getMaintainer() {
		return this.maintainer;
	}
	
	/*
	 * Hashcode in case someone needs to find a specific water fountain in a collection 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lat);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(lon);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/*
	 * In case someone needs to find an instance of the water fountain
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WaterFountain other = (WaterFountain) obj;
		if (Double.doubleToLongBits(lat) != Double.doubleToLongBits(other.lat))
			return false;
		if (Double.doubleToLongBits(lon) != Double.doubleToLongBits(other.lon))
			return false;
		return true;
	}
	
	
}
