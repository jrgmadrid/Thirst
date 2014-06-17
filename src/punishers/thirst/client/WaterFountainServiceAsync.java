package punishers.thirst.client;

import java.util.Set;

import punishers.thirst.server.WaterFountain;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WaterFountainServiceAsync {
	
	public void addWaterFountainToFavs(WaterFountain wf, AsyncCallback<Void> async);
	
	public void removeWaterFountainFromFavs(WaterFountain wf, AsyncCallback<Void> async);
	
	// think about/change the parameter for this method?
	public void getFavWaterFountains(AsyncCallback<String[]> async);

	public void getWaterFountain(int id, AsyncCallback<WaterFountain> async);

}
