package jpkmn.exceptions;

/**
 * Usage of this class is prototyping TimeoutException also
 */
public class CancelException extends Exception {
  public CancelException(String message) {
    _message = message;
  }

  public String getMessage() {
    return _message;
  }

  private String _message;
  private static final long serialVersionUID = 1L;
}
