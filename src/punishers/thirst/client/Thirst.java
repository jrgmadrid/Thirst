package punishers.thirst.client;

import java.util.ArrayList;
import java.util.List;

import punishers.thirst.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.maps.client.InfoWindow;
import com.google.gwt.maps.client.MapOptions;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.maps.client.control.LargeMapControl;
import com.google.gwt.maps.client.event.MarkerClickHandler;
import com.google.gwt.maps.client.event.MarkerInfoWindowOpenHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.impl.InfoWindowOptionsImpl;
import com.google.gwt.maps.client.overlay.Marker;
import com.google.gwt.maps.client.overlay.MarkerOptions;
import com.google.gwt.dom.client.Style.Unit;

import punishers.thirst.client.LoginInfo;
import punishers.thirst.client.LoginService;
import punishers.thirst.client.LoginServiceAsync;
import punishers.thirst.client.NotLoggedInException;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Thirst implements EntryPoint {

	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Sign in with a Google Account to quench your Thirst.");
	private Anchor signInLink = new Anchor("Sign In with Google");
	private Anchor signOutLink = new Anchor("Sign Out");
	private FlexTable waterFountainFlexTable = new FlexTable();
	private HorizontalPanel addPanel = new HorizontalPanel();  
	private TextBox newIdTextBox = new TextBox();  
	private Button addWaterFountainButton = new Button("Add");
	private Button updateDatabaseButton = new Button("Update Database");
	private ArrayList<Long> waterFountains = new ArrayList<Long>();

	private CheckBox toggleAdmin = new CheckBox("Toggle Admin Controls");

	private VerticalPanel mainPanel = new VerticalPanel();
	private boolean isAdmin = false;

	private final WaterFountainServiceAsync waterFountainService = GWT.create(WaterFountainService.class);
	private final CSVReaderServiceAsync csvReaderService = GWT.create(CSVReaderService.class);

	private Label welcomeLabel;

	private TextBox ratingTextBox = new TextBox();
	private Button addRatingButton = new Button("Rate this Fountain");
	
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

		signOutLink.setHref(loginInfo.getLogoutUrl());
		welcomeLabel = new Label("Welcome, " + loginInfo.getNickname());

		waterFountainFlexTable.setText(0, 0, "WaterFountainId");
		waterFountainFlexTable.setText(0, 1, "Rating");

		addPanel.add(newIdTextBox);
		addPanel.add(addWaterFountainButton);
		addPanel.addStyleName("addPanel");


		mainPanel.add(welcomeLabel);
		
		Maps.loadMapsApi("", "2", false, new Runnable() {
			public void run() {
				prepareMap();
			}
		});

		if (!loginInfo.getIsAdmin())
		{
			loadWaterFountains();

			waterFountainFlexTable.setCellPadding(12);

			mainPanel.add(waterFountainFlexTable);
			mainPanel.add(addPanel);
			mainPanel.add(ratingTextBox);
			mainPanel.add(signOutLink);

			RootPanel.get("logged_in").add(mainPanel);

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
		else {
			loadAdminControls();
		}
	}
	
	private void loadAdminControls() {
		mainPanel.add(signOutLink);
		mainPanel.add(updateDatabaseButton);
		RootPanel.get("logged_in").add(mainPanel);
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

	private void loadWaterFountains() {
		waterFountainService.getFavWaterFountains(new AsyncCallback<Long[]>() {
			public void onFailure(Throwable error) {
				
			}
			public void onSuccess(Long[] symbols) {
				displayFountains(symbols);
			}
		});
	}

	private void displayFountains(Long [] symbols) {
		for (Long symbol : symbols) {
			displayFountain(symbol);
			displayRating(symbol);
		}
	}

	private void displayFountain(final Long symbol) {
		int row = waterFountainFlexTable.getRowCount();
		waterFountains.add(symbol);
		waterFountainFlexTable.setText(row, 0, String.valueOf(symbol));

		Button removeWaterFountainButton = new Button("Remove Fountain");
		removeWaterFountainButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				int removedIndex = waterFountains.indexOf(symbol);
				waterFountains.remove(removedIndex);        
				waterFountainFlexTable.removeRow(removedIndex + 1);
				removeWaterFountain(symbol);
			}
		});
		waterFountainFlexTable.setWidget(row, 3, removeWaterFountainButton);
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

	private void displayRating(long idNum) {
		waterFountainService.getAverageWaterFountatinRating(idNum, new AsyncCallback<Integer>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Integer rating) {
				int row = waterFountainFlexTable.getRowCount();
				if(rating == -1) {
					waterFountainFlexTable.setText(row-1, 1, "Be the first to rate this fountain");
					waterFountainFlexTable.setWidget(row-1, 2, addRatingButton);
				} else {
					waterFountainFlexTable.setText(row-1, 1, String.valueOf(rating));
				}
			}
		});
	}
	
	private void prepareMap() {
		getLatitudesAndLongitudes();
	}

	private void getLatitudesAndLongitudes() {
		final ArrayList<LatLng> latlngs = new ArrayList<LatLng>();
		waterFountainService.getAllLatAndLng(new AsyncCallback<Double[]>() {
			public void onFailure(Throwable caught) {

			}
			@Override
			public void onSuccess(Double[] result) {
				for(int i = 0; i < result.length; i+=2){
					double lat = result[i];
					double lon = result[i+1];
					LatLng temp = LatLng.newInstance(lat, lon);
					latlngs.add(temp);
				}
				displayMap(latlngs);
			}
		});
	}
	
	protected void displayMap(ArrayList<LatLng> latlngs) {
		LatLng center = LatLng.newInstance(49.26, -123.1);
	    final MapWidget map = new MapWidget(center, 12);
	    map.setSize("100%", "100%");
	    map.addControl(new LargeMapControl());
		for(LatLng latlng : latlngs) {
			Marker marker = new Marker(latlng);
			marker.addMarkerClickHandler(new MarkerClickHandler () {
				@Override
				public void onClick(MarkerClickEvent event) {
					Window.alert("I hope this works");
				}
			});
			
			map.addOverlay(marker);
		}
	    final DockLayoutPanel dock = new DockLayoutPanel(Unit.PX);
	    dock.addNorth(map, 600);
	    RootPanel.get("map").add(dock);
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
				displayFountain(id);
			}
		});
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
}
