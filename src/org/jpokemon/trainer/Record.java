package org.jpokemon.trainer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.zachtaylor.jnodalxml.XMLException;
import org.zachtaylor.jnodalxml.XMLNode;

/**
 * Stores a player's history and useful things about what they have done
 */
public class Record {
  public static final String XML_NODE_NAME = "progress";

  public Record() {
    _events = new ArrayList<Integer>();
  }

  public void setRivalName(String rivalName) {
    _rivalName = rivalName;
  }

  public String getRivalName() {
    return _rivalName;
  }

  public void setStarterPokemon(String p) {
    _pokemon = p;
  }

  public String getStarterPokemon() {
    return _pokemon;
  }

  /**
   * Sets the specified event id
   * 
   * @param id Event number to record
   */
  public void putEvent(int id) {
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
  public boolean getEvent(int id) {
    if (id < 1)
      throw new IllegalArgumentException("Out of bounds event: " + id);

    return _events.contains(id);
  }

  /**
   * Sets the specified trainer id
   * 
   * @param id Trainer number to record
   */
  public void putTrainer(int id) {
    if (id < 1)
      throw new IllegalArgumentException("Out of bounds trainer: " + id);
    if (_trainers.contains(id))
      throw new IllegalArgumentException("Duplicate put for trainer " + id);

    _trainers.add(id);
  }

  /**
   * Gets whether a Trainer has been fought before
   * 
   * @param id Trainer number to look up
   * @return True if the Trainer has been defeated
   */
  public boolean getTrainer(int id) {
    if (id < 1)
      throw new IllegalArgumentException("Out of bounds event: " + id);

    return _trainers.contains(id);
  }

  public JSONArray toJSON() {
    JSONArray data = new JSONArray();

    for (Integer i : _events)
      data.put(i.intValue());

    return data;
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

      putEvent(Integer.parseInt(value));
    }
  }

  private String _rivalName, _pokemon;
  private List<Integer> _events = new ArrayList<Integer>(), _trainers = new ArrayList<Integer>();
}