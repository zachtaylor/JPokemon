package org.jpokemon.pokedex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zachtaylor.jnodalxml.XMLException;
import com.zachtaylor.jnodalxml.XMLNode;

/**
 * A gadget which helps keep track of which Pokemon have been seen or caught in
 * the world. Pokedex maintains a status on each Pokemon it tracks.
 */
public class Pokedex implements JPokemonConstants {
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

  public JSONObject toJSON() {
    JSONObject data = new JSONObject();
    JSONArray own = new JSONArray();
    JSONArray saw = new JSONArray();

    for (Map.Entry<Integer, PokedexStatus> entry : _data.entrySet()) {
      switch (entry.getValue()) {
      case SAW:
        saw.put(entry.getKey());
        break;
      case OWN:
        own.put(entry.getKey());
        break;
      default:
      }
    }

    try {
      data.put("saw", saw);
      data.put("own", own);
    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
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

    XMLNode cur = node.getChildren("seen").get(0);
    String[] data = cur.getValue().replace('[', ' ').replace(']', ' ').split(",");
    for (String item : data)
      _data.put(Integer.parseInt(item.trim()), PokedexStatus.SAW);

    cur = node.getChildren("owned").get(0);
    data = cur.getValue().replace('[', ' ').replace(']', ' ').split(",");
    for (String item : data)
      _data.put(Integer.parseInt(item.trim()), PokedexStatus.OWN);
  }

  private Map<Integer, PokedexStatus> _data = new HashMap<Integer, PokedexStatus>();
}