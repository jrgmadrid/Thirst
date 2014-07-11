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
public class TwitterUtil {
	private String token = null;

	private static TwitterUtil instance = new TwitterUtil();

	private static final String TWITTER_AUTH_URL = "https://api.twitter.com/oauth/authorize";
	private static final String TWITTER_API_KEY = "nxTyHRZXZrrX5EOSYHBphMr0W";
	private static final String TWITTER_API_SECRET = "nm6b1Q1Pt5tDwBFtObirM6XYcLwNHd2MzmeyQRWhGx6jWe08KC";


	private TwitterUtil() {
	}

	public static TwitterUtil getInstance() {
		return instance;
	}

	public void resetToken() {
		token = null;
	}

	private void doAuth(@SuppressWarnings("deprecation") Callback<String, Throwable> callback) {
		final AuthRequest req = new AuthRequest(TWITTER_AUTH_URL,
				TWITTER_API_KEY);
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
					TwitterUtil.this.token = token;
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
			builder = new RequestBuilder(method, "https://api.twitter.com/"
					+ id);
			builder.setHeader("Content-Type",
					"application/x-www-form-urlencoded");
		} else if (method == RequestBuilder.GET) {
			builder = new RequestBuilder(method, "https://api.twitter.com/"
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
	
