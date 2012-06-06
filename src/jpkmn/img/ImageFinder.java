package jpkmn.img;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import jpkmn.game.item.Ball;
import jpkmn.game.item.Item;
import jpkmn.game.item.Machine;
import jpkmn.game.item.Potion;
import jpkmn.game.item.Stone;
import jpkmn.game.item.XStat;
import jpkmn.game.pokemon.Pokemon;

public class ImageFinder {
  public static Image find(Object o) {
    if (o instanceof Pokemon)
      return getImage((Pokemon) o);
    else if (o instanceof Item)
      return getImage((Item) o);
    else
      return getImage(o.toString());
  }

  private static Image getImage(String path) {
    URL url = ImageFinder.class.getResource(path + ".png");
    if (url == null) url = ImageFinder.class.getResource("err.png");
    return new ImageIcon(url).getImage();
  }

  private static Image getImage(Pokemon p) {
    return getImage("pkmn/" + p.number());
  }

  private static Image getImage(Item i) {
    String dest = "item/";

    if (i instanceof Ball)
      dest += "ball/" + i.getName().toLowerCase().charAt(0);
    else if (i instanceof Machine)
      dest += "machine";
    else if (i instanceof Potion)
      dest += "potion/" + i.getName().toLowerCase().charAt(0);
    else if (i instanceof Stone)
      dest += "stone/" + i.getName().toLowerCase().charAt(0);
    else if (i instanceof XStat)
      dest += "xstat";
    else
      dest = "err";

    return getImage(dest);
  }
}
