package yucatan.core.communication;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

/**
 * JUnit tests for {@link yucatan.core.communication.SequenceDataTransportMap}.
 * 
 */
public class TestSequenceDataTransportMap {

	private SequenceDataTransportMap transportItem;

	/**
	 * Test method for {@link yucatan.core.communication.SequenceDataTransportMap#SequenceDataTransportMap()}.
	 */
	@Test
	public void testSequenceDataTransportMap() {
		// create item without param
		transportItem = new SequenceDataTransportMap();
	}

	/**
	 * Test method for {@link yucatan.core.communication.SequenceDataTransportMap#SequenceDataTransportMap(java.util.HashMap)}.
	 */
	@Test
	public void testSequenceDataTransportMapHashMap() {
		// create new sequence datatransporter by passing a null parameter
		transportItem = new SequenceDataTransportMap(null);

		// pass initial data
		// prepare inital data with a complex object (HttpParameterMap with HttpParameterValues)
		HttpParameterMap httpParams = new HttpParameterMap();
		httpParams.put("objectid", "123456");

		// create a SequenceDataTransportMap with initial data (the HttpParameterMap)
		HashMap<String, Object> initalTransportData = new HashMap<String, Object>();
		initalTransportData.put("httpParameters", httpParams);
		transportItem = new SequenceDataTransportMap(initalTransportData);

		// check values after type cast
		HttpParameterMap transportedHttpParams = (HttpParameterMap) transportItem.get("httpParameters");
		assertEquals("123456", transportedHttpParams.get("objectid").getValue());
	}

	/**
	 * Test method for {@link yucatan.core.communication.SequenceDataTransportMap#put(java.lang.String, Object)}.
	 */
	@Test
	public void testPut() {
		transportItem = new SequenceDataTransportMap();

		// null parameter check
		assertEquals(null, transportItem.put(null, null)); // the old value is null too
		assertEquals(null, transportItem.get(null));

		assertEquals(null, transportItem.put("null", null)); // still the old value is null
		assertEquals(null, transportItem.get("null"));

		// put and get a string
		assertEquals(null, transportItem.put("null", "null")); // also here the old value is still null
		assertEquals("null", transportItem.get("null"));
		assertEquals("null", transportItem.put("null", "new-null")); // now the old value is the "null" string
	}

	/**
	 * Test method for {@link yucatan.core.communication.SequenceDataTransportMap#get(java.lang.String)}.
	 */
	@Test
	public void testGet() {
		transportItem = new SequenceDataTransportMap();

		// null parameter check
		assertEquals(null, transportItem.get(null));
		assertEquals(null, transportItem.get("non-existent-key"));
	}

}
