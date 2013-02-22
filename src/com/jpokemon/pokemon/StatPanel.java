package com.jpokemon.pokemon;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.JSONException;
import org.json.JSONObject;

import com.jpokemon.JPokemonButton;

public class StatPanel extends JPanel {
  public StatPanel(PokemonView view) {
    _view = view;

    _point = new JPokemonButton("+");
    _point.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        addPoint();
      }
    });

    _name = new JLabel("");
    _value = new JLabel("");

    JPanel center = new JPanel();

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));

    //@preformat
    add(center);
      center.add(_name);
      center.add(_value);
    add(_view.spacer());
    add(_point);
    //@format
  }

  public void update(JSONObject data) {
    try {
      _stat = data.getString("name");

      _point.setText("+");
      _name.setText(_stat + " : " + data.getInt("max"));
      _value.setText("EV(" + data.getInt("ev") + ") IV(" + data.getInt("iv") + ") POINTS:" + data.getInt("points"));
    } catch (JSONException e) {
    }
  }

  public void addPoint() {
    _view.addPoint(_stat);

    if (_point.getText().equals("+"))
      _point.setText("+1");
    else
      _point.setText("+" + (Integer.parseInt(_point.getText().substring(1)) + 1));
  }

  public void enableButton(boolean enable) {
    _point.setEnabled(enable);
  }

  private String _stat;
  private JButton _point;
  private PokemonView _view;
  private JLabel _name, _value;
  private static final long serialVersionUID = 1L;
}