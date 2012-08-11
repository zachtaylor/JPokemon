package jpkmn.game.base;

import java.util.ArrayList;
import java.util.List;

import jpkmn.game.player.GymLeader;
import jpkmn.game.pokemon.Pokemon;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class GymLeaderBase {
  public static GymLeader setup(GymLeader g) {
    int number = g.badge();

    List<Pokemon> pokemon = getPartyForLeader(number);
    GymLeaderBase.Info info = getInfoForLeader(number);

    for (Pokemon p : pokemon) {
      g.party.add(p);
    }

    g.name(info.getName());
    g.prize(info.getPrize());

    return g;
  }

  public static List<Pokemon> getPartyForLeader(int number) {
    return GymLeaderBase.Party.getPartyForLeader(number);
  }

  public static GymLeaderBase.Info getInfoForLeader(int number) {
    return GymLeaderBase.Info.getInfoForLeader(number);
  }

  public static class Party {
    @PrimaryKey
    private int number;
    private int pkmn, level, move1, move2, move3, move4;

    public static List<Pokemon> getPartyForLeader(int number) {
      DataConnectionManager.init("Pokemon.db");

      try {
        List<GymLeaderBase.Party> leader = new SqlStatement()
            .select(GymLeaderBase.Party.class).where("number").eq(number)
            .getList();

        List<Pokemon> list = new ArrayList<Pokemon>();

        for (GymLeaderBase.Party glp : leader) {
          Pokemon p = new Pokemon(glp.getNumber(), glp.getLevel());

          p.moves.removeAll();
          if (glp.getMove1() != -1) p.moves.add(glp.getMove1());
          if (glp.getMove2() != -1) p.moves.add(glp.getMove2());
          if (glp.getMove3() != -1) p.moves.add(glp.getMove3());
          if (glp.getMove4() != -1) p.moves.add(glp.getMove4());

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

  private static class Info {
    @PrimaryKey
    private int number;
    private int prize;
    private String name;

    public static GymLeaderBase.Info getInfoForLeader(int number) {
      DataConnectionManager.init("Pokemon.db");

      try {
        List<GymLeaderBase.Info> info = new SqlStatement()
            .select(GymLeaderBase.Info.class).where("number").eq(number)
            .getList();

        return info.isEmpty() ? null : info.get(0);
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      return null;
    }

    //@preformat
    public int getNumber() {return number;} public void setNumber(int _val) {number = _val;}
    public int getPrize() {return prize;} public void setPrize(int _val) {prize = _val;}
    public String getName() {return name;} public void setName(String _val) {name = _val;}
    //@format
  }
}