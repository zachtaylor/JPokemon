package org.jpokemon.map;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.trainer.Player;

public class NPC {
  public void addActionSet(NPCActionSet as) {
    _actions.add(as);
  }

  public NPCActionSet action(Player p) {
    NPCActionSet actions = null;

    for (NPCActionSet as : _actions) {
      if (as.isOkay(p)) {
        if (actions != null)
          ; // TODO : this is an error

        actions = as;
      }
    }

    return actions;
  }

  List<NPCActionSet> _actions = new ArrayList<NPCActionSet>();
}