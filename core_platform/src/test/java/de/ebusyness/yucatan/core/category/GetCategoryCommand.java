package de.ebusyness.yucatan.core.category;

import de.ebusyness.yucatan.core.Command;

public class GetCategoryCommand extends Command {
	/**
	 * Execute the command get category
	 * @return Return code
	 */
	public static byte execute() {
		return Command.COMMAND_OK;
	}
}
