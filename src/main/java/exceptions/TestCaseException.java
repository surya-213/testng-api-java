package exceptions;

import java.io.Serial;

/**
 * Custom exception class for handling errors specific to TestCase Classes. This class extends
 * {@link RuntimeException} and provides constructors for different use cases.
 */
public class TestCaseException extends RuntimeException {

  // Define a serialVersionUID for this exception class
  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a new TestCaseException with no detail message or cause.
   */
  public TestCaseException() {
    super();
  }

  /**
   * Constructs a new TestCaseException with the specified detail message.
   *
   * @param message the detail message
   */
  public TestCaseException(String message) {
    super(message);
  }

  /**
   * Constructs a new TestCaseException with the specified detail message and cause.
   *
   * @param message the detail message
   * @param cause   the cause of the exception
   */
  public TestCaseException(String message, Throwable cause) {
    super(message, cause);
  }
}
