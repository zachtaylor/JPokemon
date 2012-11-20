package jpkmn.game.battle;

import java.util.HashMap;
import java.util.Map;

import jpkmn.game.player.PokemonTrainer;

public class BattleRegistry {
  public static int create() {
    _enrollable.put(_enrollID, new Battle());

    return _enrollID++;
  }

  public static void enroll(PokemonTrainer trainer, int battleID) {
    Battle b = _enrollable.get(battleID);

    if (b == null)
      throw new IllegalArgumentException("Battle ID not valid: " + battleID);

    b.add(trainer);
  }

  public static int start(int battleID) {
    Battle b = _enrollable.remove(battleID);

    if (b == null)
      throw new IllegalArgumentException("Battle ID not valid: " + battleID);

    b.start();
    _battles.put(_battleID, b);
    return _battleID++;
  }

  public static Battle get(int battleNumber) {
    return _battles.get(battleNumber);
  }

  public static void remove(Battle b) {
    int key = -1;

    for (Map.Entry<Integer, Battle> battle : _battles.entrySet())
      if (battle.getValue().equals(b))
        key = battle.getKey();

    _battles.remove(key);
  }

  private static int _enrollID, _battleID;
  private static Map<Integer, Battle> _battles = new HashMap<Integer, Battle>();
  private static Map<Integer, Battle> _enrollable = new HashMap<Integer, Battle>();
}