package jpkmn.exe.gui.battle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import jpkmn.game.pokemon.Pokemon;
import jpkmn.img.ImageFinder;

import org.jpokemon.pokemon.storage.PokemonStorageUnit;

public class PartyPanel extends JPanel {
  public PartyPanel() {
    info = new JPanel();
    partyStatus = new JPanel();

    hpBar = new JProgressBar();
    xpBar = new JProgressBar();

    title = new JLabel();
    condition = new JLabel();
    leaderPicture = new JLabel();

    setLayout(new FlowLayout());
    info.setLayout(new BoxLayout(info, BoxLayout.PAGE_AXIS));

    setMaximumSize(new Dimension(340, 80));
    setPreferredSize(new Dimension(340, 80));
    partyStatus.setPreferredSize(new Dimension(40, 50));

    hpBar.setMinimum(0);
    hpBar.setForeground(Color.PINK);
    hpBar.setBackground(Color.GRAY);
    hpBar.setStringPainted(true);
    hpBar.setBorderPainted(false);

    xpBar.setMinimum(0);
    xpBar.setForeground(Color.CYAN);
    xpBar.setBackground(Color.GRAY);
    xpBar.setStringPainted(true);
    xpBar.setBorderPainted(false);

    info.add(title);
    info.add(hpBar);
    info.add(xpBar);
    info.add(condition);

    add(partyStatus);
    add(leaderPicture);
    add(info);
  }

  public PartyPanel(PokemonStorageUnit p, boolean showXP) {
    this();
    setup(p, showXP);
  }

  public void setup(PokemonStorageUnit p, boolean showXP) {
    if (!showXP)
      info.remove(xpBar);

    refresh(p);
  }

  public void refresh(PokemonStorageUnit p) {
    Pokemon lead = p.get(0); // shortcut

    partyStatus.removeAll();
    for (Pokemon pokemon : p) {
      if (pokemon.condition.awake())
        partyStatus.add(new JLabel(ImageFinder.find("aslot")));
      else
        partyStatus.add(new JLabel(ImageFinder.find("eslot")));
    }

    leaderPicture.setIcon(ImageFinder.find(lead));

    title.setText(lead.name() + " Lvl." + lead.level());
    hpBar.setMaximum(lead.maxHealth());
    hpBar.setValue(lead.health());
    xpBar.setMaximum(lead.xpNeeded());
    xpBar.setValue(lead.xp());

    condition.setText(lead.condition.toString());
  }

  private JPanel info, partyStatus;
  private JProgressBar hpBar, xpBar;
  private JLabel title, condition, leaderPicture;
  private static final long serialVersionUID = 1L;
}