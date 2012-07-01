package yucatan.core.sequence.util;

/**
 * Base class that implements Compareable to sort elements by attribute position (ascending).
 * 
 */
public abstract class ByPositionComparable implements Comparable<ByPositionComparable> {

	protected Float position;
	
    public Float getPosition() {
        return position;
    }
    
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ByPositionComparable compareItem) {
		if (compareItem == null) {
			return 0;
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
