package jpkmn.game.service;

import jpkmn.exceptions.LoadException;
import jpkmn.exceptions.ServiceException;
import jpkmn.game.battle.Battle;
import jpkmn.game.battle.BattleRegistry;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.map.Area;
import jpkmn.map.AreaRegistry;

import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.jpokemon.trainer.PokemonTrainer;
import org.jpokemon.trainer.Trainer;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleService {
  public static void startWild(int playerID) throws ServiceException {
    Player player = PlayerFactory.get(playerID);

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
    Player player = PlayerFactory.get(playerID);

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

  public static int createEnrollable() {
    return BattleRegistry.createEnrollable();
  }

  public static void enroll(int battleID, PokemonTrainer player, int team) throws ServiceException {
    Battle battle = BattleRegistry.getEnrollable(battleID);

    if (battle == null)
      throw new ServiceException("Invalid enrollable battle: " + battleID);

    battle.addTrainer(player, team);
  }

  public static void startEnrollable(int battleID) {
    BattleRegistry.startEnrollable(battleID);
  }

  public static void startBattle(int pID, int tID) throws ServiceException {
    Player player = PlayerFactory.get(pID);

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

  public static void turn(JSONObject json) {
    int trainerID;

    try {
      trainerID = json.getInt("trainer");
    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    Player trainer = PlayerFactory.get(trainerID);
    Battle battle = BattleRegistry.get(trainer);

    battle.createTurn(json);
  }

  public static JSONObject info(int playerID) throws ServiceException {
    Player player = PlayerFactory.get(playerID);
    if (player == null)
      throw new ServiceException("PlayerID " + playerID + " not found");

    Battle battle = BattleRegistry.get(player);
    if (battle == null)
      throw new ServiceException(player.name() + " is not in a battle");

    return battle.toJSON(player);
  }

  public static JSONObject enrollableInfo(int battleID) throws ServiceException {
    Battle b = BattleRegistry.getEnrollable(battleID);

    if (b == null)
      throw new ServiceException("Invalid enrollable battle: " + battleID);

    return b.toJSON(null);
  }
}