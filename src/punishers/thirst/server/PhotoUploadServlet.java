package punishers.thirst.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import punishers.thirst.client.NotLoggedInException;

import com.google.appengine.api.datastore.Blob;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import gwtupload.server.UploadAction;
import gwtupload.server.exceptions.UploadActionException;
import gwtupload.shared.UConsts;

public class PhotoUploadServlet extends UploadAction {

	private static final long serialVersionUID = 1L;
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");

//	Hashtable<String, String> receivedContentTypes = new Hashtable<String, String>();
//	/**
//	 * Maintain a list with received photos and their content types.
//	 */
//	Hashtable<String, File> receivedFiles = new Hashtable<String, File>();
	
	HashSet<Photo> photos = new HashSet<Photo>(); 

	/**
	 * Override executeAction to save the received photos in a custom place and
	 * delete this items from session.
	 */
	@Override
	public String executeAction(HttpServletRequest request,
			List<FileItem> sessionFiles) throws UploadActionException {
		String response = "";
		for (FileItem item : sessionFiles) {
			if (false == item.isFormField()) {
				try {
					// / Create a new file based on the remote file name in the
					// client
					String wfIdNum = request.getParameter("wfidnum");
					long idNum = Long.valueOf(wfIdNum);
					String saveName = item.getName().replaceAll("[\\\\/><\\|\\s\"'{}()\\[\\]]+", "_");
					Photo photo = new Photo(saveName, false);
					
					File file = new File(item.getFieldName());
					
					photo.setImageType(item.getContentType());
					
					byte[] fileContent = new byte[1050000];
					
					FileInputStream iS = new FileInputStream(file);
					iS.read(fileContent);
					Blob image = new Blob(fileContent);
					
					checkLoggedIn();
					PersistenceManager pm = getPersistenceManager();
					try {
						Query q = pm.newQuery(WaterFountain.class);
						q.setFilter("id == idParam");
						q.declareParameters("Long idParam");
						q.declareImports("import punishers.thirst.server.WaterFountain");
						List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(idNum);
						WaterFountain wf = wfsWithId.get(0);
						
						photo.setImage(image);
						photo.setUser(getUser());
						wf.addPhoto(photo);
						pm.makePersistent(photo);
						
					} finally {
						pm.close();
					}
					
				

					// / Create a temporary file placed in /tmp (only works in
					// unix)
					// File file = File.createTempFile("upload-", ".bin", new
					// File("/tmp"));

					// / Create a temporary file placed in the default system
					// temp folder
					// File file = File.createTempFile("upload-", ".bin");
					// item.write(file);

					// / Save a list with the received files
					//receivedFiles.put(item.getFieldName(), file);
					//receivedContentTypes.put(item.getFieldName(),
					//		item.getContentType());

					// / Send a customized message to the client.
					response += "You're photo has been saved";

				} catch (Exception e) {
					throw new UploadActionException(e);
				}
			}
		}

		// / Remove files from session because we have a copy of them
		removeSessionFileItems(request);

		// / Send your customized message to the client.
		return response;
	}

//	/**
//	 * Get the content of an uploaded file.
//	 */
//	@Override
//	public void getUploadedFile(HttpServletRequest request,
//			HttpServletResponse response) throws IOException {
//		String fieldName = request.getParameter(UConsts.PARAM_SHOW);
//		
//		checkLoggedIn();
//		PersistenceManager pm = getPersistenceManager();
//		try {
//			Query q = pm.newQuery(arg0)
//		} finally {
//			pm.close();
//		}
//		
////		File f = receivedFiles.get(fieldName);
//		if (f != null) {
////			response.setContentType(receivedContentTypes.get(fieldName));
////			FileInputStream is = new FileInputStream(f);
////			copyFromInputStreamToOutputStream(is, response.getOutputStream());
//		} else {
//			renderXmlResponse(request, response, XML_ERROR_ITEM_NOT_FOUND);
//		}
//	}

	/**
	 * Remove a file when the user sends a delete request.
	 */
	@Override
	public void removeItem(HttpServletRequest request, String fieldName)
			throws UploadActionException {
		Photo photo = new Photo(fieldName, false);
		try {
			checkLoggedIn();
		} catch (NotLoggedInException e) {
			e.printStackTrace();
		}
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Photo> photos = new ArrayList<Photo>();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfs = (List<WaterFountain>) q.execute();
			for (WaterFountain wf : wfs) {
				HashSet<Photo> ps = (HashSet<Photo>) wf.getPhotos();
				for (Photo p : ps) {
					if (p.getName() == fieldName) {
						photos.add(p);
					}
				}
			}
			if (photos.size() == 1) {
				Photo p = photos.get(0);
				pm.deletePersistent(p);
			}		
		} finally {
			pm.close();
		}
		
//		File file = receivedFiles.get(fieldName);
//		receivedFiles.remove(fieldName);
//		receivedContentTypes.remove(fieldName);
//		if (file != null) {
//			file.delete();
//		}
	}
	
	private void checkLoggedIn() throws NotLoggedInException {
		if (getUser() == null) {
		    throw new NotLoggedInException("Not logged in.");
		}
	}
	
	private User getUser() {
		  UserService userService = UserServiceFactory.getUserService();
		  return userService.getCurrentUser();
	}
	
	private PersistenceManager getPersistenceManager() {
		  return PMF.getPersistenceManager();
	}
}
