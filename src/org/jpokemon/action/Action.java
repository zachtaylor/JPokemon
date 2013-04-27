package org.jpokemon.action;

import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.service.PlayerService;
import org.jpokemon.trainer.Player;
import org.zachtaylor.jnodalxml.XMLNode;

public class Action {
  public static final String XML_NODE_NAME = "action";

  public Action(int type, String data) {
    _type = ActionType.valueOf(type);
    _data = data;
  }

  public void execute(Player player) {
    switch (_type) {
    case SPEECH:
      doSpeech(player);
    break;
    case EVENT:
      doEvent(player);
    break;
    case ITEM:
      doItem(player);
    break;
    case TRANSPORT:
      doTransport(player);
    break;
    case POKEMON:
      doPokemon(player);
    break;
    }
  }

  public String data() {
    return _data;
  }

  public ActionType type() {
    return _type;
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    node.setAttribute("type", _type.toString());
    node.setAttribute("data", _data);
    node.setSelfClosing(true);

    return node;
  }

  public Action loadXML(XMLNode node) {
    _type = ActionType.valueOf(node.getAttribute("type"));
    _data = node.getAttribute("data");

    return this;
  }

  private void doSpeech(Player player) {
    PlayerService.addToMessageQueue(player, _data);
  }

  private void doEvent(Player player) {
    player.events().put(Integer.parseInt(_data));
  }

  private void doItem(Player player) {
    String[] numberAndQuantity = _data.split(" ");
    player.item(Integer.parseInt(numberAndQuantity[0])).add(Integer.parseInt(numberAndQuantity[1]));
  }

  private void doTransport(Player player) {
    String[] areaAndLocation = _data.split(" ");
    player.area(Integer.parseInt(areaAndLocation[0]));

    if (areaAndLocation.length > 1)
      ; // When doing coordinates, do that here
  }

  private void doPokemon(Player player) {
    Pokemon pokemon = null;
    String[] parameters = _data.split(" ");

    int number = Integer.parseInt(parameters[0]);

    if (number < 1) {
      for (Pokemon cur : player.party()) {
        if (cur.number() == number && pokemon == null) {
          pokemon = cur;
        }
      }

      player.party().remove(pokemon);
    }
    else {
      pokemon = new Pokemon(number, Integer.parseInt(parameters[1]));

      for (int i = 2; i < parameters.length; i++) {
        String[] parameter = parameters[i].split("=");

        if (parameter[0].equals("ot")) {
          pokemon.setTrainerName(parameter[1]);
        }
      }

      player.add(pokemon);
    }
  }

  private String _data;
  private ActionType _type;
}