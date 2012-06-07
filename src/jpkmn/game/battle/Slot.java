package jpkmn.game.battle;

import jpkmn.game.pokemon.Condition.Issue;
import jpkmn.game.pokemon.Pokemon;
import jpkmn.game.pokemon.move.Move;
import jpkmn.game.pokemon.move.MoveStyle;
import jpkmn.game.pokemon.storage.Party;

public class Slot {
  private int a;
  
  public Slot(Party p) {
    _party = p;
    _field = new Field(this);
    _bide = false;
    _human = false; // TODO getLeader().isAskable();
  }

  public Pokemon getLeader() {
    return getParty().get(0);
  }

  public Party getParty() {
    return _party;
  }

  public boolean chooseMove() {
    Pokemon leader = getLeader();
    int position;

    // Must keep using the same move
    if (leader.condition.contains(Issue.WAIT)) return true;

    if (_human) {
      position = 0; // TODO Ask for position
      if (position == -1) return false;
    }
    else {
      position = (int) (Math.random() * leader.moves.amount());
    }

    move = leader.moves.get(position);
    return true;
  }

  public boolean chooseItem() {
    // TODO

    return true;
  }

  public boolean chooseSwapPosition() {
    // TODO

    return true;
  }

  public boolean chooseAttackTarget() {
    // TODO

    return true;
  }
  
  public boolean chooseItemTarget() {
    // TODO
    
    return true;
  }

  public Turn attack() {
    Pokemon leader = getLeader();
    Turn turn = new Turn(move, _target);

    if (_bide) {
      turn.setAbsoluteDamage(_bidedamage);
      _bide = false;
      _bidedamage = 0;
    }

    // Don't perform any if they didn't choose this move
    if (!leader.condition.contains(Issue.WAIT)) {
      // 1 Measure if the user can attack
      if (!leader.canAttack()) turn.nullify(leader.condition.toString());

      // 2 Reduce and measure PP
      if (!move.use()) turn.nullify("There is not enough PP!");

      // 3 Measure accuracy
      if (!move.hits(_target._party.getLeader())) {

        // Move # 60 (Hi Jump Kick) and Move # 69 (Jump Kick) hurt on miss
        if (move.number() == 60 || move.number() == 69) {
          int damage = Battle.computeDamage(move, _target._party.getLeader());
          damage /= 8;
          takeDamageAbsolute(damage);
        }

        turn.nullify("It missed.");
      }
    }

    if (move.style() == MoveStyle.DELAY) {
      if (leader.condition.contains(Issue.WAIT)) {
        leader.condition.remove(Issue.WAIT); // take away 1 wait

        if (leader.condition.contains(Issue.WAIT) // still waiting?
            || move.style().attackBeforeDelay()) // or already attacked
          turn.nullify("Resting this turn.");
      }
      else {
        for (int i = 0; i < move.style().delay(); ++i)
          leader.condition.addIssue(Issue.WAIT); // add all the waits

        if (move.style().attackAfterDelay()) turn.nullify("Resting this turn");
      }
    }
    else if (move.style() == MoveStyle.MISC) { // Misc
      turn.nullify("This doesn't work yet. Sorry about that!");
    }

    return turn;
  }

  public Turn item() {
    // TODO
    return null;
  }

  public Turn swap() {
    // TODO
    return null;
  }

  public boolean run() {
    // TODO
    
    return true;
  }
  
  public void takeDamage(Turn turn) {
    _field.effect(turn);
    takeDamageAbsolute(turn.damage());
  }

  public void takeDamageAbsolute(int damage) {
    if (_bide) _bidedamage += damage;

    getLeader().takeDamage(damage);

    _field.rollDownDuration();
  }

  private Move move;
  private Party _party;
  private Field _field;
  private Slot _target;
  private int _bidedamage;
  private boolean _human, _bide;
}
