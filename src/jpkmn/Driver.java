package jpkmn;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

import jpkmn.gui.Splash;
import jpkmn.gui.Tools;

public class Driver {

  public static String officialSerial = "jpkmn build-ver 0.2.2";
  
  // Later incorporated into server settings
  public static final int PARTYSIZE = 6;
  public static final int MOVENUMBER = 4;
  public static final double TYPEADVANTAGE = 2.0;
  
  public static Preferences prefs;
  public static boolean debug, console;

  public static void main(String[] args) {
    prefs = Preferences.systemNodeForPackage(Driver.class);

    // Special mode
    if (args.length > 0 && args[0].charAt(0) == '-') {
      System.out.println("Arguments specified: " + args.toString());
      if (args[0].contains("d"))
        debug = true;
      if (args[0].contains("c"))
        console = true;
    }

    // Crash recovery
    if (prefs.getBoolean("crash", false)) {
      try {
        PrintWriter pw = new PrintWriter(new File("log.log"));
        pw.println(prefs.get("log", "log lookup error"));
        pw.println("\n\n\n\n\n");
        pw.close();
        JOptionPane.showMessageDialog(null, "Log file produced.",
            "JPKMN Crash Recovery", JOptionPane.INFORMATION_MESSAGE);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      prefs.put("log", "");
      prefs.putBoolean("crash", false);
      return;
    }

    new Splash(officialSerial); // Serial for versioning purposes
  }

  public static <T> void log(Class<T> c, String s) {
    if (debug)
      System.out.println(c.toString().substring(c.toString().indexOf('.') + 1)
          + " : " + s);
    log.append(c.toString() + " : " + s + "\n");
  }

  public static void logConsoleEvent(String s) {
    if (debug)
      System.out.println("Console Event : " + s);

    log.append("Console Event : " + s + "\n");
  }

  public static <T> void crash(Class<T> c, String s) {
    if (debug)
      System.out.println("CRASH " + c.toString() + " : " + s);
    log.append("CRASH " + c.toString() + " : " + s + "\n");

    prefs.putBoolean("crash", true);
    prefs.put("log", log.toString());
    Tools.crashReport();
    throw new FatalCrash();
  }

  private static StringBuilder log = new StringBuilder();
}