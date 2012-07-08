package yucatan.communication;

/**
 * Class that represents an read-only http parameter value.
 * 
 */
public final class HttpParameterValue {

	/**
	 * The value of the parameter as string
	 */
	private String value;

	/**
	 * Initialize value from String.
	 */
	public HttpParameterValue(String val) {
		value = val;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
}
