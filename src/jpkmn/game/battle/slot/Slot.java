package jpkmn.game.battle.slot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpkmn.game.battle.turn.AbstractTurn;
import jpkmn.game.battle.turn.AttackTurn;
import jpkmn.game.battle.turn.ItemTurn;
import jpkmn.game.battle.turn.RunTurn;
import jpkmn.game.battle.turn.SwapTurn;
import jpkmn.game.item.Item;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.player.Player;
import org.jpokemon.player.PokemonTrainer;
import org.jpokemon.player.TrainerType;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;
import org.json.JSONException;
import org.json.JSONObject;

public class Slot {
  public Slot(PokemonTrainer trainer, int team) {
    _trainer = trainer;
    _bide = false;
    _team = team;

    _effects = new ArrayList<SlotEffect>();
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

  public Slot target() {
    return _target;
  }

  public void target(Slot target) {
    _target = target;
  }

  public void addEffect(SlotEffect e) {
    if (!_effects.contains(e))
      _effects.add(e);
  }

  public String[] applyEffects() {
    List<String> messages = new ArrayList<String>();

    for (SlotEffect e : _effects) {
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

    for (SlotEffect e : _effects)
      if (e.reduceDuration())
        nextEffects.add(e);
      else
        messages.add(e.name() + " dissipated");

    _effects = nextEffects;
    return messages;
  }

  public void moveIndex(int moveIndex) {
    _index = moveIndex;
  }

  public void itemID(int item) {
    _itemID = item;
  }

  public void itemIndex(int itemIndex) {
    _index = itemIndex;
  }

  public void swapIndex(int slotIndex) {
    _index = slotIndex;
  }

  public AbstractTurn attack() {
    Move move = leader().moves.get(_index);

    AttackTurn turn = new AttackTurn(this, move);

    if (_bide) {
      turn.absoluteDamage(_bidedamage);
      _bide = false;
      _bidedamage = 0;
    }

    return turn;
  }

  public AbstractTurn item() {
    Item item = ((Player) _trainer).item(_itemID);

    ItemTurn turn = new ItemTurn(this, item, _index);

    return turn;
  }

  public AbstractTurn swap() {
    SwapTurn turn = new SwapTurn(this, _index);

    return turn;
  }

  public AbstractTurn run() {
    RunTurn turn = new RunTurn(this);

    return turn;
  }

  public void takeDamage(int damage) {
    if (_bide)
      _bidedamage += damage;

    leader().takeDamage(damage);

    rollDownEffectDuration();
  }

  public double damageModifier(Move m) {
    double damageModifier = 1.0;

    for (SlotEffect e : _effects)
      damageModifier *= e.damageModifier(m);

    // APPLY WEATHER CHANGES HERE WHEN APPLICABLE

    return damageModifier;
  }

  public void addRival(Slot s) {
    if (_rivalsLists.get(leader()) == null)
      _rivalsLists.put(leader(), new ArrayList<Pokemon>());

    List<Pokemon> rivalsList = _rivalsLists.get(leader());

    if (!rivalsList.contains(s.leader()))
      rivalsList.add(s.leader());

    if (_trainer.type() == TrainerType.PLAYER)
      ((Player) _trainer).pokedex().saw(s.leader().number());
  }

  public void removeRival(Slot s) {
    Pokemon p = s.leader();
    int xpReward = (int) (s.trainer().xpFactor() * (p.level() + 6));

    List<Pokemon> earnList = new ArrayList<Pokemon>();
    List<String> message = new ArrayList<String>();

    for (Pokemon cur : _trainer.party()) {
      List<Pokemon> rivalsList = _rivalsLists.get(cur);

      // TODO : If cur holding xp share, add to earners

      if (rivalsList != null && rivalsList.contains(p)) {
        earnList.add(cur);
        rivalsList.remove(p);
      }
    }

    int xpEach = Math.max(xpReward / earnList.size(), 1);

    message.add(p.name() + " fainted!");
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

  private int _team;
  private Slot _target;
  private boolean _bide;
  private PokemonTrainer _trainer;
  private List<SlotEffect> _effects;
  private int _bidedamage, _index, _itemID;
  private Map<Pokemon, List<Pokemon>> _rivalsLists;
}