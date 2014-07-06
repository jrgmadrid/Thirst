package punishers.thirst.client;

import com.google.gwt.maps.client.geom.LatLng;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("waterfountain")
public interface WaterFountainService extends RemoteService {
	
	public void addWaterFountainToFavs(long id) throws NotLoggedInException;
	
	public void removeWaterFountainFromFavs(long id) throws NotLoggedInException;
	
	public void addRating(long id, int rating) throws NotLoggedInException;
	
	public int getAverageWaterFountatinRating(long id) throws NotLoggedInException;
	
	public Long[] getFavWaterFountains() throws NotLoggedInException;
	
	public Long[] getAllIds() throws NotLoggedInException;
	
	public Double[] getAllLatAndLng() throws NotLoggedInException;
}
