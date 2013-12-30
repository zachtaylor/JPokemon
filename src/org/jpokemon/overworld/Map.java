package org.jpokemon.overworld;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jpokemon.pokemon.Pokemon;
import org.zachtaylor.jnodalxml.XmlNode;
import org.zachtaylor.jnodalxml.XmlParser;
import org.zachtaylor.myna.Myna;

public class Map {
  public static String mappath;

  private static final Entity solidPlaceholder = new Entity();

  static {
    Myna.configure(Map.class, "org.jpokemon.server");
  }

  private String name;
  private int width, height;
  private Entity[][] entities;
  private List<String> players = new ArrayList<String>();

  // used internally
  private int tilewidth, tileheight, entityz;

  public Map(String name) {
    this.name = name;

    readMapFile();
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

  public int getEntityZ() {
    return entityz;
  }

  public Entity getEntityAt(int x, int y) {
    return entities[x][y];
  }

  public Pokemon getWildPokemon() {
    int totalFlex = 0;
    List<WildPokemon> wildPokemon = WildPokemon.get(name);

    for (WildPokemon wp : wildPokemon) {
      wildPokemon.add(wp);
    }

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

  public void addPlayer(String playerId) {
    players.add(playerId);
  }

  public List<String> getPlayers() {
    return Collections.unmodifiableList(players);
  }

  public void removePlayer(String playerId) {
    players.remove(playerId);
  }

  public void readMapFile() {
    XmlNode data;

    try {
      File file = new File(mappath, name + ".tmx");
      if (!file.exists()) { throw new RuntimeException("Map does not exist: " + name); }
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

    XmlNode[] mapLayers = data.getAllChildren().toArray(new XmlNode[data.getAllChildren().size()]);
    for (int i = 0; i < mapLayers.length; i++) {
      XmlNode objectgroup = mapLayers[i];

      if (!"objectgroup".equals(objectgroup.getName())) {
        continue;
      }
      entityz = i;

      for (XmlNode object : objectgroup.getAllChildren()) {
        addEntity(object);
      }
    }
  }

  private void addEntity(XmlNode node) {
    Entity entity = solidPlaceholder;

    if (node.hasAttribute("name")) {
      String entityName = node.getAttribute("name");
      entity = new Entity();
      entity.setName(entityName);
    }

    placeEntity(entity, node);
  }

  private void placeEntity(Entity entity, XmlNode node) {
    // round down
    int x = node.getIntAttribute("x") / tilewidth;
    int y = node.getIntAttribute("y") / tileheight;
    int w = 1, h = 1;

    // round up edge as displayed if it's there
    if (node.hasAttribute("width")) {
      w += ((node.getIntAttribute("x") - x * tilewidth) + node.getIntAttribute("width")) / tilewidth;
    }
    if (node.hasAttribute("height")) {
      h += ((node.getIntAttribute("y") - y * tileheight) + node.getIntAttribute("height")) / tileheight;
    }

    Location location = new Location();
    location.setBounds(x, w, y, h);
    location.setMap(name);

    for (int i = 0; i < w; i++) {
      for (int j = 0; j < h; j++) {
        if (entities[x + i][y + j] != null) {
          continue;
        }

        entities[x + i][y + j] = entity;
      }
    }
  }
}