package punishers.thirst.client;

import com.google.gwt.maps.client.geom.LatLng;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("waterfountain")
public interface WaterFountainService extends RemoteService {
	
<<<<<<< HEAD
	void addWaterFountainToFavs(long id);
=======
	public void addWaterFountainToFavs(long id) throws NotLoggedInException;
	
	public void removeWaterFountainFromFavs(long id) throws NotLoggedInException;
	
	public void addRating(long id, int rating) throws NotLoggedInException;
	
	public int getAverageWaterFountatinRating(long id) throws NotLoggedInException;
>>>>>>> master
	
	public Long[] getFavWaterFountains() throws NotLoggedInException;
	
	public Long[] getAllIds() throws NotLoggedInException;
	
<<<<<<< HEAD
	public Set<Integer> getAllIds() throws NotLoggedInException;

	void deleteAllFountains();

	String getLatLon(long id);

	String[] getManyLocations(Long[] ids);

	String[] getManyLocationStrings(Long[] ids);

	String getSingleLocation(long id);
=======
	public Double[] getAllLatAndLngAndId() throws NotLoggedInException;
>>>>>>> master
}
