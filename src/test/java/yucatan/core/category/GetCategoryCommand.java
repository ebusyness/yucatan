package yucatan.core.category;

import java.util.HashMap;

import yucatan.core.Command;

public class GetCategoryCommand extends Command {
	/**
	 * Execute the command get category
	 * @return Return code
	 */
	public static String execute() {
		// System.out.println("GetCategoryCommand.execute()");
		return Command.COMMAND_EXECUTION_OK;
	}

	/**
	 * Execute the command get category
	 * 
	 * @return Return code
	 */
	public static String execute(HashMap<String, ?> dataTransporter) {
		// System.out.println("GetCategoryCommand.execute()");
		dataTransporter.get("HttpParameterMap");

		return Command.COMMAND_EXECUTION_OK;
	}
}
