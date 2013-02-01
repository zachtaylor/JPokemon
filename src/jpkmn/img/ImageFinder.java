package jpkmn.img;

import java.net.URL;

import javax.swing.ImageIcon;

import jpkmn.game.item.Item;
import jpkmn.game.item.ItemType;

import org.jpokemon.pokemon.Pokemon;

public class ImageFinder {
  public static ImageIcon find(Object o) {
    if (o instanceof Pokemon)
      return getImage((Pokemon) o);
    else if (o instanceof Item)
      return getImage((Item) o);
    else
      return getImage(o.toString());
  }

  private static ImageIcon getImage(String path) {
    URL url = ImageFinder.class.getResource(path + ".png");
    if (url == null) url = ImageFinder.class.getResource("err.png");
    return new ImageIcon(url);
  }

  private static ImageIcon getImage(Pokemon p) {
    return getImage("pkmn/" + p.number());
  }

  private static ImageIcon getImage(Item i) {
    String dest = "item/";

    if (i.type() == ItemType.BALL)
      dest += "ball/" + i.name().toLowerCase().charAt(0);
    else if (i.type() == ItemType.MACHINE)
      dest += "machine";
    else if (i.type() == ItemType.POTION)
      dest += "potion/" + i.name().toLowerCase().charAt(0);
    else if (i.type() == ItemType.STONE)
      dest += "stone/" + i.name().toLowerCase().charAt(0);
    else if (i.type() == ItemType.XSTAT)
      dest += "xstat";
    else
      dest = "err";

    return getImage(dest);
  }
}