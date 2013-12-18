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
package org.jboss.aerogear.android.impl.datamanager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import junit.framework.Assert;

import org.jboss.aerogear.android.Callback;
import org.jboss.aerogear.android.ReadFilter;
import org.jboss.aerogear.android.RecordId;
import org.jboss.aerogear.android.impl.helper.Data;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class SqlStoreTest extends AndroidTestCase {

    private Context context;
    private SQLStore<Data> store;
    private SQLStore<TrivialNestedClass> nestedStore;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.context = new RenamingDelegatingContext(getContext(), "test");
        this.store = new SQLStore<Data>(Data.class, context);
        this.nestedStore = new SQLStore<TrivialNestedClass>(TrivialNestedClass.class, context);
    }

    public void testSave() throws InterruptedException {

        Data data = new Data(10, "name", "description");
        saveData(10, "name", "description");
        Data readData = store.read(10);
        store.reset();
        Assert.assertEquals(data, readData);
    }

    public void testReset() throws InterruptedException {
        saveData(10, "name", "description");
        store.reset();
        Data readData = store.read(10);
        Assert.assertNull(readData);
    }

    public void testReadAll() throws InterruptedException, JSONException {
        loadBulkData();
        List<Data> allData = new ArrayList<Data>(store.readAll());
        Collections.sort(allData);
        Assert.assertEquals(6, allData.size());
        Assert.assertEquals("name", allData.get(0).getName());
        Assert.assertEquals("name2", allData.get(5).getName());

    }

    public void testRemove() throws InterruptedException, JSONException {
        loadBulkData();
        store.remove(1);

        List<Data> allData = new ArrayList<Data>(store.readAll());
        Collections.sort(allData);
        Assert.assertEquals(5, allData.size());
        Assert.assertEquals(2l, (long) allData.get(0).getId());
        Assert.assertEquals("name2", allData.get(4).getName());

    }

    public void testFilter() throws InterruptedException, JSONException {
        ReadFilter filter;
        JSONObject where;
        List<Data> result;

        loadBulkData();

        result = store.readWithFilter(null);
        System.out.println(result);
        Assert.assertEquals(result.toString(), 6, result.size());

        filter = new ReadFilter();
        where = new JSONObject();
        where.put("name", "name2");
        filter.setWhere(where);
        result = store.readWithFilter(filter);
        Assert.assertEquals(3, result.size());

        filter = new ReadFilter();
        where = new JSONObject();
        where.put("name", "name2");
        where.put("description", "description");
        filter.setWhere(where);
        result = store.readWithFilter(filter);
        Assert.assertEquals(2, result.size());

    }

    public void testNestedSaveAndFilter() throws InterruptedException, JSONException {
        ReadFilter filter;
        JSONObject where;
        List<TrivialNestedClass> result;

        Data data = new Data(10, "name", "description");

        TrivialNestedClass newNested = new TrivialNestedClass();
        newNested.setId(1);
        newNested.setText("nestedText");
        newNested.setData(data);

        open(nestedStore);
        nestedStore.save(newNested);

        filter = new ReadFilter();
        where = new JSONObject();
        where.put("text", "nestedText");
        JSONObject dataFilter = new JSONObject();
        dataFilter.put("id", 10);
        where.put("data", dataFilter);
        filter.setWhere(where);
        result = nestedStore.readWithFilter(filter);
        Assert.assertEquals(1, result.size());
        TrivialNestedClass nestedResult = result.get(0);
        Assert.assertEquals("name", nestedResult.data.getName());
        nestedStore.reset();
    }

    private void saveData(Integer id, String name, String desc) throws InterruptedException {
        open(store);
        store.save(new Data(id, name, desc));
    }

    private void open(SQLStore<?> store) throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        store.open(new Callback() {
            @Override
            public void onSuccess(Object data) {
                latch.countDown();
            }

            @Override
            public void onFailure(Exception e) {
                latch.countDown();
                throw new RuntimeException(e);
            }
        });
        latch.await();
    }

    private void loadBulkData() throws InterruptedException {
        saveData(1, "name", "description");
        saveData(2, "name", "description");
        saveData(3, "name2", "description");
        saveData(4, "name2", "description");
        saveData(5, "name", "description2");
        saveData(6, "name2", "description2");
    }

    public static final class TrivialNestedClass {

        @RecordId
        private Integer id;
        private String text;
        private Data data;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }
    }
}
