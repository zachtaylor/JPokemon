package jpkmn.exe.gui;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import jpkmn.exceptions.DialogCancelException;
import jpkmn.img.ImageFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JPokemonDialog {
  public static int getMoveIndex(Component parent, JSONObject trainer)
      throws DialogCancelException, JSONException {

    String name = null;
    String[] move_names = null;
    ImageIcon image = null;

    JSONObject data = trainer.getJSONObject("pokemon").getJSONArray("party")
        .getJSONObject(0);

    name = data.getString("name");
    image = ImageFinder.find("pkmn/" + data.getInt("number"));

    JSONArray moves = data.getJSONArray("moves");
    move_names = new String[moves.length()];
    for (int i = 0; i < moves.length(); i++)
      move_names[i] = moves.getJSONObject(i).getString("name");

    int answer = JOptionPane.showOptionDialog(parent, "Select a move for "
        + name, "MOVE CHOICE", 0, 0, image, move_names, null);

    if (answer == -1)
      throw new DialogCancelException();

    return answer;
  }

  public static int getMoveTarget(Component parent, JSONObject battle,
      JSONObject trainer, int moveIndex) throws DialogCancelException,
      JSONException {

    String move = null;
    List<String> slot_names = new ArrayList<String>();
    ImageIcon image = null;

    int slotCounter = 0;
    Map<Integer, Integer> answerKey = new HashMap<Integer, Integer>();

    JSONArray allTeams = battle.getJSONArray("teams");
    JSONArray teamData;
    JSONObject trainerData;

    JSONObject leader = trainer.getJSONObject("pokemon").getJSONArray("party")
        .getJSONObject(0);
    move = leader.getJSONArray("moves").getJSONObject(moveIndex)
        .getString("name");
    image = ImageFinder.find("pkmn/" + leader.getInt("number"));

    for (int i = 0; i < allTeams.length(); i++) {
      teamData = allTeams.getJSONArray(i);

      for (int j = 0; j < teamData.length(); j++) {
        trainerData = teamData.getJSONObject(j);
        if (trainerData.getInt("team") != battle.getInt("user_team")) {
          slot_names.add(trainerData.getJSONObject("pokemon")
              .getJSONArray("party").getJSONObject(0).getString("name"));
          answerKey.put(slotCounter++, trainerData.getInt("id"));
        }
      }
    }

    int choice = JOptionPane.showOptionDialog(parent, "Select a target for "
        + move, "MOVE CHOICE", 0, 0, image, slot_names.toArray(), null);

    if (choice == -1)
      throw new DialogCancelException();

    return answerKey.get(choice);
  }

  public static int getItemChoice(Component parent, JSONObject trainer)
      throws DialogCancelException, JSONException {

    List<String> itemTypeNames = new ArrayList<String>();
    Map<String, List<String>> itemsByName = new HashMap<String, List<String>>();
    Map<String, Integer> answerKey = new HashMap<String, Integer>();
    JSONArray items = trainer.getJSONArray("bag");

    int itemID;
    JSONObject itemData;
    String itemType, itemName;

    for (int i = 0; i < items.length(); i++) {
      itemData = items.getJSONObject(i);
      itemType = itemData.getString("type");
      itemName = itemData.getString("name");
      itemID = itemData.getInt("id");
      answerKey.put(itemName, itemID);

      if (!itemTypeNames.contains(itemType)) {
        if (!itemsByName.keySet().contains(itemType))
          itemsByName.put(itemType, new ArrayList<String>());

        itemTypeNames.add(itemType);
        itemsByName.get(itemType).add(itemName);
      }
    }

    int itemTypeChoice = JOptionPane.showOptionDialog(parent,
        "Select an item to use", "ITEM CHOICE", 0, 0, null,
        itemTypeNames.toArray(), null);

    if (itemTypeChoice == -1)
      throw new DialogCancelException();

    itemType = itemTypeNames.get(itemTypeChoice);
    int itemChoice = JOptionPane.showOptionDialog(parent,
        "Select an item to use", "ITEM CHOICE", 0, 0, null,
        itemsByName.get(itemType).toArray(), null);

    if (itemChoice == -1)
      throw new DialogCancelException();

    itemName = itemsByName.get(itemType).get(itemChoice);
    return answerKey.get(itemName);
  }
}