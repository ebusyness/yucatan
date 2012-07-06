package yucatan.core.sequence;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.log4j.Logger;

import yucatan.core.Command;
import yucatan.core.sequence.generated.XmlTypeCommand;

/**
 * The sequence runner executes all commands of the specified sequence.
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
	 * The log4j logger of this class.
	 */
	static Logger log = Logger.getLogger(SequenceRunner.class);

	/**
	 * Runs the specified sequence. Invokes the commands of the passed sequence location.
	 * 
	 * @param sequenceLocation The location of sequence to run <module:package/sequenceCollectionName-sequenceName>
	 * @return true or false
	 */
	public static String execute(String sequenceLocation) {
		if (sequenceLocation == null) {
			log.error("An invalid value (null) was passed as sequenceLocation.");
			return COMMAND_SEQUENCE_NOT_FOUND;
		}
		List<XmlTypeCommand> commandsDeclaration = SequencesManager.getSequence(sequenceLocation);
		if (commandsDeclaration == null) {
			log.error("The specified sequenceLocation (value='" + sequenceLocation + "') is not registered within the SequenceManager.");
			return Command.COMMAND_SEQUENCE_NOT_FOUND;
		}
		for (XmlTypeCommand commandDeclaration : commandsDeclaration) {
			// fetch declared class
			Class<?> currentClass;
			try {
				currentClass = Class.forName(commandDeclaration.getName());
			} catch (ClassNotFoundException e) {
				log.error("The specified command class (value='" + commandDeclaration.getName() + "') in sequence (value='" + sequenceLocation + "') does not exist.", e);
				return SEQUENCE_COMMAND_NOT_FOUND;
			}

			// do a class cast
			Class<? extends Command> castedClass;
			try {
				castedClass = currentClass.asSubclass(Command.class);
			} catch (ClassCastException e) {
				log.error("Failed to cast the specified class (" + commandDeclaration.getName() + ") to represent a subclass of the class yucatan.core.Command in sequence ("
						+ sequenceLocation + "). Check the declaration of your methods and members - these should correspond with yucatan.core.Command.", e);
				return SEQUENCE_COMMAND_CAST_EXCEPTION;
			}

			// fetch the execute method
			Method excecuteMethod = null;
			try {
				excecuteMethod = castedClass.getDeclaredMethod("execute");
			} catch (SecurityException e) {
				log.error("Could not access the declared method execute() of command class (" + commandDeclaration.getName() + ") in sequence (" + sequenceLocation
						+ "). This caused a SecurityException.", e);
				// ClassCastException should be thrown before
				return SEQUENCE_COMMAND_SECURITY_EXCEPTION;
			} catch (NoSuchMethodException e) {
				log.error("Could not access the declared method execute() of command class (" + commandDeclaration.getName() + ") in sequence (" + sequenceLocation
						+ "). This caused a NoSuchMethodException.", e);
				// ClassCastException should be thrown before
				return SEQUENCE_COMMAND_NOSUCHMETHOD_EXCEPTION;
			}

			// invoke execute method
			try {
				if (excecuteMethod != null) {
					String status = (String) excecuteMethod.invoke(null);
					// TODO recheck this part maybe there are reasons for other return codes (early sequence exit (stop sequence), sequence interuption, ...)
					if (status != Command.COMMAND_EXECUTION_OK) {
						return SEQUENCE_EXECUTION_ERROR;
					}
				}
			} catch (IllegalArgumentException e) {
				log.error("Could not invoke the declared method execute() of command class (" + commandDeclaration.getName() + ") in sequence (" + sequenceLocation
						+ "). This caused an IllegalArgumentException.", e);
				return SEQUENCE_COMMAND_ARGUMENT_EXCEPTION;
			} catch (IllegalAccessException e) {
				log.error("Could not invoke the declared method execute() of command class (" + commandDeclaration.getName() + ") in sequence (" + sequenceLocation
						+ "). This caused an IllegalAccessException.", e);
				return SEQUENCE_COMMAND_OTHER_EXCEPTION;
			} catch (InvocationTargetException e) {
				log.error("Could not invoke the declared method execute() of command class (" + commandDeclaration.getName() + ") in sequence (" + sequenceLocation
						+ "). This caused an InvocationTargetException.", e);
				return SEQUENCE_COMMAND_OTHER_EXCEPTION;
			}
		}
		return Command.COMMAND_EXECUTION_OK;
	}
}
