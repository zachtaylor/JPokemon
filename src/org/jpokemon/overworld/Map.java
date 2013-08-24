package org.jpokemon.overworld;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.jpokemon.action.ActionFactory;
import org.jpokemon.action.ActionSet;
import org.jpokemon.overworld.npc.Npc;
import org.jpokemon.pokemon.Pokemon;
import org.zachtaylor.jnodalxml.XmlNode;
import org.zachtaylor.jnodalxml.XmlParser;
import org.zachtaylor.myna.Myna;

public class Map {
  public static String mappath;

  private static final Entity solidPlaceholder;

  static {
    Myna.configure(Map.class, "org.jpokemon.server");
    (solidPlaceholder = new Entity()).setSolid(true);
  }

  private String name;
  private int width, height;
  private Entity[][] entities;
  private List<WildPokemon> wildPokemon = new ArrayList<WildPokemon>();
  // used internally
  private int tilewidth, tileheight, entityz;

  public Map(String name) {
    this.name = name;

    reload();
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

  public void reload() {
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
    XmlNode objectgroup;
    XmlNode[] mapLayers = data.getAllChildren().toArray(new XmlNode[data.getAllChildren().size()]);
    for (int i = 0; i < mapLayers.length; i++) {
      objectgroup = mapLayers[i];

      if (!"objectgroup".equals(objectgroup.getName())) {
        continue;
      }
      else {
        entityz = i;
      }

      for (XmlNode object : objectgroup.getAllChildren()) {
        objectType = object.getAttribute("type");

        if ("solid".equals(objectType)) {
          parseSolid(object);
        }
        else if ("interact".equals(objectType)) {
          parseInteract(object);
        }
        else if ("npc".equals(objectType)) {
          parseNpc(object);
        }
      }
    }

    wildPokemon.clear();
    for (WildPokemon wp : WildPokemon.get(name)) {
      wildPokemon.add(wp);
    }
  }

  private void parseSolid(XmlNode object) {
    placeEntity(solidPlaceholder, object);
  }

  private void parseInteract(XmlNode object) {
    String name = object.getAttribute("name");

    Entity entity = new Entity();
    entity.setName(name);
    entity.setSolid(true);

    List<Interaction> interactions = Interaction.get("global", name);
    HashMap<Integer, ActionSet> actionSets = new HashMap<Integer, ActionSet>();
    for (Interaction interaction : interactions) {
      if (actionSets.get(interaction.getActiongroup()) == null) {
        actionSets.put(interaction.getActiongroup(), new ActionSet());
      }

      actionSets.get(interaction.getActiongroup()).addAction(ActionFactory.get(interaction.getAction(), interaction.getDataref()));
    }
    for (Entry<Integer, ActionSet> mapEntry : actionSets.entrySet()) {
      entity.addActionSet("interact", mapEntry.getValue());
    }

    placeEntity(entity, object);
  }

  private void parseNpc(XmlNode object) {
    String id = object.getAttribute("name");

    Entity entity = Npc.get(id);

    List<Interaction> interactions = Interaction.get(name, id);
    HashMap<Integer, ActionSet> actionSets = new HashMap<Integer, ActionSet>();
    for (Interaction interaction : interactions) {
      if (actionSets.get(interaction.getActiongroup()) == null) {
        actionSets.put(interaction.getActiongroup(), new ActionSet());
      }

      actionSets.get(interaction.getActiongroup()).addAction(ActionFactory.get(interaction.getAction(), interaction.getDataref()));
    }
    for (Entry<Integer, ActionSet> mapEntry : actionSets.entrySet()) {
      entity.addActionSet("interact", mapEntry.getValue());
    }

    placeEntity(entity, object);
  }

  private void placeEntity(Entity entity, XmlNode node) {
    // round down
    int x = node.getIntAttribute("x") / tilewidth;
    int y = node.getIntAttribute("y") / tileheight;
    // round up edge as displayed if it's there
    int w = 1, h = 1;
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

        entities[x + i][y + j] = solidPlaceholder;
      }
    }
  }
}