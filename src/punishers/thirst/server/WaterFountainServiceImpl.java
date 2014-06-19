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
//			Query q = pm.newQuery("select from WaterFountain " + "where id == idParam " + "parameters Long idParam");
			Query q = pm.newQuery("select users " + "from " + WaterFountain.class.getName() + 
					"where id == idParam " + "parameters Long idParam");
			WaterFountain wf = (WaterFountain) q.execute(id);
			wf.addUser(getUser());
		} finally {
			pm.close();
		}
	}

	public void removeWaterFountainFromFavs(long id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
//			Query q = pm.newQuery("select from WaterFountain " + "where id == idParam " + "parameters Long idParam");
			Query q = pm.newQuery("select users " + "from " + WaterFountain.class.getName() + 
					"where id == idParam " + "parameters Long idParam");
			WaterFountain wf = (WaterFountain) q.execute(id);
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

	public String[] getFavWaterFountains() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		List<String> results = new ArrayList<String>();
		try {
			Query q = pm.newQuery("select WaterFountain " + "from " + WaterFountain.class.getName());
			List<WaterFountain> waterFountains = (List<WaterFountain>) q.execute();
			for(WaterFountain wf : waterFountains) {
				for (User user : wf.getUsers()) {
					if(user == getUser()) {
						results.add(wf.getLocation());
					}
				}
			}
		} finally {
			pm.close();
		}
		return (String[]) results.toArray(new String[0]);
	}
	
	public Long[] getAllIds() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		ArrayList<Long> results = null;
		try {
			Query q = pm.newQuery("select WaterFountain " + "from " + WaterFountain.class.getName());
			List<WaterFountain> wfs = (List<WaterFountain>) q.execute();
			if(!wfs.isEmpty()){
				for(WaterFountain wf : wfs) {
					results = new ArrayList<Long>();
					results.add(wf.getId());
				}
			} else {
				System.out.println("No Results.");
			}
			return (Long[]) results.toArray();
		} finally {
			pm.close();
		}
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
