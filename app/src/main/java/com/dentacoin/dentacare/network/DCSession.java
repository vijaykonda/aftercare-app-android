package com.dentacoin.dentacare.network;

import com.dentacoin.dentacare.model.DCUser;
import com.dentacoin.dentacare.network.response.DCAuthToken;
import com.dentacoin.dentacare.utils.DCSharedPreferences;
import com.google.gson.JsonSyntaxException;

/**
 * Created by Atanas Chervarov on 8/3/17.
 */

public class DCSession {

    private static DCSession instance;

    private DCAuthToken authToken;
    private DCUser user;

    public synchronized static DCSession getInstance() {
        if (instance == null)
            instance = new DCSession();

        return instance;
    }

    private DCSession() { }

    public void setUser(DCUser user) {
        this.user = user;
    }

    public DCUser getUser() {
        return user;
    }

    public void setAuthToken(DCAuthToken authToken) {
        this.authToken = authToken;
        if (authToken != null) {
            DCSharedPreferences.saveString(DCSharedPreferences.DCSharedKey.AUTH_TOKEN, DCApiManager.gson.toJson(authToken));
        }
    }

    public DCAuthToken getAuthToken() {
        if (authToken == null) {
            String token = DCSharedPreferences.loadString(DCSharedPreferences.DCSharedKey.AUTH_TOKEN);
            if (token != null) {
                try {
                    authToken = DCApiManager.gson.fromJson(token, DCAuthToken.class);
                } catch (JsonSyntaxException e) {
                }
            }
        }
        return authToken;
    }

    public String getAuthTokenString() {
        if (getAuthToken() != null) {
            return getAuthToken().getToken();
        }
        return null;
    }

    /**
     * Returns if the current session is valid
     * @return
     */
    public boolean isValid() {
        return getAuthToken() != null && getAuthToken().isValid();
    }

    /**
     * Clear the session
     */
    public void clear() {
        DCSharedPreferences.removeKey(DCSharedPreferences.DCSharedKey.AUTH_TOKEN);
        DCSharedPreferences.removeKey(DCSharedPreferences.DCSharedKey.WELCOME_SCREEN);
        authToken = null;
        user = null;
    }
}
