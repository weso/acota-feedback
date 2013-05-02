package es.weso.acota.core.exceptions;

/**
 * An exception that occurs while performing persistence (C.R.U.D.)
 * @author César Luis Alvargonzález
 */
public class AcotaPersistenceException extends Exception{

	private static final long serialVersionUID = 4386092187056764769L;

	/**
	 * Constructs a new AcotaPersistenceException with the
	 *  specified detail message and cause.
	 * @param message the detail message
	 * @param cause the cause
	 */
	public AcotaPersistenceException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Constructs a new AcotaPersistenceException with 
	 * the specified detail message.
	 * @param message the detail message
	 */
	public AcotaPersistenceException(String message) {
		super(message);
	}

	/**
	 * Constructs a new AcotaPersistenceException with the 
	 * specified cause
	 * @param cause the cause
	 */
	public AcotaPersistenceException(Throwable cause) {
		super(cause);
	}

}
