package pokemon.stat;

import jpkmn.Driver;

public class Health extends Stat {

    public Health(int base, int level) {
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
        Driver.crash(Health.class, "Effect was called on HPStat. Crashing.");
    }
}
