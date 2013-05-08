package org.jpokemon.battle;

import org.jpokemon.activity.Activity;
import org.jpokemon.activity.ActivityService;
import org.jpokemon.service.JPokemonService;
import org.jpokemon.service.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public class BattleService extends JPokemonService implements ActivityService {
  @Override
  public void handleRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Activity activity = getActivity(request);

    if (!(activity instanceof BattleActivity))
      throw new ServiceException("Current activity for " + player.name() + " is not a battle");

    Battle battle = ((BattleActivity) activity).getBattle();

    battle.createTurn(request);
  }

  @Override
  public void handleRequestOption(String option, JSONObject request) throws ServiceException {
    throw new ServiceException("Battles have no options");
  }

  public static BattleService getInstance() {
    return instance;
  }

  private static BattleService instance = new BattleService();
}