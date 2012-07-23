package yucatan.communication.presentation;

/**
 * Object of this class describe the current status of the yucatan template parser.
 * <p>
 * currently implemented format of template placeholders: ${@<action-name>(<member-query>)}.
 * </p>
 */
public class TemplateTokenStatusItem {

	/**
	 * Next explicitly expected character to switch the status to the {@link #successStatus}. If the next character is a different one switch to {@link #failStatus}.
	 */
	public char nextExpectedChar;

	/**
	 * Flag to indicate that the next char has to be an explicitly defined character. This flag is needed since chars {@link #nextExpectedChar} can not be null.
	 */
	public boolean nextExpectsExplicitChar = false;

	/**
	 * This indicates which status comes next if the pattern match. -1 means there is no change.
	 */
	public byte successStatus = TemplateTokenizer.PLACHOLDER_NO_STATUS_CHANGE;

	/**
	 * This indicates which status comes next if the pattern fail. -1 means there is no change.
	 */
	public byte failStatus = TemplateTokenizer.PLACHOLDER_NO_STATUS_CHANGE;

	/**
	 * This indicates which status comes next. -1 means there is no change. The next status will be applied with the next character execept this status defines terminators see
	 * {@link #terminatedBy}.
	 */
	public byte[] nextStatus;

	/**
	 * Current status is regulary terminated by one of these characters.
	 */
	public char[] terminatedBy;

	/**
	 * This indicates that the current character will be reevaluated with an other status item if the pattern fail. A status change is expected. (Be aware of cyclic dependencies
	 * when you use this feature)
	 */
	public boolean revaluateOnfail = false;

	/**
	 * Set a new start position at the current position.
	 */
	public boolean startNewToken = false;

	/**
	 * Set the new start position to the position of the previous character. Is stronger than the new position of {@link #startNewToken}, {@link #createToken}, {@link #dropToken}
	 */
	public boolean startNewFromPrevious = false;

	/**
	 * Block {@link #startNewToken} for the next status.
	 */
	public boolean nextStartNewTokenBlocked = false;

	/**
	 * Inidcates that a token ends here. A token of type {@link #createTokenType} will be created. It also sets a new start position at the current position for the next position.
	 */
	public boolean createToken = false;

	/**
	 * Inidcates that a token ends here. The token will be ignored.
	 */
	public boolean dropToken = false;

	/**
	 * The type of token to be created see static memebers of {@link TemplateToken}. default = TemplateToken.TOKENTYPE_TEXT
	 */
	public Byte createTokenType = TemplateToken.TOKENTYPE_TEXT;

	/**
	 * (empty) constructor method
	 */
	public TemplateTokenStatusItem() {
	}

	/**
	 * A simple inspector method for debugging purposes.
	 * 
	 * @param instance The instance to inspect.
	 * @return a JSON-like String with all properies of the instance.
	 */
	public static String inspectProperties(TemplateTokenStatusItem instance) {
		String output = "{";
		String expectedC = instance.nextExpectsExplicitChar == true ? "'" + instance.nextExpectedChar + "'" : "<undefined>";
		output += "nextExpectedChar:" + expectedC + ",";
		output += "nextExpectsExplicitChar:" + instance.nextExpectsExplicitChar + ",";
		output += "successStatus:" + instance.successStatus + ",";
		output += "failStatus:" + instance.failStatus + ",";
		output += "nextStatus:";
		if (instance.nextStatus != null) {
			output += "[";
			for (int i = 0; i < instance.nextStatus.length; i++) {
				if (i != 0) {
					output += ",";
				}
				output += instance.nextStatus[i];
			}
			output += "],";
			;
		} else {
			output += "null,";
		}
		output += "terminatedBy:";
		if (instance.terminatedBy != null) {
			output += "[";
			for (int i = 0; i < instance.terminatedBy.length; i++) {
				if (i != 0) {
					output += ",";
				}
				output += "'" + instance.terminatedBy[i] + "'";
			}
			output += "],";
		} else {
			output += "null,";
		}
		output += "revaluateOnfail:" + instance.revaluateOnfail + ",";
		output += "startNewToken:" + instance.startNewToken + ",";
		output += "startNewFromPrevious:" + instance.startNewFromPrevious + ",";
		output += "nextStartNewTokenBlocked:" + instance.nextStartNewTokenBlocked + ",";
		output += "createToken:" + instance.createToken + ",";
		output += "dropToken:" + instance.dropToken;
		output += "}";
		return output;
	}
}
