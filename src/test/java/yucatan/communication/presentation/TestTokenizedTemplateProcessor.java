/**
 * 
 */
package yucatan.communication.presentation;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;

import org.junit.Test;

/**
 * @author Samuel
 * 
 */
public class TestTokenizedTemplateProcessor {

	public static HashMap<String, Object> getTestdata() {
		HashMap<String, String> otherScope = new HashMap<String, String>();
		otherScope.put("key1", "other scope welcome");
		HashMap<String, Object> dataScope = new HashMap<String, Object>();
		dataScope.put("key1", "Welcome");
		dataScope.put("key2", "List");
		dataScope.put("otherscope", otherScope);
		return dataScope;
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TokenizedTemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderStringPlaceholdersNull() {

		// test String placholders
		// - one placeholder at the end of string
		// - another placeholder inbetween
		// * call doRender() -> general expected result
		String template = "<html><body><h1>${@data(key1)}</h1><br />${@data(key2)}";

		TemplateProcessor tp = new TokenizedTemplateProcessor();
		assertEquals("<html><body><h1></h1><br />", tp.doRender(new Object(), template));

		template = "<html><body><h1>${@data(key1)}</h1><h1>${@data(otherscope.key1)}</h1><p>${@data(otherscope.key1)}</p><br />${@data(key2)}";
		assertEquals("<html><body><h1></h1><h1></h1><p></p><br />", tp.doRender(new Object(), template));
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TokenizedTemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderStringPlaceholders1() {

		// test String placholders
		// - one placeholder at the end of string
		// - another placeholder inbetween
		// * call doRender() -> general expected result
		String template = "<html><body><h1>${@data(key1)}</h1><br />${@data(otherscope.key1)}";

		// testdata
		HashMap<String, Object> dataScope = TestTokenizedTemplateProcessor.getTestdata();

		TemplateProcessor tp = new TokenizedTemplateProcessor();
		assertEquals("<html><body><h1>Welcome</h1><br />other scope welcome", tp.doRender(dataScope, template));
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TokenizedTemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderSimplePlaceholders2() {
		// simple placeholders at start position
		// - one placeholder at the beginning of string
		// - one placeholder at the end of string
		// * getTokens -> general expected result
		String template = "${@data(otherscope.key1)}<html><ul>${@data(key2)}";

		// testdata
		HashMap<String, Object> dataScope = TestTokenizedTemplateProcessor.getTestdata();

		TemplateProcessor tp = new TokenizedTemplateProcessor();
		assertEquals("other scope welcome<html><ul>List", tp.doRender(dataScope, template));
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TokenizedTemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderSimplePlaceholders3() {

		// simple placeholder after an simple placeholder
		// - two directly following simple placeholders -
		String template = "<html><body><h1>${@data(otherscope.key1)}${@data(key1)}</h1><ul>";

		// testdata
		HashMap<String, Object> dataScope = TestTokenizedTemplateProcessor.getTestdata();

		TemplateProcessor tp = new TokenizedTemplateProcessor();
		assertEquals("<html><body><h1>other scope welcomeWelcome</h1><ul>", tp.doRender(dataScope, template));

	}
}
