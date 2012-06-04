package jpkmn.game.pokemon.stat;

public class Defense extends Stat {
  public Defense(int base, int level) {
    super(base, level);
    resetMax();
    _cur = _max;
  }

  @Override
  public void resetMax() {
    _max = (2 * _base * _lvl) / 100 + 5;
  }
}
