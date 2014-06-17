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
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import punishers.thirst.client.NotLoggedInException;
import punishers.thirst.client.WaterFountainService;

public class WaterFountainServiceImpl extends RemoteServiceServlet implements WaterFountainService {
	
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	private static final Logger LOG = Logger.getLogger(WaterFountainServiceImpl.class.getName());
	
	
	public void addWaterFountainToFavs(WaterFountain wf) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			wf.addUser(getUser());
		} finally {
			pm.close();
		}
	}

	public void removeWaterFountainFromFavs(WaterFountain wf) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			Query q = pm.newQuery(WaterFountain.class, "user == u");
			q.declareParameters("com.google.appengine.api.users.User u");
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
	
	@Override
	public WaterFountain getWaterFountain(int id) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			WaterFountain wf = pm.getObjectById(WaterFountain.class, id);
			return wf;
		} finally {
			pm.close();
		}
		
	}

	public String[] getFavWaterFountains() throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = getPersistenceManager();
		try {
			// todo
			return null;
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
