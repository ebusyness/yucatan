package yucatan.core.sequence;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import yucatan.core.sequence.generated.XmlTypeCommand;

/**
 * Test SequencesManager
 * 
 */
public class TestSequencesManager {

	/**
	 * Test method for {@link yucatan.core.sequence.SequencesManager#getSequence(java.lang.String)}.
	 */
	@Test
	public void testGetSequence() {
		// null, null parameter check
		Assert.assertEquals(SequencesManager.SEQUENCE_FILE_NOT_FOUND, SequencesManager.registerSequenceCollection(null, null));

		// file not found
		Assert.assertEquals(SequencesManager.SEQUENCE_FILE_NOT_FOUND, SequencesManager.registerSequenceCollection("notfound", "yucatan.core.sequences"));

		// invalid file format
		Assert.assertEquals(SequencesManager.SEQUENCE_FORMAT_ERROR, SequencesManager.registerSequenceCollection("invalidtag", "yucatan.core.sequences"));

		// correct format
		Assert.assertEquals(SequencesManager.SEQUENCE_FOUND, SequencesManager.registerSequenceCollection("category", "yucatan.core.sequences"));

		// non declared attributes will be ignored
		Assert.assertEquals(SequencesManager.SEQUENCE_FOUND, SequencesManager.registerSequenceCollection("ignoredinvalidattribute", "yucatan.core.sequences"));

		// test registered commands
		List<XmlTypeCommand> commandsDeclaration = SequencesManager.getSequence("category-show");
		assertEquals(2, commandsDeclaration.size());
	}

	/**
	 * Test method for {@link yucatan.core.sequence.SequencesManager#inspectSequence(java.lang.String)}.
	 */
	@Test
	public void testInspectSequence() {

		// prepare register test sequence collection
		SequencesManager.registerSequenceCollection("category", "yucatan.core.sequences");
		ArrayList<String> sequenceItemNames = SequencesManager.inspectSequence("category-show");
		String resultString = "";
		for (String sequenceItemName : sequenceItemNames) {
			resultString += sequenceItemName;
			resultString += "+";
		}
		// check position of commands
		Assert.assertEquals("yucatan.core.category.GetCategoryCommand+yucatan.core.category.ModifyCategoryCommand+", resultString);
		Assert.assertEquals( null, SequencesManager.inspectSequence("non-existent") );
	}

}
