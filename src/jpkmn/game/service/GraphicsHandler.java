package jpkmn.game.service;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import jpkmn.exceptions.DialogCancelException;
import jpkmn.exceptions.LoadException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.game.battle.Slot;
import jpkmn.game.item.Item;
import jpkmn.game.item.ItemType;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.img.ImageFinder;

import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;

public class GraphicsHandler {
  public static void main(String[] args) {
    Player zach = null;
    GraphicsHandler g = null;
    PokemonStorageUnit party = null;

    try {
      zach = PlayerRegistry.load("Zach");

    } catch (LoadException le) {
      le.printStackTrace();
      System.exit(0);
    }

    g = new GraphicsHandler(zach);
    party = zach.party();

    zach.notify("Test Notification", "Line1", "Line2");

    try {
      if (g.isEvolutionOkay(party.get(0))) {
        g.notify("Evolution allowed");
      }
      else {
        g.notify("Evolution not allowed");
      }
    } catch (DialogCancelException e) {
      g.notify("Evolution exception");
      return;
    }

    try {
      int index = g.getMoveIndex("Select a move", party.get(0));

      g.notify("Move Selected", party.get(0).moves.get(index).name());
    } catch (DialogCancelException c) {
      g.notify("Move Selection exception");
      return;
    }

    try {
      int index = g.getPartyIndex("Select a pokemon");

      g.notify("Pokemon Selected", party.get(index).name());
    } catch (DialogCancelException c) {
      g.notify("Pokemon Selection exception");
      return;
    }

    try {
      Item item = g.getItemChoice("item");

      g.notify("Item Selected", item.name());
    } catch (DialogCancelException c) {
      g.notify("Item Selection exception");
      return;
    }

    System.exit(0);
  }

  public GraphicsHandler(Player p) {
    _player = p;
    _window = new GameWindow(this, p.id());
  }

  public void notify(String... s) {
    if (mock())
      return;

    _window.inbox().addMessage(s);
  }

  public void showWorld() {
    if (mock())
      return;

    _window.showMain();
  }

  public void showBattle() {
    if (mock())
      return;

    _window.showBattle();
  }

  public void showUpgrade(int partyIndex) {
    if (mock())
      return;

    _window.showUpgrade(partyIndex);
  }

  public void refresh() {
    if (mock())
      return;

    _window.refresh();
  }

  public boolean isEvolutionOkay(Pokemon p) throws DialogCancelException {
    if (mock())
      return true;

    String message = "Allow " + p.name() + " to evolve?";
    String title = p.name() + " wants to evolve!";
    Icon icon = ImageFinder.find(p);

    return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,
        message, title, JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, icon);
  }

  public int getMoveIndex(String message, Pokemon p)
      throws DialogCancelException {
    if (mock()) {
      // TODO : stuff
    }

    ArrayList<String> moveNames = new ArrayList<String>();
    for (Move m : p.moves)
      moveNames.add(m.name());

    String title = "Select Move Index";
    Icon icon = ImageFinder.find(p);

    return JOptionPane.showOptionDialog(null, message, title,
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon,
        moveNames.toArray(), null);
  }

  public int getPartyIndex(String message) throws DialogCancelException {
    if (mock()) {
      // TODO : stuff
    }

    int index;
    ImageIcon[] options = new ImageIcon[_player.party().size()];

    for (int i = _player.party().size() - 1; i >= 0; i--)
      options[i] = ImageFinder.find(_player.party().get(i));

    index = JOptionPane.showOptionDialog(null, message, "Select From Party",
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
        options, null);

    if (index == -1)
      throw new DialogCancelException();
    return index;
  }

  public Slot getTargetSlot(List<Slot> enemySlots) throws DialogCancelException {
    if (mock()) {
      // TODO : stuff
    }
    if (enemySlots.size() == 1)
      return enemySlots.get(0);

    // TODO
    return null;
  }

  public Item getItemChoice(String message) throws DialogCancelException {
    if (mock()) {
      // TODO : stuff
    }

    try {
      Player player = (Player) _player; // Throws ClassCastException

      int pocketChoice = JOptionPane.showOptionDialog(null, message,
          "Select An Item", JOptionPane.DEFAULT_OPTION,
          JOptionPane.QUESTION_MESSAGE, null, ItemType.values(), null);

      Iterable<Item> pocket = player.bag.pocket(ItemType.valueOf(pocketChoice));

      List<Item> available = new ArrayList<Item>();
      List<ImageIcon> choices = new ArrayList<ImageIcon>();

      for (Item item : pocket) {
        available.add(item);
        choices.add(ImageFinder.find(item));
      }

      int choice = JOptionPane.showOptionDialog(null, message,
          "Select An Item", JOptionPane.DEFAULT_OPTION,
          JOptionPane.QUESTION_MESSAGE, null,
          choices.toArray(new ImageIcon[choices.size()]), null);

      // Throws ArrayIndexOutOfBoundsException
      return available.get(choice);

    } catch (Exception e) {
      throw new DialogCancelException();
    }
  }

  private boolean mock() {
    return _player == null;
  }

  private Player _player;
  private GameWindow _window;
}