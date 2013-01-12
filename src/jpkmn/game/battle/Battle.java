package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jpkmn.game.battle.slot.Slot;
import jpkmn.game.battle.turn.Round;
import jpkmn.game.battle.turn.Turn;
import jpkmn.game.pokemon.Pokemon;

import org.jpokemon.pokemon.move.Move;
import org.jpokemon.pokemon.move.MoveStyle;
import org.jpokemon.trainer.Player;
import org.jpokemon.trainer.PokemonTrainer;
import org.jpokemon.trainer.TrainerType;
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
    trainer.setState("world");

    if (slot.party().size() != 0 && slot.party().awake() == 0) {
      if (slot.trainer().type() == TrainerType.PLAYER) {
        // TODO : Punish player
      }
      else if (slot.trainer().type() == TrainerType.GYM) {
        // TODO : Reward from gym

        // Also add this gym to the list of trainers defeated
        addTrainerToPlayerHistory(slot.trainer().id());
      }
      else if (slot.trainer().type() == TrainerType.TRAINER) {
        addTrainerToPlayerHistory(slot.trainer().id());
      }
    }

    verifyTeamCount();
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
    doTrainerAttacks();
  }

  public void createTurn(JSONObject turn) {
    int trainerID, targetID;

    try {
      trainerID = turn.getInt("trainer");
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
    JSONArray teams = new JSONArray();

    try {
      if (slot != null)
        data.put("user_team", slot.team());

      for (Slot cur : this) {
        try {
          if (teams.get(cur.team()) == JSONObject.NULL)
            teams.put(cur.team(), new JSONArray());
        } catch (JSONException e) {
          teams.put(cur.team(), new JSONArray());
        }

        ((JSONArray) teams.get(cur.team())).put(cur.toJSON());
      }

      data.put("teams", teams);

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
      if (slot.trainer().type() == TrainerType.PLAYER)
        continue;

      Slot[] allSlots;
      Slot randomSlot;
      do {
        allSlots = _slots.values().toArray(new Slot[_slots.values().size()]);
        randomSlot = allSlots[(int) (Math.random() * allSlots.length)];
      } while (slot.equals(randomSlot));

      int randomMove = (int) (Math.random()) * slot.leader().moveCount();

      // TODO : make mock attacks
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
        trainer.setState("world");
      }
    }
  }

  private void addTrainerToPlayerHistory(int id) {
    for (Slot s : this) {
      if (s.trainer().type() == TrainerType.PLAYER) {
        ((Player) s.trainer()).trainers().put(id);
      }
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
  private List<Slot> _haveSelectedTurn;
}