package yucatan.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * Generic xml file unmarshaller.
 * 
 * @param <T> The root element type.
 */
public class XmlFileUnmarshaller<T> {

	/**
	 * Name of the xml file.
	 */
	private String fileName;

	private InputStream inputStream;

	private final Class<T> elementClass;

	/**
	 * Constructs an XmlFileUnmarshaller of specified type from specified file name.
	 * 
	 * @param elementClass The class of xml root to work work with.
	 * @param xmlFileName The full qualified path to the xml file.
	 */
	public XmlFileUnmarshaller(final Class<T> elementClass, String xmlFileName) {
		this.fileName = xmlFileName;
		this.elementClass = elementClass;
	}

	/**
	 * Constructs an XmlFileUnmarshaller of specified type from input stream.
	 * 
	 * @param elementClass The class of xml root to work work with.
	 * @param inputStream The file input stream.
	 */
	public XmlFileUnmarshaller(final Class<T> elementClass, InputStream inputStream) {
		this.inputStream = inputStream;
		this.elementClass = elementClass;
	}

	/**
	 * Get the xml file as object tree.
	 * 
	 * @return The xml file as an object.
	 * @throws XmlUnmarshallException
	 */
	public T unmarshall() throws XmlUnmarshallException {
		establishInputStream();
		try {
			return doUnmarshall();
		} catch (JAXBException e) {
			throw new XmlUnmarshallException("A JAXBException occured - could not unmarshall " + fileName != null ? " file: " + fileName : " InputStream.", e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				// do nothing
			}
		}
	}

	/**
	 * Make sure we can work on an InputStream.
	 * 
	 * @throws XmlUnmarshallException
	 */
	private void establishInputStream() throws XmlUnmarshallException {
		final File xmlFile;
		if (fileName != null) {
			xmlFile = new File(fileName);
			if (inputStream == null) {
				try {
					inputStream = new FileInputStream(xmlFile);
				} catch (FileNotFoundException e) {
					throw new XmlUnmarshallException("A FileNotFoundException occured - could not unmarshall file " + xmlFile.getAbsolutePath(), e);
				}
			}
		}
	}

	/**
	 * Does the unmarshalling.
	 * 
	 * @param xmlFileInputStream The InputStream to work on.
	 * @return the xmlRootNode.
	 * @throws JAXBException
	 */
	private T doUnmarshall() throws JAXBException {
		final String packageName = elementClass.getPackage().getName();
		final JAXBContext jc = JAXBContext.newInstance(packageName);
		final Unmarshaller u = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		final JAXBElement<T> xmlRooteNode = (JAXBElement<T>) u.unmarshal(inputStream);
		return xmlRooteNode.getValue();
	}
}
