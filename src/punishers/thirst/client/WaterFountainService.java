package punishers.thirst.client;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("waterfountain")
public interface WaterFountainService extends RemoteService {

	public void addWaterFountainToFavs(long id) throws NotLoggedInException;

	public void removeWaterFountainFromFavs(long id) throws NotLoggedInException;

	public void addRating(long id, int rating) throws NotLoggedInException;

	public int getAverageWaterFountatinRating(long id) throws NotLoggedInException;

	public Long[] getFavWaterFountains() throws NotLoggedInException;

	public Long[] getAllIds() throws NotLoggedInException;

	public Double[] getAllLatAndLngAndId() throws NotLoggedInException;

	public String getLocationString(long id) throws NotLoggedInException;

	public String getMaintainerString(long id) throws NotLoggedInException;

	public int getNumberOfUsers(long id) throws NotLoggedInException;

	public Double[] getLatAndLong(long id) throws NotLoggedInException;

//	public Double[] getFavWaterFountainsLatLng() throws NotLoggedInException;
	
	public byte[][] getUserImageUploads() throws NotLoggedInException; 
	
	public byte[][] getFountainImages(long id) throws NotLoggedInException;
}