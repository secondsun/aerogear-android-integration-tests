/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.aerogear.android.impl.security;

import android.util.Log;
import java.util.Arrays;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;
import org.jboss.aerogear.android.security.CryptoConfig;
import org.jboss.aerogear.crypto.CryptoBox;
import org.jboss.aerogear.fixture.TestVectors;

public class PasswordKeyServicesTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    
    
    public PasswordKeyServicesTest() {
        super(MainActivity.class);
    }
    
    public void testPasswordKeyServicesEncrypt() {
        PasswordKeyServices service = new PasswordKeyServices();
        String message = "This is a test message";
        CryptoConfig config = new CryptoConfig();
        config.setAlias("TestAlias");
        service.setPhrase("testPhrase");
        CryptoBox box = service.getCrypto(getActivity(), config);
        byte[] encrypted = box.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());
        
        assertFalse(Arrays.equals(encrypted, message.getBytes()));
        byte[] decrypted = box.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));
        
    }
    
    
    public void testPasswordKeyServicesEncryptShareKey() {
        PasswordKeyServices service = new PasswordKeyServices();
        PasswordKeyServices service2 = new PasswordKeyServices();
        String message = "This is a test message";
        CryptoConfig config = new CryptoConfig();
        config.setAlias("TestAlias");
        service.setPhrase("testPhrase");
        service2.setPhrase("testPhrase");
        CryptoBox box = service.getCrypto(getActivity(), config);
        byte[] encrypted = box.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());
        
        assertFalse(Arrays.equals(encrypted, message.getBytes()));
        box = service2.getCrypto(getActivity(), config);
        byte[] decrypted = box.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));
    }
    
    public void testBadpassPhraseCrashes() {
        PasswordKeyServices service = new PasswordKeyServices();
        
        String message = "This is a test message";
        CryptoConfig config = new CryptoConfig();
        config.setAlias("TestAlias");
        service.setPhrase("failPhrase");
        try {
            service.getCrypto(getActivity(), config);
        } catch (Exception e) {
            return;
        }
        
        fail();
        
    }
    
}
