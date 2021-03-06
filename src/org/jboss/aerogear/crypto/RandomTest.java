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
import java.util.Arrays;

public class RandomTest extends AndroidTestCase {

    public void testProducesRandomBytes() throws Exception {
        final int size = 16;
        assertEquals("Invalid random bytes", size, new Random().randomBytes(size).length);
    }

    public void testProducesDefaultRandomBytes() throws Exception {
        final int size = 16;
        assertEquals("Invalid random bytes", size, new Random().randomBytes().length);
    }

    public void testProducesDifferentRandomBytes() throws Exception {
        final int size = 16;
        assertFalse("Should produce different random bytes", Arrays.equals(new Random().randomBytes(size), new Random().randomBytes(size)));
    }

    public void testProducesDifferentDefaultRandomBytes() throws Exception {
        final int size = 32;
        assertFalse("Should produce different random bytes", Arrays.equals(new Random().randomBytes(), new Random().randomBytes(size)));
    }
}
