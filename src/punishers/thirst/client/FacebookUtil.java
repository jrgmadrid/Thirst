package punishers.thirst.client;

import java.io.IOException;

import com.google.api.gwt.oauth2.client.Auth;
import com.google.api.gwt.oauth2.client.AuthRequest;
import com.google.api.gwt.oauth2.client.Callback;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;

@SuppressWarnings("deprecation")
public class FacebookUtil {
    private String token = null;
 
    private static FacebookUtil instance = new FacebookUtil();
 
    private static final String FACEBOOK_AUTH_URL = "https://www.facebook.com/dialog/oauth";
    private static final String FACEBOOK_CLIENT_ID = "299017110280261"; 
    
    private static final String TWITTER_AUTH_URL = "https://api.twitter.com/oauth/authorize";
    private static final String TWITTER_API_KEY = "nxTyHRZXZrrX5EOSYHBphMr0W";
    private static final String TWITTER_API_SECRET = "nm6b1Q1Pt5tDwBFtObirM6XYcLwNHd2MzmeyQRWhGx6jWe08KC";

 
    private FacebookUtil() {
    }
 
    public static FacebookUtil getInstance() {
	return instance;
    }
 
    public void resetToken() {
	token = null;
    }
 
    private void doAuth(@SuppressWarnings("deprecation") Callback<String, Throwable> callback) {
	final AuthRequest req = new AuthRequest(FACEBOOK_AUTH_URL,
		FACEBOOK_CLIENT_ID).withScopes("email", "publish_actions")
	// Facebook expects a comma-delimited list of scopes
		.withScopeDelimiter(",");
	Auth.get().clearAllTokens();
	Auth.get().login(req, callback);
    }
 
    
    private void doTwitterAuth(@SuppressWarnings("deprecation") Callback<String, Throwable> callback) {
    	final AuthRequest req = new AuthRequest(TWITTER_AUTH_URL,
    		TWITTER_API_KEY).withScopes("https://api.twitter.com/1.1/statuses/update.json");
    	Auth.get().clearAllTokens();
    	Auth.get().login(req, callback);
    }
    
    public void doGraph(final String id,
	    final Callback<JSONObject, Throwable> callback) {
	doGraph(id, RequestBuilder.GET, null, callback);
    }
 
    @SuppressWarnings("deprecation")
	public void doGraph(final String id, final Method method,
	    final String params, final Callback<JSONObject, Throwable> callback) {
	if (token == null) {
	    doAuth(new Callback<String, Throwable>() {
		public void onSuccess(String token) {
		    FacebookUtil.this.token = token;
		    doGraphNoAuth(id, method, params, callback);
		}
 
		public void onFailure(Throwable reason) {
		    callback.onFailure(reason);
		}
	    });
	} else {
	    doGraphNoAuth(id, method, params, callback);
	}
    }
 
    private void doGraphNoAuth(String id, Method method, String params,
	    final Callback<JSONObject, Throwable> callback) {
	final String requestData = "access_token=" + token
		+ (params != null ? "&" + params : "");
	RequestBuilder builder;
	if (method == RequestBuilder.POST) {
	    builder = new RequestBuilder(method, "https://graph.facebook.com/"
		    + id);
	    builder.setHeader("Content-Type",
		    "application/x-www-form-urlencoded");
	} else if (method == RequestBuilder.GET) {
	    builder = new RequestBuilder(method, "https://graph.facebook.com/"
		    + id + "?" + requestData);
	} else {
	    callback.onFailure(new IOException(
		    "doGraph only supports GET and POST requests"));
	    return;
	}
	try {
	    builder.sendRequest(requestData, new RequestCallback() {
		public void onError(Request request, Throwable exception) {
		    callback.onFailure(exception);
		}
 
		public void onResponseReceived(Request request,
			Response response) {
		    if (Response.SC_OK == response.getStatusCode()) {
			callback.onSuccess(JSONParser.parseStrict(
				response.getText()).isObject());
		    } else if (Response.SC_BAD_REQUEST == response
			    .getStatusCode()) {
			callback.onFailure(new IOException("Error: "
				+ response.getText()));
		    } else {
			callback.onFailure(new IOException(
				"Couldn't retrieve JSON ("
					+ response.getStatusText() + ")"));
		    }
		}
 
	    });
	} catch (RequestException e) {
	    callback.onFailure(e);
	}
}
    
}