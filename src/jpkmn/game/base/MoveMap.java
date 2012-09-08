package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class MoveMap {
    int pokemon_number,
         pokemon_level,
           move_number;

    public static MoveMap getMapForPokemonNumberAtLevel(int number, int level) {
        DataConnectionManager.init("Pokemon.db");
        try {
            List<MoveMap> movemap = new SqlStatement()
                                     .select(MoveMap.class)
                                     .where("pokemon_number").eq(number)
                                     .and("pokemon_level").eq(level).getList();

            return movemap.isEmpty() ? null : movemap.get(0);

        } catch (DataConnectionException e) { e.printStackTrace(); }
        return null;
    }

    public int getPokemon_number() { return pokemon_number; } public void setPokemon_number(int val) { pokemon_number = val; }
    public int getPokemon_level()  { return pokemon_level;  } public void setPokemon_level(int val)  { pokemon_level  = val; }
    public int getMove_number()    { return move_number;    } public void setMove_number(int val)    { move_number    = val; }
}