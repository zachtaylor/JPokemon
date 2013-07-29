package org.jpokemon.battle.turn;

import org.jpokemon.battle.slot.Slot;
import org.jpokemon.item.Item;
import org.jpokemon.pokemon.move.Move;
import org.jpokemon.trainer.Player;
import org.json.JSONException;
import org.json.JSONObject;

public class TurnFactory {
  public static Turn create(JSONObject json, Slot user, Slot target) {
    String turn;

    try {
      turn = json.getString("turn");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    if (turn.equals("ATTACK"))
      return doCreateAttack(json, user, target);
    else if (turn.equals("SWAP"))
      return doCreateSwap(json, user, target);
    else if (turn.equals("ITEM"))
      return doCreateItem(json, user, target);
    else if (turn.equals("RUN"))
      return doCreateRun(json, user, target);

    return null;
  }

  private static Turn doCreateAttack(JSONObject json, Slot user, Slot target) {
    int moveIndex;

    try {
      moveIndex = json.getInt("move");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    Move move = user.leader().move(moveIndex);

    return new AttackTurn(user, target, move);
  }

  private static Turn doCreateSwap(JSONObject json, Slot user, Slot target) {
    int swapIndex;

    try {
      swapIndex = json.getInt("swap");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    return new SwapTurn(user, target, swapIndex);
  }

  private static Turn doCreateItem(JSONObject json, Slot user, Slot target) {
    int targetIndex, itemID;

    try {
      itemID = json.getInt("item");
      targetIndex = json.getInt("target_index");
    } catch (JSONException e) {
      e.printStackTrace();
      return null;
    }

    Item item = ((Player) user.trainer()).item(itemID);

    return new ItemTurn(user, target, item, targetIndex);
  }

  private static Turn doCreateRun(JSONObject json, Slot user, Slot target) {
    return new RunTurn(user, target);
  }
}