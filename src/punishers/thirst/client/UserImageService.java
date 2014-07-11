package punishers.thirst.client;

import java.util.List;

import punishers.thirst.shared.UploadedImage;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("images")
public interface UserImageService extends RemoteService{

	public String getBlobstoreUploadUrl();
	
	public UploadedImage get(String key);
	
	public List<UploadedImage> getRecentlyUploaded();
	
	public void deleteImage(String key);
	
}