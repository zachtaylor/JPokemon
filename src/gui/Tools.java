package gui;

import java.awt.Image;
import java.net.URL;

import javax.swing.*;

import jpkmn.Driver;

import item.*;
import pokemon.*;
import pokemon.move.Move;

public class Tools {
  static GameWindow game;

  private static MessageView messages;

  /**
   * Generates a notify window, with the specified icon
   * 
   * @param icon The icon that will be used
   * @param title Title of the window
   * @param message Message of the window
   */
  public static void notify(Object icon, String... message) {
    if (icon instanceof Image)
      throw new RuntimeException();
    notify(findImage(icon), message);
  }

  private static void notify(Image icon, String... message) {
    messages.addMessage(icon, message);
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

  public static void createMessageWindow() {
    messages = new MessageView();
    messages.setLocationRelativeTo(game);
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
  public static int selectFromParty(String message, Party userparty) {
    StringBuilder list = new StringBuilder("Party List:\n");
    String expectedOption = "0|1|2|3|4|5|6";
    int partyCount = 0;

    // We have to build the message to vary depending on
    // how many moves the active Pokemon has
    for (int i = 0; i < userparty.size(); ++i) {
      if (userparty.pkmn[i] != null) {
        partyCount++;
        list.append(i + ": " + userparty.pkmn[i].toString() + "\n");
      }
    }

    return Integer.parseInt(Tools.askForInput(message, list.toString(),
    // From 0 to 2 x The number of moves, - 1 to drop last |
        expectedOption.substring(0, partyCount * 2 - 1)));
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
   * Asks the user to pick a move to use. If m != null, displays a message
   * about learning m, in place of whichever move is picked. If m != null and
   * there are less than 4 learned moves, returns the position of the first
   * null.
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
   * Allows a user to select which move they would like to use
   * 
   * @param p The active Pokemon
   * @return The selected move index
   */
  public static int selectMove(Pokemon p) {
    StringBuilder message = new StringBuilder("Move List:\n");
    String expectedOption = "0|1|2|3";
    int moveCount = 0;

    // We have to build the message to vary depending on
    // how many moves the active Pokemon has
    for (int i = 0; i < p.move.length; i++) {
      if (p.move[i] != null) {
        moveCount++;
        message.append(i + ": " + p.move[i].toString() + "\n");
      }
    }

    return Integer.parseInt(Tools.askForInput("Select Move",
        message.toString(),
        // From 0 to 2 x The number of moves, - 1 to drop last |
        expectedOption.substring(0, moveCount * 2 - 1)));
  }

  /**
   * Continually asks a user for input until they enter a valid pattern
   * 
   * @param title Message box title
   * @param message Message for the message box
   * @param expectedPattern A regular expression with the expected input
   *          pattern
   * @return A string containing valid input
   */
  public static String askForInput(String title, String message,
      String expectedPattern) {
    String input = "";

    while (input == null || input == "" || !input.matches(expectedPattern)) {
      input = JOptionPane.showInputDialog(null, message, title,
          JOptionPane.QUESTION_MESSAGE);
    }

    return input;
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