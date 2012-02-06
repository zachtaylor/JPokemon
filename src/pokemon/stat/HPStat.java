package pokemon.stat;

import jpkmn.Driver;

public class HPStat extends Stat {

    public HPStat(int base, int level) {
        super(base, level);
        resetMax();
    }

    @Override
    public void resetMax() {
        max = 10 + lvl + ((2 * base + pts) * lvl) / 100;
        cur = max;
    }

    public void effect(int power) {
        // Being here doesn't make sense. Fatal error.
        Driver.crash(HPStat.class, "Effect was called on HPStat. Crashing.");
    }
}
