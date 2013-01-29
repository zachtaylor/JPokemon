package jpkmn.exe.gui.pokemonupgrade;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class StatPanel extends JPanel {
  public StatPanel(PokemonUpgradeView view) {
    _view = view;

    _point = new PointButton(this);

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

  public void refresh(String name, int value, int points) {
    _stat = name;

    _name.setText(name);
    _value.setText(value + " (" + points + ")");
  }

  public void addPoint() {
    // TODO
    System.out.println("You chose to spend a point on " + _stat);
    _view.refresh();
  }

  private String _stat;
  private PointButton _point;
  private JLabel _name, _value;
  private PokemonUpgradeView _view;
  private static final long serialVersionUID = 1L;
}