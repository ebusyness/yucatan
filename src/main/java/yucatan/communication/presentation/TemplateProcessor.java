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
		StringBuilder generatedText = new StringBuilder();

		for (TemplateToken token : templateTokens) {
			// append text tokens
			if (token.tokenType == TemplateToken.TOKENTYPE_TEXT) {
				generatedText.append(token.plainText);
				continue;
			}

			// collect tokens for placeholders
			if (token.tokenType == TemplateToken.TOKENTYPE_ACTIONNAME) {
				currentPlaceholder = TemplatePlaceholder.createPlaceholder(token, dataScope);
			} else if (currentPlaceholder != null) {
				currentPlaceholder.addToken(token);
			}

			// append placeholder text
			if (currentPlaceholder != null && currentPlaceholder.currentStatus() == TemplatePlaceholder.STATUS_FINISHED) {
				generatedText.append(currentPlaceholder.toString());
				// throw placeholder away
				currentPlaceholder = null;
			}
		}
		return generatedText.toString();
	}
}