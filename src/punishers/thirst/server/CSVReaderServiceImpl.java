package punishers.thirst.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import punishers.thirst.client.CSVReaderService;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

@PersistenceCapable
public class CSVReaderServiceImpl extends RemoteServiceServlet implements CSVReaderService {

	private static Set<WaterFountain> waterFountains;
	
	private static final PersistenceManagerFactory PMF = JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	/*
	 * Get fountains in the class's field
	 */
	public static Set<WaterFountain> getFountains() {
		return waterFountains;
	}
	
	/*
	 * Get water fountains from the datastore
	 */
	public Set<WaterFountain> getAllWaterFountains() {
		PersistenceManager pm = getPersistenceManager();
		Set<WaterFountain> wfs = new HashSet<WaterFountain>();
		try {
			Query q = pm.newQuery(WaterFountain.class);
			q.declareImports("import punishers.thirst.server.WaterFountain");
			wfs = (Set<WaterFountain>) q.execute();
		} finally {
			pm.close();
		}
		return wfs;
	}
	
	public WaterFountain retrieveFromSet(int id) {
		WaterFountain result = null;
		for (WaterFountain wf : waterFountains) {
			if (wf.getId() == id) {
				result = wf;
				break;
			}
		}
		return result;
	}
	
	public CSVReaderServiceImpl() {
		waterFountains = new HashSet<WaterFountain>();
	}
	
	public void updateData() {
		
		try {
			//The URL that has the data
			URL dataUrl = new URL("http://puu.sh/a5LdS.csv");
			//Opens the URL where the data is held
			URLConnection urlConnect = dataUrl.openConnection();
			//Reads the data 
			InputStreamReader inStream = new InputStreamReader(urlConnect.getInputStream());
			//adjusts the reading of the file
			BufferedReader csvFile = new BufferedReader(inStream);
			
			//Set up the string in which each line will be read into
			String line = "";
			//Set up the variable at which point the string is split
			String delim = ",";
			
			/*
			 * The first line of the file is parsed into this variable
			 * variable is a throw-away variable because first line doesn't
			 * actually contain any useful information
			 */
			String firstLine = csvFile.readLine();
			
			Set<WaterFountain> fountains = new HashSet<WaterFountain>();
			fountains = getAllWaterFountains();
			
			//Parses the rest of the file line by line
			while ((line = csvFile.readLine()) != null) {
				double lat;
				double lon;
				String location;
				String maintainer;
				
				//Array of strings that is the info for each water fountain
				String[] waterFountain = line.split(delim);
				
				//Takes each item in the array and sets it to each local variable
				lat = Double.parseDouble(waterFountain[0]);
				lon = Double.parseDouble(waterFountain[1]);
				location = waterFountain[2];
				maintainer = waterFountain[3];
				
				//Makes a new water fountain with all of the information
				WaterFountain fountain = new WaterFountain(lat, lon, location, maintainer);
				//Sets the ID of the water fountain which will be the key for the entity in the database
				//fountain.setId();
				//Adds the water fountain to the set which will then be stored in the database
				//waterFountains.add(fountain);
				
				if (!fountains.contains(fountain)) {
					PersistenceManager pm = getPersistenceManager();
					try {
						pm.makePersistent(fountain);
					} finally {
						pm.close();
					}
				}
			}
				
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void updateDataWithoutChecking() {
		
		try {
			//The URL that has the data
			URL dataUrl = new URL("http://puu.sh/a5LdS.csv");
			//Opens the URL where the data is held
			URLConnection urlConnect = dataUrl.openConnection();
			//Reads the data 
			InputStreamReader inStream = new InputStreamReader(urlConnect.getInputStream());
			//adjusts the reading of the file
			BufferedReader csvFile = new BufferedReader(inStream);
			
			//Set up the string in which each line will be read into
			String line = "";
			//Set up the variable at which point the string is split
			String delim = ",";
			
			/*
			 * The first line of the file is parsed into this variable
			 * variable is a throw-away variable because first line doesn't
			 * actually contain any useful information
			 */
			String firstLine = csvFile.readLine();
			
			//Parses the rest of the file line by line
			while ((line = csvFile.readLine()) != null) {
				double lat;
				double lon;
				String location;
				String maintainer;
				
				//Array of strings that is the info for each water fountain
				String[] waterFountain = line.split(delim);
				
				//Takes each item in the array and sets it to each local variable
				lat = Double.parseDouble(waterFountain[0]);
				lon = Double.parseDouble(waterFountain[1]);
				location = waterFountain[2];
				maintainer = waterFountain[3];
				
				//Makes a new water fountain with all of the information
				WaterFountain fountain = new WaterFountain(lat, lon, location, maintainer);
				//Sets the ID of the water fountain which will be the key for the entity in the database
				//fountain.setId();
				//Adds the water fountain to the set which will then be stored in the database
				waterFountains.add(fountain);
			}
				
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private PersistenceManager getPersistenceManager() {
		  return PMF.getPersistenceManager();
	}

}
