package yucatan.communication.presentation;


/**
 * This implements a template token.
 * 
 */
public class TemplateToken {

	/**
	 * Text Token
	 */
	public static final byte TOKENTYPE_TEXT = 0;

	/**
	 * Action Name Token
	 */
	public static final byte TOKENTYPE_ACTIONNAME = 1;

	/**
	 * Memeber Access Query Token
	 */
	public static final byte TOKENTYPE_MEMBERQUERY = 2;

	/**
	 * Placeholder end Token
	 */
	public static final byte TOKENTYPE_PLACEHOLDEREND = 3;

	/**
	 * The plainTextPart stores the plain text of the tokem.
	 */
	public String plainText;

	/**
	 * The tokenType describes the type of token.
	 */
	public byte tokenType = TemplateToken.TOKENTYPE_TEXT;

	public TemplateToken() {
	}

}
