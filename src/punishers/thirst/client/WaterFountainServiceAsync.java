package punishers.thirst.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WaterFountainServiceAsync {
	
	public void addWaterFountain(int id, AsyncCallback<Void> async);
	
	public void removeWaterFountain(int id, AsyncCallback<Void> async);
	
	public void getWaterFountains(AsyncCallback<String[]> async);

}
