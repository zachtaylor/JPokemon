package org.jpokemon.overworld;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.jpokemon.action.ActionSet;
import org.jpokemon.pokemon.Pokemon;
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
  private Entity[][] entities;
  private Entity solidPlaceholder = new Entity();
  private List<WildPokemon> wildPokemon = new ArrayList<WildPokemon>();

  // used internally to determine entity sizes
  private int tilewidth, tileheight;

  public Map(String name) {
    this.name = name;
    this.solidPlaceholder.setSolid(true);

    reloadFileData();
    reloadSqliteData();
  }

  public String getName() {
    return name;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Entity getEntityAt(int x, int y) {
    return entities[x][y];
  }

  public Pokemon getWildPokemon() {
    int totalFlex = 0;

    for (WildPokemon p : wildPokemon) {
      totalFlex += p.getFlex();
    }

    totalFlex = (int) (totalFlex * Math.random());

    for (WildPokemon p : wildPokemon) {
      if (totalFlex < p.getFlex()) {
        return p.instantiate();
      }
      else {
        totalFlex -= p.getFlex();
      }
    }

    return null;
  }

  public void reloadFileData() {
    File file = new File(mappath, name + ".tmx");
    if (!file.exists()) { throw new RuntimeException("Map does not exist: " + name); }

    XmlNode data;
    try {
      data = XmlParser.parse(file).get(1);
    }
    catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }

    width = data.getIntAttribute("width");
    height = data.getIntAttribute("height");
    tilewidth = data.getIntAttribute("tilewidth");
    tileheight = data.getIntAttribute("tileheight");

    entities = new Entity[width][height];

    String objectType;
    for (XmlNode objectgroup : data.getChildren("objectgroup")) {
      for (XmlNode object : objectgroup.getAllChildren()) {
        objectType = object.getAttribute("type");

        if ("solid".equals(objectType)) {
          parseSolid(object);
        }
        else if ("interact".equals(objectType)) {
          parseInteract(object);
        }
      }
    }
  }

  public void reloadSqliteData() {
    wildPokemon.clear();
    for (WildPokemon wp : WildPokemon.get(name)) {
      wildPokemon.add(wp);
    }
  }

  private void parseSolid(XmlNode object) {
    Location location = parseLocation(object);

    for (int w = 0; w < location.getWidth(); w++) {
      for (int h = 0; h < location.getHeight(); h++) {
        entities[location.getLeft() + w][location.getTop() + h] = solidPlaceholder;
      }
    }
  }

  private void parseInteract(XmlNode object) {
    
  }

  private Location parseLocation(XmlNode node) {
    // round down
    int x = node.getIntAttribute("x") / tilewidth;
    int y = node.getIntAttribute("y") / tileheight;
    // round up edge as displayed if it's there
    int w = 1, h = 1;
    if (node.hasAttribute("width")) {
      w += (node.getIntAttribute("x") - x * tilewidth) + node.getIntAttribute("width") / tilewidth;
    }
    if (node.hasAttribute("height")) {
      h += (node.getIntAttribute("y") - y * tileheight) + node.getIntAttribute("height") / tileheight;
    }

    Location location = new Location();
    location.setBounds(x, x + w, y, y + h);
    location.setMap(this);

    return location;
  }

  private List<ActionSet> parseActionSets(XmlNode node) {
    List<ActionSet> actionSets = new ArrayList<ActionSet>();

    if (node.hasAttribute("actionset")) {
      String actionSetId = node.getAttribute("actionset");
      ActionSet actionSet = null; // TODO
      actionSets.add(actionSet);
    }
    else if (node.hasAttribute("actionsets")) {
      String actionSetArray = node.getAttribute("actionsets");

      for (String actionSetId : actionSetArray.replace('[', ' ').replace(']', ' ').split(",")) {
        actionSetId = actionSetId.trim();
        if (actionSetId.isEmpty()) {
          continue;
        }

        ActionSet actionSet = null; // TODO
        actionSets.add(actionSet);
      }
    }

    return actionSets;
  }
}