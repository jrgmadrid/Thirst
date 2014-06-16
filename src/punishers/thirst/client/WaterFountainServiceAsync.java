package punishers.thirst.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WaterFountainServiceAsync {
	
	public void addWaterFountainToFavs(int id, AsyncCallback<Void> async);
	
	public void removeWaterFountainFromFavs(int id, AsyncCallback<Void> async);
	
	public void getFavWaterFountains(AsyncCallback<String[]> async);

}
