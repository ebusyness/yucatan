package de.ebusyness.yucatan.core.sequence;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.ebusyness.yucatan.core.sequence.generated.SequenceType;
import de.ebusyness.yucatan.core.sequence.generated.SequencesListType;

public class SequencesManager {

	/**
	 * Sequence found.
	 */
	public final static byte SEQUENCE_FOUND = 0;

	/**
	 * Sequence not found.
	 */
	public final static byte SEQUENCE_FILE_NOT_FOUND = -1;

	/**
	 * Sequence file format error.
	 */
	public final static byte SEQUENCE_FORMAT_ERROR = -2;

	/**
	 * Sequence file format error.
	 */
	public final static byte SEQUENCE_STREAMCLOSE_ERROR = -3;

	/**
	 * The file name ending for sequences files.
	 */
	private final static String FILENAME_ENDING = ".sequences.xml";

	/**
	 * HashMap of registered sequences.
	 */
	private static HashMap<String, SequenceType> sequences = new HashMap<String, SequenceType>();

	/**
	 * Gets the sequence as Vector from sequences cache.
	 * 
	 * @param sequenceLocation The location of sequence to run <sequenceCollectionName-sequenceName>
	 * @return the sequence Vector
	 */
	public static SequenceType getSequence(String sequenceLocation) {
		if (sequenceLocation == null) {
			return null;
		}
		String[] parts = sequenceLocation.split("/");
		String sequenceIdetifier = parts[Math.max(0, parts.length - 1)];
		SequenceType sequence = sequences.get(sequenceIdetifier);
		return sequence;
	}

	/**
	 * Loads the sequence from file.
	 * 
	 * @param sequenceCollectionName The full name of the sequence to run <sequenceCollectionName>
	 * @param sequenceLocation The location of sequence to run <packageName>
	 */
	public static byte registerSequenceCollection(String sequenceCollectionName, String sequenceLocation) {
		if (sequenceCollectionName == null || sequenceLocation == null) {
			return SEQUENCE_FILE_NOT_FOUND;
		}
		// resolve packageName to full filename
		String resourceName = sequenceLocation.replaceAll("\\.", "/") + "/" + sequenceCollectionName + FILENAME_ENDING;

		InputStream resourceInputStream = ClassLoader.getSystemResourceAsStream(resourceName);

		System.out.println("resourceName: " + resourceName);
		System.out.println("resourceInputStream: " + resourceInputStream);

		if (resourceInputStream == null) {
			return SEQUENCE_FILE_NOT_FOUND;
		}
		SequencesListType sequences = null;

		try {
			sequences = unmarshalSequencesInputStream(SequencesListType.class, resourceInputStream);
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
		List<SequenceType> sequenesList = sequences.getSequence();
		for (SequenceType sequence : sequenesList) {
			System.out.println("sequence registered: " + sequenceCollectionName + "-" + sequence.getId());
			SequencesManager.sequences.put(sequenceCollectionName + "-" + sequence.getId(), sequence);

		}

		return SEQUENCE_FOUND;
	}

	/**
	 * Unmarshall the XML from specified InputStream.
	 * 
	 * @param docClass The expected class of the root element.
	 * @param resourceInputStream The resource input stream
	 * @return
	 * @throws JAXBException
	 */
	private static SequencesListType unmarshalSequencesInputStream(Class<SequencesListType> docClass, InputStream resourceInputStream) throws JAXBException {
		String packageName = docClass.getPackage().getName();
		JAXBContext jc = JAXBContext.newInstance(packageName);
		Unmarshaller u = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<SequencesListType> doc = (JAXBElement<SequencesListType>) u.unmarshal(resourceInputStream); // cast TODO Check this again
		return doc.getValue();
	}
}
