package yucatan.communication.presentation;

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
	public byte successStatus = TemplateProcessor.PLACHOLDER_NO_STATUS_CHANGE;

	/**
	 * This indicates which status comes next if the pattern fail. -1 means there is no change.
	 */
	public byte failStatus = TemplateProcessor.PLACHOLDER_NO_STATUS_CHANGE;

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
	 * Set a new start position at the current position.
	 */
	public boolean startNewToken = false;

	/**
	 * Block {@link #startNewToken} for the next status.
	 */
	public boolean nextStartNewTokenBlocked = false;

	/**
	 * Inidcates that a token ends here. A token of type {@link #createTokenType} will be created.
	 */
	public boolean stopAndCreateToken = false;

	/**
	 * Inidcates that a token ends here. The token will be ignored.
	 */
	public boolean stopAndDropToken = false;

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
		output += "startNewToken:" + instance.startNewToken + ",";
		output += "stopAndCreateToken:" + instance.stopAndCreateToken + ",";
		output += "stopAndDropToken:" + instance.stopAndDropToken;
		output += "}";
		return output;
	}
}
