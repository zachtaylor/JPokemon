package jpkmn.game.item;

import jpkmn.game.battle.Target;
import jpkmn.game.pokemon.Pokemon;

public class Stone extends Item {

  public enum Type {
    FIRE, WATER, THUNDER, MOON, LEAF;

    private String formatName() {
      return name().charAt(0)
          + name().substring(1, name().length()).toLowerCase() + "stone";
    }
  }

  public Stone(int power, int quantity, Type type) {
    super(power, quantity, type.formatName());
    this.type = type;
    target = Target.SELF;
  }

  @Override
  public boolean effect(Pokemon p) {
    if (!reduce()) return false;

    int n = p.number();

    // Eevee (#133) evolutions are not linear

    if (this.type == Type.FIRE) {
      if (n == 37 || n == 58) p.changeSpecies();
      else if (n == 133) p.changeSpecies(136);
    }
    else if (this.type == Type.WATER) {
      if (n == 60 || n == 90 || n == 120) p.changeSpecies();
      else if (n == 133) p.changeSpecies(134);
    }
    else if (this.type == Type.LEAF) {
      if (n == 44 || n == 70 || n == 102) p.changeSpecies();
    }
    else if (this.type == Type.THUNDER) {
      if (p.number() == 25) p.changeSpecies();
      else if (n == 133) p.changeSpecies(135);
    }
    else if (this.type == Type.MOON) {
      if (n == 30 || n == 33 || n == 35 || n == 39) p.changeSpecies();
    }

    return p.number() != n;

  }

  private Type type;
}