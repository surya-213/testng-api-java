package exceptions;

import java.io.Serial;

/**
 * Custom exception class for handling errors specific to Utility Classes. This class extends
 * {@link RuntimeException} and provides constructors for different use cases.
 */
public class UtilException extends RuntimeException {

  // Define a serialVersionUID for this exception class
  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new UtilException with no detail message or cause.
   */
  public UtilException() {
    super();
  }

  /**
   * Constructs a new UtilException with the specified detail message.
   *
   * @param message the detail message
   */
  public UtilException(String message) {
    super(message);
  }

  /**
   * Constructs a new UtilException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause   the cause of the exception
   */
  public UtilException(String message, Throwable cause) {
    super(message, cause);
  }
}
