package jpkmn.game.pokemon.move.effect;

public enum MoveEffectType {
  ATTACK, DEFENSE, SPECATTACK, SPECDEFENSE, SPEED, BURN, PARALYZE, SLEEP,
  POISON, FREEZE, CONFUSE, WRAP, FLINCH, HEAL, LEECH, KAMIKAZE;

  public static MoveEffectType valueOf(int style) {
    return values()[style];
  }

  public boolean isStatModifier() {
    return ordinal() <= SPEED.ordinal();
  }

  public boolean isConditionModifier() {
    return ordinal() > SPEED.ordinal() && ordinal() <= FLINCH.ordinal();
  }
}