package de.ebusyness.yucatan.core.sequence;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import de.ebusyness.yucatan.core.Command;

public class TestSequenceRunner {

	@Before
	public void setUp() throws Exception {
		// load test sequences
		SequencesManager.registerSequenceCollection("category", "de.ebusyness.yucatan.core.sequences");
	}

	@Test
	public void testExecute() {

		// if no sequence identifier was specified return with COMMAND_SEQUENCE_NOT_FOUND
		assertEquals(Command.COMMAND_SEQUENCE_NOT_FOUND, SequenceRunner.execute());

		// if an invalid sequence identifier was specified return with COMMAND_SEQUENCE_NOT_FOUND
		assertEquals(Command.COMMAND_SEQUENCE_NOT_FOUND, SequenceRunner.execute("sequence_not_found-test"));

		// if an valid sequence identifier was specified (and successfully executed) return with COMMAND_OK
		assertEquals("Command didn't return with expected result Command.COMMAND_OK", Command.COMMAND_OK, SequenceRunner.execute("category-show"));

		// TODO: thats a feature of #2 make the command processor modular
		// if an valid sequence identifier was specified but an invalid xml file had been passed
		// assertEquals(Command.COMMAND_OK,
		// SequenceRunner.execute("de/ebusyness/yucatan/core/sequences/invalid-show"));

	}
}
