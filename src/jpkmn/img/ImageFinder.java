package jpkmn.img;

import java.net.URL;

import javax.swing.ImageIcon;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.item.Item;
import org.jpokemon.item.ItemType;
import org.jpokemon.pokemon.Pokemon;

public class ImageFinder implements JPokemonConstants {
  public static ImageIcon find(Object o) {
    if (o instanceof Item)
      return getImage((Item) o);
    else
      return getImage(o.toString());
  }

  public static ImageIcon pokemon(String number) {
    String path = IMAGE_PATH + number + ".png";

    ImageIcon icon = new ImageIcon(path);

    return icon;
  }

  private static ImageIcon getImage(String path) {
    URL url = ImageFinder.class.getResource(path + ".png");

    if (url == null)
      url = ImageFinder.class.getResource("err.png");

    return new ImageIcon(url);
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