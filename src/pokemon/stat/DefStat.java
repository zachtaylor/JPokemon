package pokemon.stat;

public class DefStat extends Stat {
    public DefStat(int base, int level) {
        super(base, level);
        resetMax();
        cur = max;
    }

    @Override
    public void resetMax() {
        max = (2 * base * lvl) / 100 + 5;
    }
}
