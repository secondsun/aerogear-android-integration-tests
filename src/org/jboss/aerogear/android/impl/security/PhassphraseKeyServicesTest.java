package org.jboss.aerogear.android.impl.security;

import java.util.Arrays;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.impl.util.PatchedActivityInstrumentationTestCase;
import org.jboss.aerogear.android.security.CryptoConfig;
import org.jboss.aerogear.crypto.CryptoBox;
import org.jboss.aerogear.fixture.TestVectors;


public class PhassphraseKeyServicesTest extends PatchedActivityInstrumentationTestCase<MainActivity> {

    public PhassphraseKeyServicesTest() {
        super(MainActivity.class);
    }

    public void testPassphraseKeyServicesEncrypt() {
        PassPhraseKeyServices service = new PassPhraseKeyServices();
        String message = "This is a test message";
        CryptoConfig config = new CryptoConfig();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        CryptoBox box = service.getCrypto(getActivity(), config);
        byte[] encrypted = box.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());
        
        assertFalse(Arrays.equals(encrypted, message.getBytes()));
        byte[] decrypted = box.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));
        
    }
    
    
    public void testPassphraseKeyServicesGeneratesSame() {
        PassPhraseKeyServices service = new PassPhraseKeyServices();
        
        String message = "This is a test message";
        CryptoConfig config = new CryptoConfig();
        config.setAlias("TestAlias");
        config.setPassword("testPhrase");
        
        CryptoBox box = service.getCrypto(getActivity(), config);
        byte[] encrypted = box.encrypt(TestVectors.CRYPTOBOX_IV.getBytes(), message.getBytes());
        
        assertFalse(Arrays.equals(encrypted, message.getBytes()));
        box = service.getCrypto(getActivity(), config);
        byte[] decrypted = box.decrypt(TestVectors.CRYPTOBOX_IV.getBytes(), encrypted);
        assertTrue(Arrays.equals(decrypted, message.getBytes()));
    }
    
    public void testBadpassPhraseCrashes() {
        PassPhraseKeyServices service = new PassPhraseKeyServices();
        String message = "This is a test message";
        CryptoConfig config = new CryptoConfig();
        config.setAlias("TestAlias");
        config.setPassword("failPhrase");
        try {
            service.getCrypto(getActivity(), config);
        } catch (Exception e) {
            return;
        }
        
        fail();
        
    }    
}
