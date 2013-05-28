package org.jpokemon.action;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.service.LoadException;
import org.jpokemon.trainer.Player;

public class ActionSet {
  public static final String XML_NODE_NAME = "actionset";

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

  public void execute(Player p) throws LoadException {
    if (!isOkay(p))
      return;

    for (Action action : _actions)
      action.getPerformer().execute(p);
  }

  public void addRequirement(Requirement requirement) {
    _requirements.add(requirement);
  }

  public boolean isOkay(Player p) {
    return _requirements.isOkay(p);
  }

  private String _option;
  private List<Action> _actions = new ArrayList<Action>();
  private RequirementSet _requirements = new RequirementSet();
}