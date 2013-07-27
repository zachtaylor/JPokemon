package com.jpokemon.savegenerator;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.JPokemonConstants;
import org.jpokemon.pokemon.Pokemon;
import org.jpokemon.pokemon.move.MoveInfo;
import org.jpokemon.pokemon.move.MoveMap;

import com.jpokemon.ui.ImageLoader;
import com.jpokemon.ui.JPokemonSelector;

public class PokemonDetailsPanel extends JPanel {
  public PokemonDetailsPanel(PartyEditorPanel pep) {
    parent = pep;
    setLayout(new BorderLayout());

    JPanel detailsPanel = new JPanel();
    detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
    add(detailsPanel, BorderLayout.CENTER);

    detailsPanel.add(new JPanel());

    JPanel namePanel = new JPanel();
    namePanel.setLayout(new BoxLayout(namePanel, BoxLayout.X_AXIS));
    namePanel.add(new JLabel("Name: "));
    nameField.setPreferredSize(new Dimension(100, 16));
    nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        setName();
      }
    });
    namePanel.add(nameField);
    detailsPanel.add(namePanel);

    detailsPanel.add(new JPanel());

    JPanel levelPanel = new JPanel();
    levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.X_AXIS));
    levelPanel.add(new JLabel("Level: "));
    levelField.setPreferredSize(new Dimension(100, 16));
    levelField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        setLevel();
      }
    });
    levelPanel.add(levelField);
    detailsPanel.add(levelPanel);

    detailsPanel.add(new JPanel());

    movePanel.setLayout(new BoxLayout(movePanel, BoxLayout.Y_AXIS));
    detailsPanel.add(movePanel);

    detailsPanel.add(new JPanel());

    JPanel southPanel = new JPanel();
    add(southPanel, BorderLayout.SOUTH);

    deletePokemon.addMouseListener(new DeletePokemonHandler());
    southPanel.add(deletePokemon);
  }

  public void show(Pokemon p) {
    pokemon = p;

    nameField.setText(pokemon.name());
    levelField.setText(pokemon.level() + "");

    movePanel.removeAll();

    AvailableMoveSelector ams;
    MoveInfo amsValue;

    for (int i = 0; i < JPokemonConstants.KNOWN_MOVE_COUNT; i++) {
      ams = new AvailableMoveSelector();
      ams.reload();

      amsValue = null;
      if (i < pokemon.moveCount()) {
        amsValue = MoveInfo.get(pokemon.move(i).number());
      }
      ams.setSelectedItem(amsValue);

      ams.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          setMoves();
        }
      });
      movePanel.add(ams);
    }
  }

  private void setName() {
    if (pokemon == null) {
      return;
    }

    String name = nameField.getText();
    if (name.isEmpty()) {
      name = null;
    }
    pokemon.name(name);

    parent.callParentRefresh();
  }

  private void setLevel() {
    if (pokemon == null) {
      return;
    }

    String level = levelField.getText();
    if (level.isEmpty()) {
      return;
    }
    pokemon.level(Integer.parseInt(level));

    parent.callParentRefresh();
  }

  private void setMoves() {
    pokemon.removeAllMoves();

    MoveInfo mi;
    AvailableMoveSelector ams;

    for (Component c : movePanel.getComponents()) {
      if (!(c instanceof AvailableMoveSelector)) {
        continue;
      }
      ams = (AvailableMoveSelector) c;
      mi = ams.getCurrentElement();

      if (mi == null) {
        continue;
      }

      try {
        pokemon.addMove(mi.getNumber());
      } catch (IllegalArgumentException e) {
        ams.setSelectedItem(null);
      }
    }
  }

  private Pokemon pokemon;
  private PartyEditorPanel parent;
  private JPanel movePanel = new JPanel();
  private JTextField nameField = new JTextField();
  private JTextField levelField = new JTextField();
  private JLabel deletePokemon = new JLabel(ImageLoader.find("ui/minus"));

  private class DeletePokemonHandler extends MouseAdapter {
    @Override
    public void mouseClicked(MouseEvent e) {
      parent.removePokemon(pokemon);
    }
  }

  private class AvailableMoveSelector extends JPokemonSelector<MoveInfo> {
    @Override
    protected void reloadItems() {
      removeAllItems();

      for (MoveMap movemap : MoveMap.get(pokemon.number())) {
        addElementToModel(MoveInfo.get(movemap.getMove_number()));
      }
    }

    protected void renderElement(Component c, MoveInfo element) {
      ((JLabel) c).setText(element.getName());
    }

    private static final long serialVersionUID = 1L;
  }

  private static final long serialVersionUID = 1L;
}