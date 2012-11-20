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
    _rivalsLists = new HashMap<Pokemon, List<Pokemon>>();
  }

  public int id() {
    return _id;
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

  public Slot target() {
    return _target;
  }

  public void target(Slot target) {
    _target = target;
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
    Item item = ((Player) _trainer).bag.get(_itemID);

    ItemTurn turn = new ItemTurn(this, item, _index);

    return turn;
  }

  public AbstractTurn swap() {
    SwapTurn turn = new SwapTurn(this, _index);

    return turn;
  }

  public AbstractTurn run() {
    RunTurn turn = new RunTurn(this, _battle);

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

  public void addRival(Pokemon p) {
    if (_rivalsLists.get(leader()) == null)
      _rivalsLists.put(leader(), new ArrayList<Pokemon>());

    List<Pokemon> rivalsList = _rivalsLists.get(leader());

    if (!rivalsList.contains(p))
      rivalsList.add(p);

    if (_trainer.type() == TrainerType.PLAYER)
      ((Player) _trainer).putPokedex(p.number(), PokedexStatus.SAW);
  }

  public void removeRival(Pokemon p) {
    int xpReward = (int) (p.trainer().type().xpFactor() * (p.level() + 6));

    List<Pokemon> rivalsList;
    List<Pokemon> earnList = new ArrayList<Pokemon>();
    List<String> message = new ArrayList<String>();

    for (Pokemon cur : _trainer.party()) {
      rivalsList = _rivalsLists.get(cur);

      // If cur holding xp share, add to earners

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

  private int _id;
  private Field _field;
  private Slot _target;
  private Battle _battle;
  private PokemonTrainer _trainer;
  private Map<Pokemon, List<Pokemon>> _rivalsLists;

  private boolean _bide;
  private int _bidedamage;

  private int _index, _itemID;
}