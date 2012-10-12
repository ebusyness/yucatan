/**
 * 
 */
package yucatan.xml;

import static org.junit.Assert.fail;

import java.io.InputStream;

import junit.framework.Assert;

import org.junit.Test;

import yucatan.sequence.generated.XmlTypeSequencesList;

/**
 * @author Samuel
 *
 */
public class TestXmlFileUnmarshaller {

	/**
	 * Test method for {@link yucatan.xml.XmlFileUnmarshaller#XmlFileUnmarshaller(java.lang.Class, java.lang.String)}, {@link yucatan.xml.XmlFileUnmarshaller#unmarshall()}.
	 */
	@Test
	public void testXmlFileUnmarshallerClassOfTString() {
		XmlFileUnmarshaller<XmlTypeSequencesList> unmarshaller = new XmlFileUnmarshaller<XmlTypeSequencesList>(XmlTypeSequencesList.class,
				"src/test/resources/yucatan/sequences/category.sequences.xml");
		try {
			XmlTypeSequencesList sequencesList = unmarshaller.unmarshall();
			Assert.assertEquals(6, sequencesList.getSequence().size());
		} catch (XmlUnmarshallException e) {
			fail("XmlUnmarshallException - could not unmarshall category.sequences.xml.");
		}
	}

	/**
	 * Test method for {@link yucatan.xml.XmlFileUnmarshaller#XmlFileUnmarshaller(java.lang.Class, java.io.InputStream)}, {@link yucatan.xml.XmlFileUnmarshaller#unmarshall()}.
	 */
	@Test
	public void testXmlFileUnmarshallerClassOfTInputStream() {
		InputStream resourceInputStream = ClassLoader.getSystemResourceAsStream("yucatan/sequences/category.sequences.xml");
		XmlFileUnmarshaller<XmlTypeSequencesList> unmarshaller = new XmlFileUnmarshaller<XmlTypeSequencesList>(XmlTypeSequencesList.class,resourceInputStream);
		try {
			XmlTypeSequencesList sequencesList = unmarshaller.unmarshall();
			Assert.assertEquals(6, sequencesList.getSequence().size());
		} catch (XmlUnmarshallException e) {
			fail("XmlUnmarshallException - could not unmarshall category.sequences.xml.");
		}
	}

}
