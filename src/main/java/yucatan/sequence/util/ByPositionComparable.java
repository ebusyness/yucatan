package yucatan.sequence.util;

// TODO -> It would be better to use a Comperator here

/**
 * Base class that implements Compareable to sort elements by attribute position (ascending).
 * 
 */
public abstract class ByPositionComparable implements Comparable<ByPositionComparable> {

	/**
	 * The position property defines the sort order in a sorted list.
	 */
	private Float position;

	/**
	 * Gets the value of the position property.
	 * 
	 * @return the value of the position property or null.
	 */
    public Float getPosition() {
        return position;
    }
    
	/**
	 * Compares this object with the specified object for order (sort elements by attribute position).
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than the specified object.
	 */
	@Override
	public int compareTo(ByPositionComparable compareItem) {
		if (compareItem == null) {
			return 1;
		}
		if (compareItem.getPosition() == null) {
			return 0;
		}
		if (compareItem.getPosition() > getPosition()) {
			return -1;
		}
		if (compareItem.getPosition() < getPosition()) {
			return 1;
		}
		return 0;
	}

}
