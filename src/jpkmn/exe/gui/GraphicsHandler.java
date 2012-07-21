package jpkmn.exe.gui;

import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import jpkmn.exceptions.CancelException;
import jpkmn.exceptions.LoadException;
import jpkmn.game.battle.Slot;
import jpkmn.game.item.Item;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.storage.Party;
import jpkmn.img.ImageFinder;

public class GraphicsHandler {
  public static void main(String[] args) {
    Player zach;
    Party party;
    GraphicsHandler g;

    try {
      zach = PlayerRegistry.fromFile("newfile");

    } catch (LoadException le) {
      return;
    }

    g = zach.screen;
    party = zach.party;

    g.notify("Test Notification", "Line1", "Line2");

    try {
      if (g.isEvolutionOkay(party.get(0))) {
        g.notify("Evolutioin allowed");
      }
      else {
        g.notify("Evolution not allowed");
      }
    } catch (CancelException e) {
      g.notify("Evolution exception");
      return;
    }

    try {
      int index = g.getMoveIndex("Select a move", party.get(0));

      g.notify("Move Selected", party.get(0).moves.get(index).name());
    } catch (CancelException c) {
      g.notify("Move Selection exception");
      return;
    }

    try {
      int index = g.getPartyIndex("Select a pokemon");

      g.notify("Pokemon Selected", party.get(index).name());
    } catch (CancelException c) {
      g.notify("Pokemon Selection exception");
      return;
    }

    try {
      Item item = g.getItemChoice("item");

      g.notify("Item Selected", item.name());
    } catch (CancelException c) {
      g.notify("Item Selection exception");
      return;
    }
  }

  public GraphicsHandler() {
  }

  public void player(Player p) {
    _player = p;
    _inbox = new MessageView();
    _window = new GameWindow();
  }

  public void notify(String... s) {
    if (mock()) return;

    _inbox.addMessage(s);
  }

  public void showWorld() {
    if (mock()) return;

    _window.showMain();

    refresh();
  }

  public void showBattle(int battleID, int slotID) {
    if (mock()) return;

    _window.showBattle(battleID, slotID);

    refresh();
  }

  public void refresh() {
    if (mock()) return;

    _window.refresh();
  }

  public boolean isEvolutionOkay(Pokemon p) throws CancelException {
    if (mock()) return true;

    String message = "Allow " + p.name() + " to evolve?";
    String title = p.name() + " wants to evolve!";
    Icon icon = new ImageIcon(ImageFinder.find(p));

    return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,
        message, title, JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, icon);
  }

  public int getMoveIndex(String message, Pokemon p) throws CancelException {
    if (mock()) {
      // TODO : stuff
    }

    String[] moveNames = p.moves.list();
    String title = "Select Move Index";
    Icon icon = new ImageIcon(ImageFinder.find(p));

    return JOptionPane.showOptionDialog(null, message, title,
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon,
        moveNames, null);
  }

  public int getPartyIndex(String message) throws CancelException {
    if (mock()) {
      // TODO : stuff
    }

    ImageIcon[] options = new ImageIcon[_player.party.size()];

    for (int i = _player.party.size() - 1; i >= 0; i--)
      options[i] = new ImageIcon(ImageFinder.find(_player.party.get(i)));

    return JOptionPane.showOptionDialog(null, message, "Select From Party",
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
        options, null);
  }

  public Slot getTargetSlot(List<Slot> enemySlots) throws CancelException {
    if (mock()) {
      // TODO : stuff
    }
    if (enemySlots.size() == 1) return enemySlots.get(0);

    // TODO
    return null;
  }

  public Item getItemChoice(String message) throws CancelException {
    if (mock()) {
      // TODO : stuff
    }

    Player player = (Player) _player;
    String[] options = { "Balls", "Potions", "Stones", "Machines" };

    int itemFamily = JOptionPane.showOptionDialog(null, message,
        "Select An Item", JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, options, null);

    if (itemFamily == 0) {
      ImageIcon[] ballOptions = {
          new ImageIcon(ImageFinder.find("item/ball/p")),
          new ImageIcon(ImageFinder.find("item/ball/g")),
          new ImageIcon(ImageFinder.find("item/ball/u")),
          new ImageIcon(ImageFinder.find("item/ball/m")) };

      int ball = JOptionPane.showOptionDialog(null, message, "Select An Item",
          JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
          ballOptions, null);

      if (ball != -1) return player.bag.ball(ball);
    }
    else if (itemFamily == 1) {
      ImageIcon[] potionOptions = {
          new ImageIcon(ImageFinder.find("item/potion/p")),
          new ImageIcon(ImageFinder.find("item/potion/s")),
          new ImageIcon(ImageFinder.find("item/potion/h")),
          new ImageIcon(ImageFinder.find("item/potion/f")) };

      int potion = JOptionPane.showOptionDialog(null, message,
          "Select An Item", JOptionPane.DEFAULT_OPTION,
          JOptionPane.QUESTION_MESSAGE, null, potionOptions, null);

      if (potion != -1) return player.bag.potion(potion);
    }
    else if (itemFamily == 2) {
      ImageIcon[] stoneOptions = {
          new ImageIcon(ImageFinder.find("item/stone/f")),
          new ImageIcon(ImageFinder.find("item/stone/l")),
          new ImageIcon(ImageFinder.find("item/stone/m")),
          new ImageIcon(ImageFinder.find("item/stone/t")),
          new ImageIcon(ImageFinder.find("item/stone/w")) };

      int stone = JOptionPane.showOptionDialog(null, message, "Select An Item",
          JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
          stoneOptions, null);

      if (stone == 0)
        return player.bag.stone("fire");
      else if (stone == 1)
        return player.bag.stone("leaf");
      else if (stone == 2)
        return player.bag.stone("moon");
      else if (stone == 3)
        return player.bag.stone("thunder");
      else if (stone == 4)
        return player.bag.stone("water");
    }

    throw new CancelException("");
  }

  private boolean mock() {
    return _player == null;
  }

  private Player _player;
  private MessageView _inbox;
  private GameWindow _window;
}