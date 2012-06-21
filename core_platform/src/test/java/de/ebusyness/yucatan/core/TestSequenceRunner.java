package de.ebusyness.yucatan.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.ebusyness.yucatan.core.Command;
import de.ebusyness.yucatan.core.sequence.SequenceRunner;

public class TestSequenceRunner {

	@Test
	public void testExecute() {
		assertEquals(Command.COMMAND_ERROR, SequenceRunner.execute());
		assertEquals(Command.COMMAND_SEQUENCE_NOT_FOUND, SequenceRunner.execute("sequence-notfound-bla"));
		assertEquals("Command didn't return with expected result Command.COMMAND_OK", Command.COMMAND_OK,
				SequenceRunner.execute("de/ebusyness/yucatan/core/sequences/category-show"));
//		assertEquals(Command.COMMAND_OK,
//				SequenceRunner.execute("de/ebusyness/yucatan/core/sequences/invalid-show"));
		
	}
}
