package de.ebusyness.yucatan.core.sequence;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.Vector;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import de.ebusyness.yucatan.core.sequence.generated.SequenceType;
import de.ebusyness.yucatan.core.sequence.generated.SequencesListType;

public class SequencesManager {

	/**
	 * The file name ending for sequences files.
	 */
	private final static String FILENAME_ENDING = ".sequences.xml";

	/**
	 * HashMap of registered sequences.
	 */
	private static HashMap<String, Vector<String>> sequences = new HashMap<String, Vector<String>>();

	/**
	 * Gets the sequence as Vector from sequences cache.
	 * 
	 * @param sequenceLocation The location of sequence to run <module:package/sequenceCollectionName-sequenceName>
	 * @return the sequence Vector
	 */
	public static Vector<String> getSequence(String sequenceLocation) {
		String[] parts = sequenceLocation.split("/");
		String sequenceIdetifier = parts[Math.max(0, parts.length-1)];
		Vector<String> sequence = sequences.get(sequenceIdetifier);
		
		if (sequence == null) {
			sequence = loadSequence(sequenceLocation, sequenceIdetifier);
		}
		return sequence;
	}

	/**
	 * Loads the sequence as Vector from file.
	 * 
	 * @param sequenceLocation The location of sequence to run <module:package/sequenceCollectionName-sequenceName>
	 * @param sequenceIdetifier The full name of the sequence to run <sequenceCollectionName-sequenceName>
	 * @return the sequence Vector or null
	 */
	private static Vector<String> loadSequence(String sequenceLocation, String sequenceIdetifier) {
		// resolve <module:package/sequenceCollectionName-sequenceName> to resource name
		String resourceName = sequenceLocation.replaceAll("(.*)-.*", "$1" + FILENAME_ENDING);
		String sequenceName = sequenceIdetifier.replaceAll(".*-", "");
		InputStream resourceInputStream = ClassLoader.getSystemResourceAsStream(resourceName);
		SequencesListType sequences = null;
		if (resourceInputStream == null) {
			return null;
		}
		try {
			sequences = unmarshalSequencesInputStream(SequencesListType.class, resourceInputStream);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				resourceInputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<SequenceType> sequenesList = sequences.getSequence();
		for (SequenceType sequence : sequenesList) {
			System.out.println( sequence.getName());
			if(sequenceName == sequence.getName()) {
				System.out.println( "sequence found: "+sequenceName);
			}
		}
		return new Vector<String>();
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
