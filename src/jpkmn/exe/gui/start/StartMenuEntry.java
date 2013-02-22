package jpkmn.exe.gui.start;

import javax.swing.JLabel;

import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.JPokemonMenuEntry;
import jpkmn.game.service.PlayerService;

import org.json.JSONException;
import org.json.JSONObject;

public class StartMenuEntry extends JPokemonMenuEntry {
  public StartMenuEntry(StartMenu s, StartEntryValue v) {
    super(s);
    _value = v;

    try {
      _showPokemonPlz = new JSONObject();
      _showPokemonPlz.put("id", parent().parent().playerID());
      _showPokemonPlz.put("stats", JSONObject.NULL);
    } catch (JSONException e) {
    }

    add(new JLabel(v.toString()));
  }

  public void action() {
    switch (_value) {
    case POKEMON:
      PlayerService.party(_showPokemonPlz);
      parent().refresh();
      break;
    case SAVE:
      try {
        PlayerService.save(parent().parent().playerID());
        parent().refresh();
      } catch (ServiceException e) {
        e.printStackTrace();
        return;
      }
      break;
    case EXIT:
      parent().parent().closeStart();
      break;
    case QUIT:
      parent().parent().dispose();
    }
  }

  private StartEntryValue _value;
  private JSONObject _showPokemonPlz;
}