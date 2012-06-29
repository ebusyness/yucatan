package yucatan.core.category;

import yucatan.core.Command;

public class InsecureCastCommand {
	/**
	 * Execute the command get category
	 * 
	 * @return Return code
	 */
	private static String execute() {
		// System.out.println("InsecureCastCommand.execute()");
		return Command.COMMAND_EXECUTION_ERROR;
	}

	/**
	 * dummy method to prevent warning
	 */
	public static void doExecute() {
		execute();
	}
}
