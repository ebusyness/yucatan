package yucatan.communication.presentation;

public class TemplatePlaceholderList extends TemplatePlaceholder {

	public TemplatePlaceholderList(Object scope) {
		super(scope);
	}

	public String doRender() {
		return "LIST";
	}
}
