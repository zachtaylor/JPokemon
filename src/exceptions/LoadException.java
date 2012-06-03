package exceptions;

public class LoadException extends Exception {
  public LoadException(String message) {
    super();
    _message = message;
  }

  public String getMessage() {
    return _message;
  }

  private String _message;
}
