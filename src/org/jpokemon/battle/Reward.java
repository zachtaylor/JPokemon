package org.jpokemon.battle;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.pokemon.Pokemon;

public class Reward implements JPokemonConstants {
  public Reward(Slot s) {
    double xp = s.trainer().xpFactor() * (s.leader().level() + 6) * UNIVERSAL_EXPERIENCE_MODIFIER;

    _xp = (int) xp;
    _pokemon = s.leader();
    _message = _pokemon.name() + " fainted!";
  }

  public void xp(int amount) {
    _xp = amount;
  }

  public int xp() {
    return _xp;
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

    for (Pokemon earner : hitList) {
      earner.xp(earner.xp() + xpEach);
      earner.addEV(pokemon().effortValues());
      messages.add(earner.name() + " received " + xpEach + " experience!");
    }

    s.trainer().notify(messages.toArray(new String[messages.size()]));
  }

  private int _xp;
  private String _message;
  private Pokemon _pokemon;
}