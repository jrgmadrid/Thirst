package punishers.thirst.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface WaterFountainService extends RemoteService {
	
	public void addWaterFountainToFavs(int id) throws NotLoggedInException;
	
	public void removeWaterFountainFromFavs(int id) throws NotLoggedInException;
	
	public String[] getFavWaterFountains() throws NotLoggedInException;
	
}
