package jpkmn.game;

import java.util.List;

import jpkmn.exceptions.CancelException;
import jpkmn.exe.gui.MessageView;
import jpkmn.game.battle.Slot;
import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Pokemon;

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
    // TODO
    return true;
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
