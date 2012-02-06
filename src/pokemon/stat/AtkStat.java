package pokemon.stat;

public class AtkStat extends Stat {
    public AtkStat(int base, int level) {
        super(base, level);
        resetMax();
        cur = max;
    }

    @Override
    public void resetMax() {
        max = ((2 * base + pts) * lvl) / 100 + 5;
    }
}
