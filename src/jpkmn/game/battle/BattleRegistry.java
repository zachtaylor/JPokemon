package jpkmn.game.battle;

import java.util.HashMap;
import java.util.Map;

import jpkmn.game.player.PokemonTrainer;

public class BattleRegistry {
  public static int create() {
    Battle battle = new Battle();

    _enrollable.put(_enrollID, battle);
    return _enrollID++;
  }

  public static void enroll(PokemonTrainer trainer, int battleID) {
    Battle b = _enrollable.get(battleID);

    if (b == null)
      throw new IllegalArgumentException("Battle ID not valid: " + battleID);
  }

  public static void start(int battleID) {
    Battle b = _enrollable.remove(battleID);

    if (b == null)
      throw new IllegalArgumentException("Invalid enrollable battle id");

    for (Slot slot : b)
      _battles.put(slot.trainer(), b);

    b.start();
  }

  public static void start(PokemonTrainer... trainers) {
    Battle b = new Battle();

    for (PokemonTrainer trainer : trainers) {
      b.add(trainer);
      _battles.put(trainer, b);
    }

    b.start();
  }

  public static Battle get(PokemonTrainer trainer) {
    return _battles.get(trainer);
  }

  public static void remove(PokemonTrainer key) {
    _battles.remove(key);
  }

  private static int _enrollID;
  private static Map<PokemonTrainer, Battle> _battles = new HashMap<PokemonTrainer, Battle>();
  private static Map<Integer, Battle> _enrollable = new HashMap<Integer, Battle>();
}