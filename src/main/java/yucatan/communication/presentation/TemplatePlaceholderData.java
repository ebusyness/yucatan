package yucatan.communication.presentation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;

public class TemplatePlaceholderData extends TemplatePlaceholder {
	/**
	 * The log4j logger of this class.
	 */
	private static Logger log = Logger.getLogger(TemplatePlaceholderData.class);

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
	public String doRender() {
		if (!this.isClosed()) {
			if (!this.isValid()) {
				log.error("Try to render a placeholder within an ivalid state. This may fail.");
			} else {
				log.error("Try to render an incomplte placeholder. This may fail.");
			}
		}
		Object fetchedScope = this.fetchTheScope();
		if (fetchedScope == null) {
			return "";
		}
		return fetchedScope.toString();
	}

	/**
	 * Fetch the specified data scope represented by {@link #memberQuery}.
	 * 
	 * @return The data object.
	 */
	private Object fetchTheScope() {
		if (this.getMemberQuery() == null || this.getMemberQuery().plainText == null) {
			return null;
		}
		Object fetechdScope = this.getDataScope();
		String[] queryParts = this.getMemberQuery().plainText.split("\\.");

		if (queryParts.length == 0) {
			queryParts = new String[1];
			queryParts[0] = this.getMemberQuery().plainText;
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
			log.error("Placeholder error: passed scope is null @ TemplatePlaceholderData.invokeGetMethod(). memberQuery: '" + this.getMemberQuery().plainText
					+ "'. Current memberQuery part to get:'" + queryPart + "'");
			return null;
		}
		if (queryPart == null) {
			log.error("Placeholder error: queryPart is null @ TemplatePlaceholderData.invokeGetMethod. memberQuery: '" + this.getMemberQuery().plainText
					+ "'. Current memberQuery part to get:'" + queryPart + "'");
			return null;
		}
		Method getMethod = null;
		Object newScope = null;
		try {
			getMethod = scope.getClass().getDeclaredMethod("get", Object.class);
		} catch (SecurityException e) {
			log.error("Placeholder error: Could not access the get method of memberQuery '" + this.getMemberQuery().plainText + "'. Current memberQuery part to get:'" + queryPart
					+ "'", e);
		} catch (NoSuchMethodException e) {
			log.error("Placeholder error: Could not access the get method of memberQuery '" + this.getMemberQuery().plainText + "'. Current memberQuery part to get:'" + queryPart
					+ "'", e);
		}
		if (getMethod != null) {
			Object[] invokeArgs = new Object[1];
			invokeArgs[0] = queryPart;
			try {
				newScope = getMethod.invoke(scope, invokeArgs);
			} catch (IllegalArgumentException e) {
				log.error("Placeholder error: Could not invoke the get method of memberQuery '" + this.getMemberQuery().plainText + "'. Current memberQuery part to get:'"
						+ queryPart + "'", e);
				return null;
			} catch (IllegalAccessException e) {
				log.error("Placeholder error: Could not invoke the get method of memberQuery '" + this.getMemberQuery().plainText + "'. Current memberQuery part to get:'"
						+ queryPart + "'", e);
				return null;
			} catch (InvocationTargetException e) {
				log.error("Placeholder error: Could not invoke the get method of memberQuery '" + this.getMemberQuery().plainText + "'. Current memberQuery part to get:'"
						+ queryPart + "'", e);
				return null;
			}
		}
		return newScope;
	}
}
