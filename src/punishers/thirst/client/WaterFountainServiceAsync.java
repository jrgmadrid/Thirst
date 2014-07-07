package punishers.thirst.client;


import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WaterFountainServiceAsync {
	
<<<<<<< HEAD
	void addWaterFountainToFavs(long id, AsyncCallback<Void> async);
	
	void removeWaterFountainFromFavs(int id, AsyncCallback<Void> async);
=======
	public void addWaterFountainToFavs(long id, AsyncCallback<Void> async);
	
	public void removeWaterFountainFromFavs(long id, AsyncCallback<Void> async);
>>>>>>> master
	
	public void addRating(long id, int rating, AsyncCallback<Void> async);
	
	public void getAverageWaterFountatinRating(long id, AsyncCallback<Integer> callback);
	
	public void getFavWaterFountains(AsyncCallback<Long[]> asyncCallback);

<<<<<<< HEAD
	void deleteAllFountains(AsyncCallback<Void> callback);

	void getLatLon(long id, AsyncCallback<String> callback);

	void getManyLocations(Long[] ids, AsyncCallback<String[]> callback);

	void getManyLocationStrings(Long[] ids, AsyncCallback<String[]> callback);

	void getSingleLocation(long id, AsyncCallback<String> callback);

=======
	public void getAllIds(AsyncCallback<Long[]> callback);
	
	public void getAllLatAndLngAndId(AsyncCallback<Double[]> callback);
>>>>>>> master
}
