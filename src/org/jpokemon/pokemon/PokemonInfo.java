package org.jpokemon.pokemon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;

import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.DataConnectionManager;
import com.njkremer.Sqlite.SqlStatement;
import com.njkremer.Sqlite.Annotations.OneToMany;
import com.njkremer.Sqlite.Annotations.PrimaryKey;

public class PokemonInfo {
  @PrimaryKey
  private int number;
  private String name;
  private int type1, type2, attack, specattack, defense, specdefense, speed, health, evolutionlevel, xpyield, growthrate, catchrate;

  @OneToMany("pokemon")
  private List<EffortValue> effortValues;

  private static Map<Integer, PokemonInfo> cache = new HashMap<Integer, PokemonInfo>();

  public static PokemonInfo get(int num) {
    DataConnectionManager.init(JPokemonConstants.DATABASE_PATH);

    if (cache.get(num) == null) {
      try {
        List<PokemonInfo> pokemonbase = SqlStatement.select(PokemonInfo.class).where("number").eq(num).getList();

        if (!pokemonbase.isEmpty()) {
          cache.put(num, pokemonbase.get(0));
        }

      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache.get(num);
  }

  public String toString() {
    return "Pokemon#" + getNumber() + " " + getName();
  }

  //@preformat
  public String getName()        { return name;           } public void setName(String val)        { name           = val; }
  public int getNumber()         { return number;         } public void setNumber(int val)         { number         = val; }
  public int getType1()          { return type1;          } public void setType1(int val)          { type1          = val; }
  public int getType2()          { return type2;          } public void setType2(int val)          { type2          = val; }
  public int getAttack()         { return attack;         } public void setAttack(int val)         { attack         = val; }
  public int getSpecattack()     { return specattack;     } public void setSpecattack(int val)     { specattack     = val; }
  public int getDefense()        { return defense;        } public void setDefense(int val)        { defense        = val; }
  public int getSpecdefense()    { return specdefense;    } public void setSpecdefense(int val)    { specdefense    = val; }
  public int getSpeed()          { return speed;          } public void setSpeed(int val)          { speed          = val; }
  public int getHealth()         { return health;         } public void setHealth(int val)         { health         = val; }
  public int getEvolutionlevel() { return evolutionlevel; } public void setEvolutionlevel(int val) { evolutionlevel = val; }
  public int getXpyield()        { return xpyield;        } public void setXpyield(int val)        { xpyield        = val; }
  public int getGrowthrate()     { return growthrate;     } public void setGrowthrate(int val)     { growthrate     = val; }
  public int getCatchrate()      { return catchrate;      } public void setCatchrate(int val)      { catchrate      = val; }
  public List<EffortValue> getEffortValues() { return effortValues; } public void setEffortValues(List<EffortValue> val) { effortValues = val; }
  //@format
}