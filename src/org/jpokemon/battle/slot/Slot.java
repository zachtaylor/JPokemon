package org.jpokemon.battle.slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jpokemon.battle.turn.Turn;
import org.jpokemon.battle.turn.TurnFactory;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.jpokemon.trainer.TrainerState;
import org.json.JSONObject;

public class Slot {
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

    if (_trainer instanceof Player)
      ((Player) _trainer).pokedex().saw(s.leader().number());
  }

  /**
   * Removes a rival Pokemon from being tracked as an opponent by the Slot.
   * 
   * @param p Pokemon to remove
   * @return The List of Pokemon whom consider p to be a rival
   */
  public List<Pokemon> removeRival(Pokemon p) {
    List<Pokemon> hitList = new ArrayList<Pokemon>();

    for (Pokemon cur : _trainer.party()) {
      List<Pokemon> rivalsList = rivalsList(cur);

      // TODO : If cur holding xp share etc., add to hitList

      if (rivalsList != null && rivalsList.contains(p)) {
        hitList.add(cur);
        rivalsList.remove(p);
      }
    }

    return hitList;
  }

  public JSONObject toJSON() {
    return _trainer.toJSON(TrainerState.BATTLE);
  }

  private List<Pokemon> rivalsList(Pokemon p) {
    if (_rivalsLists.get(p) == null)
      _rivalsLists.put(p, new ArrayList<Pokemon>());

    return _rivalsLists.get(p);
  }

  private int _team;
  private PokemonTrainer _trainer;
  private TurnFactory _turnFactory;
  private List<SlotEffect> _slotEffects;
  private Map<Pokemon, List<Pokemon>> _rivalsLists;
}