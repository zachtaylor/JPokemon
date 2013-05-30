package org.jpokemon.manager.component;

import org.jpokemon.battle.Battle;
import org.jpokemon.manager.Activity;
import org.jpokemon.manager.JPokemonService;
import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public class BattleService extends JPokemonService {
  @Override
  public void handleRequest(JSONObject request) throws ServiceException {
    Player player = getPlayer(request);
    Activity activity = PlayerManager.getActivity(player);

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