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
public class TestTemplateProcessor {

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderPlaceholdersWithData() {
		/*
		 * // test simple placholders
		 * String template = "";
		 * 
		 * 
		 * ${@even}${other-placeholder}</ul></body>";
		 * 
		 * 
		 * HashMap<String, String> hs = new HashMap<String, String>();
		 * hs.put("myKey", "myValue");
		 * template = "<html><body><h1>${@data(myKey)}${@even}</h1><ul>${@list(myKey)}</ul>${@count}</body>";
		 * System.out.println(TemplateProcessor.doRender(hs, template));
		 * 
		 * template = "${@data(myKey)}${@even}x${other-placeholder}";
		 * System.out.println(TemplateProcessor.doRender(new Object(), template));
		 */
	}

	/**
	 * Test method for {@link yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)}.
	 */
	@Test
	public void testDoRenderStringPlaceholders1() {

		// test String placholders
		// - one placeholder at the end of string
		// - another placeholder inbetween
		// * call doRender() -> general expected result
		String template = "<html><body><h1>${@data(key1)}</h1><br />${@data(key2)}";
		HashMap<String, String> dataScope = new HashMap<String,String>();
		dataScope.put("key1", "Welcome");
		dataScope.put("key2", "List");
		assertEquals("<html><body><h1>Welcome</h1><br />List", TemplateProcessor.doRender(dataScope, template));
	}
}
