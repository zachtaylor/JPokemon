package org.jpokemon.battle.activity;

import org.jpokemon.activity.Activity;
import org.jpokemon.battle.turn.Turn;

public interface BuildTurnActivity extends Activity {
  public Turn getTurn();
}