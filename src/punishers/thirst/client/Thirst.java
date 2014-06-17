package punishers.thirst.client;

import java.util.ArrayList;

import punishers.thirst.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
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
      
      private ArrayList<String> waterFountains = new ArrayList<String>();
	  
	  // loadThirst() related junk that will eventually be replaced
	  private VerticalPanel mainPanel = new VerticalPanel();
	  
	  private final WaterFountainServiceAsync waterFountainService = GWT.create(WaterFountainService.class);
	  
	  // Facebook Login 
	  private VerticalPanel facebookLoginPanel = new VerticalPanel();
	  private Label facebookLoginLabel = new Label("Or be a social drinker and login with Facebook.");
//	  private Anchor facebookSignInLink = new Anchor("Sign In with Facebook");
//	  private String facebookRedirect = 
//			  "https://www.facebook.com/dialog/oauth?client_id=559102767531636&redirect_uri=http://1-dot-symmetric-card-607.appspot.com";

	  // user specific greeting
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
		RootPanel.get("thirstList").add(loginPanel);
		
		// facebook login panel
		facebookLoginPanel.add(facebookLoginLabel);
		RootPanel.get("thirstList").add(facebookLoginPanel);
	}
	
	// the page that appears after the user has logged in.  
	// add a facebook signout link
	private void loadThirst() {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		RootPanel.get("logged_in").add(welcomePanel);
		
		// user specific greeting for google login
		welcomeLabel = new Label("Welcome, " + loginInfo.getNickname());
		welcomePanel.add(signOutLink);
		welcomePanel.add(welcomeLabel);
		
		loadWaterFountains();
	}
	
	private void loadWaterFountains() {
		// likely going to change the return type of getFavWaterFountains
		waterFountainService.getFavWaterFountains(new AsyncCallback<String[]>() {
			public void onFailure(Throwable error) {
			}
			public void onSuccess(String[] symbols) {
				displayFountains(symbols);
			}
		});
	}
	
	private void displayFountains(String [] symbols) {
		for (String symbol : symbols) {
			diplayFountain(symbol);
		}
	}
	
	private void diplayFountain(String symbol) {
		// TODO Auto-generated method stub
		
	}
	
	// add a user to a waterfountain's list of users.
	// executed when a user clicks the favorite button on 
	// a waterfountain's map pop-up
	// discuss with Avery
	private void addWaterFountain() {
		
	}
	
	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
	    if (error instanceof NotLoggedInException) {
	    	Window.Location.replace(loginInfo.getLogoutUrl());
	    }
	}
}
