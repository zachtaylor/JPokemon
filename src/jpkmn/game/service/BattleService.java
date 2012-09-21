package jpkmn.game.service;

import jpkmn.exceptions.ServiceException;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.player.MockPlayer;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
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

    Pokemon wild = area.spawn(null); // No tags yet

    if (wild == null)
      throw new ServiceException("Unable to generate wild pokemon");

    MockPlayer mock = new MockPlayer();
    mock.party.add(wild);

    BattleRegistry.make(player, mock);
  }

  public static void startWater(int playerID, String rodName)
      throws ServiceException {
    Player player = PlayerRegistry.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    Pokemon wild = area.spawn(rodName);

    if (wild == null)
      throw new ServiceException("Unable to generate wild pokemon");

    MockPlayer mock = new MockPlayer();
    mock.party.add(wild);

    BattleRegistry.make(player, mock);
  }

  public static void startBattle(int pID, int tID) throws ServiceException {
    Player player = PlayerRegistry.get(pID);

    if (player == null)
      throw new ServiceException("PlayerID " + pID + " not found");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    MockPlayer trainer = area.getTrainer(tID);

    if (trainer == null)
      throw new ServiceException("Trainer " + tID + " is not in this area");

    // if (player has fought this trainer)
    // throw new ServiceException(player.name() + " has already fought " +)

    BattleRegistry.make(player, trainer);
  }
}