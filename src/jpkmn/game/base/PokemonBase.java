package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class PokemonBase {
    @PrimaryKey
    private int number;
    private int type1, type2,
          attack, specattack,
        defense, specdefense,
               speed, health,
              evolutionlevel;
    private String name;

    public PokemonBase() {}
    public static PokemonBase getBaseForNumber(int _num) {
        DataConnectionManager.init("Pokemon.db");
        try {
            List<PokemonBase> pokemonbase = new SqlStatement()
                                             .select(PokemonBase.class)
                                             .where("number").eq(_num).getList();

            return pokemonbase.isEmpty() ? null : pokemonbase.get(0);

        } catch (DataConnectionException e) { e.printStackTrace(); }
        return null;
    }

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
}
