package org.jpokemon.pokedex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zachtaylor.jnodalxml.XMLException;
import org.zachtaylor.jnodalxml.XMLNode;

/**
 * A gadget which helps keep track of which Pokemon have been seen or caught in
 * the world. Pokedex maintains a status on each Pokemon it tracks.
 */
public class Pokedex {
  public static final String XML_NODE_NAME = "pokedex";

  /**
   * Update the Pokedex with having seen a new Pokemon
   * 
   * @param num Pokemon number
   */
  public void saw(int num) {
    if (num < 1)
      throw new IllegalArgumentException("#" + num + " out of range");
    if (_data.get(num) == PokedexStatus.OWN)
      return;

    _data.put(num, PokedexStatus.SAW);
  }

  /**
   * Update the Pokedex with having caught a new Pokemon.
   * 
   * @param num Pokemon number
   */
  public void own(int num) {
    if (num < 1)
      throw new IllegalArgumentException("#" + num + " out of range");

    _data.put(num, PokedexStatus.OWN);
  }

  /**
   * Gets the status of a Pokemon in the Pokedex
   * 
   * @param num Pokemon number
   * @return PokedexStatus which describes the Pokemon number in question
   */
  public PokedexStatus status(int num) {
    if (num < 1)
      throw new IllegalArgumentException("#" + num + " out of range");

    PokedexStatus status = _data.get(num);

    if (status == null)
      status = PokedexStatus.NONE;

    return status;
  }

  /**
   * Count the number of Pokemon with in the Pokedex
   * 
   * @return The number of Pokemon with non-NONE PokedexStatus
   */
  public int count() {
    return _data.size();
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    List<Integer> seen = new ArrayList<Integer>(), owned = new ArrayList<Integer>();

    for (Map.Entry<Integer, PokedexStatus> pokedexEntry : _data.entrySet()) {
      if (pokedexEntry.getValue() == PokedexStatus.OWN)
        owned.add(pokedexEntry.getKey());
      else
        seen.add(pokedexEntry.getKey());
    }

    XMLNode seenNode = new XMLNode("seen");
    seenNode.setValue(seen.toString());
    node.addChild(seenNode);

    XMLNode ownedNode = new XMLNode("owned");
    ownedNode.setValue(owned.toString());
    node.addChild(ownedNode);

    return node;
  }

  public void loadXML(XMLNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XMLException("Cannot read node");

    String[] dataPieces;
    String seenData, ownedData;
    XMLNode seen = node.getChildren("seen").get(0);
    XMLNode owned = node.getChildren("owned").get(0);

    seenData = seen.getValue().replace('[', ' ').replace(']', ' ').trim();
    if (!seenData.isEmpty()) {
      dataPieces = seenData.split(",");
      for (String item : dataPieces) {
        _data.put(Integer.parseInt(item.trim()), PokedexStatus.SAW);
      }
    }

    ownedData = owned.getValue().replace('[', ' ').replace(']', ' ').trim();
    if (!ownedData.isEmpty()) {
      dataPieces = ownedData.split(",");
      for (String item : dataPieces) {
        _data.put(Integer.parseInt(item.trim()), PokedexStatus.OWN);
      }
    }
  }

  private Map<Integer, PokedexStatus> _data = new HashMap<Integer, PokedexStatus>();
}