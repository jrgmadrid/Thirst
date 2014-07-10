package punishers.thirst.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import punishers.thirst.client.NotLoggedInException;
import punishers.thirst.client.WaterFountainService;

public class WaterFountainServiceImpl extends RemoteServiceServlet implements
		WaterFountainService {

	private static final PersistenceManagerFactory PMF = JDOHelper
			.getPersistenceManagerFactory("transactions-optional");
	private static final Logger LOG = Logger
			.getLogger(WaterFountainServiceImpl.class.getName());

	public void addWaterFountainToFavs(long id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			wf.addUser(getUser());
		} finally {
			pm.close();
		}
	}

	public void removeWaterFountainFromFavs(long id)
			throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			Set<User> users = wf.getUsers();
			for (User user : users) {
				if (user.equals(getUser())) {
					users.remove(getUser());
				}
			}
		} finally {
			pm.close();
		}
	}

	public void addRating(long id, int rating) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			Set<Integer> fountainRating = wf.getRatings();
			fountainRating.add(rating);
		} finally {
			pm.close();
		}
	}

	public String getLocationString(long id) throws NotLoggedInException {
		checkLoggedIn();
		String result = "";
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			result = wf.getLocation();
		} finally {
			pm.close();
		}
		return result;
	}

	public String getMaintainerString(long id) throws NotLoggedInException {
		checkLoggedIn();
		String result = "";
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			result = wf.getMaintainer();
		} finally {
			pm.close();
		}
		return result;
	}

	public int getNumberOfUsers(long id) throws NotLoggedInException {
		checkLoggedIn();
		int result = 0;
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			result = wf.getNumberOfUsers();
		} finally {
			pm.close();
		}
		return result;
	}

	public Double[] getLatAndLong(long id) throws NotLoggedInException {
		checkLoggedIn();
		ArrayList<Double> results = null;
		int size = 0;
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			results = new ArrayList<Double>();
			double lat = wf.getLatitude();
			double lng = wf.getLongitude();
			results.add(lat);
			results.add(lng);
		} finally {
			pm.close();
		}
		return (Double[]) results.toArray(new Double[2]);
	}

	public int getAverageWaterFountatinRating(long id)
			throws NotLoggedInException {
		checkLoggedIn();
		int averageRating = 0;
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			averageRating = wf.getAverageWaterFountainRating();
		} finally {
			pm.close();
		}
		return averageRating;
	}

	public Long[] getFavWaterFountains() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<Long> results = new ArrayList<Long>();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> waterFountains = (List<WaterFountain>) q
					.execute();
			for (WaterFountain wf : waterFountains) {
				Set<User> people = wf.getUsers();
				if (people.contains(getUser())) {
					results.add(wf.getId());
				}
			}
		} finally {
			pm.close();
		}
		return (Long[]) results.toArray(new Long[results.size()]);
	}

	// public Double[] getFavWaterFountainsLatLng() throws NotLoggedInException
	// {
	// checkLoggedIn();
	// PersistenceManager pm = getPersistenceManager();
	// List<Double> results = new ArrayList<Double>();
	// try {
	// Query q = pm.newQuery(WaterFountain.class);
	// q.declareImports("import punishers.thirst.server.WaterFountain");
	// List<WaterFountain> waterFountains = (List<WaterFountain>) q.execute();
	// for(WaterFountain wf : waterFountains) {
	// Set<User> people = wf.getUsers();
	// if(people.contains(getUser())) {
	// double lat = wf.getLatitude();
	// double lng = wf.getLongitude();
	// results.add(lat);
	// results.add(lng);
	// }
	// }
	// } finally {
	// pm.close();
	// }
	// return (Double[]) results.toArray(new Double[results.size()]);
	// }

	public Long[] getAllIds() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Long> results = null;
		int size = 0;
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfs = (List<WaterFountain>) q.execute();
			if (wfs != null) {
				size = wfs.size();
				results = new ArrayList<Long>();
				for (WaterFountain wf : wfs) {
					results.add(wf.getId());
				}
			} else {
				System.out.println("There are no ids to be found.");
			}

		} finally {
			pm.close();
		}
		return (Long[]) results.toArray(new Long[size]);
	}

	public Double[] getAllLatAndLngAndId() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Double> results = null;
		int size = 0;
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfs = (List<WaterFountain>) q.execute();
			if (wfs != null) {
				size = wfs.size();
				results = new ArrayList<Double>();
				for (WaterFountain wf : wfs) {
					double lat = wf.getLatitude();
					double lng = wf.getLongitude();
					long longId = wf.getId();
					double doubleId = (double) longId;
					results.add(lat);
					results.add(lng);
					results.add(doubleId);
				}
			} else {
				System.out.println("There are no ids to be found.");
			}

		} finally {
			pm.close();
		}
		return (Double[]) results.toArray(new Double[size]);
	}
	
	public byte[][] getUserImageUploads() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<byte[]> byteArray = new ArrayList<byte[]>();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfs = (List<WaterFountain>) q.execute();
			Set<Photo> photos = new HashSet<Photo>();
			for (WaterFountain wf : wfs) {
				Set<Photo> ps = wf.getPhotos();
				for (Photo p : ps) {
					if (p.getUser() == getUser()) {
						photos.add(p);
					}
				}
			}
			for (Photo p : photos) {
				byte[] bytes = p.getImage().getBytes();
				byteArray.add(bytes);
			}
		} finally {
			pm.close();
		}
		byte[][] arrayOfImages = new byte[byteArray.size()][1050000];
		int i = 0;
		for (byte[] bytes : byteArray) {
			arrayOfImages[i] = bytes;
			i++;
		}
		return arrayOfImages;
	}
	
	public byte[][] getFountainImages(long id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<byte[]> byteArray = new ArrayList<byte[]>();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			Set<Photo> photos = new HashSet<Photo>();
			for (Photo p : wf.getPhotos()) {
				photos.add(p);
			}
			for (Photo p : photos) {
				byte[] bytes = p.getImage().getBytes();
				byteArray.add(bytes);
			}
		} finally {
			pm.close();
		}
		byte[][] arrayOfImages = new byte[byteArray.size()][1050000];
		int i = 0;
		for (byte[] bytes : byteArray) {
			arrayOfImages[i] = bytes;
			i++;
		}
		return arrayOfImages;
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




//	 public void deleteAllFountains() throws NotLoggedInException {
//	 checkLoggedIn();
//	 PersistenceManager pm = getPersistenceManager();
//	 List<Long> results = new ArrayList<Long>();
//	 try {
//	 Query q = pm.newQuery(WaterFountain.class);
//	 q.declareImports("import punishers.thirst.server.WaterFountain");
//	 List<WaterFountain> waterFountains = (List<WaterFountain>) q.execute();
//	 for(WaterFountain wf : waterFountains) {
//	 Set<User> people = wf.getUsers();
//	 if(people.contains(getUser())) {
//	 results.add(wf.getId());
//	 }
//	 }
//	 } finally {
//	 pm.close();
//	 }
//	 return (Long[]) results.toArray(new Long[results.size()]);
//	 }
//
//	@Override
//	public int getDatastoreSize() throws NotLoggedInException {
//		checkLoggedIn();
//		PersistenceManager pm = getPersistenceManager();
//		int size = 0;
//		try {
//			Query q = pm.newQuery(WaterFountain.class);
//			q.declareImports("import punishers.thirst.server.WaterFountain");
//			List<WaterFountain> wfs = (List<WaterFountain>) q.execute();
//			if (wfs == null)
//				size = 0;
//			if (wfs != null) {
//				size = wfs.size();
//			}
//		} finally {
//			pm.close();
//		}
//		return size;
//	}


}
