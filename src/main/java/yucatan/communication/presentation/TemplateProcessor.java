package yucatan.communication.presentation;

public interface TemplateProcessor {

	/**
	 * Template render method. This method may be called recursively.
	 * 
	 * @param dataScope The current datascope to work on. (needs a get(String key) method!)
	 * @param template The template to fill.
	 * @return The rendered String
	 */
	public abstract String doRender(Object dataScope, String template);

}