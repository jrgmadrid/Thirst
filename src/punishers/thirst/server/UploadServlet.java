package punishers.thirst.server;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import punishers.thirst.shared.UploadedImage;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class UploadServlet extends HttpServlet {
	
	 private static final Logger log = Logger.getLogger(UploadServlet.class.getName());
	 
	    private BlobstoreService blobstoreService = BlobstoreServiceFactory
	            .getBlobstoreService();
	 
	    public void doPost(HttpServletRequest req, HttpServletResponse res)
	            throws ServletException, IOException {
	    	long id = 0;
	        Map blobs = blobstoreService.getUploadedBlobs(req);
	        BlobKey blobKey = (BlobKey) blobs.get("image");
	 
	        if (blobKey == null) {
	        	
	        } else {
	 
	            ImagesService imagesService = ImagesServiceFactory
	                    .getImagesService();
	            
	            UserService userService = UserServiceFactory.getUserService();
	        	User user = userService.getCurrentUser();
	        	
	            // Get the image serving URL
	            String imageUrl = imagesService.getServingUrl(blobKey);
	 
	            // For the sake of clarity, we'll use low-level entities
	            Entity uploadedImage = new Entity("UploadedImage");
	            uploadedImage.setProperty("blobKey", blobKey);
	            uploadedImage.setProperty(UploadedImage.CREATED_AT, new Date());
	            uploadedImage.setProperty(UploadedImage.OWNER_ID, user.getUserId());
	            uploadedImage.setProperty(UploadedImage.WATER_FOUNTAIN_ID, id);
	 
	            // Highly unlikely we'll ever filter on this property
	            uploadedImage.setUnindexedProperty(UploadedImage.SERVING_URL,
	                    imageUrl);
	 
	            DatastoreService datastore = DatastoreServiceFactory
	                    .getDatastoreService();
	            datastore.put(uploadedImage);
	 
	            res.sendRedirect("/upload?imageUrl=" + imageUrl);
	        }
	    }
	 
	    @Override
	    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
	            throws ServletException, IOException {
	 
	        String imageUrl = req.getParameter("imageUrl");
	        resp.setHeader("Content-Type", "text/html");
	 
	        resp.getWriter().println(imageUrl);
	 
	    }
	}

