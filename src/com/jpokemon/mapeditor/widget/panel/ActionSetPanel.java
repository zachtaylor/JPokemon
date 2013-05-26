package com.jpokemon.mapeditor.widget.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.ActionSetEntry;

import com.jpokemon.mapeditor.MapEditComponent;
import com.jpokemon.mapeditor.widget.selector.ActionTypeSelector;
import com.jpokemon.mapeditor.widget.selector.AreaSelector;
import com.jpokemon.mapeditor.widget.selector.EventSelector;
import com.jpokemon.mapeditor.widget.selector.ItemSelector;
import com.jpokemon.mapeditor.widget.selector.PokemonInfoSelector;

public class ActionSetPanel extends JPanel {
  public ActionSetPanel(MapEditComponent mec, ActionSetEntry ase) {
    parent = mec;
    actionSetEntry = ase;

    actionTypeSelector.reload();
    actionTypeSelector.setSelectedIndex(actionSetEntry.getType());
    actionTypeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onActionTypeSelect();
      }
    });
    add(actionTypeSelector);

    switch (actionTypeSelector.getCurrentElement()) {
    case ITEM:
      addItemStuff();
    break;
    case EVENT:
      addEventStuff();
    break;
    case TRANSPORT:
      addTransportStuff();
    break;
    case POKEMON:
      addPokemonStuff();
    break;
    default:
      addDefaultStuff();
    break;
    }
  }

  private void addItemStuff() {
    String[] pieces = actionSetEntry.getData().split(" ");

    itemSelector.reload();
    try {
      itemSelector.setSelectedIndex(Integer.parseInt(pieces[0]) - 1);
    } catch (NumberFormatException e) {
    }
    itemSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onItemSelect();
      }
    });
    add(itemSelector);

    dataField.setPreferredSize(new Dimension(40, 20));
    try {
      dataField.setText(Math.abs(Integer.parseInt(pieces[1])) + "");
    } catch (NumberFormatException e) {
    }
    dataField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onItemSelect();
      }
    });
    add(dataField);

    invertSelection = new JCheckBox("Remove");
    try {
      invertSelection.setSelected(Integer.parseInt(pieces[1]) < 0);
    } catch (NumberFormatException e) {
    }
    invertSelection.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onItemSelect();
      }
    });
    add(invertSelection);
  }

  private void addEventStuff() {
    eventSelector.reload();
    try {
      eventSelector.setSelectedIndex(Integer.parseInt(actionSetEntry.getData()) - 1);
    } catch (NumberFormatException e) {
    }
    eventSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onEventSelect();
      }
    });
    add(eventSelector);
  }

  private void addTransportStuff() {
    areaSelector.reload();
    try {
      areaSelector.setSelectedIndex(Integer.parseInt(actionSetEntry.getData()) - 1);
    } catch (NumberFormatException e) {
    }
    areaSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAreaSelect();
      }
    });
    add(areaSelector);
  }

  private void addPokemonStuff() {
    String[] pieces = actionSetEntry.getData().split(" ");
    ActionListener pokemonSelectCaller = new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onPokemonSelect();
      }
    };

    pokemonSelector.reload();
    try {
      pokemonSelector.setSelectedIndex(Math.abs(Integer.parseInt(pieces[0])) - 1);
    } catch (NumberFormatException e) {
    }
    pokemonSelector.addActionListener(pokemonSelectCaller);
    add(pokemonSelector);

    extraTextField = new JTextField();
    extraTextField.setPreferredSize(new Dimension(40, 20));
    if (pieces.length > 1) {
      extraTextField.setText(pieces[1]);
    }
    else {
      extraTextField.setText("1");
    }

    dataField.setPreferredSize(new Dimension(100, 20));
    if (pieces.length > 1) {
      dataField.setText(actionSetEntry.getData().replaceAll(pieces[0], "").replace(pieces[1], "").trim());
    }

    invertSelection = new JCheckBox("Remove");
    invertSelection.addActionListener(pokemonSelectCaller);

    try {
      if (Integer.parseInt(pieces[0]) > 0) {
        extraTextField.addActionListener(pokemonSelectCaller);
        add(extraTextField);
        dataField.addActionListener(pokemonSelectCaller);
        add(dataField);
      }
      else {
        invertSelection.setSelected(true);
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }

    add(invertSelection);
  }

  private void addDefaultStuff() {
    dataExplanationLabel = new JLabel("data: ");
    add(dataExplanationLabel);

    dataField.setPreferredSize(new Dimension(240, 20));
    dataField.setText(actionSetEntry.getData());
    dataField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onTextFieldEnter();
      }
    });
    add(dataField);
  }

  private void onActionTypeSelect() {
    actionSetEntry.commitTypeChange(actionTypeSelector.getSelectedIndex());

    parent.getEditor();
  }

  private void onItemSelect() {
    String newData = itemSelector.getCurrentElement().getNumber() + " ";

    if (invertSelection.isSelected()) {
      newData += "-";
    }

    newData += Math.abs(Integer.parseInt(dataField.getText()));

    dataField.setText(newData);
    onTextFieldEnter();
  }

  private void onEventSelect() {
    dataField.setText(eventSelector.getCurrentElement().getNumber() + "");
    onTextFieldEnter();
  }

  private void onAreaSelect() {
    dataField.setText(areaSelector.getCurrentElement().getNumber() + "");
    onTextFieldEnter();
  }

  private void onPokemonSelect() {
    String newData = pokemonSelector.getCurrentElement().getNumber() + " ";
    newData += Math.abs(Integer.parseInt(extraTextField.getText())) + " ";
    newData += dataField.getText();

    if (invertSelection.isSelected()) {
      newData = "-" + newData;
    }

    dataField.setText(newData);
    onTextFieldEnter();
  }

  private void onTextFieldEnter() {
    actionSetEntry.commitDataChange(dataField.getText());

    parent.getEditor();
  }

  private MapEditComponent parent;
  private JTextField extraTextField;
  private JCheckBox invertSelection;
  private JLabel dataExplanationLabel;
  private ActionSetEntry actionSetEntry;
  private JTextField dataField = new JTextField();
  private ItemSelector itemSelector = new ItemSelector();
  private AreaSelector areaSelector = new AreaSelector();
  private EventSelector eventSelector = new EventSelector();
  private PokemonInfoSelector pokemonSelector = new PokemonInfoSelector();
  private ActionTypeSelector actionTypeSelector = new ActionTypeSelector();

  private static final long serialVersionUID = 1L;
}