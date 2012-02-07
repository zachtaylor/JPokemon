package gui;

import java.awt.Image;
import java.net.URL;

import javax.swing.*;

import jpkmn.Driver;

import item.*;
import pokemon.*;
import pokemon.move.Move;

public class Tools {
  static MessageView messages;

  /**
   * Generates a notify window, with the specified icon
   * 
   * @param icon The icon that will be used
   * @param title Title of the window
   * @param message Message of the window
   */
  public static void notify(Object icon, String title, String message) {
    if (icon instanceof Image)
      throw new RuntimeException();
    notify(findImage(icon), title, message);
  }

  private static void notify(Image icon, String title, String message) {
    if (Driver.message && messages != null && messages.isVisible()) {
      messages.addMessage(icon, title, message);
    }
    else {
      try {
        JOptionPane.showMessageDialog(null, message, title,
            JOptionPane.INFORMATION_MESSAGE, new ImageIcon(icon));
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, message, title,
            JOptionPane.INFORMATION_MESSAGE);
      }
    }
  }

  /**
   * Retrieves the Image to be used for an object
   * 
   * @param o Object to get the image of.
   * @return Image for that object
   */
  public static Image findImage(Object o) {
    if (o instanceof Pokemon)
      return getImage((Pokemon) o);
    else if (o instanceof Item)
      return getImage((Item) o);
    else
      return getImage(o.toString());
  }

  private static Image getImage(String s) {
    URL resource = Tools.class.getResource("../img/" + s + ".png");
    if (resource == null)
      resource = Tools.class.getResource("../img/err.png");
    return new ImageIcon(resource).getImage();
  }

  private static Image getImage(Pokemon p) {
    return getImage("pkmn/" + p.number);
  }

  private static Image getImage(Item i) {
    String dest = "item/";

    if (i instanceof Ball) {
      dest += "ball/" + i.getName().toLowerCase().charAt(0);
    }
    else if (i instanceof Machine) {
      dest += "machine";
    }
    else if (i instanceof Potion) {
      dest += "potion/" + i.getName().toLowerCase().charAt(0);
    }
    else if (i instanceof Stone) {
      dest += "stone/"
          + i.getName().substring(0, i.getName().indexOf("stone"))
              .toLowerCase();
    }
    else {
      dest += "xstat";
    }

    return getImage(dest);
  }

  /**
   * Asks the user if they want Pokemon p to evolve.
   * 
   * @param p The Pokemon that can evolve.
   * @return true if the user wants them to.
   */
  public static boolean askEvolution(Pokemon p) {
    // TODO: figure this out

    return false;
  }

  /**
   * Return position of selected party member. Only show buttons for members
   * that are != null.
   * 
   * @param userparty The party to survey
   * @return Position of Pokemon in party (0-5). -1 if cancel.
   */
  public static int selectFromParty(Party userparty) {
    // TODO: figure this out

    return 0;
  }

  /**
   * Essentially the same as selectFromParty. Maybe some different text.
   * 
   * @param starters The party of starter Pokemon
   * @return Position of Pokemon in party (0-2). -1 if cancel
   */
  public static int selectStarter(Party starters) {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * Returns a selected item from the bag
   * 
   * @param bag Bag to choose from
   * @return Item from the bag
   */
  public static Item item(Bag bag) {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Asks the user to pick a move position. If m != null, displays a message
   * about learning m.
   * 
   * @param pokemon Pokemon to learn move
   * @param m Move to be learned
   * @return position for the move
   */
  public static int askMove(Pokemon p, Move m) {
    return Integer.parseInt(JOptionPane.showInputDialog(null,
        p.move.toString(), "What Move?", JOptionPane.QUESTION_MESSAGE));
  }

  /**
   * Generates a window which says "OOPS. I broke"
   */
  public static void crashReport() {
    JOptionPane.showMessageDialog(null, "Well, this is embarassing.\nPlease "
        + "restart the game to generate a log file. This will appear in the"
        + "\ninstall directory for the game, named \"log.log\"", "CRASH",
        JOptionPane.ERROR_MESSAGE);
  }
} 