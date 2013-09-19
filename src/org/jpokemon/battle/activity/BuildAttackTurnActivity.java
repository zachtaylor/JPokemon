package org.jpokemon.battle.activity;

import org.jpokemon.activity.Activity;
import org.jpokemon.battle.Battle;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.battle.turn.AttackTurn;
import org.jpokemon.battle.turn.Turn;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.server.ServiceException;
import org.jpokemon.trainer.Player;
import org.json.JSONObject;

public class BuildAttackTurnActivity implements BuildTurnActivity {
  private int moveIndex = -1;
  private String slotId, targetId;

  @Override
  public void onAdd(Player player) throws ServiceException {
    slotId = player.id();
    PlayerManager.addActivity(player, new SelectMoveFromLeaderPokemonActivity());
  }

  @Override
  public void beforeRemove(Player player) {
  }

  @Override
  public void onReturn(Activity activity, Player player) {
    if (activity instanceof SelectMoveFromLeaderPokemonActivity) {
      SelectMoveFromLeaderPokemonActivity smflpa = (SelectMoveFromLeaderPokemonActivity) activity;

      moveIndex = smflpa.getMoveIndex();

      String moveName = player.party().get(0).move(moveIndex).name();
      PlayerManager.addActivity(player, new SelectTargetForMoveActivity(moveName));
    }
    else if (activity instanceof SelectTargetForMoveActivity) {
      SelectTargetForMoveActivity stfma = (SelectTargetForMoveActivity) activity;

      targetId = stfma.getTargetId();
      
      PlayerManager.popActivity(player, this);
    }
  }

  @Override
  public void serve(JSONObject request, Player player) throws ServiceException {
    throw new ServiceException("Cannot serve from BuildTurnActivity");
  }

  @Override
  public Turn getTurn(Battle battle) {
    if (moveIndex == -1 || targetId == null) { return null; }

    Slot userSlot = battle.getSlot(slotId);
    Slot targetSlot = battle.getSlot(targetId);

    return new AttackTurn(battle, userSlot, targetSlot, userSlot.party().get(0).move(moveIndex));
  }
}