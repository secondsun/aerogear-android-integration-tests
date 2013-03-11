/**
 * JBoss, Home of Professional Open Source Copyright Red Hat, Inc., and
 * individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package org.jboss.aerogear.android.impl.pipeline;

import org.jboss.aerogear.android.impl.helper.UnitTestUtils;
import org.jboss.aerogear.android.impl.pipeline.ModernLoaderAdapter.CallbackHandler;
import org.jboss.aerogear.android.impl.pipeline.loader.ModernReadLoader;
import org.jboss.aerogear.android.impl.pipeline.loader.support.SupportReadLoader;
import org.jboss.aerogear.android.pipeline.AbstractActivityCallback;
import org.jboss.aerogear.android.pipeline.AbstractFragmentCallback;
import org.jboss.aerogear.android.pipeline.support.AbstractFragmentActivityCallback;
import org.jboss.aerogear.android.pipeline.support.AbstractSupportFragmentCallback;
import org.mockito.Mockito;

import android.app.Activity;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.test.AndroidTestCase;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CallbackTest extends AndroidTestCase {


	public void testPassModernFragmentCallbacks() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		Fragment fragment = Mockito.mock(Fragment.class);
		
		ModernLoaderAdapter adapter = new ModernLoaderAdapter(fragment, getContext(), null, null, "ignore");
		VoidFragmentCallback fragmentCallback = new VoidFragmentCallback();
		ModernReadLoader loader = Mockito.mock(ModernReadLoader.class);
		Mockito.when(loader.getCallback()).thenReturn(fragmentCallback);
		
		Object data = "Data"; 
		CallbackHandler handler = new CallbackHandler(adapter, loader, data);
		handler.run();
		assertTrue(fragmentCallback.successCalled);
		assertNull(UnitTestUtils.getSuperPrivateField(fragmentCallback, "fragment"));
		
	}
	
	public void testFailSupportFragmentCallbacks() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		android.support.v4.app.Fragment fragment = Mockito.mock(android.support.v4.app.Fragment.class);
		
		SupportLoaderAdapter adapter = new SupportLoaderAdapter(fragment, getContext(), null, null, "ignore");
		VoidSupportFragmentCallback fragmentCallback = new VoidSupportFragmentCallback();
		SupportReadLoader loader = Mockito.mock(SupportReadLoader.class);
		Mockito.when(loader.getCallback()).thenReturn(fragmentCallback);
		Mockito.when(loader.hasException()).thenReturn(true);
		Mockito.when(loader.getException()).thenReturn(new RuntimeException("This is only a test exception."));
		
		Object data = "Data"; 
		SupportLoaderAdapter.CallbackHandler handler = new SupportLoaderAdapter.CallbackHandler(adapter, loader, data);
		handler.run();
		assertTrue(fragmentCallback.failCalled);
		assertNull(UnitTestUtils.getSuperPrivateField(fragmentCallback, "fragment"));
		
	}
	
	public void testPassFragmentActivityCallbacks() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		FragmentActivity activity = Mockito.mock(FragmentActivity.class);
		
		SupportLoaderAdapter adapter = new SupportLoaderAdapter(activity, null, null, "ignore");
		VoidFragmentActivityCallback activityCallback = new VoidFragmentActivityCallback();
		SupportReadLoader loader = Mockito.mock(SupportReadLoader.class);
		Mockito.when(loader.getCallback()).thenReturn(activityCallback);
		
		Object data = "Data"; 
		SupportLoaderAdapter.CallbackHandler handler = new SupportLoaderAdapter.CallbackHandler(adapter, loader, data);
		handler.run();
		assertTrue(activityCallback.successCalled);
		assertNull(UnitTestUtils.getSuperPrivateField(activityCallback, "activity"));
		
	}
	
	public void testFailFragmentActivityCallbacks() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		FragmentActivity activity = Mockito.mock(FragmentActivity.class);
		
		SupportLoaderAdapter adapter = new SupportLoaderAdapter(activity, null, null, "ignore");
		VoidFragmentActivityCallback activityCallback = new VoidFragmentActivityCallback();
		SupportReadLoader loader = Mockito.mock(SupportReadLoader.class);
		Mockito.when(loader.getCallback()).thenReturn(activityCallback);
		Mockito.when(loader.hasException()).thenReturn(true);
		Mockito.when(loader.getException()).thenReturn(new RuntimeException("This is only a test exception."));
		
		Object data = "Data"; 
		SupportLoaderAdapter.CallbackHandler handler = new SupportLoaderAdapter.CallbackHandler(adapter, loader, data);
		handler.run();
		assertTrue(activityCallback.failCalled);
		assertNull(UnitTestUtils.getSuperPrivateField(activityCallback, "activity"));
		
	}
	
	public void testPassSupportFragmentCallbacks() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		android.support.v4.app.Fragment fragment = Mockito.mock(android.support.v4.app.Fragment.class);
		
		SupportLoaderAdapter adapter = new SupportLoaderAdapter(fragment, getContext(), null, null, "ignore");
		VoidSupportFragmentCallback fragmentCallback = new VoidSupportFragmentCallback();
		SupportReadLoader loader = Mockito.mock(SupportReadLoader.class);
		Mockito.when(loader.getCallback()).thenReturn(fragmentCallback);
		
		Object data = "Data"; 
		SupportLoaderAdapter.CallbackHandler handler = new SupportLoaderAdapter.CallbackHandler(adapter, loader, data);
		handler.run();
		assertTrue(fragmentCallback.successCalled);
		assertNull(UnitTestUtils.getSuperPrivateField(fragmentCallback, "fragment"));
		
	}
	
	public void testFailModernFragmentCallbacks() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		Fragment fragment = Mockito.mock(Fragment.class);
		
		ModernLoaderAdapter adapter = new ModernLoaderAdapter(fragment, getContext(), null, null, "ignore");
		VoidFragmentCallback fragmentCallback = new VoidFragmentCallback();
		ModernReadLoader loader = Mockito.mock(ModernReadLoader.class);
		Mockito.when(loader.getCallback()).thenReturn(fragmentCallback);
		Mockito.when(loader.hasException()).thenReturn(true);
		Mockito.when(loader.getException()).thenReturn(new RuntimeException("This is only a test exception."));
		
		Object data = "Data"; 
		CallbackHandler handler = new CallbackHandler(adapter, loader, data);
		handler.run();
		assertTrue(fragmentCallback.failCalled);
		assertNull(UnitTestUtils.getSuperPrivateField(fragmentCallback, "fragment"));
		
	}
	
	public void testPassModernActivityCallbacks() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		Activity activity = Mockito.mock(Activity.class);
		
		ModernLoaderAdapter adapter = new ModernLoaderAdapter(activity, null, null, "ignore");
		VoidActivityCallback activityCallback = new VoidActivityCallback();
		ModernReadLoader loader = Mockito.mock(ModernReadLoader.class);
		Mockito.when(loader.getCallback()).thenReturn(activityCallback);
		
		Object data = "Data"; 
		CallbackHandler handler = new CallbackHandler(adapter, loader, data);
		handler.run();
		assertTrue(activityCallback.successCalled);
		assertNull(UnitTestUtils.getSuperPrivateField(activityCallback, "activity"));
		
	}
	
	public void testFailModernActivityCallbacks() throws IllegalArgumentException, NoSuchFieldException, IllegalAccessException {
		Activity activity = Mockito.mock(Activity.class);
		
		ModernLoaderAdapter adapter = new ModernLoaderAdapter(activity, null, null, "ignore");
		VoidActivityCallback activityCallback = new VoidActivityCallback();
		ModernReadLoader loader = Mockito.mock(ModernReadLoader.class);
		Mockito.when(loader.getCallback()).thenReturn(activityCallback);
		Mockito.when(loader.hasException()).thenReturn(true);
		Mockito.when(loader.getException()).thenReturn(new RuntimeException("This is only a test exception."));
		
		Object data = "Data"; 
		CallbackHandler handler = new CallbackHandler(adapter, loader, data);
		handler.run();
		assertTrue(activityCallback.failCalled);
		assertNull(UnitTestUtils.getSuperPrivateField(activityCallback, "activity"));
		
	}
	
	private static class VoidFragmentCallback extends AbstractFragmentCallback<Object> {

		boolean successCalled = false;
		boolean failCalled = false;
		
		
		public VoidFragmentCallback() {
			super("HashCode");
		}
		
		@Override
		public void onSuccess(Object data) {
			assertNotNull(getFragment());
			successCalled = true;
		}

		@Override
		public void onFailure(Exception e) {
			assertNotNull(getFragment());
			failCalled = true;
		}
		
	}
	
	private static class VoidActivityCallback extends AbstractActivityCallback<Object> {

		boolean successCalled = false;
		boolean failCalled = false;
		
		
		public VoidActivityCallback() {
			super("HashCode");
		}
		
		@Override
		public void onSuccess(Object data) {
			assertNotNull(getActivity());
			successCalled = true;
		}

		@Override
		public void onFailure(Exception e) {
			assertNotNull(getActivity());
			failCalled = true;
		}
		
	}

	
	private static class VoidSupportFragmentCallback extends AbstractSupportFragmentCallback<Object> {

		boolean successCalled = false;
		boolean failCalled = false;
		
		
		public VoidSupportFragmentCallback() {
			super("HashCode");
		}
		
		@Override
		public void onSuccess(Object data) {
			assertNotNull(getFragment());
			successCalled = true;
		}

		@Override
		public void onFailure(Exception e) {
			assertNotNull(getFragment());
			failCalled = true;
		}
		
	}
	
	private static class VoidFragmentActivityCallback extends AbstractFragmentActivityCallback<Object> {

		boolean successCalled = false;
		boolean failCalled = false;
		
		
		public VoidFragmentActivityCallback() {
			super("HashCode");
		}
		
		@Override
		public void onSuccess(Object data) {
			assertNotNull(getFragmentActivity());
			successCalled = true;
		}

		@Override
		public void onFailure(Exception e) {
			assertNotNull(getFragmentActivity());
			failCalled = true;
		}
		
	}
	
}
