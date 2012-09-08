package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;

public class BonusEffectBase {
    int move_number, type,
            target, power;
    double chance, percent;

    public static List<BonusEffectBase> getBasesForMoveNumber(int number) {
        DataConnectionManager.init("Pokemon.db");
        try {
            return new SqlStatement()
                    .select(BonusEffectBase.class)
                    .where("move_number").eq(number).getList();
        } catch (DataConnectionException e) { e.printStackTrace(); }
        return null;
    }

    public int getMove_number() { return move_number; } public void setMove_number(int val) { move_number = val; }
    public int getType() { return type; } public void setType(int val) { type = val; }
    public int getTarget() { return target; } public void setTarget(int val) { target = val; }
    public int getPower() { return power; } public void setPower(int val) { power = val; }
    public double getPercent() { return percent; } public void setPercent(double val) { percent = val; }
    public double getChance() { return chance; } public void setChance(double val) { chance = val; }
}
