package item;

//Wrapper class for all the items

import battle.Target;
import java.io.PrintWriter;
import java.util.Scanner;
import jpkmn.Driver;

public class Bag {
  public int cash;
  Potion[] potions = new Potion[4];
  Ball[] balls = new Ball[4];
  Stone[] stone = new Stone[5];
  XStat[] xstat = new XStat[5];

  public Bag() {
    cash = 0;
    potions[0] = new Potion(10, 0, "Potion");
    potions[1] = new Potion(20, 0, "Super Potion");
    potions[2] = new Potion(50, 0, "Hyper Potion");
    potions[3] = new Potion(200, 0, "Full Heal");
    for (Potion p : potions)
      p.target = Target.SELF;
    balls[0] = new Ball(10, 0, "Ball");
    balls[1] = new Ball(15, 0, "Superball");
    balls[2] = new Ball(20, 0, "Ultraball");
    balls[3] = new Ball(2550, 0, "Master Ball");
    for (Ball b : balls)
      b.target = Target.ENEMY;
    xstat[0] = new XStat(0, 0, XStat.Type.ATTACK);
    xstat[1] = new XStat(0, 0, XStat.Type.SATTACK);
    xstat[2] = new XStat(0, 0, XStat.Type.DEFENSE);
    xstat[3] = new XStat(0, 0, XStat.Type.DEFENSE);
    xstat[4] = new XStat(0, 0, XStat.Type.SPEED);
    for (XStat x : xstat)
      x.target = Target.SELF;
    stone[0] = new Stone(0, 0, Stone.Type.FIRE);
    stone[1] = new Stone(0, 0, Stone.Type.WATER);
    stone[2] = new Stone(0, 0, Stone.Type.THUNDER);
    stone[3] = new Stone(0, 0, Stone.Type.MOON);
    stone[4] = new Stone(0, 0, Stone.Type.LEAF);
    for (Stone s : stone)
      s.target = Target.SELF;
  }

  public Ball ball(int p) {
    return balls[p];
  }

  public Potion potion(int p) {
    return potions[p];
  }

  public XStat xstat(String kind) {
    Driver.log(Bag.class, "XStat wanted. Target = " + kind);

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
    else
      Driver.log(Bag.class, "Kind (" + kind + ") was not detected.");
    return null;
  }

  public Stone stone(String kind) {
    Driver.log(Bag.class, "Stone wanted. Target = " + kind);

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
    else 
      Driver.log(Bag.class, "Kind (" + kind + ") was not detected.");
    return null;
  }

  public void toFile(PrintWriter p) {
    p.println();
    for (int i = 0; i < 4; i++) {
      p.print(potions[i].getQuantity() + " ");
      p.print(balls[i].getQuantity() + " ");
    }
    for (int i = 0; i < 5; i++) {
      p.print(xstat[i].getQuantity()+ " ");
      p.print(stone[i].getQuantity() + " ");
    }
    p.println();
  }

  public void fromFile(Scanner s) {
    s.nextLine();
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
}
