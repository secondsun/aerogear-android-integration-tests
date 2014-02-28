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
package org.jboss.aerogear.crypto.keys;

import android.test.AndroidTestCase;
import java.util.Arrays;
import static junit.framework.Assert.fail;

import static org.jboss.aerogear.crypto.encoders.Encoder.HEX;
import static org.jboss.aerogear.fixture.TestVectors.BOB_SECRET_KEY;

public class PrivateKeyTest extends AndroidTestCase {

    public void testGeneratePrivateKey() {
        try {
            new PrivateKey();
        } catch (Exception e) {
            fail("Should not raise any exception and generate the private key");
        }
    }

    public void testAcceptsRawValidKey() throws Exception {
        try {
            byte[] rawKey = HEX.decode(BOB_SECRET_KEY);
            new PrivateKey(rawKey);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should return a valid key size");
        }
    }

    public void testAcceptsHexValidKey() throws Exception {
        try {
            new PrivateKey(BOB_SECRET_KEY, HEX);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should return a valid key size");
        }
    }

    public void testCreateHexValidKey() throws Exception {
        try {
            new PrivateKey(BOB_SECRET_KEY, HEX).toString();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should return a valid key size");
        }
    }

    public void testCreateByteValidKey() throws Exception {
        try {
            new PrivateKey(BOB_SECRET_KEY, HEX).toBytes();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Should return a valid key size");
        }
    }

    public void testRejectNullKey() throws Exception {
        try {
            byte[] key = null;
            new PrivateKey(key);
        } catch (RuntimeException e) {
            return;
        }

        fail("Should reject null keys");
    }

    public void testRejectShortKey() throws Exception {
        try {
            byte[] key = "short".getBytes();
            new PrivateKey(key);
        } catch (RuntimeException e) {
            return;
        }

        fail("Should reject short keys");
    }

    public void testPrivateKeyToString() throws Exception {
        try {
            PrivateKey key = new PrivateKey(BOB_SECRET_KEY, HEX);
            assertEquals("Correct private key expected", BOB_SECRET_KEY, key.toString());
        } catch (Exception e) {
            fail("Should return a valid key size");
        }
    }

    public void testPrivateKeyToBytes() throws Exception {
        try {
            PrivateKey key = new PrivateKey(BOB_SECRET_KEY, HEX);
            assertTrue("Correct private key expected", Arrays.equals(HEX.decode(BOB_SECRET_KEY),
                    key.toBytes()));
        } catch (Exception e) {
            fail("Should return a valid key size");
        }
    }
}
