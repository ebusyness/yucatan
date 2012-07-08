/**
 * 
 */
package yucatan.communication;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import yucatan.communication.HttpParameterValue;

/**
 * @author Samuel
 *
 */
public class TestHttpParameterValue {

	/**
	 * Test method for {@link yucatan.communication.HttpParameterValue#HttpParameterValue(java.lang.String)}.
	 */
	@Test
	public void testHttpParameterValueString() {
		// null parameter check
		HttpParameterValue httpParam = new HttpParameterValue(null);

		// string value check
		httpParam = new HttpParameterValue("12345678910");
		assertEquals("12345678910", httpParam.getValue());
		// TODO - encoding stuff etc. as soon as needed
	}

	/**
	 * Test method for {@link yucatan.communication.HttpParameterValue#getValue()}.
	 */
	@Test
	public void testGetValue() {
		// empty string
		HttpParameterValue httpParam = new HttpParameterValue(new String());
		assertEquals("", httpParam.getValue());
	}

}
