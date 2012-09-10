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
	 * The type of token to be created see static memebers of {@link TemplateToken}.
	 */
	public byte[] createTokenType;

	/**
	 * (empty) constructor method
	 */
	public TemplateTokenStatusItem() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String output = "{";
		String expectedC = this.nextExpectsExplicitChar == true ? "'" + this.nextExpectedChar + "'" : "<undefined>";
		output += "nextExpectedChar:" + expectedC + ",";
		output += "nextExpectsExplicitChar:" + this.nextExpectsExplicitChar + ",";
		output += "successStatus:" + this.successStatus + ",";
		output += "failStatus:" + this.failStatus + ",";
		output += "nextStatus:";
		if (this.nextStatus != null) {
			output += "[";
			for (int i = 0; i < this.nextStatus.length; i++) {
				if (i != 0) {
					output += ",";
				}
				output += this.nextStatus[i];
			}
			output += "],";
			;
		} else {
			output += "null,";
		}
		output += "terminatedBy:";
		if (this.terminatedBy != null) {
			output += "[";
			for (int i = 0; i < this.terminatedBy.length; i++) {
				if (i != 0) {
					output += ",";
				}
				output += "'" + this.terminatedBy[i] + "'";
			}
			output += "],";
		} else {
			output += "null,";
		}
		output += "revaluateOnfail:" + this.revaluateOnfail + ",";
		output += "startNewToken:" + this.startNewToken + ",";
		output += "startNewFromPrevious:" + this.startNewFromPrevious + ",";
		output += "nextStartNewTokenBlocked:" + this.nextStartNewTokenBlocked + ",";
		output += "createToken:" + this.createToken + ",";
		output += "dropToken:" + this.dropToken;
		output += "}";
		return output;
	}
}
