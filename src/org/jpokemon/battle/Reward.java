package org.jpokemon.battle;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.pokemon.EffortValue;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.trainer.TrainerType;

public class Reward implements JPokemonConstants {
  public Reward(Slot s) {
    double xp = s.trainer().xpFactor() * (s.leader().level() + 6) * UNIVERSAL_EXPERIENCE_MODIFIER;

    _xp = (int) xp;
    _pokemon = s.leader();
    _message = _pokemon.name() + " fainted!";
    
    if (s.trainer().type() == TrainerType.PLAYER)
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

    for (Pokemon earner : hitList) {
      earner.xp(earner.xp() + xpEach);
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