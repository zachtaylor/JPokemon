package com.jpokemon.util.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.action.SqliteActionBinding;
import org.jpokemon.action.ActionType;

import com.jpokemon.util.mapeditor.MapEditComponent;
import com.jpokemon.util.ui.selector.ActionTypeSelector;
import com.jpokemon.util.ui.selector.AreaSelector;
import com.jpokemon.util.ui.selector.EventSelector;
import com.jpokemon.util.ui.selector.ItemSelector;
import com.jpokemon.util.ui.selector.PokemonInfoSelector;
import com.jpokemon.util.ui.selector.StoreSelector;

public class ActionPanel extends JPanel {
  public ActionPanel(MapEditComponent mec, SqliteActionBinding a) {
    parent = mec;
    action = a;

    actionTypeSelector.reload();
    if (!action.getType().toString().equals("undefined")) {
      actionTypeSelector.setSelectedItem(ActionType.valueOf(action.getType()));
    }
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
    case STORE:
      addStoreStuff();
    break;
    case UPGRADE:
    case HEAL:
    break;
    default:
      addDefaultStuff();
    break;
    }
  }

  private void addItemStuff() {
    String[] pieces = action.getData().split(" ");

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
    } catch (ArrayIndexOutOfBoundsException e) {
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
    } catch (ArrayIndexOutOfBoundsException e) {
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
      eventSelector.setSelectedIndex(Integer.parseInt(action.getData()) - 1);
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
      areaSelector.setSelectedIndex(Integer.parseInt(action.getData()) - 1);
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
    String[] pieces = action.getData().split(" ");
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
    extraTextField.addActionListener(pokemonSelectCaller);
    if (pieces.length > 1) {
      extraTextField.setText(pieces[1]);
    }
    else {
      extraTextField.setText("1");
    }

    dataField.setPreferredSize(new Dimension(100, 20));
    dataField.addActionListener(pokemonSelectCaller);
    if (pieces.length > 1) {
      dataField.setText(action.getData().replaceAll(pieces[0], "").replace(pieces[1], "").trim());
    }

    invertSelection = new JCheckBox("Remove");
    invertSelection.addActionListener(pokemonSelectCaller);

    try {
      if (Integer.parseInt(pieces[0]) > 0) {
        add(new JLabel("Level"));
        add(extraTextField);
        add(new JLabel("Params"));
        add(dataField);
      }
      else {
        invertSelection.setSelected(true);
      }
    } catch (NumberFormatException e) {
      add(new JLabel("Level"));
      add(extraTextField);
      add(new JLabel("Params"));
      add(dataField);
    }

    add(invertSelection);
  }

  private void addStoreStuff() {
    storeSelector.reload();
    try {
      storeSelector.setSelectedIndex(Integer.parseInt(action.getData()) - 1);
    } catch (NumberFormatException e) {
    }
    storeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onStoreSelect();
      }
    });
    add(storeSelector);
  }

  private void addDefaultStuff() {
    dataExplanationLabel = new JLabel("data: ");
    add(dataExplanationLabel);

    dataField.setPreferredSize(new Dimension(240, 20));
    dataField.setText(action.getData());
    dataField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onTextFieldEnter();
      }
    });
    add(dataField);
  }

  private void onActionTypeSelect() {
    action.commitTypeChange(actionTypeSelector.getCurrentElement().toString());

    parent.getEditor();
  }

  private void onItemSelect() {
    String newData = itemSelector.getCurrentElement().getNumber() + " ";

    if (invertSelection.isSelected()) {
      newData += "-";
    }

    try {
      newData += Math.abs(Integer.parseInt(dataField.getText()));
    } catch (NumberFormatException e) {
      newData += '0';
    }

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
    try {
      newData += Math.abs(Integer.parseInt(extraTextField.getText())) + " ";
    } catch (NumberFormatException e) {
      newData += '0';
    }
    newData += dataField.getText();

    if (invertSelection.isSelected()) {
      newData = "-" + newData;
    }

    dataField.setText(newData);
    onTextFieldEnter();
  }

  private void onStoreSelect() {
    dataField.setText(storeSelector.getCurrentElement().getNumber() + "");
    onTextFieldEnter();
  }

  private void onTextFieldEnter() {
    action.commitDataChange(dataField.getText());

    parent.getEditor();
  }

  private SqliteActionBinding action;
  private MapEditComponent parent;
  private JTextField extraTextField;
  private JCheckBox invertSelection;
  private JLabel dataExplanationLabel;
  private JTextField dataField = new JTextField();
  private ItemSelector itemSelector = new ItemSelector();
  private AreaSelector areaSelector = new AreaSelector();
  private EventSelector eventSelector = new EventSelector();
  private StoreSelector storeSelector = new StoreSelector();
  private PokemonInfoSelector pokemonSelector = new PokemonInfoSelector();
  private ActionTypeSelector actionTypeSelector = new ActionTypeSelector();

  private static final long serialVersionUID = 1L;
}