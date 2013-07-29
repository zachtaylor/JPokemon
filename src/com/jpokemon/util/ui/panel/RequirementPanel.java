package com.jpokemon.util.ui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.action.requirement.Requirement;

import com.jpokemon.util.mapeditor.MapEditComponent;
import com.jpokemon.util.ui.selector.EventSelector;
import com.jpokemon.util.ui.selector.RequirementTypeSelector;

public class RequirementPanel extends JPanel {
  public RequirementPanel(MapEditComponent mec, Requirement r) {
    parent = mec;
    requirement = r;

    requirementTypeSelector.reload();
    requirementTypeSelector.setSelectedIndex(requirement.getType());
    requirementTypeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onRequirementTypeSelect();
      }
    });
    add(requirementTypeSelector);

    switch (requirementTypeSelector.getCurrentElement()) {
    case EVENT:
      addEventStuff();
    break;
    case POKEDEX:
      addPokedexStuff();
    break;
    }
  }

  private void addEventStuff() {
    eventSelector.reload();
    eventSelector.setSelectedIndex(Math.abs(requirement.getData()) - 1);
    eventSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onEventSelect();
      }
    });
    add(eventSelector);

    invertSelection = new JCheckBox("Has not done yet");
    invertSelection.setSelected(requirement.getData() < 0);
    invertSelection.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onEventSelect();
      }
    });
    add(invertSelection);

    dataField.setText(Math.abs(requirement.getData()) + "");
  }

  private void addPokedexStuff() {
    dataField.setPreferredSize(new Dimension(80, 20));
    dataField.setText(Math.abs(requirement.getData()) + "");
    dataField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onPokedexSelect();
      }
    });
    add(dataField);

    invertSelection = new JCheckBox("Less than");
    invertSelection.setSelected(requirement.getData() < 0);
    invertSelection.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onPokedexSelect();
      }
    });
    add(invertSelection);
  }

  private void onRequirementTypeSelect() {
    requirement.commitTypeChange(requirementTypeSelector.getSelectedIndex());

    parent.getEditor();
  }

  private void onEventSelect() {
    int requirementData = eventSelector.getCurrentElement().getNumber();

    if (invertSelection.isSelected()) {
      requirementData = -requirementData;
    }

    dataField.setText(requirementData + "");
    commitNewData();
  }

  private void onPokedexSelect() {
    String newData;

    try {
      newData = Math.abs(Integer.parseInt(dataField.getText())) + "";
    } catch (NumberFormatException e) {
      return;
    }

    if (invertSelection.isSelected()) {
      newData = "-" + newData;
    }

    dataField.setText(newData);
    commitNewData();
  }

  private void commitNewData() {
    requirement.commitDataChange(Integer.parseInt(dataField.getText()));

    parent.getEditor();
  }

  private MapEditComponent parent;
  private JCheckBox invertSelection;
  private Requirement requirement;
  private JTextField dataField = new JTextField();
  private EventSelector eventSelector = new EventSelector();
  private RequirementTypeSelector requirementTypeSelector = new RequirementTypeSelector();

  private static final long serialVersionUID = 1L;
}