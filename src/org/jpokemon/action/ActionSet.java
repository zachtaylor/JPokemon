package org.jpokemon.action;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.trainer.Player;

public class ActionSet {
  public String getOption() {
    return _option;
  }

  public void setOption(String o) {
    _option = o;
  }

  public void addAction(Action a) {
    _actions.add(a);
  }

  public List<Action> actions() {
    return _actions;
  }

  public void execute(Player p) {
    if (!isOkay(p))
      return;

    for (Action action : _actions)
      action.execute(p);
  }

  public void addRequirements(RequirementSet requirements) {
    _requirements.add(requirements);
  }

  public boolean isOkay(Player p) {
    boolean result = true;

    for (RequirementSet option : _requirements) {
      result = option.isOkay(p);

      if (result)
        break;
    }

    return result;
  }

  private String _option;
  private List<Action> _actions = new ArrayList<Action>();
  private List<RequirementSet> _requirements = new ArrayList<RequirementSet>();
}