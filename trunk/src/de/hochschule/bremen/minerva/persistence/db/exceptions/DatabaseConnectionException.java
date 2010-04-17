package de.hochschule.bremen.minerva.persistence.db.exceptions;

public class DatabaseConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7538188076728398157L;

	/**
	 * DOCME 
	 */
	public DatabaseConnectionException() {
		super();
	}

	/**
	 * DOCME
	 * @param message
	 */
	public DatabaseConnectionException(String message) {
		super(message);
	}
}
