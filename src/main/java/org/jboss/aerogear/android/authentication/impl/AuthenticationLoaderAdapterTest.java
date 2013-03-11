/**
 * JBoss, Home of Professional Open Source Copyright Red Hat, Inc., and
 * individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.jboss.aerogear.android.authentication.impl;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.os.Bundle;
import android.test.AndroidTestCase;
import java.util.HashMap;
import java.util.Map;
import org.jboss.aerogear.android.authentication.impl.loader.ModernAuthenticationModuleAdapter;
import org.jboss.aerogear.android.impl.util.VoidCallback;
import org.mockito.ArgumentCaptor;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AuthenticationLoaderAdapterTest extends AndroidTestCase {

    private static final String USERNAME_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.USERNAME";
    private static final String PASSWORD_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.PASSWORD";
    private static final String PARAMS_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.PARAMS";
    private static final String CALLBACK_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.CALLBACK";
    private static final String METHOD_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.METHOD";
    private static String USERNAME = "testUsername";
    private static String PASSWORD = "testPassword";
    private Activity activity;
    private Fragment fragment;
    private LoaderManager manager;
    private ArgumentCaptor<Integer> idMatcher;
    private ArgumentCaptor<Bundle> bundleMatcher;
    private VoidCallback callback;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = mock(Activity.class);
        fragment = mock(Fragment.class);
        manager = mock(LoaderManager.class);

        callback = new VoidCallback();
        idMatcher = ArgumentCaptor.forClass(Integer.class);
        bundleMatcher = ArgumentCaptor.forClass(Bundle.class);

        when(activity.getLoaderManager()).thenReturn(manager);
        when(fragment.getLoaderManager()).thenReturn(manager);
    }

    public void testActivityLogin() {
        ModernAuthenticationModuleAdapter authModule = new ModernAuthenticationModuleAdapter(activity, null, "name");
        authModule.login(USERNAME, PASSWORD, callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();
        assertNotNull(bundle);
        assertEquals("LOGIN", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(USERNAME, bundle.get(USERNAME_KEY));
        assertEquals(PASSWORD, bundle.get(PASSWORD_KEY));

    }

    public void testActivityEnroll() {
        ModernAuthenticationModuleAdapter authModule = new ModernAuthenticationModuleAdapter(activity, null, "name");
        Map<String, String> userData = makeUserData();
        authModule.enroll(userData, callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();
        assertNotNull(bundle);
        assertEquals("ENROLL", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(userData, bundle.get(PARAMS_KEY));


    }

    public void testActivityLogout() {
        ModernAuthenticationModuleAdapter authModule = new ModernAuthenticationModuleAdapter(activity, null, "name");
        authModule.logout(callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();

        assertNotNull(bundle);
        assertEquals("LOGOUT", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(callback, bundle.get(CALLBACK_KEY));
    }

    public void testFragmentLogin() {
        ModernAuthenticationModuleAdapter authModule = new ModernAuthenticationModuleAdapter(fragment, this.mContext, null, "name");
        authModule.login(USERNAME, PASSWORD, callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();
        assertNotNull(bundle);
        assertEquals("LOGIN", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(USERNAME, bundle.get(USERNAME_KEY));
        assertEquals(PASSWORD, bundle.get(PASSWORD_KEY));

    }

    public void testFragmentEnroll() {
        ModernAuthenticationModuleAdapter authModule = new ModernAuthenticationModuleAdapter(fragment, this.mContext, null, "name");
        Map<String, String> userData = makeUserData();
        authModule.enroll(userData, callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();
        assertNotNull(bundle);
        assertEquals("ENROLL", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(userData, bundle.get(PARAMS_KEY));


    }

    public void testFragmentLogout() {
        ModernAuthenticationModuleAdapter authModule = new ModernAuthenticationModuleAdapter(fragment, this.mContext, null, "name");
        authModule.logout(callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();

        assertNotNull(bundle);
        assertEquals("LOGOUT", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(callback, bundle.get(CALLBACK_KEY));
    }

    private Map<String, String> makeUserData() {
        Map<String, String> toReturn = new HashMap<String, String>();
        toReturn.put(USERNAME_KEY, USERNAME);
        toReturn.put(PASSWORD_KEY, PASSWORD);
        return toReturn;
    }
}
