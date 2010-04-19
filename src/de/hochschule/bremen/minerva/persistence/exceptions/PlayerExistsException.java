package de.hochschule.bremen.minerva.persistence.exceptions;

public class PlayerExistsException extends PersistenceIOException {

	/**
	 * DOCME
	 * 
	 */
	private static final long serialVersionUID = -1406415230390126832L;

	/**
	 * DOCME
	 * 
	 */
	public PlayerExistsException() {
		super();
	}
	
	/**
	 * DOCME
	 * 
	 * @param message
	 */
	public PlayerExistsException(String message) {
		super(message);
	}
}
