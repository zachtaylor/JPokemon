package jpkmn.game.pokemon;

public enum ConditionEffect {
  BURN, PARALYZE, SLEEP, POISON, FREEZE, CONFUSE, WRAP, FLINCH;

  public double persistanceChance() {
    switch (this) {
      case BURN: case PARALYZE: case POISON:
        return 1;
      case SLEEP:
        return .333;
      case FREEZE:
        return .8;
      case CONFUSE: case WRAP:
        return .667;
      default:
        return 0;
    }
  }

  public boolean blocksAttack() {
    switch (this) {
      case FREEZE: case SLEEP: case FLINCH: case CONFUSE:
        return true;
      case PARALYZE:
        return Math.random() < .25;
      default:
        return false;
    }
  }

  public double damagePercentage() {
    switch (this) {
      case BURN: case POISON: case WRAP:
        return .1;
      case CONFUSE:
        return 0; // TODO
      default:
        return 0;
    }
  }

  public double catchBonus() {
    switch (this) {
      case FREEZE: case SLEEP:
        return 2;
      case BURN: case POISON: case PARALYZE:
        return 1.5;
      default:
        return 1;
    }
  }

  public String persistanceMessage(boolean persist) {
    switch (this) {
      case BURN:
        return " was injured by it's burn!";
      case SLEEP:
        if (persist)
          return " is still sleeping!";
        else
          return " woke up!";
      case POISON:
        return " was injured by the poison!";
      case FREEZE:
        if (persist)
          return " is still frozen!";
        else
          return " broke out of the ice!";
      case CONFUSE:
        if (persist)
          return " hurt itself in it's confusion!";
        else
          return " is no longer confused!";
      case WRAP:
        if (persist)
          return " was injured by the binding!";
        else
          return " freed itself!";
      default:
        return null;
    }
  }
}