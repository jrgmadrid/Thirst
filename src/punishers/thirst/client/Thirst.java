package punishers.thirst.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

import punishers.thirst.client.LoginInfo;
import punishers.thirst.client.LoginService;
import punishers.thirst.client.LoginServiceAsync;
import punishers.thirst.client.NotLoggedInException;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
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
      private ArrayList<String> waterFountains = new ArrayList<String>();

	  private CheckBox toggleAdmin = new CheckBox("Toggle Admin Controls");

	  // loadThirst() related junk that will eventually be replaced
	  private VerticalPanel mainPanel = new VerticalPanel();
	  private boolean isAdmin = true;
	  
	  private final WaterFountainServiceAsync waterFountainService = GWT.create(WaterFountainService.class);
	  private final CSVReaderServiceAsync csvReaderService = GWT.create(CSVReaderService.class);

	  private HorizontalPanel welcomePanel = new HorizontalPanel();
	  private Label welcomeLabel;
	  
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
	    // Check login status using login service.
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
		// Google login panel.
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
		
		waterFountainFlexTable.setText(0, 0, "WaterFountain!");
		
		waterFountainFlexTable.getRowFormatter().addStyleName(0, "favoritesListHeader");
		waterFountainFlexTable.addStyleName("favoritesList");
		
		addPanel.add(newIdTextBox);
		addPanel.add(addWaterFountainButton);
		addPanel.addStyleName("addPanel");
		
		mainPanel.add(welcomeLabel);
		
		if (!isAdmin)
		{
			mainPanel.add(waterFountainFlexTable);
			mainPanel.add(addPanel);
			mainPanel.add(signOutLink);
			
			RootPanel.get("logged_in").add(mainPanel);
			
			newIdTextBox.setFocus(true);
			
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
		    
		    loadWaterFountains();
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
		waterFountainService.getFavWaterFountains(new AsyncCallback<String[]>() {
			public void onFailure(Throwable error) {
				System.out.println("ARGH");
			}
			public void onSuccess(String[] symbols) {
				displayFountains(symbols);
			}
		});
	}
	
	private void displayFountains(String [] symbols) {
		for (String symbol : symbols) {
			displayFountain(symbol);
		}
	}
	
	private void displayFountain(final String symbol) {
	    int row = waterFountainFlexTable.getRowCount();
	    waterFountains.add(symbol);
	    waterFountainFlexTable.setText(row, 0, symbol);

	    Button removeWaterFountainButton = new Button("x");
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
	
	private void removeWaterFountain(final String symbol) {
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
	
	private void undisplayWaterFountain(String symbol) {
		int removedIndex = waterFountains.indexOf(symbol);
		waterFountains.remove(removedIndex);
		waterFountainFlexTable.removeRow(removedIndex+1);
	}

	// add a user to a waterfountain's list of users.
	// executed when a user clicks the favorite button on 
	// a waterfountain's map pop-up
	// discuss with Avery
	private void addWaterFountain() {
		final long idNum = Long.valueOf(newIdTextBox.getText().trim());
		newIdTextBox.setFocus(true);
		waterFountainService.getAllIds(new AsyncCallback<Long[]>(){
			public void onFailure(Throwable error) {
			}
			public void onSuccess(Long[] ids) {
				if (!validateId(idNum, ids)) {
					System.out.println("YO YOU DUN FUCKED UP, SON");
					Window.alert("'" + String.valueOf(idNum) + "' is not a valid ID");
					newIdTextBox.selectAll();
					return;
				}
				newIdTextBox.setText("");
				if (waterFountains.contains(String.valueOf(idNum))) {
					return;
				}
				addWaterFountainToFavs(idNum);					
			}
		});
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
				String idString = String.valueOf(id);
				displayFountain(idString);
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
