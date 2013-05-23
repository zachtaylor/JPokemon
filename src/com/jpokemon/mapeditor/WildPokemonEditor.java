package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.WildPokemon;

import com.jpokemon.JPokemonButton;
import com.jpokemon.mapeditor.widget.AreaSelector;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.SqlStatement;

public class WildPokemonEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "Wild Pokemon";

  public WildPokemonEditor() {
    JPanel northPanel = new JPanel(), southPanel = new JPanel();

    northPanel.add(areaSelector);
    areaSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAreaSelect();
      }
    });

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    southPanel.add(buttonPanel);

    JButton addRow = new JPokemonButton("Add Row");
    addRow.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onSelectAddRow();
      }
    });
    buttonPanel.add(addRow);

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(childContainer, BorderLayout.CENTER);
    editorPanel.add(southPanel, BorderLayout.SOUTH);
  }

  @Override
  public JPanel getEditor() {
    readyToEdit = false;

    areaSelector.reload();

    childContainer.removeAll();
    for (WildPokemon wildPokemon : WildPokemon.get(areaSelector.getArea().getNumber())) {
      childContainer.add(new WildPokemonPanel(wildPokemon));
    }

    readyToEdit = true;
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(480, 480);
  }

  private void onAreaSelect() {
    if (!readyToEdit)
      return;

    getEditor();
    editorPanel.repaint();
  }

  private void onSelectAddRow() {
    if (!readyToEdit)
      return;

    WildPokemon wildPokemon = new WildPokemon();
    wildPokemon.setArea(areaSelector.getArea().getNumber());

    try {
      SqlStatement.insert(wildPokemon).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    getEditor();
  }

  private class WildPokemonPanel extends JPanel {
    public WildPokemonPanel(WildPokemon wp) {
      wildPokemon = wp;

      pokemonNumberField.setText(wp.getNumber() + "");
      pokemonNumberField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onSelectNumberField();
        }
      });

      levelMinField.setText(wp.getLevelmin() + "");
      levelMinField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onSelectLevelMinField();
        }
      });

      levelMaxField.setText(wp.getLevelmax() + "");
      levelMaxField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onSelectLevelMaxField();
        }
      });

      flexField.setText(wp.getFlex() + "");
      flexField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onSelectFlexField();
        }
      });

      add(new JLabel("Pokemon #"));
      add(pokemonNumberField);
      add(new JPanel());
      add(new JLabel("Levels: "));
      add(levelMinField);
      add(new JLabel("-"));
      add(levelMaxField);
      add(new JPanel());
      add(new JLabel("Flex: "));
      add(flexField);
    }

    private void onSelectNumberField() {
      int oldNumber = wildPokemon.getNumber();
      int newNumber = Integer.parseInt(pokemonNumberField.getText());

      wildPokemon.setNumber(newNumber);

      try {
        SqlStatement.update(wildPokemon).where("area").eq(wildPokemon.getArea()).and("number").eq(oldNumber).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private void onSelectLevelMinField() {
      int newLevelMin = Integer.parseInt(levelMinField.getText());

      wildPokemon.setLevelmin(newLevelMin);

      try {
        SqlStatement.update(wildPokemon).where("area").eq(wildPokemon.getArea()).and("number").eq(wildPokemon.getNumber()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private void onSelectLevelMaxField() {
      int newLevelMax = Integer.parseInt(levelMaxField.getText());

      wildPokemon.setLevelmax(newLevelMax);

      try {
        SqlStatement.update(wildPokemon).where("area").eq(wildPokemon.getArea()).and("number").eq(wildPokemon.getNumber()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private void onSelectFlexField() {
      int newFlex = Integer.parseInt(flexField.getText());

      wildPokemon.setFlex(newFlex);

      try {
        SqlStatement.update(wildPokemon).where("area").eq(wildPokemon.getArea()).and("number").eq(wildPokemon.getNumber()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private WildPokemon wildPokemon;
    private JTextField flexField = new JTextField();
    private JTextField levelMinField = new JTextField();
    private JTextField levelMaxField = new JTextField();
    private JTextField pokemonNumberField = new JTextField();

    private static final long serialVersionUID = 1L;
  }

  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel(), childContainer = new JPanel();
  private AreaSelector areaSelector = new AreaSelector();
}
