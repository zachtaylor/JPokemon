package org.jpokemon.overworld;

import org.zachtaylor.jnodalxml.XmlNode;

public class Location {
  public static final String XML_NODE_NAME = "location";

  private String map, direction;
  private int left, top, width = 1, height = 1;

  public Location() {
  }

  public Location(int left, int top) {
    this(left, top, 1, 1);
  }

  public Location(int left, int top, int width, int height) {
    this.left = left;
    this.top = top;
    this.width = width;
    this.height = height;
  }

  public Location clone() {
    Location location = new Location();
    location.map = map;
    location.direction = direction;
    location.left = left;
    location.height = height;
    location.top = top;
    location.width = width;
    return location;
  }

  public Location translate(String direction) {
    Location location = clone();

    if ("left".equals(direction)) {
      location.left--;
    }
    else if ("up".equals(direction)) {
      location.top--;
    }
    else if ("right".equals(direction)) {
      location.left++;
    }
    else if ("down".equals(direction)) {
      location.top++;
    }
    else {
      return null;
    }

    location.setDirection(direction);
    return location;
  }

  public String getMap() {
    return map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }

  public int getLeft() {
    return this.left;
  }

  public void setLeft(int left) {
    this.left = left;
  }

  public int getTop() {
    return this.top;
  }

  public void setTop(int top) {
    this.top = top;
  }

  public int getWidth() {
    return this.width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return this.height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public int getRight() {
    return this.left + this.width;
  }

  public int getBottom() {
    return this.top + this.height;
  }

  public void setBounds(int left, int width, int top, int height) {
    this.left = left;
    this.top = top;
    this.width = width;
    this.height = height;
  }

  public boolean contains(Location location) {
    return this.left <= location.left && this.top <= location.top && this.getRight() >= location.getRight()
        && this.getBottom() >= location.getBottom();
  }

  public void loadXml(XmlNode node) {
    setMap(node.getAttribute("map"));
    setBounds(node.getIntAttribute("left"), node.getIntAttribute("width"), node.getIntAttribute("top"), node.getIntAttribute("height"));

    if (node.hasAttribute("direction")) {
      direction = node.getAttribute("direction");
    }
    else {
      direction = "down";
    }
  }

  public XmlNode toXml() {
    XmlNode node = new XmlNode(XML_NODE_NAME);
    node.setAttribute("map", map);
    node.setAttribute("direction", direction);
    node.setAttribute("left", getLeft());
    node.setAttribute("top", getTop());
    node.setAttribute("width", getWidth());
    node.setAttribute("height", getHeight());
    node.setSelfClosing(true);

    return node;
  }

  public String toString() {
    return "(" + getLeft() + "+" + getWidth() + "," + getTop() + "+" + getHeight() + ")";
  }
}