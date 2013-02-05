package org.jpokemon.battle.turn;

import jpkmn.game.item.Item;

import org.jpokemon.battle.slot.Slot;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class TurnFactory {
  public TurnFactory(Slot s) {
    _slot = s;
  }

  public Turn create(JSONObject json, Slot target) {
    String turn;

    try {
      turn = json.getString("turn");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    if (turn.equals("ATTACK"))
      return doCreateAttack(json, target);
    else if (turn.equals("SWAP"))
      return doCreateSwap(json, target);
    else if (turn.equals("ITEM"))
      return doCreateItem(json, target);
    else if (turn.equals("RUN"))
      return doCreateRun(json, target);

    return null;
  }

  private Turn doCreateAttack(JSONObject json, Slot target) {
    int moveIndex;

    try {
      moveIndex = json.getInt("move");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    Move move = _slot.leader().move(moveIndex);

    return new AttackTurn(_slot, target, move);
  }

  private Turn doCreateSwap(JSONObject json, Slot target) {
    int swapIndex;

    try {
      swapIndex = json.getInt("swap");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return new SwapTurn(_slot, target, swapIndex);
  }

  private Turn doCreateItem(JSONObject json, Slot target) {
    int targetIndex, itemID;

    try {
      itemID = json.getInt("item");
      targetIndex = json.getInt("target_index");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    Item item = ((Player) _slot.trainer()).item(itemID);

    return new ItemTurn(_slot, target, item, targetIndex);
  }

  private Turn doCreateRun(JSONObject json, Slot target) {
    return new RunTurn(_slot, target);
  }

  private Slot _slot;
}