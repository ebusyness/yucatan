package yucatan.communication.presentation;

import java.util.ArrayList;


/**
 * This class implements the yucatan template processor.
 * <p>
 * currently implemented format of template placeholders: ${@<action-name>(<member-query>)}
 * </p>
 * <h3>Examples</h3>
 * <ul>
 * <li>${@data(myProperty)}</li>
 * <li>${@data(myObject.myMethod)}</li>
 * <li>${@list(myObject.myProperty)}</li>
 * </ul>
 */
public final class TemplateProcessor {

	/**
	 * Template render method. This method may be called recursively.
	 * 
	 * @param dataScope The current datascope to work on. (needs a get(String key) method!)
	 * @param template The template to fill.
	 * @return The rendered String
	 */
	public static String doRender(Object dataScope, String template) {
		if (dataScope == null || template == null) {
			return null;
		}

		ArrayList<TemplateToken> templateTokens = TemplateTokenizer.getTokens(template);
		TemplatePlaceholder currentPlaceholder = null;
		String generatedText = "";

		for (TemplateToken token : templateTokens) {

			if (token.tokenType == TemplateToken.TOKENTYPE_TEXT) {
				generatedText += token.plainText;
				continue;
			}

			// collect placeholder tokens
			if (token.tokenType == TemplateToken.TOKENTYPE_ACTIONNAME) {
				currentPlaceholder = TemplatePlaceholder.createPlaceholder(token, dataScope);
			}
			if (token.tokenType == TemplateToken.TOKENTYPE_MEMBERQUERY) {
				currentPlaceholder.addToken(token);
				if (currentPlaceholder != null) {
					generatedText += currentPlaceholder.toString();
				}
			}
		}
		return generatedText;
	}

}