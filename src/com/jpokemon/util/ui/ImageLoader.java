package com.jpokemon.util.ui;

import javax.swing.ImageIcon;

import org.jpokemon.server.JPokemonServer;

public class ImageLoader {
  public static ImageIcon find(String name) {
    String path = JPokemonServer.imagepath + "/" + name + ".png";

    ImageIcon icon = new ImageIcon(path);

    return icon;
  }

  public static ImageIcon pokemon(String number) {
    String path = JPokemonServer.imagepath + "/pokemon/" + number + ".png";

    ImageIcon icon = new ImageIcon(path);

    return icon;
  }

  public static ImageIcon item(String type, String name) {
    String path = JPokemonServer.imagepath + "/item/" + type.toLowerCase();

    if (type.equalsIgnoreCase("ball") || type.equalsIgnoreCase("potion") || type.equals("stone"))
      path += "/" + name.substring(0, 1).toLowerCase();

    path += ".png";
    ImageIcon icon = new ImageIcon(path);

    return icon;
  }

  public static ImageIcon npc(String type) {
    String path = JPokemonServer.imagepath + "/npc/" + type.toLowerCase() + ".png";

    ImageIcon icon = new ImageIcon(path);

    return icon;
  }
}