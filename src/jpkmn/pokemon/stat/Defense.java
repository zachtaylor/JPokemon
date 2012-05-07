package jpkmn.pokemon.stat;

public class Defense extends Stat {
    public Defense(int base, int level) {
        super(base, level);
        resetMax();
        cur = max;
    }

    @Override
    public void resetMax() {
        max = (2 * base * lvl) / 100 + 5;
    }
}
