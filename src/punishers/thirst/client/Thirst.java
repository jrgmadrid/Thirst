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
import com.google.gwt.maps.client.geom.Size;
import com.google.gwt.maps.client.overlay.Icon;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.dom.client.Style.Unit;

import punishers.thirst.client.LoginInfo;
import punishers.thirst.client.LoginService;
import punishers.thirst.client.LoginServiceAsync;
import punishers.thirst.client.NotLoggedInException;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
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
	private Anchor signOutLink = new Anchor("Sign Out");
	
	private Button updateDatabaseButton = new Button("Update Database");
	private ArrayList<Long> waterFountains = new ArrayList<Long>();

	private CheckBox toggleAdmin = new CheckBox("Toggle Admin Controls");

	private DockLayoutPanel mainPanel = new DockLayoutPanel(Unit.PX);
	
	private HorizontalPanel adminPanel = new HorizontalPanel();
	private boolean isAdmin = false;

	private final WaterFountainServiceAsync waterFountainService = GWT.create(WaterFountainService.class);
	private final CSVReaderServiceAsync csvReaderService = GWT.create(CSVReaderService.class);
	
	private FlexTable waterFountainFlexTable = new FlexTable();
	private FlexTable favoritesFlexTable = new FlexTable();
	
	private HorizontalPanel addPanel = new HorizontalPanel();  
	private TextBox newIdTextBox = new TextBox();  
	private Button addWaterFountainButton = new Button("Add");

	private Label welcomeLabel;
	
	private VerticalPanel ratingPanel = new VerticalPanel();

	private TextBox ratingTextBox = new TextBox();
	private Button addRatingButton = new Button("Rate");
	
	TabLayoutPanel mapAndFlexTablePanel = new TabLayoutPanel(30, Unit.PX);
	
	/**
	 * This is the entry point method.
	 */
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
		toggleAdmin.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				isAdmin = ((CheckBox) event.getSource()).getValue();
			}
		});
		loginPanel.add(toggleAdmin);
		RootPanel.get("thirstList").add(loginPanel);
	}

	private void loadThirst() {
		
		Maps.loadMapsApi("AIzaSyC_LHn5yTElbMwbynIbKT9k7YjIDNj9GQY", "2", false, new Runnable() {
			public void run() {
				prepareMap();
			}
		});

		if (!loginInfo.getIsAdmin())
		{
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
		welcomeLabel = new Label("Welcome, " + loginInfo.getNickname());
		
		HorizontalPanel welcomePanel = new HorizontalPanel();
		welcomePanel.add(welcomeLabel);
		welcomePanel.add(signOutLink);

		waterFountainFlexTable.setText(0, 0, "WaterFountainId");
		waterFountainFlexTable.setText(0, 1, "Rating");
		
		favoritesFlexTable.setText(0, 0, "WaterFountainId");
		favoritesFlexTable.setText(0, 1, "Rating");

		addPanel.add(newIdTextBox);
		addPanel.add(addWaterFountainButton);
		addPanel.addStyleName("addPanel");
		
		mapAndFlexTablePanel.add(favoritesFlexTable, "Favorites");
		mapAndFlexTablePanel.add(waterFountainFlexTable, "All");
//		mapAndFlexTablePanel.add(map, "Map View");
		
		waterFountainFlexTable.setCellPadding(10);
		favoritesFlexTable.setCellPadding(10);
		
		mainPanel.addNorth(welcomePanel, 55);
		mainPanel.addSouth(addPanel, 220);
		mainPanel.addWest(ratingPanel, 220);
		mainPanel.add(mapAndFlexTablePanel);

		RootLayoutPanel.get().add(mainPanel);

		newIdTextBox.setFocus(true);

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
				displayFavorites(symbols);
			}
		});
	}

	private void loadWaterFountains() {
		waterFountainService.getAllIds(new AsyncCallback<Long[]>() {
			public void onFailure(Throwable error) {
				
			}
			public void onSuccess(Long[] symbols) {
				displayFountains(symbols);
			}
		});
	}
	
	private void displayFavorites(Long[] symbols) {
		for(Long symbol : symbols) {
			displayFavorite(symbol);
		}
	}

	private void displayFountains(Long [] symbols) {
		for (Long symbol : symbols) {
			displayFountain(symbol);
		}
	}
	
	private void displayFavorite(final Long symbol) {
		int row = favoritesFlexTable.getRowCount();
		waterFountains.add(symbol);
		favoritesFlexTable.setText(row, 0, String.valueOf(symbol));
		displayRating(symbol, row);
		
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

	private void displayFountain(final Long symbol) {
		int row = waterFountainFlexTable.getRowCount();
		waterFountainFlexTable.setText(row, 0, String.valueOf(symbol));
		displayRating(symbol, row);
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

	public void addRatingToWaterFountain(final long idNum, final int rating) {
		waterFountainService.addRating(idNum, rating, new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Void ignore) {
				Window.alert("Thanks for rating this fountain");
			}
		});
	}

	private void displayRating(long idNum, final int place) {
		waterFountainService.getAverageWaterFountatinRating(idNum, new AsyncCallback<Integer>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Integer rating) {
				int row = place;
					if(rating == -1) {
						waterFountainFlexTable.setText(row, 1, "Be the first to rate this fountain");
						waterFountainFlexTable.setWidget(row, 2, addRatingButton);
					} else {
						waterFountainFlexTable.setText(row, 1, String.valueOf(rating) + " out of 7");
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
	
	protected void displayMap(ArrayList<LatLng> latlngs, ArrayList<Double> ids) {
		
		Marker[] markers = new Marker[latlngs.size()];
		
		final InfoWindowContent[] infoWindows = new InfoWindowContent[latlngs.size()];
		
		LatLng center = LatLng.newInstance(49.26, -123.1);
	    final MapWidget map = new MapWidget(center, 11);

	    map.setSize("100%", "100%");
	    map.addControl(new LargeMapControl());
	    
	    for(int i = 0; i < latlngs.size(); i++) {
	    	Marker temp = new Marker(latlngs.get(i));
			markers[i] = temp;
	    }
	    
//		private HorizontalPanel addPanel = new HorizontalPanel();  
//		private TextBox newIdTextBox = new TextBox();  
//		private Button addWaterFountainButton = new Button("Add");
	    
	    // add textbox to infowindow content to make rating system work
	    for(int i = 0; i < ids.size(); i++) {
			ratingPanel.add(addRatingButton);
			ratingPanel.add(ratingTextBox);
	    	InfoWindowContent infContent = new InfoWindowContent(ratingPanel);
//			InfoWindowContent infContent = new InfoWindowContent("WaterFountain Id: " + String.valueOf(ids.get(i)));
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
	
//	private ArrayList<LatLng> getFavoriteLatLngs() {
//		final ArrayList<LatLng> latlngs = new ArrayList<LatLng>();
//		waterFountainService.getFavWaterFountainsLatLng(new AsyncCallback<Double[]>() {
//			public void onFailure(Throwable caught) {
//				handleError(caught);
//			}
//			public void onSuccess(Double[] result) {
//				for(int i = 0; i < result.length; i++) {
//					double lat = result[i];
//					double lon = result[i+1];
//					LatLng temp = LatLng.newInstance(lat, lon);
//					latlngs.add(temp);
//				}
//			}
//		});
//		return latlngs;
//	}
	
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
				displayFountain(id);
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
