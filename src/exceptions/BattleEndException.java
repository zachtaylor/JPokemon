package exceptions;

public class BattleEndException extends Exception {
  private int exitStatus;

  public BattleEndException(int status) {
    super();
    exitStatus = status;
  }

  public int getExitStatus() {
    return exitStatus;
  }

  public String getExitDescription() {
    switch (exitStatus) {
    case 0:
      return "ambiguous";
    case 101:
      return "normal:win:wild";
    case 102:
      return "normal:win:trainer";
    case 103:
      return "normal:win:gym";
    case 110:
      return "normal:lose";
    default:
      return "unknown";
    }
  }

  // Keep eclipse happy
  private static final long serialVersionUID = 1L;
}
