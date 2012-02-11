package jpkmn;

import gui.Splash;
import gui.Tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;

public class Driver {
  public static Preferences prefs;
  public static boolean debug, console, message;

  private static StringBuilder log = new StringBuilder();
  public static String officialSerial = "HELLOWORLD";

  public static void main(String[] args) {
    debug = true; // TODO : remove this
    prefs = Preferences.systemNodeForPackage(Driver.class);

    // Special mode
    if (args.length > 0 && args[0].charAt(0) == '-') {
      System.out.println("Arguments specified: " + args.toString());
      if (args[0].contains("d"))
        debug = true;
      if (args[0].contains("c"))
        console = true;
      if (args[0].contains("m"))
        message = true;
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
}