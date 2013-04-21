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
    case EVENT:
      p.events().put(Integer.parseInt(_data));
    break;
    case ITEM:
      String[] numberAndQuantity = _data.split(" ");
      p.item(Integer.parseInt(numberAndQuantity[0])).add(Integer.parseInt(numberAndQuantity[1]));
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