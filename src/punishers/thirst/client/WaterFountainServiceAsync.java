package punishers.thirst.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WaterFountainServiceAsync {
	
	void addWaterFountainToFavs(long id, AsyncCallback<Void> async);
	
	void removeWaterFountainFromFavs(int id, AsyncCallback<Void> async);
	
	public void getFavWaterFountains(AsyncCallback<String[]> async);

	public void getAllIds(AsyncCallback<Set<Integer>> callback);

	void deleteAllFountains(AsyncCallback<Void> callback);

	void getLatLon(long id, AsyncCallback<String> callback);

	void getManyLocations(Long[] ids, AsyncCallback<String[]> callback);

	void getManyLocationStrings(Long[] ids, AsyncCallback<String[]> callback);

	void getSingleLocation(long id, AsyncCallback<String> callback);

}
