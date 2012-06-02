package exe;

public class FatalCrash extends RuntimeException {
  // Keep eclipse happy
  private static final long serialVersionUID = 1L;

  public FatalCrash() {
    super("OMFG");
  }
}
