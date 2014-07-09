package punishers.thirst.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WaterFountainServiceAsync {
	
	public void addWaterFountainToFavs(long id, AsyncCallback<Void> async);
	
	public void removeWaterFountainFromFavs(long id, AsyncCallback<Void> async);
	
	public void addRating(long id, int rating, AsyncCallback<Void> async);
	
	public void getAverageWaterFountatinRating(long id, AsyncCallback<Integer> callback);
	
	public void getFavWaterFountains(AsyncCallback<Long[]> asyncCallback);

//	void deleteAllFountains(AsyncCallback<Void> callback);

	public void getAllIds(AsyncCallback<Long[]> callback);
	
	public void getAllLatAndLngAndId(AsyncCallback<Double[]> callback);

<<<<<<< HEAD
	void getDatastoreSize(AsyncCallback<Integer> callback);
=======
	public void getLocationString(long id, AsyncCallback<String> callback);

	public void getMaintainerString(long id, AsyncCallback<String> callback);

	public void getNumberOfUsers(long id, AsyncCallback<Integer> callback);

	public void getLatAndLong(long id, AsyncCallback<Double[]> callback);
	
//	public void getFavWaterFountainsLatLng(AsyncCallback<Double[]> callback);
>>>>>>> master
}
