package punishers.thirst.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.maps.client.InfoWindowContent;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.dom.client.Style.Unit;

import punishers.thirst.client.LoginInfo;
import punishers.thirst.client.LoginService;
import punishers.thirst.client.LoginServiceAsync;
import punishers.thirst.client.NotLoggedInException;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Thirst implements EntryPoint {

	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Sign in with a Google Account to quench your Thirst.");
	private Anchor signInLink = new Anchor("Sign In with Google");
	
	private ArrayList<Long> waterFountains = new ArrayList<Long>();
	
	// admin
	private HorizontalPanel adminPanel = new HorizontalPanel();
	private Button updateDatabaseButton = new Button("Update Database");
	
	private HorizontalPanel welcomePanel = new HorizontalPanel();

	private final WaterFountainServiceAsync waterFountainService = GWT.create(WaterFountainService.class);
	private final CSVReaderServiceAsync csvReaderService = GWT.create(CSVReaderService.class);
	
	// table of all data
	private FlexTable waterFountainFlexTable = new FlexTable();
	private ScrollPanel waterFountainScrollPanel = new ScrollPanel();
	
	// table of favorites
	private FlexTable favoritesFlexTable = new FlexTable();
	private ScrollPanel favoritesScrollPanel = new ScrollPanel();
	
	// add a favorite
	private VerticalPanel addPanel = new VerticalPanel();  
	private TextBox newIdTextBox = new TextBox();  
	private Button addWaterFountainButton = new Button("Add");

	// main layout container
	private DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
	private Label welcomeLabel;
	private TabLayoutPanel mapAndFlexTablePanel = new TabLayoutPanel(30, Unit.PX);
	private Anchor signOutLink = new Anchor("Sign Out");
	
	// rate a water fountain, located inside infowindow
	private VerticalPanel ratingPanel = new VerticalPanel();
	private TextBox ratingTextBox = new TextBox();
	private Button addRatingButton = new Button("Rate");
	
	/**
	 * This is the entry point method.
	 */
	// TODO make page seen before sign in more appealing
	public void onModuleLoad() {
	
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if(loginInfo.isLoggedIn()) {
					loadThirst();
				} else {
					loadLogin();
				}
			}
		});
	}
	
	private void loadLogin() {

		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);

		RootPanel.get("thirstList").add(loginPanel);
	}

	private void loadThirst() {
		Maps.loadMapsApi("AIzaSyC_LHn5yTElbMwbynIbKT9k7YjIDNj9GQY", "2", false, new Runnable() {
			public void run() {
				prepareMap();
			}
		});
		if (!loginInfo.getIsAdmin()) {
			loadWaterFountains();
			loadFavoriteWaterFountains();
			loadUI();
		}
		else {
			loadAdminControls();
		}
	}
	
	private void loadUI() {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		
		createWelcomePanel();
		
		createAddPanel();
		
		createWaterFountainTab();
		
		createFavoritesTab();
		
		createMapAndWaterFountainFlexTable();
		
		mainPanel.addNorth(welcomePanel, 55);
		mainPanel.addSouth(signOutLink, 25);
		mainPanel.addWest(addPanel, 220);
		mainPanel.add(mapAndFlexTablePanel);

		RootLayoutPanel.get().add(mainPanel);

		addRatingButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addRating();
			}
		});
		ratingTextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					addRating();
				}
			}
		});
		addWaterFountainButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				addWaterFountain();
			}
		});
		newIdTextBox.addKeyDownHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					addWaterFountain();
				}
			}
		});
	}
	
	private void createWelcomePanel() {
		welcomeLabel = new Label("Welcome, " + loginInfo.getNickname());
		welcomePanel.add(welcomeLabel);
	}
	
	private void createAddPanel() {
		Label addLabel = new Label("to add a water fountain to your favorites list enter the ID number into the text box.");
		addPanel.add(addLabel);
		addPanel.add(newIdTextBox);
		addPanel.add(addWaterFountainButton);
		addPanel.addStyleName("addPanel");
		newIdTextBox.setFocus(true);
	}
	
	private void createWaterFountainTab() {
		waterFountainFlexTable.setText(0, 0, "WaterFountainId");
		waterFountainFlexTable.setText(0, 1, "Rating");
		waterFountainFlexTable.setText(0, 2, "Lat and Long");
		waterFountainFlexTable.setText(0, 3, "Location");
		waterFountainFlexTable.setText(0, 4, "Maintainer");
		waterFountainFlexTable.setText(0, 5, "Number of People who like this Fountain");
		waterFountainFlexTable.setCellPadding(10);
		waterFountainScrollPanel.add(waterFountainFlexTable);
	}
	
	private void createFavoritesTab() {
		favoritesFlexTable.setText(0, 0, "WaterFountainId");
		favoritesFlexTable.setText(0, 1, "Rating");
		favoritesFlexTable.setText(0, 2, "Lat and Long");
		favoritesFlexTable.setText(0, 3, "Location");
		favoritesFlexTable.setText(0, 4, "Maintainer");
		favoritesFlexTable.setText(0, 5, "Number of People who like this Fountain");
		favoritesFlexTable.setCellPadding(10);
		favoritesScrollPanel.add(favoritesFlexTable);
	}
	
	private void createMapAndWaterFountainFlexTable() {
		mapAndFlexTablePanel.add(favoritesScrollPanel, "Favorites");
		mapAndFlexTablePanel.add(waterFountainScrollPanel, "All");
	}
	
	private void loadAdminControls() {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		adminPanel.add(signOutLink);
		adminPanel.add(updateDatabaseButton);
		RootPanel.get("logged_in").add(adminPanel);
		updateDatabaseButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				updateDatabase();
			}
		});
	}

	private void updateDatabase() {
		csvReaderService.updateData(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {}
			public void onSuccess(Void result) {}
		});
	}
	
	private void loadFavoriteWaterFountains() {
		waterFountainService.getFavWaterFountains(new AsyncCallback<Long[]>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Long[] symbols) {
				displayFountains(symbols, favoritesFlexTable);
			}
		});
	}

	private void loadWaterFountains() {
		waterFountainService.getAllIds(new AsyncCallback<Long[]>() {
			public void onFailure(Throwable error) {
				
			}
			public void onSuccess(Long[] symbols) {
				displayFountains(symbols, waterFountainFlexTable);
			}
		});
	}
	
	private void displayFountains(Long[] symbols, FlexTable flexTable) {
		for (Long symbol : symbols) {
			displayFountain(symbol, flexTable);
		}
	}

	private void displayFountain(final Long symbol, final FlexTable flexTable) {
		int row = flexTable.getRowCount();
		if(flexTable.toString() == favoritesFlexTable.toString()){
			displayFavorite(symbol);
		}
		flexTable.setText(row, 0, String.valueOf(symbol));
		displayRating(symbol, row, flexTable);
		displayLatLng(symbol, row, flexTable);
		displayLocation(symbol, row, flexTable);
		displayMaintainer(symbol, row, flexTable);
		displayNumberOfFavorites(symbol, row, flexTable);
	}
	
	private void displayLatLng(long symbol, final int row, final FlexTable flexTable) {
		waterFountainService.getLatAndLong(symbol, new AsyncCallback<Double[]>() {
			public void onFailure(Throwable caught) {
				handleError(caught);
			}
			public void onSuccess(Double[] result) {
				flexTable.setText(row, 2, String.valueOf(result[0]) + " , " + String.valueOf(result[1]));
			}
		});
	}
	
	private void displayLocation(long symbol, final int row, final FlexTable flexTable) {
		waterFountainService.getLocationString(symbol, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				handleError(caught);
			}
			public void onSuccess(String result) {
				flexTable.setText(row, 3, result);
			}
		});
	}

	private void displayMaintainer(long symbol, final int row, final FlexTable flexTable) {
		waterFountainService.getMaintainerString(symbol, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				handleError(caught);
			}
			public void onSuccess(String result) {
				flexTable.setText(row, 4, result);
			}
		});
	}
	
	private void displayNumberOfFavorites(long symbol, final int row, final FlexTable flexTable) {
		waterFountainService.getNumberOfUsers(symbol, new AsyncCallback<Integer>() {
			public void onFailure(Throwable caught) {
				handleError(caught);
			}
			public void onSuccess(Integer result) {
				flexTable.setText(row, 5, String.valueOf(result));
			}
			
		});
	}
	
	private void displayFavorite(final Long symbol) {
		int row = favoritesFlexTable.getRowCount();
		waterFountains.add(symbol);
		favoritesFlexTable.setText(row, 0, String.valueOf(symbol));
		displayRating(symbol, row, favoritesFlexTable);
		
		Button removeWaterFountainButton = new Button("Remove Fountain");
		removeWaterFountainButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removedIndex = waterFountains.indexOf(symbol);
				waterFountains.remove(removedIndex);        
				waterFountainFlexTable.removeRow(removedIndex + 1);
				removeWaterFountain(symbol);
			}
		});
		favoritesFlexTable.setWidget(row, 3, removeWaterFountainButton);
	}

	private void removeWaterFountain(final Long symbol) {
		long id = Long.valueOf(symbol);
		waterFountainService.removeWaterFountainFromFavs(id, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				undisplayWaterFountain(symbol);
			}
		});
	}

	private void undisplayWaterFountain(Long symbol) {
		int removedIndex = waterFountains.indexOf(symbol);
		waterFountains.remove(removedIndex);
		waterFountainFlexTable.removeRow(removedIndex+1);
	}

	private void addWaterFountain() {
		final long idNum = Long.valueOf(newIdTextBox.getText().trim());
		newIdTextBox.setFocus(true);
		waterFountainService.getAllIds(new AsyncCallback<Long[]>(){
			public void onFailure(Throwable error) {
			}
			public void onSuccess(Long[] ids) {
				if (!validateId(idNum, ids)) {
					Window.alert("'" + Long.valueOf(idNum) + "' is not a valid ID");
					newIdTextBox.selectAll();
					return;
				}
				newIdTextBox.setText("");
				if (waterFountains.contains(Long.valueOf(idNum))) {
					return;
				}
				addWaterFountainToFavs(idNum);			
			}
		});
	}

	private void addRating() {
		final int rating = Integer.valueOf(ratingTextBox.getText().trim());
		ratingTextBox.setFocus(true);
		
		final long idNum = Long.valueOf(newIdTextBox.getText().trim());
		newIdTextBox.setFocus(true);

		if(rating < 0 || rating > 7) {
			Window.alert("Please enter a number between 0 and 7");
			ratingTextBox.selectAll();
			return;
		}
		addRatingToWaterFountain(idNum, rating);
	}

	private void addRatingToWaterFountain(final long idNum, final int rating) {
		waterFountainService.addRating(idNum, rating, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				Window.alert("Thanks for rating this fountain");
			}
		});
	}

	// TODO create another flextable to store the ratings so that if there are less than 10, all ten appear
	private void displayRating(long idNum, final int place, final FlexTable flexTable) {
		waterFountainService.getAverageWaterFountatinRating(idNum, new AsyncCallback<Integer>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Integer rating) {
				int row = place;
				if(rating == -1) {
					flexTable.setText(row, 1, "Be the first to rate this fountain");
				} else {
					flexTable.setText(row, 1, String.valueOf(rating) + " out of 7");
				}
			}
		});
	}
	
	private void prepareMap() {
		getLatitudesAndLongitudes();
	}

	private void getLatitudesAndLongitudes() {
		final ArrayList<LatLng> latlngs = new ArrayList<LatLng>();
		final ArrayList<Double> ids = new ArrayList<Double>();
		waterFountainService.getAllLatAndLngAndId(new AsyncCallback<Double[]>() {
			public void onFailure(Throwable caught) {
				Window.alert("getAllLatsAndLongs is failing");
				handleError(caught);
			}
			public void onSuccess(Double[] result) {
				for(int i = 0; i < result.length; i+=3){
					double lat = result[i];
					double lon = result[i+1];
					double id = result[i+2];
					LatLng temp = LatLng.newInstance(lat, lon);
					latlngs.add(temp);
					ids.add(id);
				}
				displayMap(latlngs, ids);
			}
		});
	}
	
	// TODO refactor this method
	protected void displayMap(ArrayList<LatLng> latlngs, ArrayList<Double> ids) {
		
		Marker[] markers = new Marker[latlngs.size()];
		final InfoWindowContent[] infoWindows = new InfoWindowContent[latlngs.size()];
		
		LatLng center = LatLng.newInstance(49.26, -123.1);
	    final MapWidget map = new MapWidget(center, 12);

	    map.setSize("100%", "100%");
	    map.addControl(new LargeMapControl());
	    
	    for(int i = 0; i < latlngs.size(); i++) {
	    	Marker temp = new Marker(latlngs.get(i));
			markers[i] = temp;
	    }
	    
	    // TODO: add Id, number of favorites, link to profile, fbook and twitter to infowindow
	    for(int i = 0; i < ids.size(); i++) {
			ratingPanel.add(addRatingButton);
			ratingPanel.add(ratingTextBox);
//			Label idString = new Label("WaterFountain Id: " + String.valueOf(ids.get(i)));
//			ratingPanel.add(idString);
	    	InfoWindowContent infContent = new InfoWindowContent(ratingPanel);
			infoWindows[i] = infContent;
	    }
	    
	    for(int i = 0; i < latlngs.size(); i++) {
	    	final Marker temp = markers[i];
	    	final int position = i;
	    	temp.addMarkerClickHandler(new MarkerClickHandler() {
	    		public void onClick(MarkerClickEvent event) {
	    			map.getInfoWindow().open(temp, infoWindows[position]);
	    		}
	    	});
			map.addOverlay(temp);
	    }
	    
	    mapAndFlexTablePanel.add(map, "Map View");
	}
	
	private boolean validateId(long idNum, Long[] ids) {
		boolean result = false;
		for(Long id : ids) {
			if(idNum == id) {
				result = true;
			}
		}
		return result;
	}

	private void addWaterFountainToFavs(final long id) {
		waterFountainService.addWaterFountainToFavs(id, new AsyncCallback<Void>(){
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				displayFountain(id, favoritesFlexTable);
			}
		});
	}

	private void handleError(Throwable error) {
		Window.alert("an async call is failing");
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}
