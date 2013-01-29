package jpkmn.exe.gui.start;

public enum StartEntryValue {
  UPGRADE, SAVE, EXIT, QUIT;

  public static StartEntryValue valueOf(int v) {
    return values()[v];
  }
}