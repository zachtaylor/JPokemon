package org.jpokemon.battle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.jpokemon.battle.slot.Slot;
import org.jpokemon.battle.turn.Round;
import org.jpokemon.battle.turn.Turn;
import org.jpokemon.battle.turn.TurnFactory;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.Type;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;
import org.jpokemon.server.PlayerManager;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.jpokemon.trainer.Trainer;
import org.json.JSONException;
import org.json.JSONObject;
import org.zachtaylor.myna.Myna;

public class Battle implements Iterable<Slot> {
  public static double stab = 1.5;
  public static double typeadvantage = 2.0;
  public static double typedisadvantage = .5;

  static {
    Myna.configure(Battle.class, "org.jpokemon.battle");
  }

  private Round _round = new Round(this);
  private Map<String, Slot> _slots = new HashMap<String, Slot>();

  public Battle(PokemonTrainer... trainers) {
    PokemonTrainer trainer;

    for (int i = 0; i < trainers.length; i++) {
      trainer = trainers[i];
      addTrainer(trainer, i);
    }

    doTrainerAttacks();
  }

  public void addTrainer(PokemonTrainer trainer, int team) {
    if (contains(trainer)) throw new IllegalArgumentException("Duplicate trainer: " + trainer);

    _slots.put(trainer.id(), new Slot(trainer, team));
  }

  public int getPlayerCount() {
    return _slots.size();
  }

  public Slot getSlot(String id) {
    return _slots.get(id);
  }

  public boolean contains(PokemonTrainer trainer) {
    return _slots.get(trainer.id()) != null;
  }

  public void remove(PokemonTrainer trainer) {
    Slot slot = _slots.remove(trainer.id());

    if (slot.party().awake() == 0 && slot.trainer() instanceof Trainer) {
      addTrainerToPlayerHistory(slot.trainer().id());
    }

    verifyTeamCount();
  }

  public void addTurn(Turn turn) {
    _round.setTurn(turn.slot(), turn);

    if (_round.turnCount() == _slots.size()) {
      executeRound();
    }
  }

  @Override
  public Iterator<Slot> iterator() {
    return _slots.values().iterator();
  }

  public Turn getTurn(PokemonTrainer trainer) {
    return _round.getTurn(getSlot(trainer.id()));
  }

  private void executeRound() {
    Round current = _round;
    _round = new Round(this);

    current.execute();

    doTrainerAttacks();
  }

  private void doTrainerAttacks() {
    for (Slot slot : _slots.values()) {
      if (slot.trainer() instanceof Player) continue;

      Slot randomSlot;
      do {
        Slot[] allSlots = _slots.values().toArray(new Slot[_slots.values().size()]);
        randomSlot = allSlots[(int) (Math.random() * allSlots.length)];
      } while (slot.equals(randomSlot));

      int randomMove = (int) ((Math.random()) * slot.leader().moveCount());

      JSONObject json = new JSONObject();
      try {
        json.put("turn", "ATTACK");
        json.put("trainer", slot.trainer().id());
        json.put("target", randomSlot.trainer().id());
        json.put("move", randomMove);
      }
      catch (JSONException e) {
        e.printStackTrace();
        return;
      }

      addTurn(TurnFactory.create(json, slot, randomSlot));
    }
  }

  private void verifyTeamCount() {
    int curTeam = -1;
    boolean onlyOneTeamLeft = true;

    for (Slot s : this) {
      if (curTeam == -1)
        curTeam = s.team();
      else if (curTeam != s.team()) {
        onlyOneTeamLeft = false;
        break;
      }
    }

    if (onlyOneTeamLeft) {
      while (!_slots.isEmpty()) {
        String slotKey = (String) _slots.keySet().toArray()[0];
        Slot slot = _slots.remove(slotKey);

        if (slot.trainer() instanceof Player) {
          Player p = (Player) slot.trainer();
          PlayerManager.popActivity(p, (BattleActivity) PlayerManager.getActivity(p));
        }
      }
    }
  }

  private void addTrainerToPlayerHistory(String id) {
    Player p;

    for (Slot s : this) {
      if (!(s.trainer() instanceof Player)) continue;

      p = (Player) s.trainer();

      // If the trainer is already recorded, IllegalArgumentException will fire
      p.record().putTrainer(id);
    }
  }

  public static int computeDamage(Pokemon user, Move move, Pokemon victim) {
    //@preformat
    double damage = 1.0,
           L = user.level(),
           A = 1.0,
           P = move.power(),
           D = 0,
           E = computeEffectiveness(move, user, victim), 
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

    damage = (((2.0 * L / 5.0 + 2.0) * A * P / D) / 50.0 + 2.0) * E * R * reps;

    if (damage < 1 && E != 0) damage = 1;

    return (int) damage;
  }

  /**
   * Calculates effectiveness modifications for a Move from a user to a victim. Includes Same-Type-Attack-Bonus for user and {@link Type} modifications between
   * the move and victim.
   * 
   * @param move Move to calculate with
   * @param user Pokemon using the move
   * @param victim Pokemon getting hit by the move
   * @return A modifier for the power of the move with respect to Types
   */
  private static double computeEffectiveness(Move move, Pokemon user, Pokemon victim) {
    double answer = 1.0;

    if (move.type() == user.type1() || move.type() == user.type2()) {
      answer *= stab;
    }
    if (victim.type1() != null) {
      switch (move.type().effectiveness(victim.type1())) {
      case SUPER:
        answer *= typeadvantage;
      case NORMAL:
        answer *= 1;
      case NOT_VERY:
        answer *= typedisadvantage;
      case IMMUNE:
        answer *= 0;
      }
    }
    if (victim.type2() != null) {
      switch (move.type().effectiveness(victim.type1())) {
      case SUPER:
        answer *= typeadvantage;
      case NORMAL:
        answer *= 1;
      case NOT_VERY:
        answer *= typedisadvantage;
      case IMMUNE:
        answer *= 0;
      }
    }

    return answer;
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
}