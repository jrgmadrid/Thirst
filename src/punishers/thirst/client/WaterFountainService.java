package punishers.thirst.client;

import java.util.Set;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("waterfountain")
public interface WaterFountainService extends RemoteService {
	
	public void addWaterFountainToFavs(long id) throws NotLoggedInException;
	
	public void removeWaterFountainFromFavs(long id) throws NotLoggedInException;
	
	public String[] getFavWaterFountains() throws NotLoggedInException;
	
	public Set<Long> getAllIds() throws NotLoggedInException;
}
