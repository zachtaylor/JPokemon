package com.jpokemon.pokemon;

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

import org.jpokemon.service.ImageService;
import org.jpokemon.service.PlayerService;
import org.jpokemon.service.ServiceException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.GameWindow;
import com.jpokemon.JPokemonButton;
import com.jpokemon.JPokemonMenu;
import com.jpokemon.JPokemonView;
import com.jpokemon.party.PartyMenu;

public class PokemonView extends JPokemonView {
  public static void main(String[] args) {
    try {
      PlayerService.load("Zach.jpkmn");

    } catch (ServiceException e) {
      e.printStackTrace();
    }
  }

  public PokemonView(GameWindow g) {
    super(g);

    _menu = new PartyMenu(g, this);

    JPanel buttons = new JPanel();
    JButton accept = new JPokemonButton("Accept");
    accept.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        applyChanges();
      }
    });
    buttons.add(accept);
    JButton discard = new JPokemonButton("Discard");
    discard.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        discardChanges();
      }
    });
    buttons.add(discard);

    JPanel left = new JPanel();
    left.add(_icon = new JLabel());
    left.add(_name = new JLabel());
    left.add(_points = new JLabel());
    left.add(spacer());
    left.add(buttons);

    JPanel right = new JPanel();
    _stats = new StatPanel[6];
    for (int panelIndex = 0; panelIndex < _stats.length; panelIndex++)
      right.add(_stats[panelIndex] = new StatPanel(this));
    right.add(spacer());

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
    right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

    add(left);
    add(spacer());
    add(right);
  }

  public void addPoint(String stat) {
    _spentPoints++;

    Integer oldVal = spending.get(stat);

    if (oldVal == null)
      spending.put(stat, 1);
    else
      spending.put(stat, oldVal + 1);

    checkSpending();
  }

  public void update(JSONObject data) {
    _partyIndex = 0;

    try {
      _data = data.getJSONArray("party");
      _menu.update(_data);
    } catch (JSONException e) {
    }

    doUpdate();
  }

  public void navPokemon(int index) {
    if (index < 0)
      return;
    else if (index >= _data.length())
      return;

    _partyIndex = index;
    clearSpending();
  }

  public void applyChanges() {
    if (spending.isEmpty())
      return;

    try {
      JSONObject request = new JSONObject();
      request.put("id", parent().playerID());
      request.put("pokemon_index", _partyIndex);

      JSONArray stats = new JSONArray();
      for (Map.Entry<String, Integer> entry : spending.entrySet()) {
        JSONObject stat = new JSONObject();
        stat.put("stat", entry.getKey());
        stat.put("amount", entry.getValue());
        stats.put(stat);
      }
      request.put("stats", stats);

      PlayerService.activity(request);
    } catch (JSONException e) {
    } catch (ServiceException e) {
    }

    clearSpending();
    refresh();
  }

  public void discardChanges() {
    clearSpending();
  }

  private void doUpdate() {
    try {
      _pokemon = _data.getJSONObject(_partyIndex);

      _icon.setIcon(ImageService.pokemon(_pokemon.getInt("number") + ""));

      _name.setText(_pokemon.getString("name"));
      _points.setText("Available points: " + _pokemon.getInt("points"));

      JSONArray stats = _pokemon.getJSONArray("stats");
      for (int i = 0; i < stats.length(); i++)
        _stats[i].update(stats.getJSONObject(i));

    } catch (JSONException e) {
    }

    checkSpending();
  }

  private void clearSpending() {
    _spentPoints = 0;
    spending = new HashMap<String, Integer>();
    doUpdate();
  }

  private void checkSpending() {
    try {
      boolean enable = _data.getJSONObject(_partyIndex).getInt("points") > _spentPoints;

      for (StatPanel panel : _stats)
        panel.enableButton(enable);

    } catch (JSONException e) {
    }
  }

  public boolean hasDependentMenu() {
    return true;
  }

  public JPokemonMenu dependentMenu() {
    return _menu;
  }

  public Dimension dimension() {
    return new Dimension(500, 300);
  }

  public boolean key(KeyEvent arg0) {
    return false;
  }

  private PartyMenu _menu;
  private JSONArray _data;
  private StatPanel[] _stats;
  private JSONObject _pokemon;
  private JLabel _icon, _name, _points;
  private int _partyIndex, _spentPoints;
  private Map<String, Integer> spending = new HashMap<String, Integer>();
  private static final long serialVersionUID = 1L;
}