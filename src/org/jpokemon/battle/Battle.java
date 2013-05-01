package org.jpokemon.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.battle.slot.Slot;
import org.jpokemon.battle.turn.Round;
import org.jpokemon.battle.turn.Turn;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.jpokemon.trainer.TrainerState;
import org.jpokemon.trainer.WildTrainer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Battle implements Iterable<Slot> {
  public Battle() {
    _round = new Round(this);
    _slots = new HashMap<Integer, Slot>();
    _haveSelectedTurn = new ArrayList<Slot>();
  }

  public void addTrainer(PokemonTrainer trainer, int team) {
    if (contains(trainer))
      throw new IllegalArgumentException("Duplicate trainer: " + trainer);

    _slots.put(trainer.id(), new Slot(trainer, team));
  }

  public void remove(Slot slot) {
    PokemonTrainer trainer = slot.trainer();
    _slots.remove(trainer.id());
    BattleRegistry.remove(trainer);
    trainer.state(TrainerState.OVERWORLD);

    if (slot.party().awake() == 0) {
      rewardFrom(slot);

      if (slot.trainer() instanceof Player) {
        // TODO : Punish player
      }
      else if (!(slot.trainer() instanceof WildTrainer)) {
        addTrainerToPlayerHistory(slot.trainer().id());
      }
    }

    verifyTeamCount();
  }

  public void rewardFrom(Slot s) {
    Reward reward = new Reward(s);

    for (Slot slot : this) {
      if (slot == s)
        continue;

      reward.apply(slot);
    }
  }

  public boolean contains(PokemonTrainer trainer) {
    return _slots.get(trainer.id()) != null;
  }

  public void addTurn(Turn turn) {
    if (_haveSelectedTurn.contains(turn.slot()))
      return;

    _haveSelectedTurn.add(turn.slot());
    _round.add(turn);

    if (_round.size() == _slots.size())
      executeRound();
  }

  public void start() {
    for (Slot s : this) {
      s.trainer().state(TrainerState.BATTLE);
    }

    doTrainerAttacks();
  }

  public void createTurn(JSONObject turn) {
    int trainerID, targetID;

    try {
      trainerID = turn.getInt("id");
      targetID = turn.getInt("target");
    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    Slot slot = _slots.get(trainerID);
    Slot target = _slots.get(targetID);

    addTurn(slot.turn(turn, target));
  }

  public JSONObject toJSON(PokemonTrainer perspective) {
    Slot slot = _slots.get(perspective.id());

    JSONObject data = new JSONObject();
    Map<Integer, JSONArray> teams = new HashMap<Integer, JSONArray>();

    try {
      data.put("player", slot.toJSON());

      for (Slot cur : this) {
        if (cur == slot)
          continue;
        if (teams.get(cur.team()) == null)
          teams.put(cur.team(), new JSONArray());

        teams.get(cur.team()).put(cur.toJSON());
      }

      JSONArray enemies = new JSONArray();
      for (Map.Entry<Integer, JSONArray> team : teams.entrySet()) {
        if (team.getKey() == slot.team())
          data.put("allies", team.getValue());
        else
          enemies.put(team.getValue());
      }
      data.put("enemies", enemies);

    } catch (JSONException e) {
      e.printStackTrace();
      data = null;
    }

    return data;
  }

  @Override
  public Iterator<Slot> iterator() {
    return _slots.values().iterator();
  }

  private void executeRound() {
    Round current = _round;
    _round = new Round(this);
    _haveSelectedTurn = new ArrayList<Slot>();

    current.execute();
    doTrainerAttacks();
  }

  private void doTrainerAttacks() {
    for (Slot slot : _slots.values()) {
      if (slot.trainer() instanceof Player)
        continue;

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
      } catch (JSONException e) {
        e.printStackTrace();
        return;
      }

      addTurn(slot.turn(json, randomSlot));
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
      for (Slot slot : this) {
        PokemonTrainer trainer = slot.trainer();
        BattleRegistry.remove(trainer);
        trainer.state(TrainerState.OVERWORLD);
      }
    }
  }

  private void addTrainerToPlayerHistory(int id) {
    Player p;

    for (Slot s : this) {
      if (!(s.trainer() instanceof Player))
        continue;

      p = (Player) s.trainer();

      // If the player has the record, IllegalArgumentException will fire
      if (!p.record().getTrainer(id) || !JPokemonConstants.ALLOW_REPEAT_TRAINER_BATTLES)
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

  private Round _round;
  private Map<Integer, Slot> _slots;
  private List<Slot> _haveSelectedTurn;
}