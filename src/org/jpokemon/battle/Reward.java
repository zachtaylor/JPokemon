package org.jpokemon.battle;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.pokemon.EffortValue;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.Player;

public class Reward {
  public Reward(Slot s) {
    _pokemon = s.leader();
    _message = _pokemon.name() + " fainted!";

    double xp = s.trainer().xpFactor();
    xp *= _pokemon.xpYield();
    xp *= _pokemon.level();
    xp /= 7;
    xp *= JPokemonConstants.UNIVERSAL_EXPERIENCE_MODIFIER;
    _xp = (int) xp;

    if (s.trainer() instanceof Player)
      _evs = new ArrayList<EffortValue>();
    else
      _evs = _pokemon.effortValues();
  }

  public void xp(int amount) {
    _xp = amount;
  }

  public int xp() {
    return _xp;
  }

  public void effortValues(List<EffortValue> list) {
    _evs = list;
  }

  public List<EffortValue> effortValues() {
    return _evs;
  }

  public void message(String m) {
    _message = m;
  }

  public String message() {
    return _message;
  }

  public void pokemon(Pokemon p) {
    _pokemon = p;
  }

  public Pokemon pokemon() {
    return _pokemon;
  }

  public void apply(Slot s) {
    List<String> messages = new ArrayList<String>();
    List<Pokemon> hitList = s.removeRival(pokemon());

    messages.add(message());

    int xpEach = Math.max(xp() / hitList.size(), 1);

    /*
     * TODO s is the number of Pokemon that participated in the battle and have
     * not fainted. If any Pokemon in the party is holding an Exp. Share, s is
     * equal to 2, and for the rest of the Pokemon, s is equal to twice the
     * number of Pokemon that participated instead. If more than one Pokemon is
     * holding an Exp. Share, s is equal to twice the number of Pokemon holding
     * the Exp. Share for each Pokemon holding one.
     */

    for (Pokemon earner : hitList) {
      if (earner.hasOriginalTrainer())
        earner.xp((int) (earner.xp() + xpEach * JPokemonConstants.ORIGINAL_TRAINER_EXPERIENCE_MODIFIER));
      else
        earner.xp((int) (earner.xp() + xpEach * JPokemonConstants.NOT_ORIGINAL_TRAINER_EXPERIENCE_MODIFIER));

      earner.addEV(effortValues());
      messages.add(earner.name() + " received " + xpEach + " experience!");
    }

    s.trainer().notify(messages.toArray(new String[messages.size()]));
  }

  private int _xp;
  private String _message;
  private Pokemon _pokemon;
  private List<EffortValue> _evs;
}