package org.jpokemon.battle;

import java.util.ArrayList;
import java.util.List;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.action.ActionFactory;
import org.jpokemon.action.ActionSet;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.pokemon.EffortValue;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.service.LoadException;
import org.jpokemon.trainer.Player;

public class Reward {
  public Reward(Slot s) {
    _pokemon = s.leader();
    _faintMessage = _pokemon.name() + " fainted!";

    double xp = s.trainer().xpFactor();
    xp *= _pokemon.xpYield();
    xp *= _pokemon.level();
    xp /= 7;
    xp *= JPokemonConstants.UNIVERSAL_EXPERIENCE_MODIFIER;
    _xp = (int) xp;

    if (!(s.trainer() instanceof Player)) {
      _evs = _pokemon.effortValues();
    }

    if (s.party().awake() == 0) {
      _defeatMessage = " defeated " + s.trainer().name();

      for (RewardAction ra : RewardAction.get(s.trainer().id())) {
        _actions.addAction(ActionFactory.build(ra.getType(), ra.getData()));
      }
    }
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

  public void pokemon(Pokemon p) {
    _pokemon = p;
  }

  public Pokemon pokemon() {
    return _pokemon;
  }

  public void apply(Slot s) {
    s.trainer().notify(_faintMessage);

    applyXP(s);

    if (_defeatMessage != null && s.trainer() instanceof Player) {
      applyDefeat(s);
    }
  }

  private void applyXP(Slot s) {
    List<Pokemon> hitList = s.removeRival(pokemon());

    int xpEach = Math.max(xp() / hitList.size(), 1);

    /* TODO s is the number of Pokemon that participated in the battle and have
     * not fainted. If any Pokemon in the party is holding an Exp. Share, s is
     * equal to 2, and for the rest of the Pokemon, s is equal to twice the
     * number of Pokemon that participated instead. If more than one Pokemon is
     * holding an Exp. Share, s is equal to twice the number of Pokemon holding
     * the Exp. Share for each Pokemon holding one. */

    for (Pokemon earner : hitList) {
      if (earner.hasOriginalTrainer())
        earner.xp((int) (earner.xp() + xpEach * JPokemonConstants.ORIGINAL_TRAINER_EXPERIENCE_MODIFIER));
      else
        earner.xp((int) (earner.xp() + xpEach * JPokemonConstants.NOT_ORIGINAL_TRAINER_EXPERIENCE_MODIFIER));

      earner.addEV(effortValues());
      s.trainer().notify(earner.name() + " received " + xpEach + " experience!");
    }
  }

  private void applyDefeat(Slot s) {
    s.trainer().notify(s.trainer().name() + _defeatMessage);

    try {
      _actions.execute((Player) s.trainer());
    } catch (LoadException e) {
    }
  }

  private int _xp;
  private Pokemon _pokemon;
  private String _faintMessage, _defeatMessage;
  private ActionSet _actions = new ActionSet();
  private List<EffortValue> _evs = new ArrayList<EffortValue>();
}