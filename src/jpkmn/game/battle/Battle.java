package jpkmn.game.battle;

import java.util.ArrayList;
import java.util.List;

import jpkmn.exceptions.BattleEndException;
import jpkmn.game.Player;
import jpkmn.game.item.*;
import jpkmn.game.pokemon.*;
import jpkmn.game.pokemon.move.*;
import jpkmn.game.pokemon.storage.Party;

/**
 * A battle. Responds to to events from the BattleWindow in general terms, such
 * as attack or run. Works with Slot to correctly handle all battle events.
 * This class is a more abstracted view of a battle.
 */
public class Battle {
  boolean wild = true, gym = false;

  public Battle() {
    _turns = new ArrayList<Turn>();
  }
  
  public void addPlayer(Player p) {
    
  }

  /**
   * Sets up crucial aspects of the battle. Must be called after participants
   * are set up or change, after speed changes, etc.
   */
  public void set() throws BattleEndException {
    /** TODO
    // tie goes to the enemy for speed
    if (user.leader.speed.cur > enemy.leader.speed.cur) {
      fast = user;
      slow = enemy;
    }
    else {
      fast = enemy;
      slow = user;
    }

    user.set();
    enemy.set();

    user.applyEffects();
    enemy.applyEffects();

    // Verify that everyone is awake
    checkAwake();

    if (!participants.contains(user.leader)) {
      participants.add(user.leader);
    }

    if (window != null) window.reload();

    player.dex.saw(enemy.leader);
    **/
  }

  /**
   * Chooses moves and attacks.
   * 
   * @throws BattleEndException to end the battle
   */
  public void fight() throws BattleEndException {
    /** TODO
    // Choose moves. If cancelled, back out of the fight choice
    if (!user.chooseMove()) {
      return;
    }
    enemy.chooseMove();

    // Quick Attack fixes
    if (user.getMove().number == 100) {
      fast = user;
      slow = enemy;
    }
    // Enemy still gets speed advantage
    if (enemy.getMove().number == 100) {
      fast = enemy;
      slow = user;
    }

    // Attacks
    fast.attack();
    applyEffects(fast);
    slow.attack();
    applyEffects(slow);

    set();

    if (user.leader.condition.contains(Issue.WAIT)) {
      fight();
    }
    **/
  }

  public void item(Slot slot) throws BattleEndException {
    Pokemon user = slot.getLeader();
    Item i = null; // TODO Ask for item

    if (i == null || i.getQuantity() == 0) return;

    // Items used on self : Potion, Machine, XStat, Stone
    if (i instanceof Potion) {
      int target = -1; // TODO Ask who they want to use it on

      // If they cancelled, exit
      if (target == -1) return;

      // Effect the target
      i.effect(user.get(target));

      // Enemy attacks
      enemy.chooseMove();
      enemy.attack();
      checkAwake();
      applyEffects(enemy);
    }
    else if (i instanceof Ball) {
      if (!wild) return;

      if (i.effect(enemy.leader)) {
        if (!user.party.add(enemy.leader)) player.box.add(enemy.leader);
        player.dex.caught(enemy.leader);
        enemy.party.remove(enemy.leader);

        // a wild party? i like the idea. I'll support it
        // Essentially the same as checkAwake, but you don't win()
        if (enemy.party.countAwake() == 0)
          throw new BattleEndException(0);
        else
          enemy.doSwap();
      }
      // Failure
      else {
        // Enemy attacks
        enemy.chooseMove();
        enemy.attack();
        checkAwake();
        applyEffects(enemy);
      }
    }
  }

  /**
   * Asks the user about swapping Pokemon. If they swap, then the enemy attacks
   * 
   * @throws BattleEndException If the battle ends
   */
  public void swap() throws BattleEndException {
    /** TODO
    if (user.doSwap()) {
      enemy.chooseMove();
      enemy.attack();
      applyEffects(enemy);
    }
    set();
    **/
  }

  /**
   * Calculates if the user escapes on a run attempt. If not, then the enemy
   * attacks.
   * 
   * @throws BattleEndException If the battle ends
   */
  public void run() throws BattleEndException {
    /** TODO
    double chance = user.leader.speed.cur * 32;
    chance /= enemy.leader.speed.cur;
    chance += 30;
    if (chance > 255 || chance > Math.random() * 255) {
      Tools.notify(user.leader, "SUCCESS", "Ran away successfully!");
      clear();
      throw new BattleEndException(0);
    }
    else {
      Tools.notify(user.leader, "FAIL", "You didn't escape...");
      enemy.chooseMove();
      enemy.attack();
      checkAwake();
      applyEffects(enemy);
    }
    **/
  }

  /**
   * Computes damage through formula
   * 
   * @param move Move used
   * @param user User of the move
   * @param victim Victim of the move
   * @return Integer value of the move
   */
  public static int computeDamage(Move move, Pokemon victim) {
    Pokemon user = move.pkmn;

    double damage = 1.0, L = user.level(), A = 1.0, P = move.power(), D = 0, STAB = move
        .STAB(), E = move.effectiveness(victim), R = Math.random() * .15 + .85;

    if (move.style() == MoveStyle.SPECIAL) {
      A = user.stats.stk.cur();
      D = victim.stats.sdf.cur();
    }
    else if (move.style() == MoveStyle.PHYSICAL) {
      A = user.stats.atk.cur();
      D = victim.stats.def.cur();
    }
    else if (move.style() == MoveStyle.OHKO) {
      A = 10000000;
      D = 1;
    }
    else if (move.style() == MoveStyle.DELAY) {
      if (user.stats.atk.cur() > user.stats.stk.cur())
        A = user.stats.atk.cur();
      else
        A = user.stats.stk.cur();
      if (victim.stats.def.cur() > victim.stats.sdf.cur())
        D = victim.stats.def.cur();
      else
        D = victim.stats.sdf.cur();
    }

    damage = (((2.0 * L / 5.0 + 2.0) * A * P / D) / 50.0 + 2.0) * STAB * E * R;

    if (damage < 1 && E != 0) damage = 1;

    return (int) damage;
  }

  private boolean checkAwake() throws BattleEndException {
    if (!user.leader.awake) {
      if (user.party.countAwake() == 0)
        lose();
      else
        user.doSwap();
    }

    if (!enemy.leader.awake) {
      payxp();
      if (enemy.party.countAwake() == 0)
        win();
      else if (!enemy.doSwap()) {
        // Driver.crash(Battle.class, "countAwake reported != 0, but "
        // + "Slot.doSwap() didn't perform swap for AI.");
      }
    }

    return true;
  }

  private void payxp() {
    int xpwon = enemy.leader.xpGiven();
    if (!wild) xpwon *= 1.5;

    for (Pokemon p : participants.pkmn) {
      if (p != null && p.awake) {

        p.gainExperience(xpwon);
      }
    }

    participants = new Party();
  }

  private void win() throws BattleEndException {
    if (wild && !gym) {
      player.changeCash(23 * enemy.leader.level);
    }
    if (!gym && !wild) {

    }
    else if (!wild) {
      player.changeCash(31 * enemy.leader.level);
      player.getBadge();
    }
    else {

    }

    clear();
    throw new BattleEndException(101);
  }

  private void lose() throws BattleEndException {
    player.cash /= 2;
    clear();
    throw new BattleEndException(110);
  }

  private void clear() {
    for (Pokemon p : user.party.pkmn) {
      if (p == null) continue;
      p.condition.remove(Issue.SEEDED);
      p.condition.remove(Issue.SEEDUSR);
    }
  }

  /**
   * Calculates the confused damage a Pokemon does to itself. This method
   * exists here to unify all damage calculations.
   * 
   * @param p Pokemon to calculate for
   * @return Damage done
   */
  public static int confusedDamage(Pokemon p) {
    // For more info, see computeDamage
    double L = p.level, A = p.attack.cur, P = 40, D = p.defense.cur, STAB = 1, E = 1, R = 1.00;
    return (int) ((((2.0 * L / 5.0 + 2.0) * A * P / D) / 50.0 + 2.0) * STAB * E * R);
  }

  private Map
  private List<Turn> _turns;
}