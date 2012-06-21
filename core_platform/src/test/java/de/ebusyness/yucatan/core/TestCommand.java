package de.ebusyness.yucatan.core;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.ebusyness.yucatan.core.Command;

public class TestCommand {

	@Test
	public void testExecute() {
		assertEquals(Command.COMMAND_ERROR, Command.execute());
	}

}
