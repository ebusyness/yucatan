package yucatan.sequence;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import yucatan.Command;
import yucatan.sequence.SequenceRunner;
import yucatan.sequence.SequencesManager;

public class TestSequenceRunner {

	@Before
	public void setUp() throws Exception {
		// load test sequences
		SequencesManager.registerSequenceCollection("category", "yucatan.sequences");
	}

	/**
	 * 
	 */
	@Test
	public void testExecute() {
		// if no sequence identifier was specified return with COMMAND_SEQUENCE_NOT_FOUND
		assertEquals(Command.COMMAND_SEQUENCE_NOT_FOUND, SequenceRunner.execute());

		// null parameter check - return with COMMAND_SEQUENCE_NOT_FOUND
		assertEquals(Command.COMMAND_SEQUENCE_NOT_FOUND, SequenceRunner.execute(null));

		// if an invalid sequence identifier was specified return with COMMAND_SEQUENCE_NOT_FOUND
		assertEquals(Command.COMMAND_SEQUENCE_NOT_FOUND, SequenceRunner.execute("sequence_not_found-test"));

		// if an valid sequence identifier was specified (and successfully executed) return with COMMAND_EXECUTION_OK
		assertEquals(Command.COMMAND_EXECUTION_OK, SequenceRunner.execute("category-show"));

		// if a Command returns with COMMAND_EXECUTION_ERROR -> stop sequence processing and return SEQUENCE_EXECUTION_ERROR
		assertEquals(SequenceRunner.SEQUENCE_EXECUTION_ERROR, SequenceRunner.execute("category-commandexecutionerror"));

		// if a Command returns with COMMAND_NOT_FOUND -> stop sequence processing and return SEQUENCE_COMMAND_NOT_FOUND
		assertEquals(SequenceRunner.SEQUENCE_COMMAND_NOT_FOUND, SequenceRunner.execute("category-commandnotfound"));

		// if a Command causes a ClassCastException e.g. a private execute Method -> stop sequence processing and return SEQUENCE_COMMAND_CAST_EXCEPTION
		assertEquals(SequenceRunner.SEQUENCE_COMMAND_CAST_EXCEPTION, SequenceRunner.execute("category-insecurecast"));

		// if the execute method is missing -> stop sequence processing and return SEQUENCE_COMMAND_CAST_EXCEPTION
		assertEquals(SequenceRunner.SEQUENCE_COMMAND_CAST_EXCEPTION, SequenceRunner.execute("category-noexecutemethod"));

		// TODO: if the wrong argument execute method is passed to a Command -> stop sequence processing and return SEQUENCE_COMMAND_ARGUMENT_EXCEPTION
		// at the moment this returns SEQUENCE_COMMAND_CAST_EXCEPTION since it is not possible to force an IlligalArgumentsException
		assertEquals(SequenceRunner.SEQUENCE_COMMAND_CAST_EXCEPTION, SequenceRunner.execute("category-illegalargument"));

		// TODO: thats a feature of #2 make the command processor modular
		// if an valid sequence identifier was specified but an invalid xml file had been passed
		// assertEquals(Command.COMMAND_EXECUTION_OK,
		// SequenceRunner.execute("de/ebusyness/yucatan/core/sequences/invalid-show"));

	}
}
