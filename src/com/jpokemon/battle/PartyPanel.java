package com.jpokemon.battle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.jpokemon.service.ImageService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PartyPanel extends JPanel {
  public PartyPanel(final BattleView view, JSONObject data) {

    String condition, name;
    int hp, hp_max, level, number, xp = 0, xp_needed = 0;
    List<String> partyIcons = new ArrayList<String>();

    boolean show_buttons_and_xp = false;

    try {
      JSONObject leader = data.getJSONObject("leader");
      JSONArray party = data.getJSONArray("pokemon");

      show_buttons_and_xp = data.getString("id").equals(view.parent().playerID());

      name = leader.getString("name");
      number = leader.getInt("number");
      level = leader.getInt("level");
      hp = leader.getInt("hp");
      hp_max = leader.getInt("hp_max");
      condition = leader.getString("condition");

      if (show_buttons_and_xp) {
        xp = leader.getInt("xp");
        xp_needed = leader.getInt("xp_needed");
      }

      for (int i = 0; i < party.length(); i++) {
        partyIcons.add(party.getJSONObject(i).getString("ball_icon"));
      }

    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    JPanel partyStatus = new JPanel();
    for (String icon : partyIcons)
      partyStatus.add(new JLabel(ImageService.find(icon)));
    add(partyStatus);
    partyStatus.setPreferredSize(new Dimension(40, 50));

    add(new JLabel(ImageService.pokemon(number + "")));

    add(info = new JPanel());

    info.add(new JLabel(name + " Lvl." + level));

    JProgressBar hpBar = new JProgressBar();
    info.add(hpBar);
    hpBar.setMinimum(0);
    hpBar.setValue(hp);
    hpBar.setMaximum(hp_max);
    hpBar.setForeground(Color.PINK);
    hpBar.setBackground(Color.GRAY);
    hpBar.setStringPainted(true);
    hpBar.setBorderPainted(false);

    if (show_buttons_and_xp) {
      JProgressBar xpBar = new JProgressBar();
      info.add(xpBar);
      xpBar.setMinimum(0);
      xpBar.setValue(xp);
      xpBar.setMaximum(xp + xp_needed);
      xpBar.setForeground(Color.CYAN);
      xpBar.setBackground(Color.GRAY);
      xpBar.setStringPainted(true);
      xpBar.setBorderPainted(false);

      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
      add(buttonPanel);

      buttonPanel.add(view.fightButton());
      buttonPanel.add(view.itemButton());
      buttonPanel.add(view.swapButton());
      buttonPanel.add(view.runButton());

      add(view.spacer());
    }
    info.add(new JLabel(condition));

    setLayout(new FlowLayout());
    info.setLayout(new BoxLayout(info, BoxLayout.PAGE_AXIS));

    setPreferredSize(new Dimension(340, 80));
  }

  private JPanel info;
  private static final long serialVersionUID = 1L;
}