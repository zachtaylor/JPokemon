package jpkmn.exe.gui;

import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import jpkmn.exceptions.CancelException;
import jpkmn.game.Player;
import jpkmn.game.battle.Slot;
import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.img.ImageFinder;

public class GraphicsHandler {
  public GraphicsHandler() {
  }

  public GraphicsHandler(Player p) {
    _player = p;
    _inbox = new MessageView();
  }

  public void notify(String[] s) {
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
    // TODO
    return 0;
  }

  public int getPartyIndex(String message) throws CancelException {
    // TODO
    return 0;
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
