package org.jpokemon.service;

import javax.swing.ImageIcon;

import org.jpokemon.JPokemonConstants;

public class ImageService implements JPokemonConstants {
  public static ImageIcon find(String name) {
    String path = IMAGE_PATH + name + ".png";

    ImageIcon icon = new ImageIcon(path);

    return icon;
  }

  public static ImageIcon pokemon(String number) {
    String path = IMAGE_PATH + "pokemon/" + number + ".png";

    ImageIcon icon = new ImageIcon(path);

    return icon;
  }

  public static ImageIcon item(String type, String name) {
    String path = IMAGE_PATH + type.toLowerCase();

    if (type.equalsIgnoreCase("ball") || type.equalsIgnoreCase("potion") || type.equals("stone"))
      path += "/" + name.substring(0, 1).toLowerCase();

    path += ".png";
    ImageIcon icon = new ImageIcon(path);

    return icon;
  }

  public static ImageIcon npc(String type) {
    String path = IMAGE_PATH + "npc/" + type.toLowerCase() + ".png";

    ImageIcon icon = new ImageIcon(path);

    return icon;
  }
}