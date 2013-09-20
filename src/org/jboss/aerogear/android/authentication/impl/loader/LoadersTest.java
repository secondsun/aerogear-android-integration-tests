/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.aerogear.android.authentication.impl.loader;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import static junit.framework.Assert.assertNotNull;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.authentication.AuthenticationModule;
import org.jboss.aerogear.android.http.HeaderAndBody;
import org.jboss.aerogear.android.impl.util.VoidCallback;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class LoadersTest extends android.test.ActivityInstrumentationTestCase2<MainActivity> {

    public LoadersTest() {
        super(MainActivity.class);
    }

    
    
    final HeaderAndBody response = new HeaderAndBody(new byte[]{1, 2, 3, 4}, new HashMap<String, Object>());
    AuthenticationModule module;
    VoidCallback callback;

    @Override
    public void setUp() {
        module = mock(AuthenticationModule.class);
        callback = new VoidCallback();
        doAnswer(new ResponseAnswer<HeaderAndBody>(response)).when(module).enroll(any(Map.class), any(Callback.class));
        doAnswer(new ResponseAnswer<HeaderAndBody>(response)).when(module).login(anyString(), anyString(), any(Callback.class));
        doAnswer(new ResponseAnswer<HeaderAndBody>(response)).when(module).login(anyMapOf(String.class, String.class), any(Callback.class));
        doAnswer(new ResponseAnswer<Void>(null)).when(module).logout(any(Callback.class));

    }

    public void testEnrollLoader() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("test", "test");
        EnrollLoader loader = new EnrollLoader(getActivity(), callback, module, params);
        loader.loadInBackground();
        verify(module).enroll(eq(params), any(Callback.class));
    }

    public void testLogoutLoader() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("test", "test");
        LogoutLoader loader = new LogoutLoader(getActivity(), callback, module);
        loader.loadInBackground();
        verify(module).logout(any(Callback.class));
    }

    public void testLoginLoader() {
        Map<String, String> loginData = new HashMap<String, String>();
        LoginLoader loader = new LoginLoader(getActivity(), callback, module, loginData);
        loader.loadInBackground();
        verify(module).login(anyMapOf(String.class, String.class), any(Callback.class));
    }
    
    public void testLoginLoaderDoesNotCache() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException, InterruptedException {
        
        AuthenticationModule mockModule = mock(AuthenticationModule.class);
        final AtomicReference<HashMap> mapRef = new AtomicReference<HashMap>();
        final CountDownLatch latch = new CountDownLatch(2);
        Mockito.doAnswer(new Answer() {

            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                mapRef.set(new HashMap());
                latch.countDown();
                return null;
            }
        }).when(mockModule).login(anyString(), anyString(), (Callback<HeaderAndBody>) any());
        
        AuthenticationModuleAdapter adapter = new AuthenticationModuleAdapter(getActivity(), mockModule, "ignore");
        adapter.login("username", "password", new VoidCallback(latch));
        
        
        Map firstMap = mapRef.get();
        
        adapter.login("username", "password", new VoidCallback(latch));
        assertTrue(latch.await(5, TimeUnit.SECONDS));
        Map secondMap = mapRef.get();
        
        assertNotNull(firstMap);
        assertNotNull(secondMap);
        assertFalse(firstMap ==  secondMap);
        
    }

    private final class ResponseAnswer<T> implements Answer<Void> {

        T response;
        
        public ResponseAnswer(T response) {
            this.response = response;
        }
        
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            Callback callback = null;
            for (Object argument : invocation.getArguments()) {
                if (argument instanceof Callback) {
                    callback = (Callback) argument;
                }
            }

            if (callback == null) {
                throw new IllegalArgumentException("A callback was not passed");
            }

            callback.onSuccess(response);

            return null;
        }
    }
   
}
