package jpkmn.map;

import jpkmn.game.player.Player;

public class Event {
  private enum Type {
    GIFTPOKEMON, GIFTITEM, TRADEPOKEMON, TRADEITEM, BATTLE;

    static Type valueOf(int t) {
      return Type.values()[t];
    }
  }

  public Event(int eventType, int int1, int int2) {
    _type = Type.valueOf(eventType);
    _int1 = int1;
    _int2 = int2;
  }

  public Event(int eventType, int int1) {
    this(eventType, int1, -1);
  }

  public void trigger(Player p) {
    // TODO
  }

  private Event.Type _type;
  private int id, _int1, _int2;
}