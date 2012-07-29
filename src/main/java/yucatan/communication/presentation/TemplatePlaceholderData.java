package yucatan.communication.presentation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class TemplatePlaceholderData extends TemplatePlaceholder {
	/**
	 * The log4j logger of this class.
	 */
	static Logger log = Logger.getLogger(TemplatePlaceholderData.class);

	/**
	 * Constructs a data placeholder.
	 * 
	 * @param scope The base scope object for this placeholder. In most cases this is a instance of {@link yucatan.communication.SequenceDataMap}.
	 */
	public TemplatePlaceholderData(Object scope) {
		super(scope);
	}

	@Override
	/**
	 * Render the placeholder to String.
	 * @return the placeholder String representation. Empty/invalid placeholders will be resolved to an empty String.
	 */
	public String toString() {
		Object fetchedScope = this.fetchTheScope();
		if (fetchedScope == null) {
			return "";
		}
		return fetchedScope.toString();
	}

	@Override
	/**
	 * Add the passed token to the placeholder.
	 * 
	 * @param token The token to add.
	 */
	public void addToken(TemplateToken token) {
		super.addToken(token);
		if (token.tokenType == TemplateToken.TOKENTYPE_PLACEHOLDEREND) {
			if (this.memberQuery == null) {
				log.error("Could not create a valid placeholder. The member query token is missing.");
				this.status = TemplatePlaceholder.STATUS_ERROR;
			} else {
				this.status = TemplatePlaceholder.STATUS_FINISHED;
			}
		}
	}

	/**
	 * Fetch the specified data scope represented by {@link #memberQuery}.
	 * 
	 * @return The data object.
	 */
	private Object fetchTheScope() {
		if (this.memberQuery == null || this.memberQuery.plainText == null) {
			return null;
		}
		Object fetechdScope = this.dataScope;
		String[] queryParts = this.memberQuery.plainText.split("\\.");

		if (queryParts.length == 0) {
			queryParts = new String[1];
			queryParts[0] = this.memberQuery.plainText;
		}
		for (int i = 0; i < queryParts.length; i++) {
			fetechdScope = this.invokeGetMethod(fetechdScope, queryParts[i]);
		}
		return fetechdScope;
	}

	/**
	 * Get new scope from current scope by invoking the get method of the current scope.
	 * 
	 * @param scope The (current) scope to work on.
	 * @param queryPart The name of new scope to get.
	 * @return
	 */
	private Object invokeGetMethod(Object scope, String queryPart) {
		if (scope == null) {
			log.error("Placeholder error: passed scope is null @ TemplatePlaceholderData.invokeGetMethod(). memberQuery: '" + this.memberQuery.plainText
					+ "'. Current memberQuery part to get:'" + queryPart
					+ "'");
			return null;
		}
		if (queryPart == null) {
			log.error("Placeholder error: queryPart is null @ TemplatePlaceholderData.invokeGetMethod. memberQuery: '" + this.memberQuery.plainText
					+ "'. Current memberQuery part to get:'"
					+ queryPart + "'");
			return null;
		}
		Method getMethod = null;
		Object newScope = null;
		try {
			getMethod = scope.getClass().getDeclaredMethod("get", Object.class);
		} catch (SecurityException e) {
			log.error(
					"Placeholder error: Could not access the get method of memberQuery '" + this.memberQuery.plainText + "'. Current memberQuery part to get:'" + queryPart + "'",
					e);
		} catch (NoSuchMethodException e) {
			log.error(
					"Placeholder error: Could not access the get method of memberQuery '" + this.memberQuery.plainText + "'. Current memberQuery part to get:'" + queryPart + "'",
					e);
		}
		if (getMethod != null) {
			Object[] invokeArgs = new Object[1];
			invokeArgs[0] = queryPart;
			try {
				newScope = getMethod.invoke(scope, invokeArgs);
			} catch (IllegalArgumentException e) {
				log.error("Placeholder error: Could not invoke the get method of memberQuery '" + this.memberQuery.plainText + "'. Current memberQuery part to get:'" + queryPart
						+ "'", e);
				return null;
			} catch (IllegalAccessException e) {
				log.error("Placeholder error: Could not invoke the get method of memberQuery '" + this.memberQuery.plainText + "'. Current memberQuery part to get:'" + queryPart
						+ "'", e);
				return null;
			} catch (InvocationTargetException e) {
				log.error("Placeholder error: Could not invoke the get method of memberQuery '" + this.memberQuery.plainText + "'. Current memberQuery part to get:'" + queryPart
						+ "'", e);
				return null;
			}
		}
		return newScope;
	}
}
