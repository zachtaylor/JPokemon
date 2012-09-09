package jpkmn.exe.gui.pokemonupgrade;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.exe.gui.JPokemonView;
import jpkmn.game.player.Player;
import jpkmn.game.player.PlayerRegistry;
import jpkmn.game.service.PlayerService;
import jpkmn.img.ImageFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonUpgradeView extends JPokemonView {
  public static void main(String[] args) {
    try {
      Player zach = PlayerRegistry.fromFile("Zach");

      zach.screen.showUpgrade(0);

    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public PokemonUpgradeView(GameWindow g) {
    window = g;
    _playerID = g.playerID();

    _stats = new StatPanel[5];

    _exit = new ExitButton(this);

    _icon = new JLabel();
    _name = new JLabel();

    JPanel info = new JPanel();
    JPanel pokemon = new JPanel();
    JPanel allStats = new JPanel();

    for (int panelIndex = 0; panelIndex < _stats.length; panelIndex++)
      _stats[panelIndex] = new StatPanel(this);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    pokemon.setLayout(new BoxLayout(pokemon, BoxLayout.Y_AXIS));
    allStats.setLayout(new BoxLayout(allStats, BoxLayout.Y_AXIS));

    //@preformat
    add(pokemon);
      pokemon.add(_icon);
      pokemon.add(info);
        info.add(_name);
      pokemon.add(_exit);
      pokemon.add(spacer());
    add(spacer());
    add(allStats);
      for (StatPanel sp : _stats) allStats.add(sp);
      allStats.add(spacer());
    //@format
  }

  public void setup(int partyIndex) {
    _partyIndex = partyIndex;
  }

  public void refresh() {
    try {
      JSONObject pInfo = PlayerService.pokemonInfo(_playerID, _partyIndex);

      _name.setText(pInfo.getInt("number") + " " + pInfo.getString("name"));
      _icon.setIcon(ImageFinder.find("pkmn/" + pInfo.getInt("number")));

      JSONArray allStats = pInfo.getJSONArray("stats");
      for (int index = 0; index < allStats.length(); index++) {
        JSONObject stat = allStats.getJSONObject(index);

        _stats[index].refresh(stat.getString("name"), stat.getInt("value"),
            stat.getInt("points"));
      }

    } catch (ServiceException s) {

    } catch (JSONException j) {

    }
  }

  public Dimension dimension() {
    return new Dimension(300, 200);
  }

  GameWindow window;
  private ExitButton _exit;
  private StatPanel[] _stats;
  private JLabel _icon, _name;
  private int _playerID, _partyIndex;
  private static final long serialVersionUID = 1L;
}