package yucatan.communication;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

/**
 * JUnit tests for {@link yucatan.communication.SequenceDataMap}.
 * 
 */
public class TestSequenceDataMap {

	private SequenceDataMap transportItem;

	/**
	 * Test method for {@link yucatan.communication.SequenceDataMap#SequenceDataTransportMap()}.
	 */
	@Test
	public void testSequenceDataTransportMap() {
		// create item without param
		transportItem = new SequenceDataMap();
	}

	/**
	 * Test method for {@link yucatan.communication.SequenceDataMap#SequenceDataTransportMap(java.util.HashMap)}.
	 */
	@Test
	public void testSequenceDataTransportMapHashMap() {
		// create new sequence datatransporter by passing a null parameter
		transportItem = new SequenceDataMap(null);

		// pass initial data
		// prepare inital data with a complex object (HttpParameterMap with HttpParameterValues)
		HttpParameterMap httpParams = new HttpParameterMap();
		httpParams.put("objectid", "123456");

		// create a SequenceDataMap with initial data (the HttpParameterMap)
		HashMap<String, Object> initalTransportData = new HashMap<String, Object>();
		initalTransportData.put("httpParameters", httpParams);
		transportItem = new SequenceDataMap(initalTransportData);

		// check values after type cast
		HttpParameterMap transportedHttpParams = (HttpParameterMap) transportItem.get("httpParameters");
		assertEquals("123456", transportedHttpParams.get("objectid").getValue());
	}

	/**
	 * Test method for {@link yucatan.communication.SequenceDataMap#put(java.lang.String, Object)}.
	 */
	@Test
	public void testPut() {
		transportItem = new SequenceDataMap();
		HashMap<String, Object> initalTransportData = null;

		// null parameter check
		assertEquals(null, transportItem.put(null, initalTransportData)); // the old value is null too
		assertEquals(null, transportItem.get(null));

		assertEquals(null, transportItem.put("null", initalTransportData)); // still the old value is null
		assertEquals(null, transportItem.get("null"));

		// put and get a string
		assertEquals(null, transportItem.put("null", "null")); // also here the old value is still null
		assertEquals("null", transportItem.get("null"));
		assertEquals("null", transportItem.put("null", "new-null")); // now the old value is the "null" string
	}

	/**
	 * Test method for {@link yucatan.communication.SequenceDataMap#get(java.lang.String)}.
	 */
	@Test
	public void testGet() {
		transportItem = new SequenceDataMap();

		// null parameter check
		assertEquals(null, transportItem.get(null));
		assertEquals(null, transportItem.get("non-existent-key"));
	}

}
