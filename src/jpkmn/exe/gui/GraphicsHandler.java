package jpkmn.exe.gui;

import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import jpkmn.exceptions.CancelException;
import jpkmn.exceptions.LoadException;
import jpkmn.game.Player;
import jpkmn.game.PlayerRegistry;
import jpkmn.game.battle.Slot;
import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.storage.Party;
import jpkmn.img.ImageFinder;

public class GraphicsHandler {
  public static void main(String[] args) {
    Player zach;
    Party party;
    GraphicsHandler g;

    try {
      zach = PlayerRegistry.fromFile("Zach");

    } catch (LoadException le) {
      return;
    }

    g = zach.screen;
    party = zach.party;

    g.notify("Test Notification", "Line1", "Line2");

    try {
      if (g.isEvolutionOkay(party.getLeader())) {
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
      int index = g.getMoveIndex("Select a move", party.getLeader());

      g.notify("Move Selected", party.getLeader().moves.get(index).name());
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
  }

  public GraphicsHandler() {
  }

  public GraphicsHandler(Player p) {
    _player = p;
    _inbox = new MessageView();
  }

  public void notify(String... s) {
    if (_player == null) return;
    _inbox.addMessage(s);
  }

  public boolean isEvolutionOkay(Pokemon p) throws CancelException {
    String message = "Allow " + p.name() + " to evolve?";
    String title = p.name() + " wants to evolve!";
    Icon icon = new ImageIcon(ImageFinder.find(p));

    return JOptionPane.OK_OPTION == JOptionPane.showConfirmDialog(null,
        message, title, JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.QUESTION_MESSAGE, icon);
  }

  public int getMoveIndex(String message, Pokemon p) throws CancelException {
    String[] moveNames = p.moves.list();
    String title = "Select Move Index";
    Icon icon = new ImageIcon(ImageFinder.find(p));

    return JOptionPane.showOptionDialog(null, message, title,
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, icon,
        moveNames, null);
  }

  public int getPartyIndex(String message) throws CancelException {
    String[] options = new String[_player.party.size()];

    for (int i = _player.party.size() - 1; i >= 0; i--) {
      Pokemon p = _player.party.get(i);
      options[i] = p.name() + " " + p.stats.hp.cur() + "/" + p.stats.hp.max();
    }

    return JOptionPane.showOptionDialog(null, message, "Select From Party",
        JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
        options, null);
  }

  public Slot getTargetSlot(List<Slot> enemySlots) throws CancelException {
    if (enemySlots.size() == 1) return enemySlots.get(0);

    // TODO
    return null;
  }

  public Item getItemChoice() throws CancelException {
    // TODO
    return null;
  }

  private Player _player;
  private MessageView _inbox;
}
