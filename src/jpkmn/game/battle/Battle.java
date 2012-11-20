package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jpkmn.game.battle.turn.AbstractTurn;
import jpkmn.game.item.Item;
import jpkmn.game.player.PokemonTrainer;
import jpkmn.game.player.TrainerType;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;

public class Battle implements Iterable<Slot> {
  public Battle() {
    _round = new Round(this);
    _slots = new HashMap<Integer, Slot>();
    _haveSelectedTurn = new ArrayList<Slot>();
  }

  public void add(PokemonTrainer trainer) {
    Slot slot = new Slot(this, trainer, _slots.size());

    _slots.put(_slots.size(), slot);
  }

  public void remove(Slot slot) {
    if (slot.party().size() != 0 && slot.party().awake() == 0) {
      if (slot.trainer().type() == TrainerType.PLAYER) {
        // TODO : Punish player
      }
      else if (slot.trainer().type() == TrainerType.GYM) {
        // TODO : Reward from gym
      }
      else if (slot.trainer().type() == TrainerType.TRAINER) {
        // TODO : Prevent players from fighting this trainer again
      }
    }

    if (_slots.size() == 1) {
      remove((Slot) _slots.values().toArray()[0]);
      BattleRegistry.remove(this);
    }
  }

  public Slot get(int slotID) {
    return _slots.get(slotID);
  }

  public void add(AbstractTurn turn) {
    if (_haveSelectedTurn.contains(turn.getUserSlot()))
      return;

    _haveSelectedTurn.add(turn.getUserSlot());
    _round.add(turn);

    if (_round.size() == _slots.size())
      doRound();
  }

  public void start() {
    doTrainerAttacks();
  }

  public void fight(int slotID, int enemySlotID, int moveIndex) {
    Slot slot = _slots.get(slotID);
    Slot enemySlot = _slots.get(enemySlotID);

    slot.target(enemySlot);
    slot.moveIndex(moveIndex);

    add(slot.attack());
  }

  public void item(int slotID, int slotIndex, int itemID) {
    Slot slot = _slots.get(slotID);
    Target itemTarget = new Item(itemID).target();

    int itemTargetIndex;
    Slot targetSlot;
    if (itemTarget == Target.SELF) {
      itemTargetIndex = slotIndex;
      targetSlot = slot;
    }
    else {
      itemTargetIndex = 0;
      targetSlot = get(slotIndex);
    }

    slot.target(targetSlot);
    slot.itemID(itemID);
    slot.itemIndex(itemTargetIndex);

    add(slot.item());
  }

  public void swap(int slotID, int slotIndex) {
    Slot slot = _slots.get(slotID);

    slot.swapIndex(slotIndex);

    add(slot.swap());
  }

  public void run(int slotID) {
    Slot slot = _slots.get(slotID);

    add(slot.run());
  }

  @Override
  public Iterator<Slot> iterator() {
    return _slots.values().iterator();
  }

  private void doRound() {
    Round current = _round;
    _round = new Round(this);
    _haveSelectedTurn = new ArrayList<Slot>();

    current.play();
    doTrainerAttacks();
  }

  private void doTrainerAttacks() {
    for (Slot slot : _slots.values()) {
      if (slot.trainer().type() == TrainerType.PLAYER)
        continue;

      int randomSlot;
      do {
        randomSlot = (int) (Math.random() * _slots.entrySet().size());
      } while (get(randomSlot).equals(slot));

      int randomMove = (int) (Math.random()) * slot.leader().moves.count();

      fight(slot.id(), randomSlot, randomMove);
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
    else if (move.style() == MoveStyle.DELAYNEXT
        || move.style() == MoveStyle.DELAYBEFORE) {
      A = Math.max(user.attack(), user.specattack());
      D = Math.max(victim.defense(), victim.specdefense());
    }

    damage = (((2.0 * L / 5.0 + 2.0) * A * P / D) / 50.0 + 2.0) * STAB * E * R
        * reps;

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

  private Round _round;
  private Map<Integer, Slot> _slots;
  List<Slot> _haveSelectedTurn;
}