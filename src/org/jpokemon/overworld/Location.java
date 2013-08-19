package org.jpokemon.overworld;

import org.zachtaylor.jnodalxml.XmlNode;

public class Location {
  public static final String XML_NODE_NAME = "location";

  private String map;
  private int[] coordinates = new int[4];

  public Location clone() {
    Location location = new Location();
    location.map = map;
    for (int i = 0; i < coordinates.length; i++) {
      location.coordinates[i] = coordinates[i];
    }
    return location;
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public int getLeft() {
    return coordinates[0];
  }

  public int getTop() {
    return coordinates[1];
  }

  public int getRight() {
    return coordinates[2];
  }

  public int getBottom() {
    return coordinates[3];
  }

  public int getWidth() {
    return coordinates[2] - coordinates[0];
  }

  public int getHeight() {
    return coordinates[3] - coordinates[1];
  }

  public void setBounds(int left, int width, int top, int height) {
    coordinates[0] = left;
    coordinates[1] = top;
    coordinates[2] = width;
    coordinates[3] = height;
  }

  public boolean contains(Location location) {
    return coordinates[0] <= location.coordinates[0] && coordinates[1] <= location.coordinates[1]
        && coordinates[2] >= location.coordinates[2] && coordinates[3] >= location.coordinates[3];
  }

  public void loadXml(XmlNode node) {
    setMap(node.getAttribute("map"));
    setBounds(node.getIntAttribute("left"), node.getIntAttribute("width"), node.getIntAttribute("top"), node.getIntAttribute("height"));
  }

  public XmlNode toXml() {
    XmlNode node = new XmlNode(XML_NODE_NAME);
    node.setAttribute("map", map);
    node.setAttribute("left", getLeft());
    node.setAttribute("width", getWidth());
    node.setAttribute("top", getTop());
    node.setAttribute("height", getHeight());
    node.setSelfClosing(true);

    return node;
  }
}