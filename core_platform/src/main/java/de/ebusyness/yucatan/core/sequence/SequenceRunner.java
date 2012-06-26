package de.ebusyness.yucatan.core.sequence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import de.ebusyness.yucatan.core.Command;
import de.ebusyness.yucatan.core.sequence.generated.CommandType;
import de.ebusyness.yucatan.core.sequence.generated.SequenceType;

/**
 * sequence runner
 * 
 */
public class SequenceRunner extends Command {

	/**
	 * Runs the specified sequence. (invokes 
	 * 
	 * @param sequenceLocation The location of sequence to run <module:package/sequenceCollectionName-sequenceName>
	 * @return true or false
	 */
	public static byte execute(String sequenceLocation) {
		if (sequenceLocation == null) {
			return COMMAND_SEQUENCE_NOT_FOUND;
		}
		SequenceType sequenceDeclaration = SequencesManager.getSequence(sequenceLocation);
		if( sequenceDeclaration == null ) {
			return Command.COMMAND_SEQUENCE_NOT_FOUND;
		}
		List<CommandType> commandsDeclaration = sequenceDeclaration.getCommands();
		for (CommandType commandDeclaration : commandsDeclaration) {
			try {
				Class<?> currentClass = Class.forName(commandDeclaration.getId());
				Class<? extends Command> castedClass = currentClass.asSubclass(Command.class);
				Method excecuteMethod = null;
				try {
					excecuteMethod = castedClass.getDeclaredMethod("execute");
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NoSuchMethodException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if (excecuteMethod != null) {
						excecuteMethod.invoke(null);
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Command.COMMAND_OK;
	}
}
