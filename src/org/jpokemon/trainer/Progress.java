package org.jpokemon.trainer;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.zachtaylor.jnodalxml.XmlException;
import org.zachtaylor.jnodalxml.XmlNode;

/**
 * A representation of the progress a Player has made
 */
public class Progress {
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

  public XmlNode toXML() {
    XmlNode node = new XmlNode(XML_NODE_NAME);

    node.setValue(_events.toString());

    return node;
  }

  public void loadXML(XmlNode node) {
    if (!XML_NODE_NAME.equals(node.getName()))
      throw new XmlException("Cannot read node");

    for (String value : node.getValue().replace('[', ' ').replace(']', ' ').trim().split(",")) {
      if (value.isEmpty())
        continue;

      put(Integer.parseInt(value));
    }
  }

  private List<Integer> _events;
}