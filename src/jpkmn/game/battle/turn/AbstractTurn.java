package jpkmn.game.battle.turn;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.battle.slot.Slot;

public abstract class AbstractTurn implements Comparable<AbstractTurn> {
  public AbstractTurn(Slot user) {
    _user = user;
    _needSwap = false;
    _messages = new ArrayList<String>();
  }

  public Slot slot() {
    return _user;
  }

  public abstract String[] execute();

  public void changeToSwap() {
    _needSwap = true;
  }

  public boolean needSwap() {
    return _needSwap;
  }

  public String[] executeForcedSwap() {
    // TODO
    return null;
  }

  protected String[] getNotifications() {
    return _messages.toArray(new String[_messages.size()]);
  }

  protected Slot _user;
  private boolean _needSwap;
  protected List<String> _messages;
}