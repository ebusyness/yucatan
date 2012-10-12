package yucatan.xml;

/**
 * An XmlUnmarshallException is thrown if something went wrong during unmarshalling. It's used as container of various other exceptions.
 */
@SuppressWarnings("serial")
public class XmlUnmarshallException extends Exception {

	@Deprecated
	/**
	 * Constructs an XmlUnmarshallException without message and cause. Please don't use the plain constructor and add message and cause.
	 */
	public XmlUnmarshallException() {
		super();
	}

	/**
	 * Constructs an XmlUnmarshallException without cause. Please be more specific and add cause and message if possible.
	 * 
	 * @param message A message that describes the reason of this exception.
	 */
	public XmlUnmarshallException(String message) {
		super(message);
	}

	/**
	 * Constructs an XmlUnmarshallException without message. Please be more specific and add cause and message if possible.
	 * 
	 * @param cause The Throwable to wrap within this exception.
	 */
	public XmlUnmarshallException(Throwable cause) {
		super(cause);
	}

	/**
	 * Constructs an XmlUnmarshallException.
	 * 
	 * @param message
	 * @param cause
	 */
	public XmlUnmarshallException(String message, Throwable cause) {
		super(message, cause);
	}

}
