package jpkmn.exe.gui.pokemonupgrade;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.exe.gui.JPokemonButton;
import jpkmn.exe.gui.JPokemonView;
import jpkmn.game.service.ImageFinder;
import jpkmn.game.service.PlayerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PokemonUpgradeView extends JPokemonView {
  public static void main(String[] args) {
    try {
      PlayerService.load("Zach.jpkmn");

    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  public PokemonUpgradeView(GameWindow g) {
    super(g);

    _stats = new StatPanel[6];

    _icon = new JLabel();
    _name = new JLabel();
    _points = new JLabel();

    JPanel pokemon = new JPanel();
    JPanel allStats = new JPanel();

    JPanel navPanel = new JPanel();

    JButton left = new JPokemonButton("<");
    left.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PokemonUpgradeView.this.navPokemon(PokemonUpgradeView.this._partyIndex - 1);
      }
    });
    navPanel.add(left);

    navPanel.add(_icon);

    JButton right = new JPokemonButton(">");
    right.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        PokemonUpgradeView.this.navPokemon(PokemonUpgradeView.this._partyIndex + 1);
      }
    });
    navPanel.add(right);

    for (int panelIndex = 0; panelIndex < _stats.length; panelIndex++)
      _stats[panelIndex] = new StatPanel(this);

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    navPanel.setLayout(new BoxLayout(navPanel, BoxLayout.X_AXIS));
    pokemon.setLayout(new BoxLayout(pokemon, BoxLayout.Y_AXIS));
    allStats.setLayout(new BoxLayout(allStats, BoxLayout.Y_AXIS));

    //@preformat
    add(pokemon);
      pokemon.add(navPanel);
      pokemon.add(_name);
      pokemon.add(_points);
      pokemon.add(spacer());
    add(spacer());
    add(allStats);
      for (StatPanel sp : _stats) allStats.add(sp);
      allStats.add(spacer());
    //@format
  }

  public void addPoint(String stat) {
    _spentPoints++;

    Integer oldVal = spending.get(stat);

    if (oldVal == null)
      spending.put(stat, 1);
    else
      spending.put(stat, oldVal + 1);

    checkSpending();
    System.out.println(spending.get(stat) + " points spent on " + stat);
  }

  public void update(JSONObject data) {
    _partyIndex = 0;

    try {
      _data = data.getJSONArray("party");
    } catch (JSONException e) {
    }

    doUpdate();
  }

  private void doUpdate() {
    try {
      _pokemon = _data.getJSONObject(_partyIndex);

      _icon.setIcon(ImageFinder.pokemon(_pokemon.getInt("number") + ""));

      _name.setText(_pokemon.getString("name"));
      _points.setText("Available points: " + _pokemon.getInt("points"));

      JSONArray stats = _pokemon.getJSONArray("stats");
      for (int i = 0; i < stats.length(); i++)
        _stats[i].update(stats.getJSONObject(i));

    } catch (JSONException e) {
    }

    checkSpending();
  }

  public Dimension dimension() {
    return new Dimension(400, 300);
  }

  public boolean key(KeyEvent arg0) {
    return false;
  }

  private void checkSpending() {
    try {
      boolean enable = _data.getJSONObject(_partyIndex).getInt("points") > _spentPoints;

      for (StatPanel panel : _stats)
        panel.enableButton(enable);

    } catch (JSONException e) {
    }
  }

  private void navPokemon(int index) {
    if (index < 0)
      return;
    else if (index >= _data.length())
      return;

    _partyIndex = index;
    _spentPoints = 0;
    spending = new HashMap<String, Integer>();
    doUpdate();
  }

  private int _partyIndex, _spentPoints;
  private JSONArray _data;
  private StatPanel[] _stats;
  private JSONObject _pokemon;
  private JLabel _icon, _name, _points;
  private Map<String, Integer> spending = new HashMap<String, Integer>();
  private static final long serialVersionUID = 1L;
}