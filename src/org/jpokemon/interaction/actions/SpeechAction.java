package org.jpokemon.interaction.actions;

import org.jpokemon.interaction.Action;
import org.jpokemon.server.Message;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.trainer.Player;

public class SpeechAction extends Action {
  public SpeechAction(String data) {
    super(data);
  }

  public void execute(Player player) {
//    Message message = new Message("SPEECH", getData(), Message.Level.MESSAGE);
//    PlayerManager.pushMessage(player, message);
  }
}