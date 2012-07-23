package yucatan.communication.presentation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
		String generatedText = "";

		for (TemplateToken token : templateTokens) {

			if (token.tokenType == TemplateToken.TOKENTYPE_TEXT) {
				generatedText += token.plainText;
				continue;
			}
			// TODO
			// Methods to call
			// get( )
			// getById( String ), getByQuery( String )
			
			if (token.tokenType == TemplateToken.TOKENTYPE_ACTIONNAME) {
				
				
			}
			if (token.tokenType == TemplateToken.TOKENTYPE_MEMBERQUERY) {

				Method getMethod = null;
				try {
					getMethod = dataScope.getClass().getDeclaredMethod("get", Object.class);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	
				if (getMethod != null) {
					Object[] invokeArgs = new Object[1];
					invokeArgs[0] = token.plainText;
					try {
						generatedText += getMethod.invoke(dataScope, invokeArgs);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return generatedText;
	}

}