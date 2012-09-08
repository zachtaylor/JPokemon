package jpkmn.exceptions;

public class FatalCrash extends RuntimeException {
  public FatalCrash() {
    super("OMFG");
  }
  private static final long serialVersionUID = 1L;
}