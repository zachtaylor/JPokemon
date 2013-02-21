package jpkmn.exe.gui.start;

import javax.swing.JLabel;

import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.JPokemonMenuEntry;
import jpkmn.game.service.PlayerService;

public class StartMenuEntry extends JPokemonMenuEntry {
  public StartMenuEntry(StartMenu s, StartEntryValue v) {
    super(s);
    _value = v;

    add(new JLabel(v.toString()));
  }

  public void action() {
    switch (_value) {
    case POKEMON:
      // TODO
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
}