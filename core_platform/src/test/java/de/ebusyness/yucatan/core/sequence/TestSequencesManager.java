/**
 * 
 */
package de.ebusyness.yucatan.core.sequence;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Samuel
 *
 */
public class TestSequencesManager {

	/**
	 * Test method for {@link de.ebusyness.yucatan.core.sequence.SequencesManager#getSequence(java.lang.String)}.
	 */
	@Test
	public void testGetSequence() {
		Assert.assertEquals(SequencesManager.SEQUENCE_FOUND, SequencesManager.registerSequenceCollection("category", "de.ebusyness.yucatan.core.sequences"));
		// TODO tests with non exisiting attributes
	}

}
