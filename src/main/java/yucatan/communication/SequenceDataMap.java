package yucatan.communication;

import java.util.HashMap;

/**
 * First draft of a transport item map.
 */
public class SequenceDataMap {

	/**
	 * The {@link HashMap} which contains the key value pairs.
	 */
	private HashMap<String, Object> dataItems;

	/**
	 * Create empty SequenceDataMap.
	 */
	public SequenceDataMap() {
		dataItems = new HashMap<String, Object>();
	}

	/**
	 * Create a SequenceDataMap with initial data.
	 * 
	 * @param initalData The initial data {@link HashMap} to register.
	 */
	public SequenceDataMap(HashMap<String, Object> initalData) {
		// create hash map instance
		dataItems = new HashMap<String, Object>();

		// check initial data
		if (initalData == null) {
			return;
		}

		// put initial data to hash map
		dataItems.putAll(initalData);
	}

	/**
	 * Associates the specified value with the specified key in this map.
	 * 
	 * @param key key with which the specified value is to be associated
	 * @param value value to be associated with the specified key
	 * @return the old value
	 */
	public Object put(String key, Object value) {
		Object oldValue = dataItems.get(key);
		dataItems.put(key, value);
		return oldValue;
	}

	/**
	 * Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
	 * 
	 * @param key the key whose associated value is to be returned
	 * @return the requested value or null
	 */
	public Object get(String key) {
		return dataItems.get(key);
	}
}
