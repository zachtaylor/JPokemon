package org.jpokemon.action;

import org.jpokemon.service.PlayerService;
import org.jpokemon.trainer.Player;

public class SpeechAction extends Action {
  public SpeechAction(String data) {
    super(data);
  }

  public void execute(Player player) {
    String data = player.record().replaceMacros(data(), player.name());
    PlayerService.addToMessageQueue(player, data);
  }
}