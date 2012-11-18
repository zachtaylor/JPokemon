package jpkmn.exe.launcher;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import jpkmn.exceptions.ServiceException;
import jpkmn.game.service.PlayerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayButton extends JButton implements ActionListener {
  public PlayButton(Launcher l) {
    super("Play");

    _launcher = l;

    setFocusable(false);
    setBorderPainted(false);
    setBounds(550, 60, 110, 30);
    setBackground(new Color(206, 77, 77));

    addActionListener(this);
  }

  @Override
  public void actionPerformed(ActionEvent event) {
    String name = JOptionPane.showInputDialog(_launcher,
        "Please enter your username", "LOGIN", JOptionPane.QUESTION_MESSAGE);

    if (name == null)
      return;
    if (!name.endsWith(".jpkmn"))
      name += ".jpkmn";

    JSONObject player = null;
    try {
      player = PlayerService.loadPlayer(name);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(_launcher, e.toString(), "LOGIN ERROR",
          JOptionPane.ERROR_MESSAGE);
    }

    try {
      if (player.getInt("id") == -1) {
        JSONArray starterJSON = PlayerService.starterPokemon();

        String[] starterOptions = new String[starterJSON.length()];

        for (int i = 0; i < starterJSON.length(); i++)
          starterOptions[i] = starterJSON.getString(i);

        String choice = (String) JOptionPane.showInputDialog(null, "", "title",
            JOptionPane.QUESTION_MESSAGE, null, starterOptions, null);

        if (choice == null)
          return;

        player = PlayerService.newPlayer(name, choice);
      }
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (ServiceException e) {
      e.printStackTrace();
    }

    _launcher.dispose();
  }

  private Launcher _launcher;
  private static final long serialVersionUID = 1L;
}