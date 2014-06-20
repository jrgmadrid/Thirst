package punishers.thirst.server;

import java.util.ArrayList;
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

public class WaterFountainServiceImpl extends RemoteServiceServlet implements WaterFountainService {
	
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	private static final Logger LOG = Logger.getLogger(WaterFountainServiceImpl.class.getName());
	
	
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
	
	public String[] getManyLocations(Long[] ids) throws NotLoggedInException {
		String[] locations = new String[ids.length];
		for (int i = 0; i < ids.length; i++)
		{
			locations[i] = getLatLon(ids[i]);
		}
		return locations;
	}
	
	public String getLatLon(long id) throws NotLoggedInException {
		checkLoggedIn();
		double lat = 0;
		double lon = 0;
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.setFilter("id == idParam");
			q.declareParameters("Long idParam");
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfsWithId = (List<WaterFountain>) q.execute(id);
			WaterFountain wf = wfsWithId.get(0);
			lat = wf.getLatitude();
			lon = wf.getLongitude();
			
		} finally {
			pm.close();
		}
		return String.valueOf(lat) + "," + String.valueOf(lon);
	}

	public void removeWaterFountainFromFavs(long id) throws NotLoggedInException {
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
			for(User user : users) {
				if(user.equals(getUser())){
					users.remove(getUser());
				}
			}
		} finally {
			pm.close();
		}
	}

	public Long[] getFavWaterFountains() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<Long> results = new ArrayList<Long>();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> waterFountains = (List<WaterFountain>) q.execute();
			for(WaterFountain wf : waterFountains) {
				Set<User> people = wf.getUsers();
				if(people.contains(getUser())) {
					results.add(wf.getId());
				}
			}
		} finally {
			pm.close();
		}
		return (Long[]) results.toArray(new Long[results.size()]);
	}
	
	public Long[] getAllIds() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Long> results = null;
		int size = 0;
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.declareImports("import punishers.thirst.server.WaterFountain");
			List<WaterFountain> wfs = (List<WaterFountain>) q.execute();
			System.out.println("The query has executed.");
			if(wfs != null){
				System.out.println("WFS is not null.");
				System.out.println(wfs.size());
				size = wfs.size();
				results = new ArrayList<Long>();
				for(WaterFountain wf : wfs) {
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
