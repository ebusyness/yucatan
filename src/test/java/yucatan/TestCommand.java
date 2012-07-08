package yucatan;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import yucatan.Command;


public class TestCommand {

	@Test
	public void testExecute() {
		assertEquals(Command.COMMAND_SEQUENCE_NOT_FOUND, Command.execute());
	}

}
