package yucatan.communication.presentation;


/**
 * This implements a template token.
 * 
 */
public class TemplateToken {

	/**
	 * Text Token
	 */
	public static final Byte TOKENTYPE_TEXT = 0;

	/**
	 * Action Name Token
	 */
	public static final Byte TOKENTYPE_ACTIONNAME = 1;

	/**
	 * Memeber Access Query Token
	 */
	public static final Byte TOKENTYPE_MEMBERQUERY = 2;

	/**
	 * The plainTextPart stores the plain text of the tokem.
	 */
	public String plainText;

	/**
	 * The tokenType describes the type of token.
	 */
	public Byte tokenType = TemplateToken.TOKENTYPE_TEXT;

	public TemplateToken() {
	}

}
