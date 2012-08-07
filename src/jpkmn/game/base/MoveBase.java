package jpkmn.game.base;

import java.util.List;

import com.kremerk.Sqlite.DataConnectionException;
import com.kremerk.Sqlite.DataConnectionManager;
import com.kremerk.Sqlite.SqlStatement;
import com.kremerk.Sqlite.Annotations.PrimaryKey;

public class MoveBase {
    @PrimaryKey
    private int number;
    private int type, power,
                  pp, style;
    private double accuracy;
    private String name;

    public static MoveBase getBaseForNumber(int number) {
        DataConnectionManager.init("Pokemon.db");
        try {
            List<MoveBase> moves = new SqlStatement()
                                    .select(MoveBase.class)
                                    .where("number").eq(number).getList();

            return moves.isEmpty() ? null : moves.get(0);
        } catch (DataConnectionException e) { e.printStackTrace(); }
        return null;
    }

    public String getName()     { return name;     } public void setName(String _val)     { name     = _val; }
    public int getNumber()      { return number;   } public void setNumber(int _val)      { number   = _val; }
    public int getType()        { return type;     } public void setType(int _val)        { type     = _val; }
    public int getPower()       { return power;    } public void setPower(int _val)       { power    = _val; }
    public int getPp()          { return pp;       } public void setPp(int _val)          { pp       = _val; }
    public int getStyle()       { return style;    } public void setStyle(int _val)       { style    = _val; }
    public double getAccuracy() { return accuracy; } public void setAccuracy(double _val) { accuracy = _val; }
}
