package punishers.thirst.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;

public interface WaterFountainService extends RemoteService {
	
	void addWaterFountainToFavs(long id);
	
	public void removeWaterFountainFromFavs(int id) throws NotLoggedInException;
	
	public String[] getFavWaterFountains() throws NotLoggedInException;
	
	public Set<Integer> getAllIds() throws NotLoggedInException;

	void deleteAllFountains();

	String getLatLon(long id);

	String[] getManyLocations(Long[] ids);

	String[] getManyLocationStrings(Long[] ids);

	String getSingleLocation(long id);
}
