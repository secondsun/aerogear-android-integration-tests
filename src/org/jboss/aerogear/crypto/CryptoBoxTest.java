/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.crypto;

import android.test.AndroidTestCase;
import org.jboss.aerogear.crypto.keys.KeyPair;
import org.jboss.aerogear.crypto.keys.PrivateKey;

import java.util.Arrays;
import static junit.framework.Assert.fail;

import static org.jboss.aerogear.crypto.encoders.Encoder.HEX;
import static org.jboss.aerogear.fixture.TestVectors.BOB_SECRET_KEY;
import static org.jboss.aerogear.fixture.TestVectors.BOX_MESSAGE;
import static org.jboss.aerogear.fixture.TestVectors.BOX_NONCE;
import static org.jboss.aerogear.fixture.TestVectors.CRYPTOBOX_CIPHERTEXT;
import static org.jboss.aerogear.fixture.TestVectors.CRYPTOBOX_IV;
import static org.jboss.aerogear.fixture.TestVectors.CRYPTOBOX_MESSAGE;

public class CryptoBoxTest extends AndroidTestCase {

    public void testAcceptStrings() throws Exception {
        try {
            new CryptoBox(BOB_SECRET_KEY, HEX);
        } catch (Exception e) {
            fail("CryptoBox should accept strings");
        }
    }

    public void testAcceptPrivateKey() throws Exception {
        try {
            new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
        } catch (Exception e) {
            fail("CryptoBox should accept key pairs");
        }
    }

    public void testNullPrivateKey() throws Exception {
        try {
            String key = null;
            new CryptoBox(new PrivateKey(key));
        } catch (RuntimeException e) {
            return;
        }

        fail("Should raise an exception");
    }

    public void testInvalidPrivateKey() throws Exception {
        try {
            String key = "hello";
            new CryptoBox(new PrivateKey(key));
        } catch (RuntimeException e) {
            return;
        }

        fail("Should raise an exception");
    }

    public void testEncryptRawBytes() throws Exception {
        CryptoBox cryptoBox = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
        byte[] IV = HEX.decode(CRYPTOBOX_IV);
        byte[] message = HEX.decode(CRYPTOBOX_MESSAGE);
        byte[] ciphertext = HEX.decode(CRYPTOBOX_CIPHERTEXT);
        byte[] result = cryptoBox.encrypt(IV, message);
        assertTrue("failed to generate ciphertext", Arrays.equals(result, ciphertext));
    }

    public void testDecryptRawBytes() throws Exception {
        CryptoBox cryptoBox = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
        byte[] IV = HEX.decode(CRYPTOBOX_IV);
        byte[] expectedMessage = HEX.decode(CRYPTOBOX_MESSAGE);
        byte[] ciphertext = cryptoBox.encrypt(IV, expectedMessage);

        CryptoBox pandora = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
        byte[] message = pandora.decrypt(IV, ciphertext);
        assertTrue("failed to decrypt ciphertext", Arrays.equals(message, expectedMessage));
    }

    public void testEncryptHexBytes() throws Exception {
        CryptoBox cryptoBox = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
        byte[] ciphertext = HEX.decode(CRYPTOBOX_CIPHERTEXT);

        byte[] result = cryptoBox.encrypt(CRYPTOBOX_IV, CRYPTOBOX_MESSAGE, HEX);
        assertTrue("failed to generate ciphertext", Arrays.equals(result, ciphertext));
    }

    public void testDecryptHexBytes() throws Exception {
        CryptoBox cryptoBox = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
        byte[] expectedMessage = HEX.decode(CRYPTOBOX_MESSAGE);
        byte[] ciphertext = cryptoBox.encrypt(CRYPTOBOX_IV, CRYPTOBOX_MESSAGE, HEX);

        CryptoBox pandora = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
        byte[] message = pandora.decrypt(CRYPTOBOX_IV, HEX.encode(ciphertext), HEX);
        assertTrue("failed to decrypt ciphertext", Arrays.equals(message, expectedMessage));
    }

    public void testDecryptCorruptedCipherText() throws Exception {
        try {
            CryptoBox cryptoBox = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
            byte[] IV = HEX.decode(CRYPTOBOX_IV);
            byte[] message = HEX.decode(CRYPTOBOX_MESSAGE);
            byte[] ciphertext = cryptoBox.encrypt(IV, message);
            ciphertext[23] = ' ';

            CryptoBox pandora = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
            pandora.decrypt(IV, ciphertext);
        } catch (RuntimeException e) {
            return;
        }

        fail("Should raise an exception");
    }

    public void testDecryptCorruptedIV() throws Exception {
        try {
            CryptoBox cryptoBox = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
            byte[] IV = HEX.decode(CRYPTOBOX_IV);
            byte[] message = HEX.decode(CRYPTOBOX_MESSAGE);
            byte[] ciphertext = cryptoBox.encrypt(IV, message);
            IV[23] = ' ';

            CryptoBox pandora = new CryptoBox(new PrivateKey(BOB_SECRET_KEY));
            pandora.decrypt(IV, ciphertext);
        } catch (RuntimeException e) {
            return;
        }

        fail("Should raise an exception");
    }

    public void testAcceptKeyPairs() throws Exception {
        try {
            KeyPair keyPair = new KeyPair();
            new CryptoBox(keyPair.getPrivateKey(), keyPair.getPublicKey());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Box should accept key pairs");
        }
    }

    public void testNullPublicKey() throws Exception {
        try {
            KeyPair keyPair = new KeyPair();
            new CryptoBox(keyPair.getPrivateKey(), null);
        } catch (RuntimeException e) {
            return;
        }

        fail("Should raise an exception");
    }

    public void testNullSecretKey() throws Exception {
        try {
            KeyPair keyPair = new KeyPair();
            new CryptoBox(null, keyPair.getPublicKey());
        } catch (RuntimeException e) {
            return;
        }

        fail("Should raise an exception");
    }

    public void testAsymmetricDecryptRawBytes() throws Exception {
        KeyPair keyPair = new KeyPair();
        KeyPair keyPairPandora = new KeyPair();

        CryptoBox cryptoBox = new CryptoBox(keyPair.getPrivateKey(), keyPairPandora.getPublicKey());
        byte[] IV = HEX.decode(BOX_NONCE);
        byte[] expectedMessage = HEX.decode(BOX_MESSAGE);
        byte[] ciphertext = cryptoBox.encrypt(IV, expectedMessage);

        CryptoBox pandora = new CryptoBox(keyPairPandora.getPrivateKey(), keyPair.getPublicKey());
        byte[] message = pandora.decrypt(IV, ciphertext);
        assertTrue("failed to decrypt ciphertext", Arrays.equals(message, expectedMessage));
    }

    public void testAsymmetricDecryptCorruptedCipherText() throws Exception {
        try {
            KeyPair keyPair = new KeyPair();
            CryptoBox box = new CryptoBox(keyPair.getPrivateKey(), keyPair.getPublicKey());
            byte[] nonce = HEX.decode(BOX_NONCE);
            byte[] message = HEX.decode(BOX_MESSAGE);
            byte[] ciphertext = box.encrypt(nonce, message);
            ciphertext[23] = ' ';

            KeyPair keyPairPandora = new KeyPair();
            CryptoBox pandora = new CryptoBox(keyPairPandora.getPrivateKey(), keyPairPandora.getPublicKey());
            pandora.decrypt(nonce, ciphertext);
        } catch (RuntimeException e) {
            return;
        }

        fail("Should raise an exception");
    }
}
