package org.jpokemon.action;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.action.requirement.Requirement;
import org.jpokemon.action.requirement.RequirementSet;
import org.jpokemon.activity.ServiceException;
import org.jpokemon.trainer.Player;

public class ActionSet {
  public static final String XML_NODE_NAME = "actionset";

  public String getOptionName() {
    return _optionName;
  }

  public void setOptionName(String o) {
    _optionName = o;
  }

  public void addAction(Action a) {
    _actions.add(a);
  }

  public List<Action> actions() {
    return _actions;
  }

  public void execute(Player p) throws ServiceException {
    if (!isOkay(p)) { return; }

    for (Action action : _actions) {
      action.execute(p);
    }
  }

  public void addRequirement(Requirement requirement) {
    _requirements.add(requirement);
  }

  public boolean isOkay(Player p) {
    return _requirements.isOkay(p);
  }

  /* Indicates the name of this actionset as an option among others */
  private String _optionName;

  private List<Action> _actions = new ArrayList<Action>();
  private RequirementSet _requirements = new RequirementSet();
}