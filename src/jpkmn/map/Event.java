package jpkmn.map;

import jpkmn.game.player.Player;

public class Event {
  public Event(Event.Type t, int int1, int int2) {
    _type = t;
    _int1 = int1;
    _int2 = int2;
  }

  public Event(Event.Type t, int int1) {
    this(t, int1, -1);
  }

  public void trigger(Player p) {
    // TODO
  }

  public enum Type {
    GIFTPOKEMON, GIFTITEM, TRADEPOKEMON, TRADEITEM, BATTLE;
  }

  private Event.Type _type;
  private int id, _int1, _int2;
}