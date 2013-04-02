package org.jpokemon.trainer;

import java.util.ArrayList;
import java.util.List;

import jpkmn.exceptions.LoadException;

import org.jpokemon.JPokemonConstants;
import org.json.JSONArray;
import org.json.JSONException;

import com.zachtaylor.jnodalxml.XMLException;
import com.zachtaylor.jnodalxml.XMLNode;

/**
 * A representation of the progress a Player has made
 */
public class Progress implements JPokemonConstants {
  public static final String XML_NODE_NAME = "progress";

  public Progress() {
    _events = new ArrayList<Integer>();
  }

  /**
   * Sets the specified event id
   * 
   * @param id Event number to record
   */
  public void put(int id) {
    if (id < 1)
      throw new IllegalArgumentException("Out of bounds event: " + id);
    if (_events.contains(id))
      throw new IllegalArgumentException("Duplicate put for event: " + id);

    _events.add(id);
  }

  /**
   * Gets the status of an event
   * 
   * @param id Event number to look up
   * @return True if the event has been completed
   */
  public boolean get(int id) {
    if (id < 1)
      throw new IllegalArgumentException("Out of bounds event: " + id);

    return _events.contains(id);
  }

  public JSONArray toJSON() {
    JSONArray data = new JSONArray();

    for (Integer i : _events)
      data.put(i.intValue());

    return data;
  }

  public void loadJSON(JSONArray json) throws LoadException {
    try {
      for (int i = 0; i < json.length(); i++)
        put(json.getInt(i));
    } catch (Exception e) {
      if (e instanceof JSONException)
        e.printStackTrace();

      throw new LoadException("Progress could not load: " + json);
    }
  }

  public XMLNode toXML() {
    XMLNode node = new XMLNode(XML_NODE_NAME);

    node.setValue(_events.toString());

    return node;
  }

  public void loadXML(XMLNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XMLException("Cannot read node");

    for (String value : node.getValue().replace('[', ' ').replace(']', ' ').trim().split(",")) {
      if (value.isEmpty())
        continue;

      put(Integer.parseInt(value));
    }
  }

  private List<Integer> _events;
}