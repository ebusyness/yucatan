package yucatan.sequence;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import yucatan.sequence.generated.XmlTypeCommand;
import yucatan.sequence.generated.XmlTypeSequence;
import yucatan.sequence.generated.XmlTypeSequencesList;
import yucatan.xml.XmlFileUnmarshaller;
import yucatan.xml.XmlUnmarshallException;

/**
 * The SequencesManager provides methods to register sequence and provide acces to them.
 * 
 */
final class SequencesManager {

	/*
	 * Hide Utility Class Constructor.
	 */
	private SequencesManager() {
	}

	/**
	 * Sequence found.
	 */
	public static final String SEQUENCE_FOUND = "SEQUENCE_FOUND";

	/**
	 * Sequence not found.
	 */
	public static final String SEQUENCE_FILE_NOT_FOUND = "SEQUENCE_FILE_NOT_FOUND";

	/**
	 * Sequence file format error.
	 */
	public static final String SEQUENCE_FORMAT_ERROR = "SEQUENCE_FORMAT_ERROR";

	/**
	 * The file name ending for sequences files.
	 */
	private static final String FILENAME_ENDING = ".sequences.xml";

	/**
	 * HashMap of registered sequences.
	 */
	private static Map<String, XmlTypeSequence> sequences = new HashMap<String, XmlTypeSequence>();

	/**
	 * The log4j logger of this class.
	 */
	private static Logger log = Logger.getLogger(SequencesManager.class);

	/**
	 * Gets the sequence from sequences cache.
	 * 
	 * @param sequenceLocation The location of sequence to run &lt;sequenceCollectionName-sequenceName&gt;.
	 * @return Returns the sequence of commands or null.
	 */
	public static List<XmlTypeCommand> getSequence(String sequenceLocation) {
		if (sequenceLocation == null) {
			return null;
		}
		String[] parts = sequenceLocation.split("/");
		String sequenceIdetifier = parts[Math.max(0, parts.length - 1)];
		XmlTypeSequence sequenceDeclaration = sequences.get(sequenceIdetifier);
		if (sequenceDeclaration == null) {
			return null;
		}
		return sequenceDeclaration.getCommand();
	}

	/**
	 * Returns all sequence command names.
	 * 
	 * @param sequenceLocation The location of sequence to run &lt;sequenceCollectionName-sequenceName&gt;
	 * @return Returns the command names of specified sequence or null.
	 */
	public static List<String> inspectSequence(String sequenceLocation) {
		if (sequenceLocation == null) {
			return null;
		}
		ArrayList<String> sequenceItems = new ArrayList<String>();
		String[] parts = sequenceLocation.split("/");
		String sequenceIdetifier = parts[Math.max(0, parts.length - 1)];

		// get sequence commands
		List<XmlTypeCommand> commandsDeclaration = getSequence(sequenceIdetifier);
		if (commandsDeclaration == null) {
			return null;
		}
		for (XmlTypeCommand commandDeclaration : commandsDeclaration) {
			sequenceItems.add(commandDeclaration.getName());
		}
		return sequenceItems;
	}

	/**
	 * Loads the sequence from file.
	 * 
	 * @param sequenceCollectionName The full name of the sequence to run &lt;sequenceCollectionName&gt;.
	 * @param sequenceLocation The location of sequence to run &ltpackageName&gt;.
	 */
	public static String registerSequenceCollection(String sequenceCollectionName, String sequenceLocation) {
		if (sequenceCollectionName == null || sequenceLocation == null) {
			log.error("Failed to register the specified sequence collection due to an null value. passed values: sequenceCollectionName='" + sequenceCollectionName
					+ "' sequenceLocation='" + sequenceLocation + "'");
			return SEQUENCE_FILE_NOT_FOUND;
		}

		// resolve packageName to full filename
		String resourceName = sequenceLocation.replaceAll("\\.", "/") + "/" + sequenceCollectionName + FILENAME_ENDING;

		// load resource
		InputStream resourceInputStream = ClassLoader.getSystemResourceAsStream(resourceName);
		if (resourceInputStream == null) {
			log.error("Failed to register the specified sequence collection. The sequence collection file ('" + resourceName
					+ "') could not be found. passed values: sequenceCollectionName='" + sequenceCollectionName + "' sequenceLocation='" + sequenceLocation + "'");
			return SEQUENCE_FILE_NOT_FOUND;
		}

		// unmarshall
		XmlFileUnmarshaller<XmlTypeSequencesList> unmarshaller = new XmlFileUnmarshaller<XmlTypeSequencesList>(XmlTypeSequencesList.class, resourceInputStream);
		XmlTypeSequencesList sequencesCollection;
		try {
			sequencesCollection = unmarshaller.unmarshall();
		} catch (XmlUnmarshallException e) {
			log.error("Failed to register the specified sequence collection. The content of the sequence collection file '" + resourceName + "' could not be unmarshalled.", e);
			return SEQUENCE_FORMAT_ERROR;
		}

		// put sequences to sequences cache and sort
		List<XmlTypeSequence> sequenesList = sequencesCollection.getSequence();
		for (XmlTypeSequence sequence : sequenesList) {
			log.info("registered sequence: " + sequenceCollectionName + "-" + sequence.getId());
			SequencesManager.sequences.put(sequenceCollectionName + "-" + sequence.getId(), sequence);

			// sort commands
			List<XmlTypeCommand> commandsDeclaration = sequence.getCommand();
			Collections.sort(commandsDeclaration);
		}

		return SEQUENCE_FOUND;
	}

}
