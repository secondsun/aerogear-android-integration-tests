package org.jboss.aerogear.android.impl.security;

import java.security.SecureRandom;
import java.util.Arrays;
import static junit.framework.Assert.assertTrue;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;


public class PhassphraseKeyServicesTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    private static byte[] SALT = new SecureRandom().generateSeed(1024);
    
    public PhassphraseKeyServicesTest() {
        super(MainActivity.class);
    }

    public void testPassphraseKeyServicesEncrypt() {
        PassPhraseKeyServices.PassPhraseCryptoConfig config = new PassPhraseKeyServices.PassPhraseCryptoConfig();
        config.setPassphrase("testPhrase");
        config.setSalt(SALT);
        
        PassPhraseKeyServices service = new PassPhraseKeyServices(getActivity(), config);
        String message = "This is a test message";
        
        
        byte[] encrypted = service.encrypt(message.getBytes());
        
        
        byte[] decrypted = service.decrypt(encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));
        
    }
    
    
    
}
