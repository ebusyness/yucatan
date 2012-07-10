/**
 * 
 */
package yucatan.communication.presentation;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

/**
 * @author Samuel
 * 
 */
public class TestTemplateProcessor {

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderSimplePlaceholders() {

		// test simple placholders
		// * doRender -> general expected result
		String template = "<html><body><h1>${@odd}</h1><ul>${@count}";
		assertEquals("<html><body><h1><PH></h1><ul><PH>", TemplateProcessor.doRender(new Object(), template));

		// * getTokens -> analyze tokens
		ArrayList<TemplateToken> tokens = TemplateProcessor.getTokens(template);
		TemplateToken token0 = tokens.get(0);
		TemplateToken token1 = tokens.get(1);
		TemplateToken token2 = tokens.get(2);
		TemplateToken token3 = tokens.get(3);
		// TemplateToken token4 = tokens.get(4);

		assertEquals(0, token0.tokenType.byteValue());
		assertEquals("<html><body><h1>", token0.plainText );

		assertEquals(1, token1.tokenType.byteValue());
		assertEquals("odd", token1.plainText);

		assertEquals(0, token2.tokenType.byteValue());
		assertEquals("</h1><ul>", token2.plainText);

		assertEquals(1, token3.tokenType.byteValue());
		assertEquals("count", token3.plainText);

		// simple placeholders at start position
		// * doRender -> general expected result
		template = "${@odd}<html><ul>${@data}";
		assertEquals("<PH><html><ul><PH>", TemplateProcessor.doRender(new Object(), template));

		// * getTokens -> analyze tokens
		tokens = TemplateProcessor.getTokens(template);
		token0 = tokens.get(0);
		token1 = tokens.get(1);
		token2 = tokens.get(2);


		assertEquals(1, token0.tokenType.byteValue());
		assertEquals("odd", token0.plainText);

		assertEquals(0, token1.tokenType.byteValue());
		assertEquals("<html><ul>", token1.plainText);

		assertEquals(1, token2.tokenType.byteValue());
		assertEquals("data", token2.plainText);


		// simple placeholder after simple placeholder
		template = "<html><body><h1>${@odd}${@count}</h1><ul>";
		assertEquals("<html><body><h1><PH><PH></h1><ul>", TemplateProcessor.doRender(new Object(), template));



	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderPlaceholdersWithData() {

		// test simple placholders
		String template = "";

		/*
		 * ${@even}${other-placeholder}</ul></body>";
		 */

		HashMap<String, String> hs = new HashMap<String, String>();
		hs.put("myKey", "myValue");
		template = "<html><body><h1>${@data(myKey)}${@even}</h1><ul>${@list(myKey)}</ul>${@count}</body>";
		System.out.println(TemplateProcessor.doRender(hs, template));

		// template = "${@data(myKey)}${@even}x${other-placeholder}";
		// System.out.println(TemplateProcessor.doRender(new Object(), template));

	}
}

