package de.ebusyness.yucatan.core.sequence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

import de.ebusyness.yucatan.core.Command;

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
		Vector<String> sequence = SequencesManager.getSequence( sequenceLocation);
		if( sequence == null ) {
			return Command.COMMAND_SEQUENCE_NOT_FOUND;
		}
		for (String commandKey : sequence) {
			try {
				Class<?> currentClass = Class.forName(commandKey);
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
