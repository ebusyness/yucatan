/**
 * 
 */
package yucatan.category;

import yucatan.Command;

/**
 * @author Samuel
 *
 */
public class ModifyCategoryCommand extends Command {
	/**
	 * Execute the command get category
	 * 
	 * @return Return code
	 */
	public static String execute() {
		// System.out.println("ModifyCategoryCommand.execute()");
		return Command.COMMAND_EXECUTION_OK;
	}
}
