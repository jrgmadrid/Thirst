package punishers.thirst.client;

import java.io.Serializable;

public class LoginInfo implements Serializable{
	
	 private boolean loggedIn = false;
	 private String loginUrl;
	 private String logoutUrl;
	 private String emailAddress;
	 private String nickname;
	 private boolean isAdmin;
	 public boolean isLoggedIn() {
	   return loggedIn;
	 }

	 public void setLoggedIn(boolean loggedIn) {
	   this.loggedIn = loggedIn;
	 }

	 public String getLoginUrl() {
	   return loginUrl;
	 }

	 public void setLoginUrl(String loginUrl) {
	   this.loginUrl = loginUrl;
	 }

	 public String getLogoutUrl() {
	   return logoutUrl;
	 }

	 public void setLogoutUrl(String logoutUrl) {
	   this.logoutUrl = logoutUrl;
	 }

	 public String getEmailAddress() {
	   return emailAddress;
	 }

	 public void setEmailAddress(String emailAddress) {
	   this.emailAddress = emailAddress;
	 }

	 public String getNickname() {
	   return nickname;
	 }

	 public void setNickname(String nickname) {
	   this.nickname = nickname;
	 }

	 
	 public boolean getIsAdmin() {
		 return isAdmin;
	 }
	 
	 public void setIsAdmin(boolean isAdmin) {
		 this.isAdmin = isAdmin;
	 }
}
