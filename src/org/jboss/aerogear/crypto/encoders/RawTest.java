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
package org.jboss.aerogear.crypto.encoders;

import android.test.AndroidTestCase;
import android.util.Log;
import java.util.Arrays;

import static junit.framework.Assert.assertNull;

public class RawTest extends AndroidTestCase {

    private static final String TAG = RawTest.class.getSimpleName();

    private Encoder encoder;

    @Override
    public void setUp() {
        encoder = new Raw();
    }

    public void testEncode() throws Exception {
        String value = "hello";
        assertEquals(value, encoder.encode(value.getBytes()));
    }

    public void testEncodeNullString() throws Exception {
        byte[] value = null;
        try {
            assertNull(encoder.encode(value));
        } catch (Exception e) {
            fail("Should not raise any exception");
        }
    }

    public void testDecode() throws Exception {
        String value = "hello";
        assertTrue(Arrays.equals(encoder.decode(value), value.getBytes()));
    }

    public void testDecodeNullString() throws Exception {
        String value = null;
        try {
            assertNull(encoder.decode(value));
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
            fail("Should not raise any exception");
        }
    }
}
