/**
 * 
 */
package yucatan.communication;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

import yucatan.communication.HttpParameterMap;
import yucatan.communication.HttpParameterValue;



public class TestHttpParameterMap {

	/**
	 * Test method for {@link yucatan.communication.HttpParameterMap#HttpParameterMap()}.
	 */
	@Test
	public void testHttpParameterMap() {
		HttpParameterMap parameterMap = new HttpParameterMap();
		assertEquals(null, parameterMap.put("test", "test"));
	}

	/**
	 * Test method for {@link yucatan.communication.HttpParameterMap#HttpParameterMap(java.util.HashMap)}.
	 */
	@Test
	public void testHttpParameterMapHashMapOfStringHttpParameterValue() {
		// null param check
		HttpParameterMap httpParameterMap = new HttpParameterMap(null);

		// pass a HashMap
		HashMap<String, HttpParameterValue> paramMap = new HashMap<String, HttpParameterValue>();
		paramMap.put("key1", new HttpParameterValue("value1"));
		paramMap.put("key2", new HttpParameterValue("value2"));
		httpParameterMap = new HttpParameterMap(paramMap);
		assertEquals("value1", httpParameterMap.get("key1").getValue());
		assertEquals("value2", httpParameterMap.get("key2").getValue());
	}

	/**
	 * Test method for {@link yucatan.communication.HttpParameterMap#put(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testPutString() {
		HttpParameterMap httpParameterMap = new HttpParameterMap();
		
		// null param check
		assertEquals(null, httpParameterMap.put(null, "null")); // the old value is null too
		assertEquals(null, httpParameterMap.get(null));
		assertEquals(null, httpParameterMap.put("null", "null")); // still the old value is null
		assertEquals("null", httpParameterMap.get("null").getValue());
		assertEquals("null", httpParameterMap.put("null", "new-null").getValue()); // now the old value is "null"

		// TODO later - add read only
	}

	/**
	 * Test method for {@link yucatan.communication.HttpParameterMap#put(java.lang.String, yucatan.communication.HttpParameterValue)}.
	 */
	@Test
	public void testPutHttpParameterValue() {
		HttpParameterMap httpParameterMap = new HttpParameterMap();

		// null param check
		assertEquals(null, httpParameterMap.put(null, new HttpParameterValue(null))); // the old value is null too
		assertEquals(null, httpParameterMap.get(null));
		assertEquals(null, httpParameterMap.put("null", new HttpParameterValue("null"))); // still the old value is null
		assertEquals("null", httpParameterMap.get("null").getValue());
		assertEquals("null", httpParameterMap.put("null", new HttpParameterValue("new-null")).getValue()); // now the old value is "null"
		assertEquals("new-null", httpParameterMap.put("null", new HttpParameterValue(null)).getValue());
	}

	/**
	 * Test method for {@link yucatan.communication.HttpParameterMap#get(java.lang.String)}.
	 */
	@Test
	public void testGet() {
		HttpParameterMap httpParameterMap = new HttpParameterMap();

		// null param check
		assertEquals(null, httpParameterMap.get(null));
		HttpParameterValue myValue = new HttpParameterValue("myValue");
		httpParameterMap.put("myKey", myValue);
		assertEquals(myValue, httpParameterMap.get("myKey"));
	}

}
