package jpkmn.game.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jpkmn.Constants;
import jpkmn.game.base.ItemInfo;

public class Bag {
  public Bag() {
    _pockets = new HashMap<ItemType, BagPocket>();

    ItemInfo nfo;
    ItemType type;
    BagPocket pocket;

    for (int itemNum = 1; itemNum <= Constants.ITEMNUMBER; itemNum++) {
      nfo = ItemInfo.getInfo(itemNum);
      type = ItemType.valueOf(nfo.getType());

      if (_pockets.get(type) == null) _pockets.put(type, new BagPocket());

      pocket = _pockets.get(type);

      if (type == ItemType.BALL)
        pocket.add(new Ball(nfo.getData(), nfo.getName(), nfo.getValue()));
      else if (type == ItemType.POTION)
        pocket.add(new Potion(nfo.getData(), nfo.getName(), nfo.getValue()));
      else if (type == ItemType.XSTAT)
        pocket.add(new XStat(nfo.getData(), nfo.getValue()));
      else if (type == ItemType.STONE)
        pocket.add(new Stone(nfo.getData(), nfo.getName(), nfo.getValue()));
      else if (type == ItemType.MACHINE)
        pocket.add(new Machine(nfo.getData(), nfo.getValue()));
      else if (type == ItemType.KEYITEM)
        pocket.add(new KeyItem(nfo.getData(), nfo.getName(), nfo.getValue()));
    }
  }

  public Ball ball(int p) {
    return (Ball) _pockets.get(ItemType.BALL).get(p);
  }

  public Potion potion(int p) {
    return (Potion) _pockets.get(ItemType.POTION).get(p);
  }

  public XStat xstat(String kind) {
    if (kind.equalsIgnoreCase("attack"))
      return xstat[0];
    else if (kind.equalsIgnoreCase("sattack"))
      return xstat[1];
    else if (kind.equalsIgnoreCase("defense"))
      return xstat[2];
    else if (kind.equalsIgnoreCase("sdefense"))
      return xstat[3];
    else if (kind.equalsIgnoreCase("speed")) return xstat[4];
    return null;
  }

  public Stone stone(String kind) {
    if (kind.equalsIgnoreCase("fire"))
      return stone[0];
    else if (kind.equalsIgnoreCase("water"))
      return stone[1];
    else if (kind.equalsIgnoreCase("thunder"))
      return stone[2];
    else if (kind.equalsIgnoreCase("moon"))
      return stone[3];
    else if (kind.equalsIgnoreCase("leaf")) return stone[4];
    return null;
  }

  public String saveToString() {
    StringBuffer s = new StringBuffer();

    for (int i = 0; i < 4; i++) {
      s.append(potions[i].amount() + " ");
      s.append(balls[i].amount() + " ");
    }
    for (int i = 0; i < 5; i++) {
      s.append(xstat[i].amount() + " ");
      s.append(stone[i].amount() + " ");
    }

    return s.toString();
  }

  public void fromFile(Scanner s) {
    for (int i = 0; i < 4; i++) {
      potions[i].amount(s.nextInt());
      balls[i].amount(s.nextInt());
    }
    for (int i = 0; i < 5; i++) {
      xstat[i].amount(s.nextInt());
      stone[i].amount(s.nextInt());
    }
    s.nextLine();
  }

  private Potion[] potions;
  private Ball[] balls;
  private Stone[] stone;
  private XStat[] xstat;

  private Map<ItemType, BagPocket> _pockets;
}