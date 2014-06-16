package punishers.thirst.client;

import com.google.gwt.user.client.rpc.RemoteService;

public interface WaterFountainService extends RemoteService {
	
	public void addWaterFountain(int id) throws NotLoggedInException;
	
	public void removeWaterFountain(int id) throws NotLoggedInException;
	
	public String[] getWaterFountains() throws NotLoggedInException;
	
}
