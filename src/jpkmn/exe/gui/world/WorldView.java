package jpkmn.exe.gui.world;

import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jpkmn.exceptions.ServiceException;
import jpkmn.exe.gui.GameWindow;
import jpkmn.exe.gui.JPokemonView;
import jpkmn.game.service.PlayerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WorldView extends JPokemonView implements KeyListener {
  public WorldView(GameWindow g) {
    window = g;

    _title = new JLabel();

    _buttons = new JPanel();

    _fish = new FishButton(this);
    _grass = new GrassButton(this);
    _center = new CenterButton(this);

    _north = new AreaConnectionButton(this, "NORTH");
    _east = new AreaConnectionButton(this, "EAST");
    _south = new AreaConnectionButton(this, "SOUTH");
    _west = new AreaConnectionButton(this, "WEST");

    JPanel left = new JPanel();
    JPanel right = new JPanel();
    JPanel center = new JPanel();

    setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
    left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
    right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
    center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
    _buttons.setLayout(new BoxLayout(_buttons, BoxLayout.X_AXIS));

    //@preformat
    add(left);
      left.add(_west);
    add(spacer());
    add(center);
      center.add(_north);
      center.add(spacer());
      center.add(_title);
      center.add(_buttons);
      center.add(spacer());
      center.add(_south);
    add(spacer());
    add(right);
      right.add(_east);
    //@format

    setFocusable(true);
    setFocusTraversalKeysEnabled(false);
    addKeyListener(this);
  }

  public void refresh() {
    try {
      JSONObject areaInfo = PlayerService.areaInfo(window.playerID());

      _title.setText(areaInfo.getString("name"));

      _buttons.removeAll();
      if (areaInfo.getBoolean("hasGrass"))
        _buttons.add(_grass);
      if (areaInfo.getBoolean("hasWater"))
        _buttons.add(_fish);
      if (areaInfo.getBoolean("hasCenter"))
        _buttons.add(_center);

      JSONArray trainers = areaInfo.getJSONArray("trainers");
      for (int index = 0; index < trainers.length(); index++) {
        JSONObject trainer = trainers.getJSONObject(index);

        TrainerButton button = new TrainerButton(this, trainer.getInt("id"),
            trainer.getString("name"));

        _buttons.add(button);
      }

      JSONArray events = areaInfo.getJSONArray("events");
      for (int index = 0; index < events.length(); index++) {
        JSONObject event = events.getJSONObject(index);

        EventButton button = new EventButton(this, event.getInt("id"),
            event.getString("description"));

        _buttons.add(button);
      }

      //@preformat
    /* 
    for (Building b : a.buildings()) {
      if (b == Building.CENTER)
        _buttons.add(new CenterButton(this, _areaID));
      else if (b == Building.MART)
        _buttons.add(new MartButton(this, _areaID));
      // TODO : Figure the rest out
    } 
    */
    //@format

      _north.setUp(areaInfo.getString("NORTH"));
      _east.setUp(areaInfo.getString("EAST"));
      _south.setUp(areaInfo.getString("SOUTH"));
      _west.setUp(areaInfo.getString("WEST"));
    } catch (ServiceException s) {

    } catch (JSONException j) {

    }
  }

  public Dimension dimension() {
    return new Dimension(600, 300);
  }

  @Override
  public void keyPressed(KeyEvent event) {
    int keyCode = event.getKeyCode();

    if (keyCode == 10)
      window.showStart();
    else if (keyCode > 36 || keyCode < 41) {
      AreaConnectionButton button = null;

      if (keyCode == 37)
        button = _west;
      else if (keyCode == 38)
        button = _north;
      else if (keyCode == 39)
        button = _east;
      else if (keyCode == 40)
        button = _south;

      if (button != null)
        button.actionPerformed(null);
    }
  }

  @Override
  public void keyReleased(KeyEvent arg0) {
  }

  @Override
  public void keyTyped(KeyEvent arg0) {
  }

  GameWindow window;
  private JLabel _title;
  private JPanel _buttons;
  private FishButton _fish;
  private GrassButton _grass;
  private CenterButton _center;
  private AreaConnectionButton _north, _east, _south, _west;

  private static final long serialVersionUID = 1L;

}