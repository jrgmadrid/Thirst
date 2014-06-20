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
			Query q = pm.newQuery(WaterFountain.class);
			List<WaterFountain> waterFountains = (List<WaterFountain>) q.execute();
			for(WaterFountain wf : waterFountains) {
				Set<User> people = wf.getUsers();
				if(!people.isEmpty()) {
					for (User user : wf.getUsers()) {
						if(user == getUser()) {
							results.add(wf.getLocation());
						}
					}
				}
				else {
					System.out.println("No Users found.");
				}
			}
		} finally {
			pm.close();
		}
		return (String[]) results.toArray(new String[0]);
	}
	
	public Long[] getAllIds() throws NotLoggedInException {
		System.out.println("The method started correctly! But are we logged in?");
		checkLoggedIn();
		System.out.println("We're logged in! But will the PM start?");
		PersistenceManager pm = getPersistenceManager();
		System.out.println("PM Started! Let's do the try-statement stuff, shall we?");
		ArrayList<Long> results = null;
		try {
			System.out.println("Query?");
			Query q = pm.newQuery(WaterFountain.class);
			System.out.println("Query created! Execute query?");
			List<WaterFountain> wfs = (List<WaterFountain>) q.execute();
			System.out.println("Query executed!?");
			if(wfs != null) {
				System.out.println("DO A THING");
				System.out.println(wfs.size() + "YO");
				for(WaterFountain wf : wfs) {
					System.out.println("We got into a for-loop!!???");
					results = new ArrayList<Long>();
					System.out.println("YO, we have our results, but");
					results.add(wf.getId());
					System.out.println("did we really get the ids? I guess we did");
				}
			} else {
				System.out.println("No Results.");
			}
			System.out.println("Wait, what");
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
