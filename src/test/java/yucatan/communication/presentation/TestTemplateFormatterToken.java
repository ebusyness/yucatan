/**
 * 
 */
package yucatan.communication.presentation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

/**
 * Test cases for TestTemplateFormatterToken.
 * 
 */
public class TestTemplateFormatterToken {

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateFormatterToken#TemplateFormatterToken(java.lang.String)}.
	 */
	@Test
	public void testTemplateFormatterToken() {
		// null parameter check
		TemplateFormatterToken token = new TemplateFormatterToken(null, null);
		assertEquals(null, token.plainTextPart);
		assertEquals(null, token.argumentsPart);

		// check arguments
		ArrayList<String> alist = new ArrayList<String>();
		alist.add( "10.1" );
		alist.add( "\"some string\"");
		
		token = new TemplateFormatterToken("beautify(10.1;\"some string\")",alist);
		assertEquals("beautify(10.1;\"some string\")", token.plainTextPart);
		assertEquals(alist, token.argumentsPart);
	}

}
