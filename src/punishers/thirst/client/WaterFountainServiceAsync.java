package punishers.thirst.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WaterFountainServiceAsync {
	
	public void addWaterFountainToFavs(long id, AsyncCallback<Void> async);
	
	public void removeWaterFountainFromFavs(long id, AsyncCallback<Void> async);
	
	public void getFavWaterFountains(AsyncCallback<Long[]> asyncCallback);

	public void getAllIds(AsyncCallback<Long[]> callback);

	void getSingleLocation(long id, AsyncCallback<String> callback);

	void getManyLocationStrings(Long[] ids, AsyncCallback<String[]> callback);

	void getLatLon(long id, AsyncCallback<String> callback);

	void getManyLocations(Long[] ids, AsyncCallback<String[]> callback);

}
