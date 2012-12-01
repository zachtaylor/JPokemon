package jpkmn.exe.gui.battle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import jpkmn.img.ImageFinder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PartyPanel extends JPanel {
  public PartyPanel(JSONObject data, int playerID) {
    int hp, hp_max, level, xp, xp_needed, team_id, trainer_id;
    String condition, name, image_path;
    List<String> partyIcons = new ArrayList<String>();

    try {
      JSONArray pokemon = data.getJSONArray("pokemon");
      JSONObject leader = pokemon.getJSONObject(0);
      JSONObject stats = leader.getJSONObject("stats");

      name = leader.getString("name");
      level = leader.getInt("level");
      hp = stats.getJSONObject("HEALTH").getInt("cur");
      hp_max = stats.getJSONObject("HEALTH").getInt("max");
      image_path = "pkmn/" + leader.getInt("number");
      condition = leader.getString("condition");
      xp = leader.getInt("xp");
      xp_needed = leader.getInt("xp_needed");
      team_id = data.getInt("team");
      trainer_id = data.getInt("trainer");

      JSONObject cur;
      for (int i = 0; i < pokemon.length(); i++) {
        cur = pokemon.getJSONObject(i);
        if (cur.getJSONObject("stats").getJSONObject("HEALTH").getInt("cur") == 0)
          partyIcons.add("battle/slot_empty");
        else if (cur.getString("condition").equals(""))
          partyIcons.add("battle/slot_ok");
        else
          partyIcons.add("battle/slot_sick");
      }

    } catch (JSONException e) {
      e.printStackTrace();
      return;
    }

    JPanel partyStatus = new JPanel();
    for (String icon : partyIcons)
      partyStatus.add(new JLabel(ImageFinder.find(icon)));
    add(partyStatus);
    partyStatus.setPreferredSize(new Dimension(40, 50));

    add(new JLabel(ImageFinder.find(image_path)));

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

    if (trainer_id == playerID) {
      JProgressBar xpBar = new JProgressBar();
      info.add(xpBar);
      xpBar.setMinimum(0);
      xpBar.setValue(xp);
      xpBar.setMaximum(xp_needed);
      xpBar.setForeground(Color.CYAN);
      xpBar.setBackground(Color.GRAY);
      xpBar.setStringPainted(true);
      xpBar.setBorderPainted(false);
    }
    info.add(new JLabel(condition));

    setBackground(teamColors[team_id]);
    setLayout(new FlowLayout());
    info.setLayout(new BoxLayout(info, BoxLayout.PAGE_AXIS));

    setPreferredSize(new Dimension(340, 80));
  }

  private Color[] teamColors = { Color.red, Color.yellow, Color.orange,
      Color.blue };

  private JPanel info;
  private static final long serialVersionUID = 1L;
}