package jpkmn.game.service;

import jpkmn.exceptions.LoadException;
import jpkmn.exceptions.ServiceException;
import jpkmn.map.Area;
import jpkmn.map.AreaRegistry;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.battle.Battle;
import org.jpokemon.battle.BattleRegistry;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PlayerFactory;
import org.jpokemon.trainer.Trainer;
import org.jpokemon.trainer.TrainerState;
import org.json.JSONException;
import org.json.JSONObject;

public class BattleService implements JPokemonConstants {
  public static void grass(int playerID) throws ServiceException {
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
    player.state(TrainerState.BATTLE);
  }

  public static void fish(int playerID, String rodName) throws ServiceException {
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
    player.state(TrainerState.BATTLE);
  }

  public static void startBattle(int pID, int tID) throws ServiceException {
    Player player = PlayerFactory.get(pID);

    if (player == null)
      throw new ServiceException("PlayerID " + pID + " not found");

    Area area = AreaRegistry.get(player.area());

    if (area == null)
      throw new ServiceException(player.name() + " has no area");

    Trainer trainer = area.trainer(tID);

    if (trainer == null)
      throw new ServiceException("Trainer " + tID + " is not in this area");

    if (!ALLOW_REPEAT_TRAINER_BATTLES && player.trainers().get(tID))
      throw new ServiceException(player.name() + " has already fought" + trainer.name());

    BattleRegistry.start(player, trainer);
    player.state(TrainerState.BATTLE);
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
}