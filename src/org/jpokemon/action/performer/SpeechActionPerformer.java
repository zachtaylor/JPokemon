package org.jpokemon.action.performer;

import org.jpokemon.manager.PlayerManager;
import org.jpokemon.trainer.Player;

public class SpeechActionPerformer extends AbstractActionPerformer {
  public SpeechActionPerformer(String data) {
    super(data);
  }

  public void execute(Player player) {
    PlayerManager.addMessageToQueue(player, getData());
  }
}