package yucatan.core.sequence;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import yucatan.core.sequence.generated.XmlTypeCommand;
import yucatan.core.sequence.generated.XmlTypeSequence;
import yucatan.core.sequence.generated.XmlTypeSequencesList;

/**
 * The SequencesManager provides methods to register sequence and provide acces to them.
 * 
 */
public class SequencesManager {

	/**
	 * Sequence found.
	 */
	public final static String SEQUENCE_FOUND = "SEQUENCE_FOUND";

	/**
	 * Sequence not found.
	 */
	public final static String SEQUENCE_FILE_NOT_FOUND = "SEQUENCE_FILE_NOT_FOUND";

	/**
	 * Sequence file format error.
	 */
	public final static String SEQUENCE_FORMAT_ERROR = "SEQUENCE_FORMAT_ERROR";

	/**
	 * Sequence file format error.
	 */
	public final static String SEQUENCE_STREAMCLOSE_ERROR = "SEQUENCE_STREAMCLOSE_ERROR";

	/**
	 * The file name ending for sequences files.
	 */
	private final static String FILENAME_ENDING = ".sequences.xml";

	/**
	 * HashMap of registered sequences.
	 */
	private static HashMap<String, XmlTypeSequence> sequences = new HashMap<String, XmlTypeSequence>();

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
		List<XmlTypeCommand> commandsDeclaration = sequenceDeclaration.getCommand();
		return commandsDeclaration;
	}

	/**
	 * Returns all sequence command names.
	 * 
	 * @param sequenceLocation The location of sequence to run &lt;sequenceCollectionName-sequenceName&gt;
	 * @return Returns the command names of specified sequence or null.
	 */
	public static ArrayList<String> inspectSequence(String sequenceLocation) {
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
			return SEQUENCE_FILE_NOT_FOUND;
		}

		// resolve packageName to full filename
		String resourceName = sequenceLocation.replaceAll("\\.", "/") + "/" + sequenceCollectionName + FILENAME_ENDING;

		// load resource
		InputStream resourceInputStream = ClassLoader.getSystemResourceAsStream(resourceName);
		if (resourceInputStream == null) {
			return SEQUENCE_FILE_NOT_FOUND;
		}

		// unmarshall
		XmlTypeSequencesList sequencesCollection = null;
		try {
			sequencesCollection = unmarshalSequencesInputStream(XmlTypeSequencesList.class, resourceInputStream);
		} catch (JAXBException e) {
			e.printStackTrace();
			return SEQUENCE_FORMAT_ERROR;
		} finally {
			try {
				resourceInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
				return SEQUENCE_STREAMCLOSE_ERROR;
			}
		}

		// put sequences to sequences cache and sort
		List<XmlTypeSequence> sequenesList = sequencesCollection.getSequence();
		for (XmlTypeSequence sequence : sequenesList) {
			// TODO logging
			// System.out.println("sequence registered: " + sequenceCollectionName + "-" + sequence.getId());
			SequencesManager.sequences.put(sequenceCollectionName + "-" + sequence.getId(), sequence);

			// sort commands
			List<XmlTypeCommand> commandsDeclaration = sequence.getCommand();
			Collections.sort(commandsDeclaration);
		}

		return SEQUENCE_FOUND;
	}

	/**
	 * Unmarshall the XML from specified InputStream.
	 * 
	 * @param docClass The expected class of the root element.
	 * @param resourceInputStream The resource input stream.
	 * @return
	 * @throws JAXBException
	 */
	private static XmlTypeSequencesList unmarshalSequencesInputStream(Class<XmlTypeSequencesList> docClass, InputStream resourceInputStream) throws JAXBException {
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Unmarshaller u = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<XmlTypeSequencesList> doc = (JAXBElement<XmlTypeSequencesList>) u.unmarshal(resourceInputStream); // cast TODO Check this again
		return doc.getValue();
	}
}
