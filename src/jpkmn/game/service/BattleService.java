package jpkmn.game.service;

import jpkmn.exceptions.LoadException;
import jpkmn.exceptions.ServiceException;
import jpkmn.game.battle.Battle;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.game.player.Trainer;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.Area;
import jpkmn.map.AreaRegistry;

public class BattleService {
  public static void startWild(int playerID) throws ServiceException {
    Player player = PlayerRegistry.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    Pokemon wild = null;
    try {
      wild = area.spawn(null); // No tags yet
    } catch (LoadException e) {
      throw new ServiceException(e.getMessage());
    }

    Trainer mock = new Trainer();
    mock.add(wild);

    BattleRegistry.start(player, mock);
    player.setState("battle");
  }

  public static void startWater(int playerID, String rodName)
      throws ServiceException {
    Player player = PlayerRegistry.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    Pokemon wild = null;

    try {
      wild = area.spawn(rodName);
    } catch (LoadException e) {
      throw new ServiceException(e.getMessage());
    }

    Trainer mock = new Trainer();
    mock.add(wild);

    BattleRegistry.start(player, mock);
    player.setState("battle");
  }

  public static void startBattle(int pID, int tID) throws ServiceException {
    Player player = PlayerRegistry.get(pID);

    if (player == null)
      throw new ServiceException("PlayerID " + pID + " not found");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    Trainer trainer = area.trainer(tID, player);

    if (trainer == null)
      throw new ServiceException("Trainer " + tID + " is not in this area");

    // if (player has fought this trainer)
    // throw new ServiceException(player.name() + " has already fought " +)

    BattleRegistry.start(player, trainer);
    player.setState("battle");
  }

  public static void attack(int playerID, int enemySlotID, int moveIndex) {
    Player player = PlayerRegistry.get(playerID);
    Battle battle = BattleRegistry.get(player);

    battle.fight(player.id(), enemySlotID, moveIndex);
  }

  public static void item(int playerID, int targetID, int itemID) {
    Player player = PlayerRegistry.get(playerID);
    Battle battle = BattleRegistry.get(player);

    battle.item(player.id(), targetID, itemID);
  }

  public static void swap(int playerID, int slotIndex) {
    Player player = PlayerRegistry.get(playerID);
    Battle battle = BattleRegistry.get(player);

    battle.swap(player.id(), slotIndex);
  }

  public static void run(int playerID) {
    Player player = PlayerRegistry.get(playerID);
    Battle battle = BattleRegistry.get(player);

    battle.run(player.id());
  }
}