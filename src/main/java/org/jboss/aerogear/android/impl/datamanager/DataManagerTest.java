/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.aerogear.android.impl.datamanager;

import static org.jboss.aerogear.android.impl.datamanager.StoreTypes.MEMORY;
import static org.jboss.aerogear.android.impl.datamanager.StoreTypes.SQL;

import java.net.MalformedURLException;

import org.jboss.aerogear.MainActivity;
import org.jboss.aerogear.android.DataManager;
import org.jboss.aerogear.android.datamanager.IdGenerator;
import org.jboss.aerogear.android.datamanager.Store;
import org.jboss.aerogear.android.datamanager.StoreFactory;
import org.jboss.aerogear.android.impl.helper.Data;
import org.jboss.aerogear.android.impl.helper.UnitTestUtils;

import android.test.ActivityInstrumentationTestCase2;

public class DataManagerTest extends ActivityInstrumentationTestCase2<MainActivity> {

    public DataManagerTest() {
        super(MainActivity.class);
    }

    private DataManager dataManager;

    @Override
    public void setUp() {
        dataManager = new DataManager();
    }

    public void testConstructors() throws Exception {
        IdGenerator defaultGenerator = new DefaultIdGenerator();
        StoreFactory defaultFactory = new DefaultStoreFactory();
        DataManager manager = new DataManager(defaultGenerator);
        assertEquals(defaultGenerator, UnitTestUtils.getPrivateField(manager,
                            "idGenerator", IdGenerator.class));

        manager = new DataManager(defaultFactory);
        assertEquals(defaultFactory, UnitTestUtils.getPrivateField(manager,
                            "storeFactory", StoreFactory.class));

        manager = new DataManager(defaultGenerator, defaultFactory);
        assertEquals(defaultFactory, UnitTestUtils.getPrivateField(manager,
                            "storeFactory", StoreFactory.class));
        assertEquals(defaultGenerator, UnitTestUtils.getPrivateField(manager,
                            "idGenerator", IdGenerator.class));
    }

    public void testRegisterStoreFactory() throws MalformedURLException {
        @SuppressWarnings("LocalVariableHidesMemberVariable")
        DataManager dataManager = new DataManager(new StubStoreFactory());

        Store store = dataManager.store("stub store");

        assertNotNull("store could not be null", store);
        assertEquals("verifying the type", "Stub", store.getType().getName());
    }

    public void testCreateStoreWithDefaultType() {
        Store store = dataManager.store("foo");

        assertNotNull("store could not be null", store);
        assertEquals("verifying the type", MEMORY, store.getType());
    }

    public void testCreateStoreWithMemoryType() {
        Store store = dataManager.store("foo", new StoreConfig());

        assertNotNull("store could not be null", store);
        assertEquals("verifying the type", MEMORY, store.getType());
    }

    public void testAddStoreWithDefaultType() {
        dataManager.store("foo");
        Store store = dataManager.get("foo");

        assertNotNull("store could not be null", store);
        assertEquals("verifying the type", MEMORY, store.getType());
    }

    public void testAddStoreWithMemoryType() {
        dataManager.store("foo", new StoreConfig());
        Store store = dataManager.get("foo");

        assertNotNull("foo store could not be null", store);
        assertEquals("verifying the type", MEMORY, store.getType());
    }

    public void testAddStoreWithSQLType() {
        StoreConfig sqlStoreConfig = new StoreConfig();
        sqlStoreConfig.setContext(getActivity().getApplicationContext());
        sqlStoreConfig.setType(SQL);
        sqlStoreConfig.setKlass(Data.class);
        dataManager.store("foo", sqlStoreConfig);
        Store store = dataManager.get("foo");
        assertNotNull("foo store could not be null", store);
        assertEquals("verifying the type", SQL, store.getType());
    }

    public void testAndAddAndRemoveStores() {
        dataManager.store("foo", new StoreConfig());
        dataManager.store("bar");

        Store fooStore = dataManager.get("foo");
        assertNotNull("foo store could not be null", fooStore);

        Store barStore = dataManager.get("bar");
        assertNotNull("bar store could not be null", barStore);

        fooStore = dataManager.remove("foo");
        assertNotNull("foo store could not be null", fooStore);

        fooStore = dataManager.get("foo");
        assertNull("foo store should be null", fooStore);
    }

}
