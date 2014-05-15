package org.jboss.aerogear.android.authorization;

import com.google.gson.JsonObject;
import java.net.URL;
import java.util.Calendar;
import static java.util.Calendar.HOUR;
import java.util.HashMap;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.datamanager.Store;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.http.HttpProvider;
import org.jboss.aerogear.android.impl.authz.AuthzConfig;
import org.jboss.aerogear.android.impl.authz.AuthzService;
import org.jboss.aerogear.android.impl.authz.OAuth2AuthorizationException;
import org.jboss.aerogear.android.impl.authz.oauth2.OAuth2AuthzSession;
import org.jboss.aerogear.android.impl.datamanager.MemoryStorage;
import org.jboss.aerogear.android.impl.helper.UnitTestUtils;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class AuthzServiceTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    private AuthzService service;
    private Store mockStore;
    private OAuth2AuthzSession account;
    private URL baseUrl;
    private HttpProvider mockProvider;
    
    public AuthzServiceTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mockStore = mock(MemoryStorage.class);
        mockProvider = mock(HttpProvider.class);
        service = new AuthzService() {

            @Override
            protected HttpProvider getHttpProvider(URL url) {
                return mockProvider;
            }
            
        };
        UnitTestUtils.setPrivateField(service, "sessionStore", mockStore);
        
        account = new OAuth2AuthzSession();
        account.setAccessToken("testToken");
        account.setAccountId("testAccountId");
        account.setAuthorizationCode(null);
        account.setCliendId("testClientId");
        account.setRefreshToken("testRefreshToken");

        baseUrl = new URL("http://example.com");
        
    }

    public void testFetchTokenReturnsNullForNoAccount() throws OAuth2AuthorizationException {
        assertEquals(null, service.fetchAccessToken("testAccount", new AuthzConfig(null, null)));
    }

    public void testFetchTokenForFreshAccount() throws OAuth2AuthorizationException {
        account.setExpires_on(hourFromNow());
        when(mockStore.read(eq("testAccountId"))).thenReturn(account);

        assertEquals("testToken", service.fetchAccessToken("testAccountId", new AuthzConfig(null, null)));
    }

    public void testExchangeToken() {

    }

    public void testRefreshToken() throws OAuth2AuthorizationException {
        account.setExpires_on(hourAgo());
        when(mockStore.read(eq("testAccountId"))).thenReturn(account);

        when(mockProvider.post((byte[])any())).thenAnswer(new Answer<HeaderAndBody>(){

            @Override
            public HeaderAndBody answer(InvocationOnMock invocation) throws Throwable {
                
                JsonObject object = new JsonObject();
                object.addProperty("access_token", "testRefreshedAccessToken");
                object.addProperty("expires_in", 3600);
                object.addProperty("refresh_token", "testRefreshToken");
                
                
                return new HeaderAndBody(object.toString().getBytes(), new HashMap<String, Object>());
            }
        });
        
        assertEquals("testRefreshedAccessToken", service.fetchAccessToken("testAccountId", new AuthzConfig(baseUrl, null)));
    }

    private long hourFromNow() {
        Calendar hourFromNow = Calendar.getInstance();
        hourFromNow.set(HOUR, hourFromNow.get(HOUR) + 1);
        return hourFromNow.getTimeInMillis();
    }

    private long hourAgo() {
        Calendar hourFromNow = Calendar.getInstance();
        hourFromNow.set(HOUR, hourFromNow.get(HOUR) - 1);
        return hourFromNow.getTimeInMillis();
    }

}
