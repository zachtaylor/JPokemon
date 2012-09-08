package jpkmn.game.base;

import java.util.ArrayList;
import java.util.List;

import jpkmn.Constants;
import jpkmn.game.pokemon.Pokemon;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class AIParty {
  private int number, pkmn, level, move1, move2, move3, move4;

  public static List<Pokemon> getPartyForLeader(int number) {
    if (number <= 0 || number > Constants.GYMNUMBER) return null;
    return doGet(number);
  }

  public static List<Pokemon> getPartyForRival(int number) {
    if (number <= 0 || number > Constants.RIVALNUMBER) return null;
    return doGet(number + Constants.GYMNUMBER);
  }

  public static List<Pokemon> getPartyForTrainer(int number) {
    if (number <= 0 || number > Constants.TRAINERNUMBER) return null;
    return doGet(number + Constants.GYMNUMBER + Constants.RIVALNUMBER);
  }

  private static List<Pokemon> doGet(int number) {
    DataConnectionManager.init("Pokemon.db");

    try {
      List<AIParty> data = new SqlStatement().select(AIParty.class)
          .where("number").eq(number).getList();

      List<Pokemon> list = new ArrayList<Pokemon>();

      for (AIParty aip : data) {
        Pokemon p = new Pokemon(aip.getPkmn(), aip.getLevel());

        if (aip.getMove1() != -1) {
          p.moves.removeAll();
          p.moves.add(aip.getMove1());
          if (aip.getMove2() != -1) p.moves.add(aip.getMove2());
          if (aip.getMove3() != -1) p.moves.add(aip.getMove3());
          if (aip.getMove4() != -1) p.moves.add(aip.getMove4());
        }

        list.add(p);
      }

      return list;
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    return null;
  }

  //@preformat
  public int getNumber() {return number;} public void setNumber(int _val) {number = _val;}
  public int getPkmn() {return pkmn;} public void setPkmn(int _val) {pkmn = _val;}
  public int getLevel() {return level;} public void setLevel(int _val) {level = _val;}
  public int getMove1() {return move1;} public void setMove1(int _val) {move1 = _val;}
  public int getMove2() {return move2;} public void setMove2(int _val) {move2 = _val;}
  public int getMove3() {return move3;} public void setMove3(int _val) {move3 = _val;}
  public int getMove4() {return move4;} public void setMove4(int _val) {move4 = _val;}
  //@format
}