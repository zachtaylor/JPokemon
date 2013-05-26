package org.jpokemon.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jpokemon.trainer.Player;
import org.zachtaylor.jnodalxml.XMLNode;

public class RequirementSet implements Iterable<Requirement> {
  public static final String XML_NODE_NAME = "requirementset";

  public void add(Requirement r) {
    _data.add(r);
  }

  public boolean isOkay(Player p) {
    boolean result = true;

    for (Requirement r : _data) {
      if (!r.isOkay(p)) {
        result = false;
      }
    }

    return result;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    for (Requirement requirement : _data) {
      node.addChild(requirement.toXML());
    }

    return node;
  }

  public RequirementSet loadXML(XMLNode node) {
    _data = new ArrayList<Requirement>();

    for (XMLNode child : node.getChildren(Requirement.XML_NODE_NAME)) {
      _data.add(new Requirement(0, -1).loadXML(child));
    }

    return this;
  }

  @Override
  public Iterator<Requirement> iterator() {
    return _data.iterator();
  }

  private List<Requirement> _data = new ArrayList<Requirement>();

}