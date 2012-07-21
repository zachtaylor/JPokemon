package jpkmn.game.battle;

import java.util.HashMap;
import java.util.Map;

import jpkmn.game.player.MockPlayer;
import jpkmn.game.player.Player;
import jpkmn.game.pokemon.Pokemon;

public class BattleRegistry {
  public static void make(Player player, Pokemon wildEnemy) {
    Battle b = new Battle();
    MockPlayer mp = new MockPlayer();

    mp.party.add(wildEnemy);

    b.add(SlotType.PLAYER, player.party);
    b.add(SlotType.WILD, mp.party);

    start(b);
  }

  public static int make() {
    Battle b = new Battle();

    _enroll.put(_enrollID, b);

    return _enrollID++;
  }

  public static boolean enroll(Player player, int battle) {
    Battle b = _enroll.get(battle);

    if (b == null) return false;

    b.add(SlotType.PLAYER, player.party);

    return true;
  }

  public static void finish(int battle) {
    Battle b = _enroll.remove(battle);

    if (b != null) start(b);
  }

  public static void remove(int battleNumber) {
    _battles.remove(battleNumber);
  }

  public static Battle get(int battleNumber) {
    return _battles.get(battleNumber);
  }

  private static void start(Battle b) {
    _battles.put(_battleID, b);
    b.start(_battleID++);
  }

  private static int _enrollID, _battleID;
  private static Map<Integer, Battle> _enroll = new HashMap<Integer, Battle>();
  private static Map<Integer, Battle> _battles = new HashMap<Integer, Battle>();
}