package jpkmn.item;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Bag {
  public Bag() {
    potions = new Potion[4];
    potions[0] = new Potion(10, 0, "Potion");
    potions[1] = new Potion(20, 0, "Super Potion");
    potions[2] = new Potion(50, 0, "Hyper Potion");
    potions[3] = new Potion(200, 0, "Full Heal");
    
    balls = new Ball[4];
    balls[0] = new Ball(10, 0, "Poke-ball");
    balls[1] = new Ball(15, 0, "Great Ball");
    balls[2] = new Ball(20, 0, "Ultraball");
    balls[3] = new Ball(2550, 0, "Master Ball");
    
    xstat = new XStat[5];
    xstat[0] = new XStat(0, 0, XStat.Type.ATTACK);
    xstat[1] = new XStat(0, 0, XStat.Type.SATTACK);
    xstat[2] = new XStat(0, 0, XStat.Type.DEFENSE);
    xstat[3] = new XStat(0, 0, XStat.Type.SDEFENSE);
    xstat[4] = new XStat(0, 0, XStat.Type.SPEED);

    stone = new Stone[5];
    stone[0] = new Stone(0, 0, Stone.Type.FIRE);
    stone[1] = new Stone(0, 0, Stone.Type.WATER);
    stone[2] = new Stone(0, 0, Stone.Type.THUNDER);
    stone[3] = new Stone(0, 0, Stone.Type.MOON);
    stone[4] = new Stone(0, 0, Stone.Type.LEAF);
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
    else if (kind.equalsIgnoreCase("speed"))
      return xstat[4];
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
    else if (kind.equalsIgnoreCase("leaf"))
      return stone[4];
    return null;
  }

  public ArrayList<String> toStringArray() {
    ArrayList<String> response = new ArrayList<String>();

    String cur = "balls: ";
    for (int i = 0; i < 4; ++i)
      cur += balls[i].getQuantity() + " ";
    response.add(cur);

    cur = "potions: ";
    for (int i = 0; i < 4; ++i)
      cur += potions[i].getQuantity() + " ";
    response.add(cur);

    cur = "";
    int i = 0;
    for (Stone.Type t : Stone.Type.values()) {
      cur += t.name().charAt(0) + ": " + stone[i].getQuantity() + " ";
      ++i;
    }
    response.add(cur);

    cur = "a: " + xstat[0].getQuantity() + " ";
    cur += "sa: " + xstat[1].getQuantity() + " ";
    cur += "d: " + xstat[2].getQuantity() + " ";
    cur += "sd: " + xstat[3].getQuantity() + " ";
    cur += "sp: " + xstat[4].getQuantity() + " ";
    response.add(cur);

    return response;
  }

  public String saveToString() {
    StringBuffer s = new StringBuffer();
    
    for (int i = 0; i < 4; i++) {
      s.append(potions[i].getQuantity() + " ");
      s.append(balls[i].getQuantity() + " ");
    }
    for (int i = 0; i < 5; i++) {
      s.append(xstat[i].getQuantity() + " ");
      s.append(stone[i].getQuantity() + " ");
    }
    
    return s.toString();
  }

  public void fromFile(Scanner s) {
    for (int i = 0; i < 4; i++) {
      potions[i].add(s.nextInt());
      balls[i].add(s.nextInt());
    }
    for (int i = 0; i < 5; i++) {
      xstat[i].add(s.nextInt());
      stone[i].add(s.nextInt());
    }
    s.nextLine();
  }

  private Potion[] potions;
  private Ball[] balls;
  private Stone[] stone;
  private XStat[] xstat;
}