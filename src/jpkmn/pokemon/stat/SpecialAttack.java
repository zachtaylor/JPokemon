package jpkmn.pokemon.stat;

public class SpecialAttack extends Stat {
    public SpecialAttack(int base, int level) {
        super(base, level);
        resetMax();
        cur = max;
    }

    @Override
    public void resetMax() {
        max = ((2 * base + pts) * lvl) / 100 + 5;
    }
}
