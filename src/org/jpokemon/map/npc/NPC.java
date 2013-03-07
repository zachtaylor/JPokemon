package org.jpokemon.map.npc;

import java.util.List;

import org.jpokemon.trainer.Player;

public class NPC {
  public NPC(NPCInfo info) {
    _info = info;

    _type = NPCType.get(info.getType());
  }

  public String shortName() {
    return _info.getName();
  }

  public String longName() {
    return _type.getName() + " " + shortName();
  }

  public String icon() {
    return _type.getIcon();
  }

  public NPCType type() {
    return _type;
  }

  public void actions(List<ActionSet> actions) {
    _actions = actions;
  }

  public ActionSet action(Player p) {
    ActionSet actions = null;

    for (ActionSet as : _actions) {
      if (as.isOkay(p)) {
        if (actions != null)
          ; // TODO : this is an error

        actions = as;
      }
    }

    return actions;
  }

  private NPCType _type;
  private NPCInfo _info;
  private List<ActionSet> _actions;
}