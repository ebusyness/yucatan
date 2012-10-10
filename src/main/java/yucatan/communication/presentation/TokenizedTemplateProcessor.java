package yucatan.communication.presentation;

import java.util.List;

/**
 * This class implements the yucatan template processor.
 * <p>
 * currently implemented format of template placeholders: ${&#64;<action-name>(<member-query>)}
 * </p>
 * <h3>Examples</h3>
 * <ul>
 * <li>${&#64;data(myProperty)}</li>
 * <li>${&#64;data(myObject.myMethod)}</li>
 * <li>${&#64;list(myObject.myProperty)}</li>
 * </ul>
 */
public class TokenizedTemplateProcessor implements TemplateProcessor {

	public TokenizedTemplateProcessor() {
	}

	/* (non-Javadoc)
	 * @see yucatan.communication.presentation.TemplateProcessor#doRender(java.lang.Object, java.lang.String)
	 */
	@Override
	public String doRender(final Object dataScope, final String template) {
		if (dataScope == null) {
			throw new IllegalArgumentException("Passed value for dataScope is null.");
		}

		if (template == null) {
			throw new IllegalArgumentException("Passed value for template is null.");
		}

		return this.generateTextParts(dataScope, TemplateTokenizer.getTokens(template));
	}

	/**
	 * Generates the String from list with templateTokens.
	 * 
	 * @param dataScope
	 * @param templateTokens
	 * @return
	 */
	private String generateTextParts(final Object dataScope, final List<TemplateToken> templateTokens) {
		final StringBuilder generatedText = new StringBuilder();
		TemplatePlaceholder currentPlaceholder = null;

		// loop through tokens and build the output string
		for (TemplateToken token : templateTokens) {
			// append text tokens
			if (token.tokenType == TemplateToken.TOKENTYPE_TEXT) {
				generatedText.append(token.plainText);
				continue;
			}

			// collect tokens for placeholders
			if (token.tokenType == TemplateToken.TOKENTYPE_ACTIONNAME) {
				if (currentPlaceholder != null && !currentPlaceholder.isClosed()) {
					throw new IllegalStateException("An unexpected TemplateToken (TemplateToken.TOKENTYPE_ACTIONNAME) occured. But the current Placeholder is still incomplete.");
				}
				currentPlaceholder = TemplatePlaceholder.createPlaceholder(token, dataScope);
			} else if (currentPlaceholder != null) {
				currentPlaceholder.addToken(token);
			}

			// append placeholder text
			if (currentPlaceholder != null && currentPlaceholder.isClosed()) {
				generatedText.append(currentPlaceholder.doRender());
				// throw placeholder away
				currentPlaceholder = null;
			}
		}
		return generatedText.toString();
	}
}