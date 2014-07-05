package punishers.thirst.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface WaterFountainServiceAsync {
	
	public void addWaterFountainToFavs(long id, AsyncCallback<Void> async);
	
	public void removeWaterFountainFromFavs(long id, AsyncCallback<Void> async);
	
	public void getFavWaterFountains(AsyncCallback<Long[]> asyncCallback);

	public void getAllIds(AsyncCallback<Long[]> callback);
	
	public void getAllLatLng(AsyncCallback<LatLng[]> callback);

}
