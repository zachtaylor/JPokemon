package org.jpokemon.overworld;

import java.io.File;
import java.io.FileNotFoundException;

import org.zachtaylor.jnodalxml.XmlNode;
import org.zachtaylor.jnodalxml.XmlParser;
import org.zachtaylor.myna.Myna;

public class Map {
  public static String mappath;

  static {
    Myna.configure(Map.class, "org.jpokemon.server");
  }

  private String name;
  private int width, height;

  public Map(String name) {
    this.name = name;

    File file = new File(mappath, name + ".tmx");
    if (!file.exists()) { throw new RuntimeException("Map does not exist: " + name); }

    XmlNode data;
    try {
      data = XmlParser.parse(file).get(1);
    }
    catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    this.width = data.getIntAttribute("width");
    this.height = data.getIntAttribute("height");
    // int width = data.getIntAttribute("tilewidth");
    // int height = data.getIntAttribute("tileheight");

    // TODO : add object groups as entities
  }

  public String getName() {
    return name;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }
}