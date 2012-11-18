package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jpkmn.game.battle.turn.AttackTurn;
import jpkmn.game.battle.turn.ItemTurn;
import jpkmn.game.battle.turn.RunTurn;
import jpkmn.game.battle.turn.SwapTurn;
import jpkmn.game.battle.turn.AbstractTurn;
import jpkmn.game.item.Item;
import jpkmn.game.player.Player;
import jpkmn.game.player.PokemonTrainer;
import jpkmn.game.player.TrainerType;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokedex.PokedexStatus;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.storage.PokemonStorageUnit;

public class Slot {
  public Slot(Battle battle, PokemonTrainer trainer, int id) {
    _id = id;
    _battle = battle;
    _trainer = trainer;
    _bide = false;

    _field = new Field(this);
    _rivals = new HashMap<Pokemon, List<Pokemon>>();
  }

  public int id() {
    return _id;
  }

  public Battle battle() {
    return _battle;
  }

  public PokemonTrainer trainer() {
    return _trainer;
  }

  public PokemonStorageUnit party() {
    return _trainer.party();
  }

  public Pokemon leader() {
    return party().get(0);
  }

  public int target() {
    return _targetID;
  }

  public void target(int targetID) {
    _targetID = targetID;
  }

  public void setMoveChoice(int moveIndex) {
    _index = moveIndex;
  }

  public void setItemID(int item) {
    _index = item;
  }

  public void setSwapPosition(int slotIndex) {
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
    Item item = ((Player) _trainer).bag.get(_index);

    ItemTurn turn = new ItemTurn(this, item, _targetID);

    return turn;
  }

  public AbstractTurn swap() {
    SwapTurn turn = new SwapTurn(this, _index);

    return turn;
  }

  public AbstractTurn run(Battle b) {
    RunTurn turn = new RunTurn(this);

    return turn;
  }

  public void takeDamage(int damage) {
    if (_bide)
      _bidedamage += damage;

    leader().takeDamage(damage);

    _field.rollDownDuration();
  }

  public double damageModifier(Move m) {
    return _field.damageModifier(m);
  }

  public int getXPAwarded() {
    double factor = _trainer.type().xpFactor();
    factor *= (Math.random() * .5 + 2);
    return (int) (factor * leader().level());
  }

  public void rival(Pokemon p) {
    if (_rivals.get(leader()) == null)
      _rivals.put(leader(), new ArrayList<Pokemon>());

    List<Pokemon> rivals = _rivals.get(leader());

    if (!rivals.contains(p))
      rivals.add(p);

    if (_trainer.type() == TrainerType.PLAYER)
      ((Player) _trainer).putPokedex(p.number(), PokedexStatus.SAW);
  }

  public void rival(Slot s) {
    if (s.id() == id())
      return;

    Pokemon dead = s.leader();
    int xp = s.getXPAwarded(), count = 0;
    List<String> message = new ArrayList<String>();
    List<Pokemon> rivals, earners = new ArrayList<Pokemon>();

    message.add(dead.name() + " fained!");

    for (Pokemon cur : _trainer.party()) {
      rivals = _rivals.get(cur);

      // If cur holding xp share, add to earners

      if (rivals == null)
        continue;
      if (rivals.contains(dead)) {
        count++;
        earners.add(cur);
        rivals.remove(dead);
      }
      if (rivals.isEmpty())
        _rivals.remove(cur);
    }

    xp = (xp / count) > 0 ? (xp / count) : 1;
    for (Pokemon cur : earners) {
      message.add(cur.name() + " received " + xp + " experience!");
      cur.xp(xp);
    }

    _trainer.notify(message.toArray(new String[message.size()]));
  }

  // Slot
  private int _id;
  private Field _field;
  private Battle _battle;
  private PokemonTrainer _trainer;
  private Map<Pokemon, List<Pokemon>> _rivals;

  // Move
  private boolean _bide;
  private int _bidedamage;

  // Attack: Move index
  // Item: index in party for user
  // Swap: index in party
  private int _index, _targetID;
}