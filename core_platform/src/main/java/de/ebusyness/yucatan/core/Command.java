package de.ebusyness.yucatan.core;

/**
 * abstract command
 */
public abstract class Command {

	/**
	 * Command finished with error.
	 */
	public static byte COMMAND_ERROR = -1;
	
	/**
	 * Command sequence not found.
	 */
	public static byte COMMAND_SEQUENCE_NOT_FOUND = -2;

	/**
	 * command return code ok
	 */
	public static byte COMMAND_OK = 0;

	/**
	 * @return return code
	 */
	public static byte execute() {
		return COMMAND_ERROR;
	}

}
