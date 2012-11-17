package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.storage.Party;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;

public class Battle implements Iterable<Slot> {
  public Battle() {
    _slots = new HashMap<Integer, Slot>();
    _round = new Round(this);
  }

  public int add(SlotType t, Party p) {
    if (ready() || _slots.size() == JPokemonConstants.MAXBATTLESIZE)
      return -1;

    int id = _slots.size();

    _slots.put(id, new Slot(id, t, p));

    return id;
  }

  public void remove(int slotID) {
    Slot slot = _slots.remove(slotID);

    if (_slots.size() == 1) {
      remove((Integer) _slots.keySet().toArray()[0]);
      BattleRegistry.remove(_id);
    }

    slot.party().owner().screen.showWorld();
  }

  public void start(int battleID) {
    _id = battleID;

    for (Slot slot : this) {
      slot.party().owner().screen.showBattle(battleID, slot.id());
    }

    makeMockAttacks();
  }

  public void rewardFrom(int slotID) {
    Slot loser = _slots.get(slotID);

    for (Slot slot : this) {
      if (slot.id() != slotID) {
        slot.rival(loser);
      }
    }

    if (loser.party().countAwake() == 0) {
      if (loser.type() == SlotType.PLAYER) {
        // TODO : Punish player
      }
      else if (loser.type() == SlotType.GYM) {
        // TODO : Reward from gym
      }
      else if (loser.type() == SlotType.TRAINER) {
        // TODO : Prevent players from fighting this trainer again
      }
    }
  }

  public int id() {
    return _id;
  }

  public void id(int battleID) {
    _id = battleID;
  }

  public Slot get(int slotID) {
    return _slots.get(slotID);
  }

  public void fight(int slotID) {
    Slot slot = _slots.get(slotID);

    if (!ready() || slot == null)
      return;

    if (!slot.chooseMove())
      return;
    if (!slot.chooseAttackTarget(getEnemySlotsForSlot(slot)))
      return;

    _round.add(slot.attack());

    if (_round.size() == _slots.size())
      executeRound();
  }

  public void item(int slotID) {
    Slot slot = _slots.get(slotID);

    if (!ready() || slot == null)
      return;

    if (!slot.chooseItem())
      return;
    if (!slot.chooseItemTarget(getEnemySlotsForSlot(slot)))
      return;

    _round.add(slot.item());

    if (_round.size() == _slots.size())
      executeRound();
  }

  public void swap(int slotID) {
    Slot slot = _slots.get(slotID);

    if (!ready() || slot == null)
      return;

    if (!slot.chooseSwapPosition())
      return;

    _round.add(slot.swap());

    if (_round.size() == _slots.size())
      executeRound();
  }

  public void run(int slotID) {
    Slot slot = _slots.get(slotID);

    if (!ready() || slot == null)
      return;

    _round.add(slot.run(this));

    if (_round.size() == _slots.size())
      executeRound();
  }

  private void executeRound() {
    Round current = _round;
    _round = new Round(this);
    current.play();
    executeConditionEffects();

    for (Slot slot : this)
      slot.party().owner().screen.refresh();

    makeMockAttacks();
  }

  private void executeConditionEffects() {
    for (Slot slot : _slots.values()) {
      String[] messages = slot.leader().condition.applyEffects();
      if (messages.length > 0)
        notifyAll(messages);
    }
  }

  private void makeMockAttacks() {
    for (Slot slot : this) {
      if (slot.type() != SlotType.PLAYER)
        fight(slot.id());
    }
  }

  public static int computeDamage(Pokemon user, Move move, Pokemon victim) {
    //@preformat
    double damage = 1.0, 
           L = user.level(), 
           A = 1.0, 
           P = move.power(), 
           D = 0, 
           STAB = move.STAB(user),
           E = move.effectiveness(victim), 
           R = Math.random() * .15 + .85,
           reps = move.reps(); // repetitions
    //@format

    if (move.style() == MoveStyle.SPECIAL) {
      A = user.specattack();
      D = victim.specdefense();
    }
    else if (move.style() == MoveStyle.PHYSICAL) {
      A = user.attack();
      D = victim.defense();
    }
    else if (move.style() == MoveStyle.OHKO) {
      A = 10000000;
      D = 1;
    }
    else if (move.style() == MoveStyle.DELAYNEXT || move.style() == MoveStyle.DELAYBEFORE) {
      A = Math.max(user.attack(), user.specattack());
      D = Math.max(victim.defense(), victim.specdefense());
    }

    damage = (((2.0 * L / 5.0 + 2.0) * A * P / D) / 50.0 + 2.0) * STAB * E * R * reps;

    if (damage < 1 && E != 0)
      damage = 1;

    return (int) damage;
  }

  /**
   * Calculates the confused damage a Pokemon does to itself.
   * 
   * @param p Pokemon to calculate for
   * @return Damage done
   */
  public static int confusedDamage(Pokemon p) {
    // For more info, see computeDamage
    //@preformat
    double L = p.level(), 
           A = p.attack(), 
           P = 40, 
           D = p.defense(), 
           STAB = 1, 
           E = 1, 
           R = 1.00;
    //@format
    
    return (int) ((((2.0 * L / 5.0 + 2.0) * A * P / D) / 50.0 + 2.0) * STAB * E * R);
  }

  @Override
  public Iterator<Slot> iterator() {
    return _slots.values().iterator();
  }

  void notifyAll(String... s) {
    for (Slot slot : _slots.values()) {
      slot.party().owner().screen.notify(s);
    }
  }

  // Later used to implement teams
  private List<Slot> getEnemySlotsForSlot(Slot slot) {
    List<Slot> enemySlots = new ArrayList<Slot>();

    for (Slot s : _slots.values())
      if (s.id() != slot.id())
        enemySlots.add(s);

    return enemySlots;
  }

  private boolean ready() {
    return _id != Integer.MIN_VALUE;
  }

  private Round _round;
  private Map<Integer, Slot> _slots;
  private int _id = Integer.MIN_VALUE;
}