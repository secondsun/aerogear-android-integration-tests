package org.jboss.aerogear.android.authorization;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.impl.authz.AuthzConfig;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthzModule;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

public class OAuth2AuthzModuleTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    private static final URL BASE_URL;
    
    static {
        try {
            BASE_URL = new URL("https://example.com");
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public OAuth2AuthzModuleTest() {
        super(MainActivity.class);
    }

    public void testCreation() throws MalformedURLException {
        AuthzConfig config = new AuthzConfig(BASE_URL, "name");
        OAuth2AuthzModule module = new OAuth2AuthzModule(config);
        
        assertFalse(module.isAuthorized());
        
    }
    
    public void testRequestAccess() {
        AuthzConfig config = new AuthzConfig(BASE_URL, "name");
        OAuth2AuthzModule module = new OAuth2AuthzModule(config);
        String state = "testState";
        Activity mockActivity = mock(Activity.class);
        Callback mockCallback = mock(Callback.class);
        when(mockActivity.bindService(any(Intent.class), any(ServiceConnection.class), any(Integer.class))).thenReturn(Boolean.TRUE);
        when(mockActivity.getApplicationContext()).thenReturn(super.getActivity());
        module.requestAccess(state, mockActivity, mockCallback);
        
        Mockito.verify(mockActivity, times(1)).bindService((Intent) any(),(ServiceConnection) any(), any(Integer.class));
        
    }
}
