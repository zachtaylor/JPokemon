package org.jpokemon.battle.activity;

import org.jpokemon.activity.Activity;
import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.battle.turn.RunTurn;
import org.jpokemon.battle.turn.Turn;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public class BuildRunTurnActivity implements BuildTurnActivity {
  private Battle battle;
  private String slotId;

  public BuildRunTurnActivity(Battle b) {
    battle = b;
  }

  @Override
  public void onAdd(Player player) throws ServiceException {
    slotId = player.id();

    PlayerManager.popActivity(player, this);
  }

  @Override
  public void logout(Player player) {
  }

  @Override
  public void onReturn(Activity activity, Player player) {
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
  }

  @Override
  public Turn getTurn() {
    Slot slot = battle.getSlot(slotId);

    return new RunTurn(battle, slot);
  }
}