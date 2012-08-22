package jpkmn.game.service;

import jpkmn.exceptions.ServiceException;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.player.GymLeader;
import jpkmn.game.player.MockPlayer;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.game.player.Trainer;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.Area;

public class BattleService {
  public static void startWild(int playerID) throws ServiceException {
    Player player = PlayerRegistry.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Area area = player.area();

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    Pokemon wild = area.spawn(null); // No tags yet

    if (wild == null)
      throw new ServiceException("Unable to generate wild pokemon");

    MockPlayer mock = new MockPlayer();
    mock.party.add(wild);

    BattleRegistry.make(player, mock);
  }

  public static void startWater(int playerID) throws ServiceException {
    Player player = PlayerRegistry.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Area area = player.area();

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    Pokemon wild = area.spawn("oldrod"); // No tags yet

    if (wild == null)
      throw new ServiceException("Unable to generate wild pokemon");

    MockPlayer mock = new MockPlayer();
    mock.party.add(wild);

    BattleRegistry.make(player, mock);

  }

  public static void startGym(int playerID) throws ServiceException {
    Player player = PlayerRegistry.get(playerID);

    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Area area = player.area();

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    int gymNumber = area.gym();

    if (gymNumber == 0)
      throw new ServiceException(area.name() + " has no gym");
    else if (gymNumber != player.badge() + 1)
      throw new ServiceException("You are not qualified for this gym");

    GymLeader gym = new GymLeader(gymNumber);

    BattleRegistry.make(player, gym);
  }

  public static void startTrainer(int pID, int tID) throws ServiceException {
    Player player = PlayerRegistry.get(pID);

    if (player == null)
      throw new ServiceException("PlayerID " + pID + " not found");

    Area area = player.area();

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    Trainer trainer = new Trainer(tID);

    // if (player has fought this trainer)
    // throw new ServiceException(player.name() + " has already fought " +)

    BattleRegistry.make(player, trainer);
  }
}