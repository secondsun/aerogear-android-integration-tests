/**
 * JBoss, Home of Professional Open Source Copyright Red Hat, Inc., and
 * individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.jboss.aerogear.crypto.password;

import android.test.AndroidTestCase;
import static junit.framework.Assert.fail;
import org.jboss.aerogear.crypto.Random;

import static org.jboss.aerogear.fixture.TestVectors.INVALID_PASSWORD;
import static org.jboss.aerogear.fixture.TestVectors.PASSWORD;

public class Pbkdf2Test extends AndroidTestCase {

    public void testPasswordValidationWithRandomSaltProvided() throws Exception {
        Pbkdf2 pbkdf2 = new Pbkdf2();
        byte[] salt = new Random().randomBytes();
        byte[] rawPassword = pbkdf2.encrypt(PASSWORD, salt);
        assertTrue("Password should be valid", pbkdf2.validate(PASSWORD, rawPassword, salt));
    }

    public void testPasswordValidationWithSaltGenerated() throws Exception {
        Pbkdf2 pbkdf2 = new Pbkdf2();
        byte[] rawPassword = pbkdf2.encrypt(PASSWORD);
        assertTrue("Password should be valid", pbkdf2.validate(PASSWORD, rawPassword, pbkdf2.getSalt()));
    }

    public void testInvalidPasswordValidationWithRandomSaltProvided() throws Exception {
        Pbkdf2 pbkdf2 = new Pbkdf2();
        byte[] salt = new Random().randomBytes();
        byte[] rawPassword = pbkdf2.encrypt(PASSWORD, salt);
        assertFalse("Password should be valid", pbkdf2.validate(INVALID_PASSWORD, rawPassword, salt));
    }

    public void testInvalidPasswordValidationWithSaltGenerated() throws Exception {
        Pbkdf2 pbkdf2 = new Pbkdf2();
        byte[] rawPassword = pbkdf2.encrypt(PASSWORD);
        assertFalse("Password should be valid", pbkdf2.validate(INVALID_PASSWORD, rawPassword, pbkdf2.getSalt()));
    }

    public void testThrowExceptionWithPoorSaltProvided() throws Exception {
        try {
            Pbkdf2 pbkdf2 = new Pbkdf2();
            byte[] salt = "42".getBytes();
            pbkdf2.encrypt(PASSWORD, salt);
        } catch (RuntimeException e) {
            return;
        }
        fail("Expected RuntimeException");
    }

    public void testThrowExceptionWithPoorIterationProvided() throws Exception {
        try {
            Pbkdf2 pbkdf2 = new Pbkdf2();
            byte[] salt = new Random().randomBytes();
            int iterations = 42;
            pbkdf2.encrypt(PASSWORD, salt, iterations);
        } catch (RuntimeException e) {
            return;
        }
        fail("Expected RuntimeException");
    }
}
