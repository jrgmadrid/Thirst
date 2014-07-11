package punishers.thirst.shared;

import java.io.Serializable;
import java.util.Date;

@SuppressWarnings("serial")
public class UploadedImage implements Serializable {

	public static final String SERVING_URL = "servingUrl";
	public static final String CREATED_AT = "createdAt";
	public static final String OWNER_ID = "ownerId";
	public static final String WATER_FOUNTAIN_ID = "waterFountainId";

	String key;
	String servingUrl;
	Date createdAt;
	String ownerId; // Refers to the User
	Long waterFountainId;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getServingUrl() {
		return servingUrl;
	}

	public void setServingUrl(String servingUrl) {
		this.servingUrl = servingUrl;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}
	
	public long getWaterFountainId() {
		return waterFountainId;
	}
	
	public void setWaterFountainId(Long id) {
		this.waterFountainId = id;
	}

}
