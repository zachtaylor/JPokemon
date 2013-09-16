package org.jpokemon.battle;

import org.jpokemon.activity.Activity;
import org.jpokemon.activity.PlayerManager;
import org.jpokemon.activity.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public class BattleActivity implements Activity {
  Battle battle;

  public BattleActivity(Battle b) {
    battle = b;
  }

  public Battle getBattle() {
    return battle;
  }

  @Override
  public void onAdd(Player player) throws ServiceException {
    if (PlayerManager.getActivity(player) != null) { throw new ServiceException("Battle must be root activity"); }
  }

  @Override
  public void onReturn(Activity activity, Player player) {
    // TODO Auto-generated method stub
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
  }
}