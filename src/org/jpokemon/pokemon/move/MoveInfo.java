package org.jpokemon.pokemon.move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.pokemon.move.effect.MoveEffect;
import org.jpokemon.server.JPokemonServer;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.OneToMany;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class MoveInfo {
  @PrimaryKey
  private int number;

  private String name;
  private double accuracy;
  private int type, power, pp, style;

  @OneToMany("move_number")
  private List<MoveEffect> effects = new ArrayList<MoveEffect>();

  private static Map<Integer, MoveInfo> cache = new HashMap<Integer, MoveInfo>();

  public static MoveInfo get(int number) {
    DataConnectionManager.init(JPokemonServer.databasepath);

    if (cache.get(number) == null) {
      try {
        List<MoveInfo> moves = SqlStatement.select(MoveInfo.class).where("number").eq(number).getList();
        // List<MoveEffect> effects = SqlStatement.select(MoveEffect.class)
        // .where("move_number").eq(number).getList();

        if (!moves.isEmpty()) {
          // moves.get(0).setEffects(effects);
          cache.put(number, moves.get(0));
        }
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(number);
  }

  public String toString() {
    return "Move#" + getNumber() + " " + getName();
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int n) {number = n;}
  public String getName() {return name;} public void setName(String s) {name = s;}
  public double getAccuracy() {return accuracy;} public void setAccuracy(double d) {accuracy = d;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public int getPower() {return power;} public void setPower(int p) {power = p;}
  public int getPp() {return pp;} public void setPp(int p) {pp = p;}
  public int getStyle() {return style;} public void setStyle(int s) {style = s;}
  public List<MoveEffect> getEffects() {return effects;} public void setEffects(List<MoveEffect> l) {effects = l;}
  //@format
}
