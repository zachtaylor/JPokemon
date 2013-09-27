package org.jpokemon.pokemon;

public enum ConditionEffect {
  BURN, PARALYZE, SLEEP, POISON, FREEZE, CONFUSE, WRAP, FLINCH;

  public double persistanceChance() {
    switch (this) {
    case BURN:
    case PARALYZE:
    case POISON:
      return 1;
    case SLEEP:
      return .333;
    case FREEZE:
      return .8;
    case CONFUSE:
    case WRAP:
      return .667;
    default:
      return 0;
    }
  }

  public boolean blocksAttack() {
    switch (this) {
    case FREEZE:
    case SLEEP:
    case FLINCH:
    case CONFUSE:
      return true;
    case PARALYZE:
      return Math.random() < .25;
    default:
      return false;
    }
  }

  public double damagePercentage() {
    switch (this) {
    case BURN:
    case POISON:
    case WRAP:
      return .1;
    case CONFUSE:
      return 0; // TODO
    default:
      return 0;
    }
  }

  public double catchBonus() {
    switch (this) {
    case FREEZE:
    case SLEEP:
      return 2;
    case BURN:
    case POISON:
    case PARALYZE:
      return 1.5;
    default:
      return 1;
    }
  }

  public String getPersistanceMessage() {
    switch (this) {
    case BURN:
      return " was injured by it's burn!";
    case SLEEP:
      return " is still sleeping!";
    case POISON:
      return " was injured by the poison!";
    case FREEZE:
      return " is still frozen!";
    case CONFUSE:
      return " hurt itself in it's confusion!";
    case WRAP:
      return " was injured by the binding!";
    case PARALYZE:
      return " is paralyzed!";
    default:
      return null;
    }
  }

  public String getDissipationMessage() {
    switch (this) {
    case SLEEP:
      return " woke up!";
    case FREEZE:
      return " broke out of the ice!";
    case WRAP:
      return " freed itself!";
    case CONFUSE:
      return " is no longer confused!";
    default:
      return null;
    }
  }
}