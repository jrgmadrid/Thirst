package punishers.thirst.client;

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
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
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
	  
	  // loadThirst() related junk that will eventually be replaced
	  private VerticalPanel mainPanel = new VerticalPanel();
	  
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
//		facebookSignInLink.setHref(facebookRedirect);
		facebookLoginPanel.add(facebookLoginLabel);
//		facebookLoginPanel.add(facebookSignInLink);
		RootPanel.get("thirstList").add(facebookLoginPanel);
	}
	
	// the page that appears after the user has logged in.  
	// add a facebook signout link
	private void loadThirst() {
		signOutLink.setHref(loginInfo.getLogoutUrl());
		RootPanel.get("thirstList").add(mainPanel);
		
		// user specific greeting for google login
		welcomeLabel = new Label("Welcome, " + loginInfo.getNickname());
		mainPanel.add(signOutLink);
		welcomePanel.add(welcomeLabel);
		mainPanel.add(welcomePanel);
	}
	
	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
	    if (error instanceof NotLoggedInException) {
	    	Window.Location.replace(loginInfo.getLogoutUrl());
	    }
	}
}
