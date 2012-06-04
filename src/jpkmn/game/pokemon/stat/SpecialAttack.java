package jpkmn.game.pokemon.stat;

public class SpecialAttack extends Stat {
  public SpecialAttack(int base, int level) {
    super(base, level);
    resetMax();
    _cur = _max;
  }

  @Override
  public void resetMax() {
    _max = ((2 * _base + _pts) * _lvl) / 100 + 5;
  }
}
