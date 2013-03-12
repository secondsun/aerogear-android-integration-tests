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
package org.jboss.aerogear.android.authentication.impl.loader.support;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.test.AndroidTestCase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.jboss.aerogear.android.authentication.impl.loader.support.SupportAuthenticationModuleAdapter;
import org.jboss.aerogear.android.authentication.impl.loader.support.SupportEnrollLoader;
import org.jboss.aerogear.android.authentication.impl.loader.support.SupportLoginLoader;
import org.jboss.aerogear.android.authentication.impl.loader.support.SupportLogoutLoader;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.impl.helper.UnitTestUtils;
import org.jboss.aerogear.android.impl.util.VoidCallback;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

public class SupportAuthenticationLoaderAdapterTest extends AndroidTestCase {

    private static final String USERNAME_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.USERNAME";
    private static final String PASSWORD_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.PASSWORD";
    private static final String PARAMS_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.PARAMS";
    private static final String CALLBACK_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.CALLBACK";
    private static final String METHOD_KEY = "org.jboss.aerogear.android.authentication.loader.ModernAuthenticationModuleAdapter.METHOD";
    private static String USERNAME = "testUsername";
    private static String PASSWORD = "testPassword";
    private FragmentActivity activity;
    private Fragment fragment;
    private LoaderManager manager;
    private ArgumentCaptor<Integer> idMatcher;
    private ArgumentCaptor<Bundle> bundleMatcher;
    private VoidCallback callback;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = mock(FragmentActivity.class);
        fragment = mock(Fragment.class);
        manager = mock(LoaderManager.class);

        callback = new VoidCallback();
        idMatcher = ArgumentCaptor.forClass(Integer.class);
        bundleMatcher = ArgumentCaptor.forClass(Bundle.class);

        when(activity.getSupportLoaderManager()).thenReturn(manager);
        when(activity.getApplicationContext()).thenReturn(getContext());
        when(fragment.getLoaderManager()).thenReturn(manager);
    }
    
    
    public void testCreateEnrollLoader() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    	SupportAuthenticationModuleAdapter authModule = new SupportAuthenticationModuleAdapter(activity, null, "name");
        Bundle bundle = new Bundle();
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("username", "new User name");
        Object method = SupportAuthenticationModuleAdapter.Methods.ENROLL;
        bundle.putSerializable(METHOD_KEY, (Serializable) method);
        bundle.putSerializable(PARAMS_KEY, data);
        SupportEnrollLoader loader = (SupportEnrollLoader) authModule.onCreateLoader(-1, bundle);
        @SuppressWarnings("unchecked")
		Map<String, String> result = (Map<String, String>) UnitTestUtils.getPrivateField(loader, "params");
        assertEquals("new User name", result.get("username"));
    }
    
    public void testCreateLoginLoader() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    	SupportAuthenticationModuleAdapter authModule = new SupportAuthenticationModuleAdapter(activity, null, "name");
        Bundle bundle = new Bundle();
        
        Object method = SupportAuthenticationModuleAdapter.Methods.LOGIN;
        bundle.putSerializable(METHOD_KEY, (Serializable) method);
        bundle.putSerializable(USERNAME_KEY, USERNAME);
        bundle.putSerializable(PASSWORD_KEY, PASSWORD);
        
        SupportLoginLoader loader = (SupportLoginLoader) authModule.onCreateLoader(-1, bundle);
        @SuppressWarnings("unchecked")
        String username = UnitTestUtils.getPrivateField(loader, "username").toString();
        String password = UnitTestUtils.getPrivateField(loader, "password").toString();
        assertEquals(USERNAME, username);
        assertEquals(PASSWORD, password);
    }
    
    public void testCreateLogoutLoader() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
    	SupportAuthenticationModuleAdapter authModule = new SupportAuthenticationModuleAdapter(activity, null, "name");
        Bundle bundle = new Bundle();
        Object method = SupportAuthenticationModuleAdapter.Methods.LOGOUT;
        bundle.putSerializable(METHOD_KEY, (Serializable) method);
        SupportLogoutLoader loader = (SupportLogoutLoader) authModule.onCreateLoader(-1, bundle);
        assertNotNull(loader);
    }

    public void testActivityLogin() {
        SupportAuthenticationModuleAdapter authModule = new SupportAuthenticationModuleAdapter(activity, null, "name");
        authModule.login(USERNAME, PASSWORD, callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();
        assertNotNull(bundle);
        assertEquals("LOGIN", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(USERNAME, bundle.get(USERNAME_KEY));
        assertEquals(PASSWORD, bundle.get(PASSWORD_KEY));

    }

    public void testActivityEnroll() {
        SupportAuthenticationModuleAdapter authModule = new SupportAuthenticationModuleAdapter(activity, null, "name");
        Map<String, String> userData = makeUserData();
        authModule.enroll(userData, callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();
        assertNotNull(bundle);
        assertEquals("ENROLL", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(userData, bundle.get(PARAMS_KEY));


    }

    public void testActivityLogout() {
        SupportAuthenticationModuleAdapter authModule = new SupportAuthenticationModuleAdapter(activity, null, "name");
        authModule.logout(callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();

        assertNotNull(bundle);
        assertEquals("LOGOUT", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(callback, bundle.get(CALLBACK_KEY));
    }

    public void testFragmentLogin() {
        SupportAuthenticationModuleAdapter authModule = new SupportAuthenticationModuleAdapter(fragment, this.mContext, null, "name");
        authModule.login(USERNAME, PASSWORD, callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();
        assertNotNull(bundle);
        assertEquals("LOGIN", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(USERNAME, bundle.get(USERNAME_KEY));
        assertEquals(PASSWORD, bundle.get(PASSWORD_KEY));

    }

    public void testFragmentEnroll() {
        SupportAuthenticationModuleAdapter authModule = new SupportAuthenticationModuleAdapter(fragment, this.mContext, null, "name");
        Map<String, String> userData = makeUserData();
        authModule.enroll(userData, callback);
        verify(manager).initLoader(idMatcher.capture(), bundleMatcher.capture(), (LoaderManager.LoaderCallbacks) any());
        Bundle bundle = bundleMatcher.getValue();
        assertNotNull(bundle);
        assertEquals("ENROLL", ((Enum) bundle.get(METHOD_KEY)).name());
        assertEquals(userData, bundle.get(PARAMS_KEY));


    }

    public void testFragmentLogout() {
        SupportAuthenticationModuleAdapter authModule = new SupportAuthenticationModuleAdapter(fragment, this.mContext, null, "name");
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
