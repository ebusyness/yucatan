package yucatan.communication.presentation;

import org.apache.log4j.Logger;

/**
 * This abstract class provides base functionality for yucatan placeholders. It also provides a static factory method for placeholders.
 */
public abstract class TemplatePlaceholder {

	/**
	 * The placeholder is not finished. There are still missing tokens.
	 */
	private static final byte STATUS_OPENED = 0;

	/**
	 * The placeholder is valid and ready to use. All tokens are added.
	 */
	private static final byte STATUS_CLOSED = 1;

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
	protected byte status = STATUS_OPENED;

	// TODO -> planned token type
	// private TemplateToken formatter;

	/**
	 * The log4j logger of this class.
	 */
	private static Logger log = Logger.getLogger(TemplatePlaceholder.class);

	/**
	 * Placeholder factory method. Tries to create a placeholder instance from action token. Please note: the passed token has to have a {@link TemplateToken#tokenType} that equals
	 * {@link TemplateToken#TOKENTYPE_ACTIONNAME}.
	 * 
	 * @param token
	 * @param scope
	 * @return the placeholder.
	 */
	public static TemplatePlaceholder createPlaceholder(TemplateToken token, Object scope) {
		if (token.tokenType != TemplateToken.TOKENTYPE_ACTIONNAME) {
			log.error("Could not create placeholder. The passed token is not of type action name.");
			return null;
		}
		if (token.plainText == null) {
			log.error("Could not create placeholder. The passed token has no plainText.");
			return null;
		}
		TemplatePlaceholder placeholder = null;
		if (token.plainText.equals("data")) {
			placeholder = new TemplatePlaceholderData(scope);
		}
		if (token.plainText.equals("list")) {
			placeholder = new TemplatePlaceholderList(scope);
		}
		if (placeholder == null) {
			log.error("Could not create placeholder. The passed token action is unknown.");
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

	/**
	 * Add the passed token to the placeholder.
	 * 
	 * @param token The token to add
	 * @throws IllegalStateException
	 */
	public void addToken(TemplateToken token) throws IllegalStateException {
		if (isClosed()) {
			throw new IllegalStateException("The current placeholder is already closed. It is not possible to add more tokens.");
		}
		if (token == null) {
			throw new IllegalArgumentException("Passed token is null.");
		}
		if (token.tokenType == TemplateToken.TOKENTYPE_ACTIONNAME) {
			this.actionName = token;
		}
		if (token.tokenType == TemplateToken.TOKENTYPE_MEMBERQUERY) {
			this.memberQuery = token;
		}
		if (token.tokenType == TemplateToken.TOKENTYPE_PLACEHOLDEREND) {
			this.close();
		}
	}

	/**
	 * Indicates that the placeholder is valid. (All required tokens are present)
	 * 
	 * @return the status code.
	 */
	public boolean isValid() {
		if (this.actionName == null || this.actionName.tokenType != TemplateToken.TOKENTYPE_ACTIONNAME || this.memberQuery == null
				|| this.memberQuery.tokenType != TemplateToken.TOKENTYPE_MEMBERQUERY) {
			return false;
		}
		return true;
	}

	/**
	 * Indicates that the placeholder is complete and ready for usage.
	 * 
	 * @return true or false.
	 */
	public boolean isClosed() {
		return this.status == TemplatePlaceholder.STATUS_CLOSED;
	}

	/**
	 * @return The rendered Placeholder
	 */
	abstract public String doRender();

	@Override
	public String toString() {
		return getClass().getSimpleName() + "=> { actionName: {" + actionName + "}, memberQuery: {" + memberQuery + "}, dataScope: {" + dataScope + "}, status: " + status + "}";
	}

	/**
	 * Closes the Placeholder. This only possible in an vaild placeholder state.
	 * 
	 * @throws IllegalStateException
	 */
	protected void close() throws IllegalStateException {
		if (!isValid()) {
			throw new IllegalStateException("The current placeholder is in an invalid state. An expected TemplateToken is missing.");
		} else {
			this.status = TemplatePlaceholder.STATUS_CLOSED;
		}
	}
}
