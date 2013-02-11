package org.jpokemon.pokemon.move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.exception.ConfigurationException;
import org.jpokemon.pokemon.move.effect.MoveEffect;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.OneToMany;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class MoveInfo implements JPokemonConstants {
  @PrimaryKey
  private int number;

  private String name;
  private double accuracy;
  private int type, power, pp, style;

  @OneToMany("move_number")
  private List<MoveEffect> effects = new ArrayList<MoveEffect>();

  private static Map<Integer, MoveInfo> cache = new HashMap<Integer, MoveInfo>();

  public static MoveInfo get(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    if (number < 1)
      throw new ConfigurationException(number + " is outside move range.");

    if (cache.get(number) == null) {
      try {
        List<MoveInfo> moves = SqlStatement.select(MoveInfo.class)
            .where("number").eq(number).getList();
        List<MoveEffect> effects = SqlStatement.select(MoveEffect.class)
            .where("move_number").eq(number).getList();

        if (!moves.isEmpty()) {
          moves.get(0).setEffects(effects);
          cache.put(number, moves.get(0));
        }
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(number);
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