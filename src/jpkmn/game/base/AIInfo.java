package jpkmn.game.base;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.exception.ConfigurationException;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.OneToMany;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class AIInfo implements JPokemonConstants {
  @PrimaryKey
  private int number;

  private int area, cash, type;
  private String name;

  @OneToMany("ai_number")
  private List<String> pokemon = new ArrayList<String>();

  private static AIInfo[] cache = new AIInfo[JPokemonConstants.AINUMBER];

  public static AIInfo get(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    if (number < 1 || number > AINUMBER)
      throw new ConfigurationException(number + " is outside move range.");

    if (cache[number - 1] == null) {
      try {
        List<AIInfo> info = SqlStatement.select(AIInfo.class).where("number")
            .eq(number).getList();
        List<AIPokemon> pokemon = SqlStatement.select(AIPokemon.class)
            .where("ai_number").eq(number).getList();

        if (!info.isEmpty()) {
          for (AIPokemon p : pokemon)
            info.get(0).getPokemon().add(p.getEntry());

          cache[number - 1] = info.get(0);
        }
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }
    }

    return cache[number - 1];
  }

  public static List<AIInfo> getAIForArea(int number) {
    DataConnectionManager.init(DATABASE_PATH);

    try {
      List<AIInfo> info = SqlStatement.select(AIInfo.class).where("area")
          .eq(number).getList();

      return info;
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getArea() {return area;} public void setArea(int a) {area = a;}
  public int getCash() {return cash;} public void setCash(int _val) {cash = _val;}
  public int getNumber() {return number;} public void setNumber(int _val) {number = _val;}
  public int getType() {return type;} public void setType(int t) {type = t;}
  public String getName() {return name;} public void setName(String _val) {name = _val;}
  public List<String> getPokemon() {return pokemon;} public void setPokemon(List<String> p) {pokemon = p;}
  //@format
}