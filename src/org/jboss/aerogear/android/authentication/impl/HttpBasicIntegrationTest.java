/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.aerogear.android.authentication.impl;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.Pipeline;
import org.jboss.aerogear.android.authentication.AuthenticationConfig;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.impl.pipeline.PipeConfig;
import org.jboss.aerogear.android.impl.util.VoidCallback;
import org.jboss.aerogear.android.pipeline.Pipe;

import android.test.ActivityInstrumentationTestCase2;

public class HttpBasicIntegrationTest  extends ActivityInstrumentationTestCase2 implements AuthenticationModuleTest {
    
    private static final URL CONTROLLER_URL;
    private static final PipeConfig AUTOBOT_CONFIG;
    private static final Pipeline PIPELINE;
    private static final Authenticator AUTHENTICATOR;
    private static final AuthenticationConfig AUTHENTICATION_CONFIG;
    
    static {
        try {
            CONTROLLER_URL = new URL("http://controller-aerogear.rhcloud.com/aerogear-controller-demo/");
            AUTOBOT_CONFIG = new PipeConfig(CONTROLLER_URL, String.class);
            AUTOBOT_CONFIG.setEndpoint("autobots");
            PIPELINE = new Pipeline(CONTROLLER_URL);
            AUTHENTICATOR = new Authenticator(CONTROLLER_URL);
            AUTHENTICATION_CONFIG = new AuthenticationConfig();
            AUTHENTICATION_CONFIG.setAuthType(AuthTypes.HTTP_BASIC);
            
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public HttpBasicIntegrationTest() {
        super(MainActivity.class);
    }

    public void testBadLogin() throws InterruptedException {
        HttpBasicAuthenticationModule basicAuthModule = new HttpBasicAuthenticationModule(CONTROLLER_URL, AUTHENTICATION_CONFIG);
        final AtomicBoolean success = new AtomicBoolean(false);
        AUTOBOT_CONFIG.setAuthModule(basicAuthModule);
        basicAuthModule.login("fakeUser", "fakePass", new Callback<HeaderAndBody>() {

			@Override
			public void onFailure(Exception arg0) {
			}

			@Override
			public void onSuccess(HeaderAndBody arg0) {
			}
		});
        Pipe<String> autobots = PIPELINE.pipe(String.class, AUTOBOT_CONFIG);
        final CountDownLatch latch = new CountDownLatch(1);
        
        
        autobots.read(new Callback<List<String>>() {

            @Override
            public void onSuccess(List<String> data) {
                success.set(true);
                latch.countDown();
            }

            @Override
            public void onFailure(Exception e) {
            	latch.countDown();
            }
        });
        
        latch.await(10, TimeUnit.SECONDS);
        assertFalse(success.get());
    }
    
    public void testLogin() throws InterruptedException {
        HttpBasicAuthenticationModule basicAuthModule = new HttpBasicAuthenticationModule(CONTROLLER_URL, AUTHENTICATION_CONFIG);
        final AtomicBoolean success = new AtomicBoolean(false);
        AUTOBOT_CONFIG.setAuthModule(basicAuthModule);
        basicAuthModule.login("john", "123", new Callback<HeaderAndBody>() {

			@Override
			public void onFailure(Exception arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(HeaderAndBody arg0) {
				// TODO Auto-generated method stub
				
			}
		});
        Pipe<String> autobots = PIPELINE.pipe(String.class, AUTOBOT_CONFIG);
        final CountDownLatch latch = new CountDownLatch(1);
        
        
        autobots.read(new Callback<List<String>>() {

            @Override
            public void onSuccess(List<String> data) {
                success.set(true);
                latch.countDown();
            }

            @Override
            public void onFailure(Exception ignore) {
            	latch.countDown();
            }
        });
        
        latch.await(10, TimeUnit.SECONDS);
        assertTrue(success.get());
    }
    
    
    public void testLogout() throws InterruptedException {
        HttpBasicAuthenticationModule basicAuthModule = new HttpBasicAuthenticationModule(CONTROLLER_URL, AUTHENTICATION_CONFIG);
        final AtomicBoolean success = new AtomicBoolean(false);
        AUTOBOT_CONFIG.setAuthModule(basicAuthModule);
        basicAuthModule.login("john", "123", new Callback<HeaderAndBody>() {

			@Override
			public void onFailure(Exception arg0) {
			}

			@Override
			public void onSuccess(HeaderAndBody arg0) {
			}
		});
        Pipe<String> autobots = PIPELINE.pipe(String.class, AUTOBOT_CONFIG);
        final CountDownLatch latch = new CountDownLatch(1);
        
        
        autobots.read(new Callback<List<String>>() {

            @Override
            public void onSuccess(List<String> data) {
                success.set(true);
                latch.countDown();
            }

            @Override
            public void onFailure(Exception e) {
            	latch.countDown();
            }
        });
        
        latch.await(10, TimeUnit.SECONDS);
        assertTrue(success.get());
        
        final CountDownLatch latch2 = new CountDownLatch(1);
        
        
        basicAuthModule.logout(new VoidCallback());
        autobots.read(new Callback<List<String>>() {

            @Override
            public void onSuccess(List<String> data) {
                success.set(true);
                latch2.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                success.set(false);
            	latch2.countDown();
            }
        });
        
        latch2.await(10, TimeUnit.SECONDS);
        assertFalse(success.get());
        
        
    }

    
}
