package battle;

import gui.*;
import item.*;
import jpkmn.Driver;
import jpkmn.Player;
import pokemon.*;
import pokemon.move.*;
import pokemon.Status.Effect;

/**
 * A battle. Responds to to events from the BattleWindow in general terms, such
 * as attack or run. Works with Slot to correctly handle all battle events.
 * This class is a more abstracted view of a battle.
 */
public class Battle {
  public BattleView window;
  public Slot user, enemy;

  boolean wild = true, gym = false;

  private Player player;
  private Slot fast;
  private Slot slow;
  private Party participants;

  public Battle(Player p, Party e) throws BattleEndException {
    player = p;
    user = new Slot(player.party, true);
    enemy = new Slot(e, false);
    user.enemy = enemy;
    enemy.enemy = user;
    participants = new Party(user.leader);
    set();
  }

  public Battle(Player p, Pokemon pkmn) throws BattleEndException {
    this(p, new Party(pkmn));
  }

  /**
   * Sets up crucial aspects of the battle. Must be called after participants
   * are set up or change, after speed changes, etc.
   */
  public void set() throws BattleEndException {

    // Verify that everyone is awake
    checkAwake();

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

    if (!participants.contains(user.leader)) {
      Driver.log(Battle.class, user.leader.name + " added to participants.");
      participants.add(user.leader);
    }

    if (window != null)
      window.reload();
  }

  /**
   * Chooses moves and attacks.
   * 
   * @throws BattleEndException to end the battle
   */
  public void fight() throws BattleEndException {
    Driver.log(Battle.class, "Fight selected");

    // Choose moves. If cancelled, back out of the fight choice
    if (!user.chooseMove()) {
      Driver.log(Battle.class, "No move selected. Fight cancelled.");
      return;
    }
    enemy.chooseMove();

    // Quick Attack fixes
    if (user.getMove().number == 100) {
      Driver.log(Battle.class, "User set to fast because of Quick Attack");
      fast = user;
      slow = enemy;
    }
    // Enemy still gets speed advantage
    if (enemy.getMove().number == 100) {
      Driver.log(Battle.class, "Enemy set to fast because of Quick Attack");
      fast = enemy;
      slow = user;
    }

    // Attacks
    Driver.log(Battle.class, fast.leader.name + " is fastest this round.");
    fast.attack();
    checkAwake();
    slow.attack();
    checkAwake();

    applyEffects();

    if (user.leader.status.contains(Effect.WAIT)) {
      Driver.log(Battle.class, "Leader (" + user.leader.name
          + ") contains wait effect in " + user.leader.status.toString()
          + ". Calling fight recursively.");
      fight();
    }
  }

  /**
   * Apply status effects at the end of the turn
   * 
   * @throws BattleEndException If applying effects ended the battle
   */
  private void applyEffects() throws BattleEndException {
    user.applyEffects();
    enemy.applyEffects();
    checkAwake();
    set();
  }

  public void addEnemy(Pokemon p) throws BattleEndException {
    if (!enemy.party.add(p))
      Driver.crash(Battle.class,
          "More than 6 Pokemon attempted added to enemy party : " + p.name);
    Driver.log(Battle.class, p.name + " added to enemy party");
    set();
  }

  public void item() throws BattleEndException {
    Driver.log(Battle.class, "Item selected");
    // Get item to use
    Item i = Tools.item(player.bag);

    // If they cancelled, exit
    if (i == null) {
      Driver.log(Battle.class, "Item selection cancelled.");
      return;
    }
    Driver.log(Battle.class, "Item selected = " + i.name());

    // Items used on self : Potion, Machine, XStat, Stone
    if (i.target == Target.SELF) {

      // Don't allow Stones or Machines in battle
      if (i instanceof Stone || i instanceof Machine) {
        Driver.log(Battle.class, i.getClass().toString()
            + "s cannot be used in battle.");
        Tools.notify(Tools.findImage(i), i.name(), i.getClass().toString()
            + "s cannot be used in battle!");
        return;
      }

      // Select target pokemon
      Tools.notify(Tools.findImage(i.name()), i.name(),
          "Who do you want to use this on?");
      int target = Tools.selectFromParty("Who do you want to use this on?",
          user.party);

      // If they cancelled, exit
      if (target == -1) {
        Driver.log(Battle.class, "Use cancelled for item = " + i.name());
        return;
      }

      // Effect the target
      i.effect(user.party.pkmn[target]);

      // Enemy attacks
      enemy.attack();
      checkAwake();
      applyEffects();
    }
    // Items used on enemy : Ball
    else {

      // Can only catch wild pokemon!
      if (!wild) {
        Tools.notify(enemy.leader, "PLAY FAIR!",
            "You can only catch wild Pokemon!");
        return;
      }

      // Success
      if (i.effect(enemy.leader)) {
        Tools.notify(enemy.leader, "SUCCESS", enemy.leader.name
            + " was caught!");
        if (!user.party.add(enemy.leader))
          player.box.add(enemy.leader);
        enemy.party.remove(enemy.leader);

        // a wild party? i like the idea. I'll support it
        // Essentially the same as checkAwake, but you don't win()
        if (enemy.party.countAwake() == 0)
          throw new BattleEndException(0);
        else if (!enemy.doSwap()) {
          Driver.crash(Battle.class, "party.countAwake reported != 0, but "
              + "Slot.doSwap() didn't perform swap for AI.");
        }
      }
      // Failure
      else {
        Tools.notify(enemy.leader, "FAILURE", "Y U NO GET CAUGHT!?");

        // Enemy attacks
        enemy.attack();
        checkAwake();
        applyEffects();
      }
    }
  }

  /**
   * Asks the user about swapping Pokemon. If they swap, then the enemy attacks
   * 
   * @throws BattleEndException If the battle ends
   */
  public void swap() throws BattleEndException {
    Driver.log(Battle.class, "Swap selected.");
    if (user.doSwap()) {
      enemy.attack();
      checkAwake();
      applyEffects();
    }
  }

  /**
   * Calculates if the user escapes on a run attempt. If not, then the enemy
   * attacks.
   * 
   * @throws BattleEndException If the battle ends
   */
  public void run() throws BattleEndException {
    Driver.log(Battle.class, "Run selected");
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
      applyEffects();
    }
  }

  /**
   * Computes damage through formula
   * 
   * @param move Move used
   * @param user User of the move
   * @param victim Victim of the move
   * @return Integer value of the move
   */
  public static int computeDamage(Move move, Pokemon user, Pokemon victim) {
    double damage = 1.0, L = user.level, A = 1.0, P = move.power, D = 0, STAB = move
        .STAB(), E = move.effectiveness(victim), R = Math.random() * .15 + .85;
    if (move.style == MoveStyle.SPECIAL) {
      A = user.specattack.cur;
      D = victim.specdefense.cur;
    }
    else if (move.style == MoveStyle.PHYSICAL) {
      A = user.attack.cur;
      D = victim.defense.cur;
    }
    else if (move.style == MoveStyle.OHKO) {
      A = 10000000;
      D = 1;
    }
    else if (move.style == MoveStyle.DELAY) {
      // TODO
      // Ugh. Figure out whether spec or reg attack/defense
      // Based on the type of the attack.
    }
    damage = (((2.0 * L / 5.0 + 2.0) * A * P / D) / 50.0 + 2.0) * STAB * E * R;
    if (damage < 1 && move.effectiveness(victim) != 0)
      damage = 1;
    return (int) damage;
  }

  private boolean checkAwake() throws BattleEndException {
    if (!user.leader.awake) {
      Driver.log(Battle.class, user.leader.name + " is unconscious.");
      if (user.party.countAwake() == 0)
        lose();
      else
        user.doSwap();
    }

    if (!enemy.leader.awake) {
      Driver.log(Battle.class, enemy.leader.name + " is defeated.");
      payxp();
      if (enemy.party.countAwake() == 0)
        win();
      else if (!enemy.doSwap()) {
        Driver.crash(Battle.class, "countAwake reported != 0, but "
            + "Slot.doSwap() didn't perform swap for AI.");
      }
    }

    return true;
  }

  private void payxp() {
    int xpwon = enemy.leader.xpGiven();
    if (!wild)
      xpwon *= 1.5;

    Driver.log(Battle.class, xpwon + "xp per " + participants.countAwake()
        + " participants = " + (xpwon /= participants.countAwake()));

    for (Pokemon p : participants.pkmn) {
      if (p != null && p.awake) {
        Driver.log(Battle.class, "xp payed to : " + p.name
            + (p.awake ? " awake" : " asleep"));
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
      if (p == null)
        continue;
      p.status.remove(Effect.SEEDED);
      p.status.remove(Effect.SEEDUSR);
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
}