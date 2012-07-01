package yucatan.core.sequence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import yucatan.core.Command;
import yucatan.core.sequence.generated.XmlTypeCommand;

/**
 * The sequence runner executes all commands of the specified sequence.
 * 
 */
public class SequenceRunner extends Command {
	/**
	 * Sequence processing stopped due to an Command execution error.
	 */
	public static final String SEQUENCE_EXECUTION_ERROR = "SEQUENCE_EXECUTION_ERROR";

	/**
	 * Sequence processing stopped because a Command wasn't found.
	 */
	public static final String SEQUENCE_COMMAND_NOT_FOUND = "SEQUENCE_COMMAND_NOT_FOUND";

	/**
	 * Sequence processing stopped because a Command class caused a ClassCastException.
	 */
	public static final String SEQUENCE_COMMAND_CAST_EXCEPTION = "SEQUENCE_COMMAND_CAST_EXCEPTION";

	/**
	 * Sequence processing stopped because a Command class caused a SecurityException.
	 */
	public static final String SEQUENCE_COMMAND_SECURITY_EXCEPTION = "SEQUENCE_COMMAND_SECURITY_EXCEPTION";
	
	/**
	 * Sequence processing stopped because a Command class caused a NoSuchMethodException.
	 */
	public static final String SEQUENCE_COMMAND_NOSUCHMETHOD_EXCEPTION = "SEQUENCE_COMMAND_NOSUCHMETHOD_EXCEPTION";

	/**
	 * Sequence processing stopped because a Command class caused a IllegalArgumentException.
	 */
	public static final String SEQUENCE_COMMAND_ARGUMENT_EXCEPTION = "SEQUENCE_COMMAND_ARGUMENT_EXCEPTION";

	/**
	 * Sequence processing stopped because a Command caused an Exception.
	 */
	public static final String SEQUENCE_COMMAND_OTHER_EXCEPTION = "SEQUENCE_COMMAND_OTHER_EXCEPTION";

	/**
	 * Runs the specified sequence. Invokes the commands of the passed sequence location.
	 * 
	 * @param sequenceLocation The location of sequence to run <module:package/sequenceCollectionName-sequenceName>
	 * @return true or false
	 */
	public static String execute(String sequenceLocation) {
		if (sequenceLocation == null) {
			return COMMAND_SEQUENCE_NOT_FOUND;
		}
		List<XmlTypeCommand> commandsDeclaration = SequencesManager.getSequence(sequenceLocation);
		if (commandsDeclaration == null) {
			return Command.COMMAND_SEQUENCE_NOT_FOUND;
		}
		for (XmlTypeCommand commandDeclaration : commandsDeclaration) {
			// fetch declared class
			Class<?> currentClass;
			try {
				currentClass = Class.forName(commandDeclaration.getName());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return SEQUENCE_COMMAND_NOT_FOUND;
			}

			// do a class cast
			Class<? extends Command> castedClass;
			try {
				castedClass = currentClass.asSubclass(Command.class);
			} catch (ClassCastException e) {
				e.printStackTrace();
				return SEQUENCE_COMMAND_CAST_EXCEPTION;
			}

			// fetch the execute method
			Method excecuteMethod = null;
			try {
				excecuteMethod = castedClass.getDeclaredMethod("execute");
			} catch (SecurityException e) {
				e.printStackTrace();
				// ClassCastException should be thrown before
				return SEQUENCE_COMMAND_SECURITY_EXCEPTION;
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				// ClassCastException should be thrown before
				return SEQUENCE_COMMAND_NOSUCHMETHOD_EXCEPTION;
			}

			// invoke execute method
			try {
				if (excecuteMethod != null) {
					String status = (String) excecuteMethod.invoke(null);
					if (status != Command.COMMAND_EXECUTION_OK) {
						return SEQUENCE_EXECUTION_ERROR;
					}
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				return SEQUENCE_COMMAND_ARGUMENT_EXCEPTION;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				return SEQUENCE_COMMAND_OTHER_EXCEPTION;
			} catch (InvocationTargetException e) {
				e.printStackTrace();
				return SEQUENCE_COMMAND_OTHER_EXCEPTION;
			}
		}
		return Command.COMMAND_EXECUTION_OK;
	}
}
