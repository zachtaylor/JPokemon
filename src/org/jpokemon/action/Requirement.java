package org.jpokemon.action;

import org.jpokemon.trainer.Player;
import org.zachtaylor.jnodalxml.XMLNode;

public class Requirement {
  public static final String XML_NODE_NAME = "requirement";

  public Requirement(int type, int data) {
    _type = RequirementType.valueOf(type);
    _data = data;
  }

  public Requirement(XMLNode node) {
    loadXML(node);
  }

  public RequirementType type() {
    return _type;
  }

  public boolean isOkay(Player p) {
    switch (_type) {
    case EVENT:
      return p.record().getEvent(Math.abs(_data)) ^ (_data < 0); // XOR
    case POKEDEX:
      return p.pokedex().count() >= Math.abs(_data) ^ (_data < 0); // XOR
    }
    return false;
  }

  public String denialReason() {
    switch (_type) {
    case EVENT:
      return "You are not ready yet";
    case POKEDEX:
      return "You haven't seen enough pokemon yet";
    }

    return "foo bar?";
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    node.setAttribute("type", _type.toString());
    node.setAttribute("data", _data);
    node.setSelfClosing(true);

    return node;
  }

  public Requirement loadXML(XMLNode node) {
    _type = RequirementType.valueOf(node.getAttribute("type"));
    _data = node.getIntAttribute("data");

    return this;
  }

  private int _data;
  private RequirementType _type;
}