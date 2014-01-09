package org.jpokemon.overworld;

import java.io.File;
import java.io.FileNotFoundException;

import org.jpokemon.interaction.ActionSet;
import org.zachtaylor.jnodalxml.XmlNode;
import org.zachtaylor.jnodalxml.XmlParser;
import org.zachtaylor.myna.Myna;

public class TmxMapParser {
  public static String mappath;

  static {
    Myna.configure(TmxMapParser.class, "org.jpokemon.server");
  }

  protected Map map;
  protected int tileHeight, tileWidth;

  public static Map parse(String mapName) {
    TmxMapParser parser = new TmxMapParser(mapName);

    parser.parse();

    return null;
  }

  protected TmxMapParser(String name) {
    map = new Map(name);
  }

  private void parse() {
    try {
      XmlNode data = XmlParser.parse(new File(mappath, map.getName() + ".tmx")).get(1);

      parseMapDimensions(data);

      for (XmlNode objectLayer : data.getChildren("objectgroup")) {
        for (XmlNode objectNode : objectLayer.getAllChildren()) {
          parseEntity(objectNode);
        }
      }
    }
    catch (FileNotFoundException e) {
      System.out.println(e);
    }
  }

  private void parseMapDimensions(XmlNode mapRoot) {
    map.setWidth(mapRoot.getIntAttribute("width"));
    map.setHeight(mapRoot.getIntAttribute("height"));
    tileWidth = mapRoot.getIntAttribute("tilewidth");
    tileHeight = mapRoot.getIntAttribute("tileheight");

    int entityZ = 1;
    for (XmlNode layerNode : mapRoot.getAllChildren()) {
      if ("objectgroup".equals(layerNode.getName())) {
        break;
      }
      else if ("layer".equals(layerNode.getName())) {
        entityZ++;
      }
      // ignore "tileset" layers
    }

    map.setEntityZ(entityZ);
  }

  private void parseEntity(XmlNode objectNode) {
    Entity entity = new Entity();
    entity.setLocation(parseLocation(objectNode));

    if (objectNode.hasAttribute("type")) {
      // String entityType = objectNode.getAttribute("type");
      // TODO - support things like warp tiles, doors, grass
    }
    else if (objectNode.hasAttribute("name")) {
      String entityName = objectNode.getAttribute("name");
      entity.setName(entityName);

      entity.addAllActionSets("interact", ActionSet.get("global", entityName));
      entity.addAllActionSets("interact", ActionSet.get(map.getName(), entityName));
    }

    map.addEntity(entity);
  }

  private Location parseLocation(XmlNode objectNode) {
    Location location = new Location();
    location.setLeft(objectNode.getIntAttribute("x") / tileWidth);
    location.setTop(objectNode.getIntAttribute("y") / tileHeight);

    // round up edge as displayed if it's there
    if (objectNode.hasAttribute("width")) {
      location.setWidth((objectNode.getIntAttribute("x") + objectNode.getIntAttribute("width")) / tileWidth + 1);
    }
    if (objectNode.hasAttribute("height")) {
      location.setHeight((objectNode.getIntAttribute("y") + objectNode.getIntAttribute("height")) / tileHeight + 1);
    }

    return location;
  }
}