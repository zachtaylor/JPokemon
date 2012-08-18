package jpkmn.game.pokemon.stat;

public class Health extends Stat {
  @Override
  public void resetMax() {
    _max = 10 + _lvl + ((2 * _base + _pts) * _lvl) / 100;
    _cur = _max;
  }

  @Override
  public void effect(int power) {
    _cur += power;
    verify();
  }

  private void verify() {
    if (_cur > _max) _cur = _max;
    if (_cur < 0) _cur = 0;
  }
}