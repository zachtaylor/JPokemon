package jpkmn.exe;

import jpkmn.exe.launcher.Launcher;

public class Driver {
  public static final String officialSerial = "jpkmn build-ver 0.2.3";
  public static boolean debug, console;

  public static void main(String[] args) {
    new Launcher();
  }

  private static StringBuilder log = new StringBuilder();
}