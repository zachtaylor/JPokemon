package jpkmn.exe.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import jpkmn.exceptions.DialogCancelException;
import jpkmn.game.service.ImageFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JPokemonDialog {

  public static int getMoveIndex(Component parent, JSONObject trainer) throws DialogCancelException, JSONException {

    JSONObject leader = trainer.getJSONObject("leader");
    JSONArray moves = leader.getJSONArray("moves");

    String name = leader.getString("name");
    ImageIcon image = ImageFinder.find("pkmn/" + leader.getInt("number"));

    List<String> move_names = new ArrayList<String>();
    for (int i = 0; i < moves.length(); i++) {
      JSONObject move = moves.getJSONObject(i);
      move_names.add(move.getString("name"));
    }

    int answer = JOptionPane.showOptionDialog(parent, "Select a move for "
        + name, "MOVE CHOICE", 0, 0, image, move_names.toArray(), null);

    if (answer == -1)
      throw new DialogCancelException();

    return answer;
  }

  public static int getMoveTarget(Component parent, JSONObject battle, JSONObject trainer, int moveIndex) throws DialogCancelException, JSONException {

    JSONArray allTeams = battle.getJSONArray("teams");
    JSONObject leader = trainer.getJSONObject("leader");

    String move = leader.getJSONArray("moves").getJSONObject(moveIndex)
        .getString("name");
    ImageIcon image = ImageFinder.find("pkmn/" + leader.getInt("number"));

    List<String> slot_names = new ArrayList<String>();
    List<Integer> trainerID_key = new ArrayList<Integer>();

    for (int i = 0; i < allTeams.length(); i++) {
      JSONArray teamData = allTeams.getJSONArray(i);

      for (int j = 0; j < teamData.length(); j++) {
        JSONObject trainerData = teamData.getJSONObject(j);

        if (trainerData.getInt("team") != battle.getInt("user_team")) {
          slot_names.add(trainerData.getJSONObject("leader").getString("name"));
          trainerID_key.add(trainerData.getInt("id"));
        }
      }
    }

    int answer = JOptionPane.showOptionDialog(parent, "Select a target for "
        + move, "MOVE CHOICE", 0, 0, image, slot_names.toArray(), null);

    if (answer == -1)
      throw new DialogCancelException();

    return trainerID_key.get(answer);
  }

  public static int getItemChoice(Component parent, JSONObject trainer) throws DialogCancelException, JSONException {

    JSONArray items = trainer.getJSONArray("bag");

    List<String> itemTypeNames = new ArrayList<String>();
    Map<String, List<String>> itemsByType = new HashMap<String, List<String>>();
    Map<String, Integer> answerKey = new HashMap<String, Integer>();

    for (int i = 0; i < items.length(); i++) {
      JSONObject itemData = items.getJSONObject(i);
      String itemType = itemData.getString("type");
      String itemName = itemData.getString("name");
      int itemID = itemData.getInt("id");
      answerKey.put(itemName, itemID);

      if (!itemsByType.keySet().contains(itemType)) {
        itemsByType.put(itemType, new ArrayList<String>());
        itemTypeNames.add(itemType);
      }
      itemsByType.get(itemType).add(itemName);
    }

    int itemTypeChoice = JOptionPane.showOptionDialog(parent,
        "Select an item to use", "ITEM CHOICE", 0, 0, null,
        itemTypeNames.toArray(), null);

    if (itemTypeChoice == -1)
      throw new DialogCancelException();

    String itemType = itemTypeNames.get(itemTypeChoice);
    List<String> itemsInType = itemsByType.get(itemType);

    int itemChoice = JOptionPane.showOptionDialog(parent,
        "Select an item to use", "ITEM CHOICE", 0, 0, null,
        itemsInType.toArray(), null);

    if (itemChoice == -1)
      throw new DialogCancelException();

    String itemName = itemsInType.get(itemChoice);
    return answerKey.get(itemName);
  }

  public static int getSwapIndex(Component parent, JSONObject trainer)
      throws DialogCancelException, JSONException {

    JSONArray party = trainer.getJSONArray("pokemon");
    JSONObject leader = trainer.getJSONObject("leader");

    ImageIcon image = ImageFinder.find("pkmn/" + leader.getInt("number"));

    List<String> names = new ArrayList<String>();
    for (int i = 1; i < party.length(); i++) {
      JSONObject pokemon = party.getJSONObject(i);
      names.add(pokemon.getString("name"));
    }

    int answer = JOptionPane.showOptionDialog(parent, "Select a Pokemon",
        "SWAP CHOICE", 0, 0, image, names.toArray(), null);

    if (answer <= 0)
      throw new DialogCancelException();

    return answer;
  }
}