package jpkmn.game.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import jpkmn.Constants;
import jpkmn.game.base.ItemInfo;
import jpkmn.game.pokemon.stat.StatType;

public class Bag {
  public Bag() {
    _pockets = new HashMap<ItemType, BagPocket>();

    ItemInfo nfo;
    ItemType type;
    BagPocket pocket;

    for (int itemNum = 0; itemNum < Constants.ITEMNUMBER; itemNum++) {
      nfo = ItemInfo.getInfo(itemNum);
      type = ItemType.valueOf(nfo.getType());

      if (_pockets.get(type) == null) _pockets.put(type, new BagPocket());

      pocket = _pockets.get(type);

      if (type == ItemType.BALL)
        pocket.add(new Ball(nfo.getData(), nfo.getName(), nfo.getValue()));
      else if (type == ItemType.POTION)
        pocket.add(new Potion(nfo.getData(), nfo.getName(), nfo.getValue()));
      else if (type == ItemType.MACHINE)
        pocket.add(new Machine(nfo.getData(), nfo.getValue()));
      else if (type == ItemType.STONE)
        pocket.add(new Stone(nfo.getData(), nfo.getName(), nfo.getValue()));
      else if (type == ItemType.XSTAT)
        pocket.add(new XStat(nfo.getData(), nfo.getValue()));
      else if (type == ItemType.KEYITEM)
        pocket.add(new KeyItem(nfo.getData(), nfo.getName(), nfo.getValue()));
    }
  }

  public void oldData() {
    potions = new Potion[4];
    potions[0] = new Potion(10, "Potion");
    potions[1] = new Potion(20, "Super Potion");
    potions[2] = new Potion(50, "Hyper Potion");
    potions[3] = new Potion(200, "Full Heal");

    balls = new Ball[4];
    balls[0] = new Ball(10, "Poke-ball");
    balls[1] = new Ball(15, "Great Ball");
    balls[2] = new Ball(20, "Ultraball");
    balls[3] = new Ball(2550, "Master Ball");

    xstat = new XStat[5];
    xstat[0] = new XStat(0, StatType.ATTACK);
    xstat[1] = new XStat(0, StatType.SPECATTACK);
    xstat[2] = new XStat(0, StatType.DEFENSE);
    xstat[3] = new XStat(0, StatType.SPECDEFENSE);
    xstat[4] = new XStat(0, StatType.SPEED);

    stone = new Stone[5];
    stone[0] = new Stone(0, StoneType.FIRE);
    stone[1] = new Stone(0, StoneType.WATER);
    stone[2] = new Stone(0, StoneType.THUNDER);
    stone[3] = new Stone(0, StoneType.MOON);
    stone[4] = new Stone(0, StoneType.LEAF);
  }

  public Ball ball(int p) {
    return balls[p];
  }

  public Potion potion(int p) {
    return potions[p];
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

  public ArrayList<String> toStringArray() {
    ArrayList<String> response = new ArrayList<String>();

    String cur = "balls: ";
    for (int i = 0; i < 4; ++i)
      cur += balls[i].amount() + " ";
    response.add(cur);

    cur = "potions: ";
    for (int i = 0; i < 4; ++i)
      cur += potions[i].amount() + " ";
    response.add(cur);

    cur = "";
    int i = 0;
    for (StoneType t : StoneType.values()) {
      cur += t.name().charAt(0) + ": " + stone[i].amount() + " ";
      ++i;
    }
    response.add(cur);

    cur = "a: " + xstat[0].amount() + " ";
    cur += "sa: " + xstat[1].amount() + " ";
    cur += "d: " + xstat[2].amount() + " ";
    cur += "sd: " + xstat[3].amount() + " ";
    cur += "sp: " + xstat[4].amount() + " ";
    response.add(cur);

    return response;
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