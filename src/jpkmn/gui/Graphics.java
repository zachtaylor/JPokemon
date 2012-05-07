package jpkmn.gui;

import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;

import jpkmn.item.*;
import jpkmn.pokemon.Pokemon;

// Wrapper class for all graphics

public class Graphics {
  public Graphics() {
    notifications = new MessageView();
  }

  public void alert(Object icon, String... message) {
    notifications.addMessage(findImage(icon), message);
  }

  private Image findImage(Object o) {
    if (o instanceof Image)
      return (Image) o;
    else
      return getImage(findImagePath(o));
  }

  private Image getImage(String path) {
    URL url = Graphics.class.getResource("../../img/" + path + ".png");
    if (url == null) url = Graphics.class.getResource("../../img/err.png");
    return new ImageIcon(url).getImage();
  }

  private String findImagePath(Object o) {
    String dest;

    if (o instanceof Pokemon)
      dest = "pkmn/" + ((Pokemon) o).number();
    else if (o instanceof Item) {
      dest = "item/";
      if (o instanceof Ball)
        dest += "ball/" + ((Item) o).getName().toLowerCase().charAt(0);
      else if (o instanceof Machine)
        dest += "machine";
      else if (o instanceof Potion)
        dest += "potion/" + ((Item) o).getName().toLowerCase().charAt(0);
      else if (o instanceof Stone)
        dest += "stone/" + ((Item) o).getName().toLowerCase();
      else
        dest += "xstat";
    }
    else
      dest = o.toString();

    return dest;
  }

  private MessageView notifications;
}
