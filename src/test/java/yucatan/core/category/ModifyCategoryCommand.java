/**
 * 
 */
package yucatan.core.category;

import yucatan.core.Command;

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
	public static byte execute() {
		return Command.COMMAND_OK;
	}
}
