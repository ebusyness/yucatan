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
	public void testDoRenderSimplePlaceholders1() {

		// test simple placholders
		// - one placeholder at the end of string
		// - another inbetween
		// * call doRender() -> general expected result
		String template = "<html><body><h1>${@odd}</h1><ul>${@count}";
		assertEquals("<html><body><h1><PH></h1><ul><PH>", TemplateProcessor.doRender(new Object(), template));

		// same test string but this time have a look at the tokens
		// * call getTokens() -> analyze tokens
		ArrayList<TemplateToken> tokens = TemplateProcessor.getTokens(template);
		TemplateToken token0 = tokens.get(0);
		TemplateToken token1 = tokens.get(1);
		TemplateToken token2 = tokens.get(2);
		TemplateToken token3 = tokens.get(3);

		assertEquals(0, token0.tokenType.byteValue());
		assertEquals("<html><body><h1>", token0.plainText);

		assertEquals(1, token1.tokenType.byteValue());
		assertEquals("odd", token1.plainText);

		assertEquals(0, token2.tokenType.byteValue());
		assertEquals("</h1><ul>", token2.plainText);

		assertEquals(1, token3.tokenType.byteValue());
		assertEquals("count", token3.plainText);

		assertEquals(4, tokens.size());
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderSimplePlaceholders2() {
		// simple placeholders at start position
		// - one placeholder at the beginning of string
		// - one placeholder at the end of string
		// * doRender -> general expected result
		String template = "${@odd}<html><ul>${@data}";
		assertEquals("<PH><html><ul><PH>", TemplateProcessor.doRender(new Object(), template));

		// same test string but this time have a look at the tokens
		// * getTokens -> analyze tokens
		ArrayList<TemplateToken> tokens = TemplateProcessor.getTokens(template);
		TemplateToken token0 = tokens.get(0);
		TemplateToken token1 = tokens.get(1);
		TemplateToken token2 = tokens.get(2);

		assertEquals(1, token0.tokenType.byteValue());
		assertEquals("odd", token0.plainText);

		assertEquals(0, token1.tokenType.byteValue());
		assertEquals("<html><ul>", token1.plainText);

		assertEquals(1, token2.tokenType.byteValue());
		assertEquals("data", token2.plainText);

		assertEquals(3, tokens.size());
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderSimplePlaceholders3() {

		// simple placeholder after an simple placeholder
		// - two directly following simple placeholders -
		String template = "<html><body><h1>${@odd}${@count}</h1><ul>";
		assertEquals("<html><body><h1><PH><PH></h1><ul>", TemplateProcessor.doRender(new Object(), template));

		// same test string but this time have a look at the tokens
		// * getTokens -> analyze tokens
		ArrayList<TemplateToken> tokens = TemplateProcessor.getTokens(template);
		TemplateToken token0 = tokens.get(0);
		TemplateToken token1 = tokens.get(1);
		TemplateToken token2 = tokens.get(2);
		TemplateToken token3 = tokens.get(3);

		assertEquals(0, token0.tokenType.byteValue());
		assertEquals("<html><body><h1>", token0.plainText);

		assertEquals(1, token1.tokenType.byteValue());
		assertEquals("odd", token1.plainText);

		assertEquals(1, token2.tokenType.byteValue());
		assertEquals("count", token2.plainText);

		assertEquals(0, token3.tokenType.byteValue());
		assertEquals("</h1><ul>", token3.plainText);

		assertEquals(4, tokens.size());
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderSimplePlaceholders4() {
		// simple placeholder after an simple placeholder
		// - seperated by one character
		String template = "<h1>${@odd}-${@count}</h1>";
		TemplateProcessor.doRender(new Object(), template);
		assertEquals("<h1><PH>-<PH></h1>", TemplateProcessor.doRender(new Object(), template));

		// same test string but this time have a look at the tokens
		// * getTokens -> analyze tokens
		ArrayList<TemplateToken> tokens = TemplateProcessor.getTokens(template);
		TemplateToken token0 = tokens.get(0);
		TemplateToken token1 = tokens.get(1);
		TemplateToken token2 = tokens.get(2);
		TemplateToken token3 = tokens.get(3);
		TemplateToken token4 = tokens.get(4);

		assertEquals(0, token0.tokenType.byteValue());
		assertEquals("<h1>", token0.plainText);

		assertEquals(1, token1.tokenType.byteValue());
		assertEquals("odd", token1.plainText);

		assertEquals(0, token2.tokenType.byteValue());
		assertEquals("-", token2.plainText);

		assertEquals(1, token3.tokenType.byteValue());
		assertEquals("count", token3.plainText);

		assertEquals(0, token4.tokenType.byteValue());
		assertEquals("</h1>", token4.plainText);

		assertEquals(5, tokens.size());
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderSimplePlaceholders5() {
		// invalid placeholder format
		String template = "<h1>${other-placeholder}</h1>";
		assertEquals("<h1>${other-placeholder}</h1>", TemplateProcessor.doRender(new Object(), template));

		// same test string but this time have a look at the tokens
		// * getTokens -> analyze tokens
		ArrayList<TemplateToken> tokens = TemplateProcessor.getTokens(template);
		TemplateToken token0 = tokens.get(0);
		TemplateToken token1 = tokens.get(1);

		assertEquals(0, token0.tokenType.byteValue());
		assertEquals("<h1>", token0.plainText);
		assertEquals(0, token1.tokenType.byteValue());
		assertEquals("${other-placeholder}</h1>", token1.plainText);

		assertEquals(2, tokens.size());
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderSimplePlaceholders6() {
		// invalid placeholder followed by another invalid placeholder
		String template = "<h1>${other-placeholder}${other-placeholder}</h1>";
		assertEquals("<h1>${other-placeholder}${other-placeholder}</h1>", TemplateProcessor.doRender(new Object(), template));

		// same test string but this time have a look at the tokens
		// * getTokens -> analyze tokens
		ArrayList<TemplateToken> tokens = TemplateProcessor.getTokens(template);
		TemplateToken token0 = tokens.get(0);
		TemplateToken token1 = tokens.get(1);
		TemplateToken token2 = tokens.get(2);

		assertEquals(0, token0.tokenType.byteValue());
		assertEquals("<h1>", token0.plainText);
		assertEquals(0, token1.tokenType.byteValue());
		assertEquals("${other-placeholder}", token1.plainText);
		assertEquals(0, token2.tokenType.byteValue());
		assertEquals("${other-placeholder}</h1>", token2.plainText);

		assertEquals(3, tokens.size());
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderSimplePlaceholders7() {
		// invalid placeholder followed by a valid placeholder
		String template = "<h1>${other-placeholder}${@odd}</h1>";
		assertEquals("<h1>${other-placeholder}<PH></h1>", TemplateProcessor.doRender(new Object(), template));

		// same test string but this time have a look at the tokens
		// * getTokens -> analyze tokens
		ArrayList<TemplateToken> tokens = TemplateProcessor.getTokens(template);
		TemplateToken token0 = tokens.get(0);
		TemplateToken token1 = tokens.get(1);
		TemplateToken token2 = tokens.get(2);
		TemplateToken token3 = tokens.get(3);

		assertEquals(0, token0.tokenType.byteValue());
		assertEquals("<h1>", token0.plainText);
		assertEquals(0, token1.tokenType.byteValue());
		assertEquals("${other-placeholder}", token1.plainText);
		assertEquals(1, token2.tokenType.byteValue());
		assertEquals("odd", token2.plainText);
		assertEquals(0, token3.tokenType.byteValue());
		assertEquals("</h1>", token3.plainText);

		assertEquals(4, tokens.size());
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
