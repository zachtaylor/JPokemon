package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

import item.Item;
import jpkmn.*;
import pokemon.Pokemon;

public class Console extends JTextField implements ActionListener {
  private Player player;

  public Console(Player p) {
    super("", 30);
    player = p;
    addActionListener(this);
  }

  private void handle(String s) {
    Driver.logConsoleEvent(s);
    ArrayList<String> string = parse(s);

    try {
      if (string.get(0).equals("pokemon")) {
        Pokemon target;
        int pos;
        boolean box = false;

        // 1-argument flavor
        if (string.size() == 1) {
          Tools.notify("err", "All Pokemon", player.party.getNameList());
          Driver.logConsoleEvent("Listing all pokemon\n"
              + player.party.getNameList());
          return;
        }

        // 2-argument flavor
        try {
          pos = Integer.parseInt(string.get(1));
        } catch (Exception e) {
          pos = Integer.parseInt(string.get(1).substring(3));
          box = true;
        }
        if (box) {
          target = player.box.get(pos);
        }
        else {
          target = player.party.pkmn[pos];
        }
        if (target == null) {
          Tools.notify("err", "CONSOLE ERROR", "Pokemon " + string.get(1)
              + " doesn't exist : " + s);
          Driver.logConsoleEvent("Console event failure. Pokemon "
              + string.get(1) + " doesn't exist : " + s);
          return;
        }
        if (string.size() == 2) {
          Tools.notify(target, "Pokemon Lookup", target.toString());
          Driver.logConsoleEvent("Looking up pokemon\n" + target.toString());
          return;
        }

        // x-argument flavor
        if (string.get(2).equals("heal")) {
          target.healDamage(target.health.max);
        }
        else if (string.get(2).equals("level")) {
          for (int levels = Integer.parseInt(string.get(3)); levels > 0; --levels)
            target.gainExperience(target.xpNeeded());
        }
        else if (string.get(2).equals("restore")) {
          target.resetTempStats();
          target.condition.reset();
        }
        else if (string.get(2).equals("move")) {
          Tools.notify(target, "Pokemon Lookup", target.getMoveList());
          Driver.logConsoleEvent("Looking up move list\n"
              + target.getMoveList());
          return;
        }
        else if (string.get(2).equals("lead") && !box) {
          Pokemon swap = player.party.pkmn[0];
          player.party.pkmn[0] = target;
          player.party.pkmn[pos] = swap;
        }
        else {
          printError(s);
          return;
        }
      }

      else if (string.get(0).equals("bag")) {
        Item target = null;

        // 1-argument flavor
        if (string.size() == 1) {
          ArrayList<String> contents = new ArrayList<String>();
          contents.add("Bag Contents");
          contents.addAll(player.bag.toStringArray());
          Tools.notify("err", contents.toArray(new String[contents.size()]));
          Driver.logConsoleEvent("Listing bag contents"
              + contents.toArray(new String[contents.size()]));
          return;
        }

        // 2/3-argument flavor
        if (string.get(1).equals("potion")) {

          if (string.size() == 2) {
            Tools.notify("err", "Item Lookup", player.bag.toStringArray()
                .get(1));
            Driver.logConsoleEvent("Listing potion pocket contents: "
                + player.bag.toStringArray().get(1));
            return;
          }
          else
            target = player.bag.potion(Integer.parseInt(string.get(2)));
        }
        else if (string.get(1).equals("ball")) {

          if (string.size() == 2) {
            Tools.notify("err", "Item Lookup", player.bag.toStringArray()
                .get(0));
            Driver.logConsoleEvent("Listing ball pocket contents: "
                + player.bag.toStringArray().get(1));
            return;
          }
          else
            target = player.bag.ball(Integer.parseInt(string.get(2)));
        }
        else if (string.get(1).equals("stone")) {

          if (string.size() == 2) {
            Tools.notify("err", "Item Lookup", player.bag.toStringArray()
                .get(2));
            Driver.logConsoleEvent("Listing stone pocket contents: "
                + player.bag.toStringArray().get(1));
            return;
          }
          else
            target = player.bag.stone(string.get(2));
        }
        else if (string.get(1).charAt(0) == 'x') {

          if (string.get(1).length() == 1) {
            Tools.notify("err", "Item Lookup", player.bag.toStringArray()
                .get(3));
            Driver.logConsoleEvent("Listing xstat pocket contents: "
                + player.bag.toStringArray().get(1));
            return;
          }
          else {
            target = player.bag.xstat(string.get(1).substring(1));
            if (string.size() == 2) {
              Tools.notify("err", "Item Lookup", target.toString());
              Driver.logConsoleEvent("Describing item: " + target.toString());
              return;
            }
            else {
              target.add(Integer.parseInt(string.get(2)));
              Tools.notify("err", "Populating Item", target.toString());
              Driver.logConsoleEvent("Populating item: " + target.toString());
            }
          }
        }
        else {
          printError(s);
          return;
        }
        
        if (string.size() == 3) {
          Tools.notify("err", "Item Lookup", target.toString());
          Driver.logConsoleEvent("Describing item: " + target.toString());
          return;
        }
        else {
          target.add(Integer.parseInt(string.get(3)));
          Tools.notify("err", "Populating Item", target.toString());
          Driver.logConsoleEvent("Populating item: " + target.toString());
        }

      }

      else if (string.get(0).equals("player")) {

        // 1-argument flavor
        if (string.size() == 1) {
          Tools.notify("err", "All Pokemon", player.party.getNameList());
          Driver.logConsoleEvent("Listing all pokemon\n"
              + player.party.getNameList());
          return;
        }

        if (string.get(1).equals("cash")) {
          player.bag.cash += Integer.parseInt(string.get(2));
        }
        else if (string.get(1).equals("badge")) {
          player.badge += Integer.parseInt(string.get(2));
        }
        else {
          printError(s);
          return;
        }
      }
      else if (string.get(0).equals("create")) {

        // 1-argument flavor
        if (string.size() == 1) {
          Tools.notify("err", "Create", "Syntax not correct : 'create'");
          Driver.logConsoleEvent("Empty Create");
          return;
        }

        // 2-argument flavor
        if (string.get(1).equals("win")) {
          if (string.size() == 2) {
            Tools.notify("err", "Won Game", "Registry values set for beating game");
            Driver.logConsoleEvent("Registry set to beating game true");
            Driver.prefs.putBoolean("beat", true);
          }
          else {
            if (string.get(2).equals("on")) {
              Tools.notify("err", "Won Game", "Registry values set for beating game");
              Driver.logConsoleEvent("Registry set to beating game true");
              Driver.prefs.putBoolean("beat", true);
            }
            else if (string.get(2).equals("off")) {
              Tools.notify("err", "Un-Won Game", "Registry values set for not beating game");
              Driver.logConsoleEvent("Registry set to beating game false");
              Driver.prefs.putBoolean("beat", false);
            }
            else {
              printError(s);
              return;
            }
          }
        }
        else if (string.get(1).equals("pokemon")) {
          if (string.size() < 4) {
            printError(s);
            return;
          }
          else {
            Pokemon p = new Pokemon(Integer.parseInt(string.get(2)), Integer
                .parseInt(string.get(3)));
            if (string.size() == 5) {
              Tools.notify("err", "Created : " + p.name, p.name + " is now in party at position "+string.get(4));
              Driver.logConsoleEvent(p.toString()+" created in slot "+string.get(4));
              player.party.pkmn[Integer.parseInt(string.get(4))] = p;
            }
            else {
              Tools.notify("err", "Created : " + p.name, p.name + " is now in party at position 0");
              Driver.logConsoleEvent(p.toString()+" created in slot 0");
              player.party.pkmn[0] = p;
            }
          }
        }
        else {
          printError(s);
          return;
        }
      }

      else {
        printError(s);
        return;
      }

    } catch (Exception e) {
      printError(s);
    }
  }

  private void printError(String s) {
    Tools.notify("err", "CONSOLE ERROR", "Unknown command : " + s);
    Driver.logConsoleEvent("Console event failure. Unknown command: " + s);
  }

  private ArrayList<String> parse(String s) {
    if (s == null)
      return null;

    int length = s.length(), lastindex = 0;

    ArrayList<String> response = new ArrayList<String>();

    for (int i = 0; i < length; ++i) {
      if (s.charAt(i) == ' ') {
        response.add(s.substring(lastindex, i));
        lastindex = i + 1;
      }
      else if (i + 1 == length) {
        response.add(s.substring(lastindex));
      }
    }

    return response;
  }

  @Override
  public void actionPerformed(ActionEvent arg0) {
    String text = this.getText();
    handle(text);
    setText("");
  }

  private static final long serialVersionUID = 1L;
}
