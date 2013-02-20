package jpkmn.exe.gui;

import javax.swing.JFrame;

import jpkmn.exe.gui.battle.BattleView;
import jpkmn.exe.gui.pokemonupgrade.PokemonUpgradeView;
import jpkmn.exe.gui.start.StartView;
import jpkmn.exe.gui.world.WorldView;
import jpkmn.game.service.ImageFinder;
import jpkmn.game.service.PlayerService;

import org.json.JSONException;
import org.json.JSONObject;

public class GameWindow extends JFrame {
  public GameWindow(int playerID) {
    _playerID = playerID;
    _inbox = new MessageView();
    _battle = new BattleView(this);
    _main = new WorldView(this);
    _start = new StartView(this);
    _dialogs = new JPokemonDialog(this);
    _upgrade = new PokemonUpgradeView(this);

    // setResizable(false);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    setIconImage(ImageFinder.find("main-icon").getImage());

    setVisible(true);
  }

  public int playerID() {
    return _playerID;
  }

  public JPokemonDialog dialogs() {
    return _dialogs;
  }

  public MessageView inbox() {
    return _inbox;
  }

  public void refresh() {
    JSONObject data = PlayerService.pull(_playerID);

    try {
      _dialogs.setData(data.getJSONObject("player"));

      if (data.getString("state").equals("BATTLE"))
        show(_battle, data.getJSONObject("battle"));
      else if (data.getString("state").equals("UPGRADE"))
        show(_upgrade, data.getJSONObject("upgrade"));

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }

  private void show(JPokemonView view, JSONObject data) {
    if (view != _active) {
      if (_active != null)
        remove(_active);

      _active = view;
      add(_active);

      setSize(_active.dimension());
    }

    _active.update(data);
  }

  @Override
  public void dispose() {
    super.dispose();
    _inbox.dispose();
  }

  private int _playerID;
  private WorldView _main;
  private StartView _start;
  private BattleView _battle;
  private MessageView _inbox;
  private JPokemonView _active;
  private JPokemonDialog _dialogs;
  private PokemonUpgradeView _upgrade;
  private static final long serialVersionUID = 1L;
}