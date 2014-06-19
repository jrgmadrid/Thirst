package punishers.thirst.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WaterFountainServiceAsync {
	
	public void addWaterFountainToFavs(long id, AsyncCallback<Void> async);
	
	public void removeWaterFountainFromFavs(long id, AsyncCallback<Void> async);
	
	public void getFavWaterFountains(AsyncCallback<String[]> async);

	public void getAllIds(AsyncCallback<Long[]> callback);

}
