package org.jpokemon.map.npc;

import org.jpokemon.service.PlayerService;
import org.jpokemon.trainer.Player;

public class Action {
  public Action(int type, String data) {
    _type = ActionType.valueOf(type);
    _data = data;
  }

  public void execute(Player p) {
    switch (_type) {
    case SPEECH:
      PlayerService.addToMessageQueue(p, _data);
      break;
    }
  }

  public String data() {
    return _data;
  }

  public ActionType type() {
    return _type;
  }

  private String _data;
  private ActionType _type;
}