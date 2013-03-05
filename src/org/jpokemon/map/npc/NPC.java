package org.jpokemon.map.npc;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.trainer.Player;

public class NPC {
  public void addActionSet(ActionSet as) {
    _actions.add(as);
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

  public NPCType type() {
    return _type;
  }

  public void type(NPCType type) {
    _type = type;
  }

  private NPCType _type;
  private List<ActionSet> _actions = new ArrayList<ActionSet>();
}