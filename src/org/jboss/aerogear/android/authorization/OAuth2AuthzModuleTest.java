package org.jboss.aerogear.android.authorization;

import android.app.Activity;
import android.content.Intent;
import android.content.ServiceConnection;
import android.util.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.authentication.AuthorizationFields;
import org.jboss.aerogear.android.impl.authz.AuthzConfig;
import org.jboss.aerogear.android.impl.authz.AuthzService;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthzModule;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthzSession;
import org.jboss.aerogear.android.impl.helper.UnitTestUtils;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;
import org.jboss.aerogear.android.impl.util.VoidCallback;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import org.mockito.Mockito;
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
    
    public void testGetAccessTokens() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        
        AuthzConfig config = new AuthzConfig(BASE_URL, "name");
        OAuth2AuthzModule module = new OAuth2AuthzModule(config);
        
        OAuth2AuthzSession account = new OAuth2AuthzSession();
        account.setAccessToken("testToken");
        
        UnitTestUtils.setPrivateField(module, "account", account);
        
        assertEquals("Bearer testToken", module.getAuthorizationFields(null, null, null).getHeaders().get(0).second);
        assertEquals("Authorization", module.getAuthorizationFields(null, null, null).getHeaders().get(0).first);
    }
    
    public void testOAuth2AccessCallback() throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchFieldException {
        AuthzService mockService = mock(AuthzService.class);
        Activity mockActivity = mock(Activity.class);
        ServiceConnection mockServiceConnection = mock(ServiceConnection.class);
        OAuth2AuthzSession account = new OAuth2AuthzSession();
        account.setAccessToken("testToken");
        
        when(mockService.getAccount(matches("testAccountId"))).thenReturn(account);
        
        AuthzConfig config = new AuthzConfig(BASE_URL, "name");
        config.setAccountId("testAccountId");
        
        OAuth2AuthzModule module = new OAuth2AuthzModule(config);
        Class<?> callbackClass = Class.forName("org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthzModule$OAuth2AccessCallback");
        Constructor<?> constructor = callbackClass.getDeclaredConstructor(OAuth2AuthzModule.class, Activity.class, Callback.class, ServiceConnection.class);
        constructor.setAccessible(true);
        
        Callback callback = (Callback) constructor.newInstance(module, mockActivity, new VoidCallback(), mockServiceConnection);
        
        
        UnitTestUtils.setPrivateField(module, "service", mockService);
        
        callback.onSuccess("testToken");
        
        Mockito.verify(mockActivity, times(1)).unbindService(eq(mockServiceConnection));
        assertEquals("testToken", UnitTestUtils.getPrivateField(module, "account", OAuth2AuthzSession.class).getAccessToken());
    }
}