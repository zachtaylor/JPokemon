package org.jpokemon.action;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.service.LoadException;
import org.jpokemon.trainer.Player;
import org.zachtaylor.jnodalxml.XMLNode;

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

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    if (_option != null) {
      node.setAttribute("option", _option);
    }

    for (Action action : _actions) {
      node.addChild(action.toXML());
    }
    for (RequirementSet requirement : _requirements) {
      node.addChild(requirement.toXML());
    }

    return node;
  }

  public ActionSet loadXML(XMLNode node) {
    _actions = new ArrayList<Action>();
    _requirements = new ArrayList<RequirementSet>();

    if (node.hasAttribute("option")) {
      _option = node.getAttribute("option");
    }

    for (XMLNode actionchild : node.getChildren(Action.XML_NODE_NAME)) {
      _actions.add(new Action(0, null).loadXML(actionchild));
    }

    for (XMLNode requirementsetchild : node.getChildren(RequirementSet.XML_NODE_NAME)) {
      _requirements.add(new RequirementSet().loadXML(requirementsetchild));
    }

    return this;
  }

  private String _option;
  private List<Action> _actions = new ArrayList<Action>();
  private List<RequirementSet> _requirements = new ArrayList<RequirementSet>();
}