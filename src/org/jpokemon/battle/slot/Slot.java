package org.jpokemon.battle.slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.battle.turn.Turn;
import org.jpokemon.battle.turn.TurnFactory;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.jpokemon.trainer.TrainerType;
import org.json.JSONException;
import org.json.JSONObject;

public class Slot implements JPokemonConstants {
  public Slot(PokemonTrainer trainer, int team) {
    _trainer = trainer;
    _team = team;

    _turnFactory = new TurnFactory(this);
    _slotEffects = new ArrayList<SlotEffect>();
    _rivalsLists = new HashMap<Pokemon, List<Pokemon>>();
  }

  public PokemonTrainer trainer() {
    return _trainer;
  }

  public PokemonStorageUnit party() {
    return _trainer.party();
  }

  public int team() {
    return _team;
  }

  public Pokemon leader() {
    return party().get(0);
  }

  public void addSlotEffect(SlotEffect e) {
    // Remove and add to reset turn duration
    if (_slotEffects.contains(e))
      _slotEffects.remove(e);

    _slotEffects.add(e);
  }

  public String[] applySlotEffects() {
    List<String> messages = new ArrayList<String>();

    for (SlotEffect e : _slotEffects) {
      String message = e.apply();

      if (message != null)
        messages.add(message);
    }

    messages.addAll(rollDownEffectDuration());
    return messages.toArray(new String[messages.size()]);
  }

  private List<String> rollDownEffectDuration() {
    List<String> messages = new ArrayList<String>();
    List<SlotEffect> nextEffects = new ArrayList<SlotEffect>();

    for (SlotEffect e : _slotEffects)
      if (e.reduceDuration())
        nextEffects.add(e);
      else
        messages.add(e.name() + " dissipated");

    _slotEffects = nextEffects;
    return messages;
  }

  public Turn turn(JSONObject json, Slot target) {
    return _turnFactory.create(json, target);
  }

  public void takeDamage(int damage) {
    leader().takeDamage(damage);
  }

  public double damageModifier(Move m) {
    double damageModifier = 1.0;

    for (SlotEffect e : _slotEffects)
      damageModifier *= e.damageModifier(m);

    // APPLY WEATHER CHANGES HERE WHEN APPLICABLE

    return damageModifier;
  }

  public void addRival(Slot s) {
    List<Pokemon> rivalsList = rivalsList(leader());

    if (!rivalsList.contains(s.leader()))
      rivalsList.add(s.leader());

    if (_trainer.type() == TrainerType.PLAYER)
      ((Player) _trainer).pokedex().saw(s.leader().number());
  }

  public void removeRival(Slot s) {
    Pokemon p = s.leader();
    int xpReward = (int) (s.trainer().xpFactor() * (p.level() + 6) * UNIVERSAL_EXPERIENCE_MODIFIER);

    List<Pokemon> earnList = new ArrayList<Pokemon>();
    List<String> message = new ArrayList<String>();

    message.add(p.name() + " fainted!");

    for (Pokemon cur : _trainer.party()) {
      List<Pokemon> rivalsList = _rivalsLists.get(cur);

      // TODO : If cur holding xp share, add to earners

      if (rivalsList != null && rivalsList.contains(p)) {
        earnList.add(cur);
        rivalsList.remove(p);
      }
    }

    int xpEach = Math.max(xpReward / earnList.size(), 1);

    for (Pokemon earner : earnList) {
      earner.xp(earner.xp() + xpEach);
      message.add(earner.name() + " received " + xpEach + " experience!");
    }

    _trainer.notify((String[]) message.toArray());
  }

  public JSONObject toJSON() {
    JSONObject data = null;

    try {
      data = _trainer.toJSON();
      data.put("team", team());
      data.put("leader", _trainer.party().get(0).toJSON());
    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  private List<Pokemon> rivalsList(Pokemon p) {
    if (_rivalsLists.get(leader()) == null)
      _rivalsLists.put(leader(), new ArrayList<Pokemon>());

    return _rivalsLists.get(leader());
  }

  private int _team;
  private PokemonTrainer _trainer;
  private TurnFactory _turnFactory;
  private List<SlotEffect> _slotEffects;
  private Map<Pokemon, List<Pokemon>> _rivalsLists;
}