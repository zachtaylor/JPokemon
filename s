diff --git a/src/gui/GameWindow.java b/src/gui/GameWindow.java
index 07270ea..fdc9a1a 100644
--- a/src/gui/GameWindow.java
+++ b/src/gui/GameWindow.java
@@ -21,7 +21,7 @@ public class GameWindow extends JFrame {
   private JPanel main;
   private MessageView mview;
   private BattleView bwin;
-  private GodWindow gwin;
+  private Console console;
 
   private static final long serialVersionUID = 1L;
 
@@ -29,16 +29,24 @@ public class GameWindow extends JFrame {
     try {
       player = p;
       root = new JPanel();
-      add(root);
       main = new JPanel();
       
+      if (Driver.console) {
+        console = new Console(player);
+        JPanel blah = new JPanel();
+        blah.setLayout(new FlowLayout());
+        add(blah);
+        blah.add(root);
+        blah.add(console);
+      }
+      else {
+        add(root);
+      }
+      
 
       construct();
 
-      if (Driver.god) {
-        gwin = new GodWindow(player);
-        gwin.setLocationRelativeTo(this);
-      }
+      
       if (Driver.message) {
         mview = new MessageView();
         Tools.messages = mview;
@@ -66,8 +74,6 @@ public class GameWindow extends JFrame {
   }
 
   private void destruct() {
-    if (gwin != null)
-      gwin.dispose();
     if (mview != null)
       mview.destruct();
     super.dispose();
diff --git a/src/gui/GodWindow.java b/src/gui/GodWindow.java
deleted file mode 100644
index 8b74b0e..0000000
--- a/src/gui/GodWindow.java
+++ /dev/null
@@ -1,137 +0,0 @@
-package gui;
-
-import java.awt.FlowLayout;
-import java.awt.event.*;
-import javax.swing.*;
-
-import item.Bag;
-import jpkmn.*;
-import pokemon.Pokemon;
-
-public class GodWindow extends JFrame {
-  private static final long serialVersionUID = 1L;
-// TODO
-  // Heal All button, All Items, Level Leader X
-  private Player player;
-
-  public GodWindow(Player p) {
-    player = p;
-
-    construct();
-    setVisible(true);
-  }
-
-  private void construct() {
-    setTitle("");
-    setIconImage(Tools.findImage("main-icon"));
-    setSize(150, 110);
-    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
-    setLocationRelativeTo(null);
-    setLayout(new FlowLayout());
-    add(new HealButton());
-    add(new ItemButton());
-    add(new LevelButton());
-    add(new WinButton());
-  }
-
-  private class HealButton extends JButton implements ActionListener {
-
-    private static final long serialVersionUID = 1L;
-
-    public HealButton() {
-      super("Heal");
-      addActionListener(this);
-    }
-
-    @Override
-    public void actionPerformed(ActionEvent arg0) {
-      Driver.logGodEvent("Heal All Pokemon");
-      for (Pokemon p : player.party.pkmn) {
-        if (p != null) p.healDamage(10000);
-      }
-    }
-
-  }
-
-  private class ItemButton extends JButton implements ActionListener {
-
-    private static final long serialVersionUID = 1L;
-
-    public ItemButton() {
-      super("Item");
-      addActionListener(this);
-    }
-
-    @Override
-    public void actionPerformed(ActionEvent e) {
-      Driver.logGodEvent("Populate Bag");
-      Bag bag = player.bag;
-      for (int i = 0; i < 4; ++i) {
-        bag.potion(i).add(10);
-        bag.ball(i).add(10);
-      }
-      bag.xstat("attack").add(10);
-      bag.xstat("sattack").add(10);
-      bag.xstat("defense").add(10);
-      bag.xstat("sdefense").add(10);
-      bag.xstat("speed").add(10);
-      bag.stone("fire").add(10);
-      bag.stone("water").add(10);
-      bag.stone("thunder").add(10);
-      bag.stone("moon").add(10);
-      bag.stone("leaf").add(10);
-      bag.cash += 1000;
-    }
-
-  }
-
-  private class LevelButton extends JButton implements ActionListener {
-
-    private static final long serialVersionUID = 1L;
-
-    public LevelButton() {
-      super("Level");
-      addActionListener(this);
-    }
-
-    @Override
-    public void actionPerformed(ActionEvent e) {
-      try {
-        int x = Integer.parseInt(JOptionPane.showInputDialog(null,
-            "How many levels to advance : ", "God Ability : Level Leader",
-            JOptionPane.QUESTION_MESSAGE));
-        Driver.logGodEvent(player.party.leader().name + " advanced " + x
-            + " levels.");
-        while (x > 0) {
-          player.party.leader().gainExperience(player.party.leader().xpNeeded());
-          --x;
-        }
-      } catch (NumberFormatException f) {
-        // Do nothing
-      }
-    }
-
-  }
-
-  private class WinButton extends JButton implements ActionListener {
-
-    private static final long serialVersionUID = 1L;
-
-    public WinButton() {
-      super("Win");
-      addActionListener(this);
-    }
-
-    @Override
-    public void actionPerformed(ActionEvent arg0) {
-      boolean old = Driver.prefs.getBoolean("beat", false);
-      Driver.prefs.putBoolean("beat", !old);
-      try {
-        Driver.prefs.flush();
-      } catch (Exception e) {
-      }
-      Driver.logGodEvent("Registry Win toggled. Now : " + (!old));
-    }
-
-  }
-}
diff --git a/src/gui/Splash.java b/src/gui/Splash.java
index 38dc657..3591409 100644
--- a/src/gui/Splash.java
+++ b/src/gui/Splash.java
@@ -114,7 +114,7 @@ public class Splash extends JFrame {
           
           // Store the default save location
           Driver.prefs.put("save_location", f.getAbsolutePath());
-          Driver.prefs.flush();
+          //Driver.prefs.flush();
 
           // Load game
           Scanner scan = new Scanner(f);
diff --git a/src/jpkmn/Driver.java b/src/jpkmn/Driver.java
index 6c57956..615166c 100644
--- a/src/jpkmn/Driver.java
+++ b/src/jpkmn/Driver.java
@@ -12,7 +12,7 @@ import javax.swing.JOptionPane;
 
 public class Driver {
   public static Preferences prefs;
-  public static boolean debug, god, message;
+  public static boolean debug, console, message;
 
   private static StringBuilder log = new StringBuilder();
   public static String officialSerial = "HELLOWORLD";
@@ -26,8 +26,8 @@ public class Driver {
       System.out.println("Arguments specified: " + args.toString());
       if (args[0].contains("d"))
         debug = true;
-      if (args[0].contains("g"))
-        god = true;
+      if (args[0].contains("c"))
+        console = true;
       if (args[0].contains("m"))
         message = true;
     }
@@ -59,11 +59,11 @@ public class Driver {
     log.append(c.toString() + " : " + s + "\n");
   }
 
-  public static void logGodEvent(String s) {
+  public static void logConsoleEvent(String s) {
     if (debug)
-      System.out.println("God Event : " + s);
+      System.out.println("Console Event : " + s);
 
-    log.append("God Event : " + s + "\n");
+    log.append("Console Event : " + s + "\n");
   }
 
   public static <T> void crash(Class<T> c, String s) {
