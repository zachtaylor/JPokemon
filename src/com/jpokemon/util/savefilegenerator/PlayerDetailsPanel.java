package com.jpokemon.util.savefilegenerator;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.jpokemon.util.ui.ImageLoader;

public class PlayerDetailsPanel extends JPanel {
  public PlayerDetailsPanel(SaveGeneratorWindow sgw) {
    parent = sgw;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setPreferredSize(new Dimension(200, 300));

    add(usernameLabel);
    add(cashLabel);
    add(badgeLabel);

    JPanel playerPanel = new JPanel();
    playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.X_AXIS));
    playerPanel.add(new JLabel("Edit player"));
    editPlayer.addMouseListener(new EditPlayerHandler());
    playerPanel.add(editPlayer);
    add(playerPanel);

    JPanel partyPanel = new JPanel();
    partyPanel.setLayout(new BoxLayout(partyPanel, BoxLayout.X_AXIS));
    partyPanel.add(new JLabel("Edit party"));
    editParty.addMouseListener(new EditPartyHandler());
    partyPanel.add(editParty);
    add(partyPanel);

    JPanel downloadPanel = new JPanel();
    saveFile.addMouseListener(new SaveHandler());
    downloadPanel.add(saveFile);
    add(downloadPanel);

    refresh();
  }

  public void refresh() {
    usernameLabel.setText("name: " + parent.getPlayer().name());
    cashLabel.setText("cash: " + parent.getPlayer().cash());
    badgeLabel.setText("badges: " + parent.getPlayer().badge());
  }

  private SaveGeneratorWindow parent;
  private JLabel usernameLabel = new JLabel("");
  private JLabel cashLabel = new JLabel("");
  private JLabel badgeLabel = new JLabel("");
  private JLabel editParty = new JLabel(ImageLoader.find("ui/arrow_right"));
  private JLabel editPlayer = new JLabel(ImageLoader.find("ui/arrow_right"));
  private JLabel saveFile = new JLabel(ImageLoader.find("ui/download"));

  public static final long serialVersionUID = 1L;

  private class EditPartyHandler extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      parent.toggleEditParty();
    }
  }

  private class EditPlayerHandler extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      parent.toggleEditPlayer();
    }
  }

  private class SaveHandler extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      parent.savePlayer();
    }
  }
}