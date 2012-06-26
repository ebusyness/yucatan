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
		// null, null parameter check
		Assert.assertEquals(SequencesManager.SEQUENCE_FILE_NOT_FOUND, SequencesManager.registerSequenceCollection(null, null));

		// file not found
		Assert.assertEquals(SequencesManager.SEQUENCE_FILE_NOT_FOUND, SequencesManager.registerSequenceCollection("notfound", "de.ebusyness.yucatan.core.sequences"));

		// invalid file format
		Assert.assertEquals(SequencesManager.SEQUENCE_FORMAT_ERROR, SequencesManager.registerSequenceCollection("invalidtag", "de.ebusyness.yucatan.core.sequences"));

		// correct format
		Assert.assertEquals(SequencesManager.SEQUENCE_FOUND, SequencesManager.registerSequenceCollection("category", "de.ebusyness.yucatan.core.sequences"));

		// non declared attributes will be ignored
		Assert.assertEquals(SequencesManager.SEQUENCE_FOUND, SequencesManager.registerSequenceCollection("ignoredinvalidattribute", "de.ebusyness.yucatan.core.sequences"));
	}

}
