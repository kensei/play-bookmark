package controllers;

import models.*;

public class Security extends Secure.Security {

    static boolean authenticate(String username, String password) {
    	return User.connect(username, password) != null;
    }
    
    static boolean check(String profile) {
        if("admin".equals(profile)) {
            return User.find("byEmail", connected()).<User>first().isAdmin;
        }
        return false;
    }
    
    static void onDisconnected() {
    	session.clear();
        Application.index();
    }
    
    static void onAuthenticated() {
    	User user = User.find("byEmail", Security.connected()).first();
        session.put("name", user.fullname);
    	Application.index();
    }
}