package de.hochschule.bremen.minerva.persistence.exceptions;

public class PlayerNotFoundException extends PersistenceIOException {

	/**
	 * DOCME
	 * 
	 */
	private static final long serialVersionUID = -2158564109074058756L;
	
	/**
	 * DOCME
	 * 
	 */
	public PlayerNotFoundException() {
		super();
	}
	
	/**
	 * DOCME
	 * 
	 * @param message
	 */
	public PlayerNotFoundException(String message) {
		super(message);
	}

}
