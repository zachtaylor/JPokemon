package jpkmn.exe.gui.start;

public enum StartEntryValue {
  POKEMON, SAVE, EXIT, QUIT;

  public static StartEntryValue valueOf(int v) {
    return values()[v];
  }
}