/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.jboss.aerogear.android.impl.security;

import java.util.Arrays;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;
import org.jboss.aerogear.fixture.TestVectors;

public class PasswordKeyServicesTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    
    
    public PasswordKeyServicesTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        //Generate the keyStore with the correct password.
        PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig config = new PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        
        PasswordKeyServices service = new PasswordKeyServices(config, getActivity());
        
    }
    
    
    
    public void testPasswordKeyServicesEncrypt() {
        String message = "This is a test message";
        PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig config = new PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        
        PasswordKeyServices service = new PasswordKeyServices(config, getActivity());
        byte[] encrypted = service.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());
        
        assertFalse(Arrays.equals(encrypted, message.getBytes()));
        byte[] decrypted = service.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));
        
    }
    
    
    public void testPasswordKeyServicesEncryptShareKey() {
        PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig config = new PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        
        PasswordKeyServices service = new PasswordKeyServices(config, getActivity());
        PasswordKeyServices service2 = new PasswordKeyServices(config, getActivity());
        String message = "This is a test message";
        
        
        byte[] encrypted = service.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());
        
        assertFalse(Arrays.equals(encrypted, message.getBytes()));
        byte[] decrypted = service2.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));
    }
    
    public void testBadpassPhraseCrashes() {
        String message = "This is a test message";
        PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig config = new PasswordKeyServices.PasswordProtectedKeystoreCryptoConfig();
        config.setAlias("TestAlias");
        config.setPassword("failPhrase");
        
        try {
            new PasswordKeyServices(config, getActivity());
        } catch (Exception e) {
            return;
        }
        
        fail();
        
    }
    
}
