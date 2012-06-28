package yucatan.core.category;

import yucatan.core.Command;

public class GetCategoryCommand extends Command {
	/**
	 * Execute the command get category
	 * @return Return code
	 */
	public static byte execute() {
		return Command.COMMAND_OK;
	}
}
