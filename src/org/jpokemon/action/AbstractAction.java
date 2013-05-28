package org.jpokemon.action;

import org.jpokemon.action.performer.ActionPerformer;
import org.jpokemon.action.performer.BattleActionPerformer;
import org.jpokemon.action.performer.EventActionPerformer;
import org.jpokemon.action.performer.ItemActionPerformer;
import org.jpokemon.action.performer.PokemonActionPerformer;
import org.jpokemon.action.performer.SpeechActionPerformer;
import org.jpokemon.action.performer.StoreActionPerformer;
import org.jpokemon.action.performer.TransportActionPerformer;
import org.jpokemon.action.performer.UpgradeActionPerformer;

public abstract class AbstractAction implements Action {
  @Override
  public ActionPerformer getPerformer() {
    switch (ActionType.valueOf(getType())) {
    case SPEECH:
      return new SpeechActionPerformer(getData());
    case EVENT:
      return new EventActionPerformer(getData());
    case ITEM:
      return new ItemActionPerformer(getData());
    case TRANSPORT:
      return new TransportActionPerformer(getData());
    case POKEMON:
      return new PokemonActionPerformer(getData());
    case BATTLE:
      return new BattleActionPerformer(getData());
    case UPGRADE:
      return new UpgradeActionPerformer();
    case STORE:
      return new StoreActionPerformer(getData());
    }

    return null;
  }
}