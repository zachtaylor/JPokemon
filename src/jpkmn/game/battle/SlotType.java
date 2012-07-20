package jpkmn.game.battle;

public enum SlotType {
  WILD(.6), PLAYER(0), TRAINER(1.3), GYM(1.6);

  private SlotType(double factor) {
    _factor = factor;
  }

  public double getXPFactor() {
    return _factor;
  }

  private double _factor;
}