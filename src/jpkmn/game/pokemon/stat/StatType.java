package jpkmn.game.pokemon.stat;

public enum StatType {
  ATTACK, SPECATTACK, DEFENSE, SPECDEFENSE, SPEED;

  public static StatType valueOf(int st) {
    return StatType.values()[st];
  }
}