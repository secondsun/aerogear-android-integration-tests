/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.aerogear.android.authentication.impl;

import android.test.ActivityInstrumentationTestCase2;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.authentication.AuthenticationModule;
import org.jboss.aerogear.android.impl.helper.UnitTestUtils;

public class AuthenticatorTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public AuthenticatorTest() {
        super(MainActivity.class);
    }

    private static final URL SIMPLE_URL;
    private static final String SIMPLE_MODULE_NAME = "simple";

    static {
        try {
            SIMPLE_URL = new URL("http", "localhost", 80, "/");
        } catch (MalformedURLException ex) {
            Logger.getLogger(AuthenticatorTest.class.getName()).log(
                    Level.SEVERE, null, ex);
            throw new RuntimeException(ex);
        }
    }

    public void testAuthenticatorConstructor() throws Exception {
        Authenticator authenticator = new Authenticator(SIMPLE_URL.toString());
        assertEquals(SIMPLE_URL, UnitTestUtils.getPrivateField(authenticator,
                "baseURL", URL.class));
    }

    public void testAddAuthenticatorFailsWithUnsupportedType() {
        try {
            Authenticator authenticator = new Authenticator(SIMPLE_URL);
            AGSecurityAuthenticationConfig config = new AGSecurityAuthenticationConfig();
            config.setAuthType(null);
            AuthenticationModule simpleAuthModule = authenticator.auth(
                    SIMPLE_MODULE_NAME, config);

            assertNotNull(simpleAuthModule);
        } catch (IllegalArgumentException ex) {
            return;
        }
        assertFalse("Illegal Argument exception should have been called.", true);
    }

    public void testAddSimpleAuthenticator() {

        Authenticator authenticator = new Authenticator(SIMPLE_URL);
        AuthenticationModule simpleAuthModule = authenticator.auth(
                SIMPLE_MODULE_NAME, new AGSecurityAuthenticationConfig());

        assertNotNull(simpleAuthModule);

    }

    public void testAddAndGetSimpleAuthenticator() {
        Authenticator authenticator = new Authenticator(SIMPLE_URL);
        AuthenticationModule simpleAuthModule = authenticator.auth(
                SIMPLE_MODULE_NAME, new AGSecurityAuthenticationConfig());
        assertEquals(simpleAuthModule, authenticator.get(SIMPLE_MODULE_NAME));
        authenticator.remove(SIMPLE_MODULE_NAME);
        assertNull(authenticator.get(SIMPLE_MODULE_NAME));
    }

    public void testAddAuthenticator() {

        Authenticator authenticator = new Authenticator(SIMPLE_URL);

        AGSecurityAuthenticationConfig config = new AGSecurityAuthenticationConfig();
        config.setAuthType(AuthTypes.AG_SECURITY);
        config.setEnrollEndpoint("testEnroll");
        config.setLoginEndpoint("testLogin");
        config.setLogoutEndpoint("testLogout");

        AuthenticationModule simpleAuthModule = authenticator.auth(
                SIMPLE_MODULE_NAME, config);

        assertEquals(simpleAuthModule, authenticator.get(SIMPLE_MODULE_NAME));
        assertEquals("testEnroll", simpleAuthModule.getEnrollEndpoint());
        assertEquals("testLogin", simpleAuthModule.getLoginEndpoint());
        assertEquals("testLogout", simpleAuthModule.getLogoutEndpoint());
    }

    public void testGetNullAuthModule() {

        Authenticator authenticator = new Authenticator(SIMPLE_URL);

        assertNull(authenticator.get(SIMPLE_MODULE_NAME));
    }
}
