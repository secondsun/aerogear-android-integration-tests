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
package org.jboss.aerogear.android.impl.security;

import android.content.Context;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;
import org.jboss.aerogear.android.security.CryptoConfig;
import org.jboss.aerogear.android.security.EncryptionService;
import org.jboss.aerogear.android.security.KeyManager;
import org.jboss.aerogear.android.security.KeyServiceFactory;
import org.jboss.aerogear.crypto.Random;

import static org.mockito.Mockito.mock;

public class KeyManagerTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    public KeyManagerTest() {
        super(MainActivity.class);
    }
    
    public void testPassPhraseKeyManager() {
        PassPhraseKeyServices.PassPhraseCryptoConfig config = new PassPhraseKeyServices.PassPhraseCryptoConfig();
        config.setPassphrase("testPhrase");
        config.setSalt(new Random().randomBytes(1024));
        
        KeyManager manager = new KeyManager();
        EncryptionService service1 = manager.encryptionService(config, "testService", getActivity());
        EncryptionService service2 = manager.get("testService");
        assertTrue(service1 instanceof PassPhraseKeyServices);
        assertSame(service1, service2);
        
        manager.remove("testService");
        assertNull(manager.get("testService"));
        
        
    }

    public void testFactory() {
        final EncryptionService mockService = mock(EncryptionService.class);
        
        KeyManager manager = new KeyManager(new KeyServiceFactory() {

            @Override
            public EncryptionService getService(CryptoConfig config, Context context) {
                return mockService;
            }
        });
        
        assertSame(mockService, manager.encryptionService(null,"testService", null));
        
        
    }
    
    public void testPasswordKeyManager() {
        PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig config = new PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        
        KeyManager manager = new KeyManager();
        EncryptionService service1 = manager.encryptionService(config, "testService", getActivity());
        EncryptionService service2 = manager.get("testService");
        
        assertSame(service1, service2);
        assertTrue(service1 instanceof PasswordKeyServices);
        manager.remove("testService");
        assertNull(manager.get("testService"));
        
        
    }
    
}
