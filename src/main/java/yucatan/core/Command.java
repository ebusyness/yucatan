package yucatan.core;

/**
 * abstract command
 */
public abstract class Command {

	/**
	 * Command finished without error.
	 */
	public static final String COMMAND_EXECUTION_OK = "COMMAND_EXECUTION_OK";

	/**
	 * Command finished with error.
	 */
	public static final String COMMAND_EXECUTION_ERROR = "COMMAND_EXECUTION_ERROR";

	/**
	 * Command sequence not found.
	 */
	public static final String COMMAND_SEQUENCE_NOT_FOUND = "COMMAND_SEQUENCE_NOT_FOUND";

	/**
	 * Stub execute function.
	 * 
	 * @return return code
	 */
	public static String execute() {
		return COMMAND_SEQUENCE_NOT_FOUND;
	}

}
