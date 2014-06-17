package punishers.thirst.client;

import punishers.thirst.server.WaterFountain;

import com.google.gwt.user.client.rpc.RemoteService;

public interface WaterFountainService extends RemoteService {
	
	public void addWaterFountainToFavs(WaterFountain wf) throws NotLoggedInException;
	
	public void removeWaterFountainFromFavs(WaterFountain wf) throws NotLoggedInException;
	
	public String[] getFavWaterFountains() throws NotLoggedInException;
	
}
