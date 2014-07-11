package punishers.thirst.client;

import java.util.List;

import punishers.thirst.shared.UploadedImage;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UserImageServiceAsync {
	
	public void getBlobstoreUploadUrl(AsyncCallback<String> callback);
	
	void get(String key, AsyncCallback<UploadedImage> callback);

	void getRecentlyUploaded(AsyncCallback<List<UploadedImage>> callback);

	void deleteImage(String key, AsyncCallback<Void> callback);

}
