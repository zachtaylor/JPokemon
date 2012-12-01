package jpkmn.game.battle;

import java.util.HashMap;
import java.util.Map;

import jpkmn.game.player.PokemonTrainer;

public class BattleRegistry {
  public static int createEnrollable() {
    Battle battle = new Battle();

    _enrollable.put(_enrollID, battle);
    return _enrollID++;
  }

  public static void startEnrollable(int battleID) {
    Battle b = _enrollable.remove(battleID);

    if (b == null)
      throw new IllegalArgumentException("Invalid enrollable battle id");

    for (Slot slot : b) {
      _battles.put(slot.trainer(), b);
      slot.trainer().setState("battle");
    }

    b.start();
  }

  public static void start(PokemonTrainer... trainers) {
    Battle b = new Battle();

    PokemonTrainer trainer;
    for (int i = 0; i < trainers.length; i++) {
      trainer = trainers[i];
      b.add(trainer, i);
      _battles.put(trainer, b);
    }

    b.start();
  }

  public static Battle get(PokemonTrainer trainer) {
    return _battles.get(trainer);
  }

  public static Battle getEnrollable(int battleID) {
    return _enrollable.get(battleID);
  }

  public static void remove(PokemonTrainer key) {
    _battles.remove(key);
  }

  private static int _enrollID;
  private static Map<PokemonTrainer, Battle> _battles = new HashMap<PokemonTrainer, Battle>();
  private static Map<Integer, Battle> _enrollable = new HashMap<Integer, Battle>();
}