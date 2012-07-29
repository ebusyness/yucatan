package yucatan.communication.presentation;

import org.apache.log4j.Logger;

public abstract class TemplatePlaceholder {

	/**
	 * The placeholder is not finished. There are still missing tokens.
	 */
	public static final byte STATUS_UNFINISHED = 0;

	/**
	 * The placeholder is finished. All tokens are present.
	 */
	public static final byte STATUS_FINISHED = 1;

	/**
	 * The token that contains the name of the placeholder action.
	 */
	protected TemplateToken actionName;

	/**
	 * The token that contains the memberQuery for a placeholder.
	 */
	protected TemplateToken memberQuery;

	/**
	 * The base data scope of the placeholder. In most cases this is a instance of {@link yucatan.communication.SequenceDataMap}.
	 */
	protected Object dataScope;

	/**
	 * The current status of the placeholder.
	 */
	public byte status = STATUS_UNFINISHED;

	// TODO -> planned token
	// private TemplateToken formatter;

	/**
	 * The log4j logger of this class.
	 */
	static Logger log = Logger.getLogger(TemplatePlaceholder.class);
	
	/**
	 * @param token
	 * @param scope
	 * @return
	 */
	public static TemplatePlaceholder createPlaceholder(TemplateToken token, Object scope) {
		if( token.tokenType != TemplateToken.TOKENTYPE_ACTIONNAME ) {
			log.error("Could not create Placeholder. The passed token is not of type action name.");
			return null;
		}
		if (token.plainText == null) {
			log.error("Could not create Placeholder. The passed token has no plainText.");
			return null;
		}
		TemplatePlaceholder placeholder = null;
		if (token.plainText.equals("data")) {
			placeholder = new TemplatePlaceholderData(scope);
		}
		if (token.plainText.equals("list")) {
			placeholder = new TemplatePlaceholderList(scope);
		}
		if( placeholder == null ) {
			log.error("Could not create Placeholder. The passed token action is unknown.");
			return null;
		}
		placeholder.addToken(token);
		return placeholder;
	}

	/**
	 * Constructs a data placeholder.
	 * 
	 * @param scope The base scope object for this placeholder. In most cases this is a instance of {@link yucatan.communication.SequenceDataMap}.
	 */
	public TemplatePlaceholder(Object scope) {
		this.dataScope = scope;
	}

	public void addToken(TemplateToken token) {
		if (token.tokenType == TemplateToken.TOKENTYPE_ACTIONNAME) {
			actionName = token;
		}
		if (token.tokenType == TemplateToken.TOKENTYPE_MEMBERQUERY) {
			memberQuery = token;
		}
	}

}
