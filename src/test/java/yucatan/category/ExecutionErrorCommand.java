package yucatan.category;

import yucatan.Command;

public class ExecutionErrorCommand extends Command {
	/**
	 * Execute the command get category
	 * 
	 * @return Return code
	 */
	public static String execute() {
		// System.out.println("ExecutionErrorCommand.execute()");
		return Command.COMMAND_EXECUTION_ERROR;
	}
}
