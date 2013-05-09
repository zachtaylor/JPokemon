package org.jpokemon.action;

import org.zachtaylor.jnodalxml.XMLNode;

public class ActionFactory {
  public static Action build(int type, String data) {
    return build(ActionType.valueOf(type), data);
  }

  public static Action build(XMLNode node) {
    String data = node.getAttribute("data");
    String type = node.getAttribute("type");

    return build(ActionType.valueOf(type), data);
  }

  private static Action build(ActionType type, String data) {
    switch (type) {
    case SPEECH:
      return new SpeechAction(data);
    case EVENT:
      return new EventAction(data);
    case ITEM:
      return new ItemAction(data);
    case TRANSPORT:
      return new TransportAction(data);
    case POKEMON:
      return new PokemonAction(data);
    case BATTLE:
      return new BattleAction(data);
    case UPGRADE:
      return new UpgradeAction(data);
    }

    return null;
  }

  public static XMLNode toXML(Action a) {
    XMLNode node = new XMLNode(Action.XML_NODE_NAME);

    node.setAttribute("data", a.data());
    node.setAttribute("type", a.getClass().getName().toLowerCase().replace("action", ""));

    return node;
  }
}