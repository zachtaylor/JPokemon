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
  private List<Region> regions = new ArrayList<Region>();
  private List<WildPokemon> wildPokemon = new ArrayList<WildPokemon>();

  // used internally to determine entity sizes
  private int tilewidth, tileheight;

  public Map(String name) {
    this.name = name;

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

  public List<Region> getRegions() {
    return regions;
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

        if ("entity".equals(objectType)) {
          parseEntity(object);
        }
        else if ("region".equals(objectType)) {
          parseRegion(object);
        }
        else if ("border".equals(objectType)) {
          // TODO
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

  private void parseEntity(XmlNode entityNode) {
    String name = entityNode.getAttribute("name");
    if (name == null) { return; }

    Entity entity = new Entity();
    entity.setName(name);

    List<String> actionSets = new ArrayList<String>();
    if (entityNode.hasAttribute("actionset")) {
      actionSets.add(entityNode.getAttribute("actionset"));
    }
    else if (entityNode.hasAttribute("actionsets")) {
      String actionSetArray = entityNode.getAttribute("actionsets");
      for (String actionSet : actionSetArray.replace('[', ' ').replace(']', ' ').split(",")) {
        actionSet = actionSet.trim();
        if (!actionSet.isEmpty()) {
          actionSets.add(actionSet);
        }
      }
    }

    for (String s : actionSets) {
      ActionSet actionSet = null; // TODO
      // if (actionSet == null) { log(something); continue; }
      entity.putActionSet(s, actionSet);
    }

    // round down
    int x = entityNode.getIntAttribute("x") / tilewidth;
    int y = entityNode.getIntAttribute("y") / tileheight;
    // round up edge as displayed if width is there
    int w = 1, h = 1;
    if (entityNode.hasAttribute("x")) {
      w += (entityNode.getIntAttribute("x") - x) + entityNode.getIntAttribute("width") / tilewidth;
    }
    if (entityNode.hasAttribute("y")) {
      h += (entityNode.getIntAttribute("y") - y) + entityNode.getIntAttribute("height") / tileheight;
    }

    for (int iw = 0; iw < w; iw++) {
      for (int ih = 0; ih < h; ih++) {
        if (entities[x + iw][y + ih] != null) {
          // log("Multiple entities at " + (x + iw) + "," + (y + ih));
        }

        entities[x + iw][y + ih] = entity;
      }
    }

    if (entity.getActionSet("sight") != null) {
      Location lineOfSight = new Location();
      lineOfSight.setDirection(Direction.SOUTH);
      lineOfSight.setMap(this);
      lineOfSight.setRegion(x, x + w, y, y + h);

      Region region = new Region();
      region.setTrigger("step");
      region.getActionSets().add(entity.getActionSet("sight"));
      region.setLocation(lineOfSight);
      regions.add(region);
    }
  }

  private void parseRegion(XmlNode regionNode) {
    String name = regionNode.getAttribute("name");
    if (name == null) { return; }

    Region region = new Region();
    region.setTrigger(name);

    List<String> actionSets = new ArrayList<String>();
    if (regionNode.hasAttribute("actionset")) {
      actionSets.add(regionNode.getAttribute("actionset"));
    }
    else if (regionNode.hasAttribute("actionsets")) {
      String actionSetArray = regionNode.getAttribute("actionsets");
      for (String actionSet : actionSetArray.replace('[', ' ').replace(']', ' ').split(",")) {
        actionSet = actionSet.trim();
        if (!actionSet.isEmpty()) {
          actionSets.add(actionSet);
        }
      }
    }

    for (String s : actionSets) {
      ActionSet actionSet = null; // TODO
      // if (actionSet == null) { log(something); continue; }
      region.getActionSets().add(actionSet);
    }

    // round down
    int x = regionNode.getIntAttribute("x") / tilewidth;
    int y = regionNode.getIntAttribute("y") / tileheight;
    // round up edge as displayed
    int w = (regionNode.getIntAttribute("x") - x) + regionNode.getIntAttribute("width") / tilewidth;
    int h = (regionNode.getIntAttribute("y") - y) + regionNode.getIntAttribute("height") / tileheight;

    Location location = new Location();
    location.setRegion(x, x + w, y, y + h);
    location.setMap(this);
    region.setLocation(location);

    regions.add(region);
  }

  private void parseBorder(XmlNode borderNode) { // TODO
  }
}