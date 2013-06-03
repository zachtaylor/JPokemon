package org.jpokemon.action.performer;

import org.jpokemon.manager.PlayerManager;
import org.jpokemon.manager.message.Message;
import org.jpokemon.manager.message.MessageLevel;
import org.jpokemon.trainer.Player;

public class SpeechActionPerformer extends AbstractActionPerformer {
  public SpeechActionPerformer(String data) {
    super(data);
  }

  public void execute(Player player) {
    Message message = new Message("SPEECH", getData(), MessageLevel.MESSAGE);
    PlayerManager.addMessageToQueue(player, message);
  }
}