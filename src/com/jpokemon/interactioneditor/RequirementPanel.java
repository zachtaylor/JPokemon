package com.jpokemon.interactioneditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.interaction.RequirementData;
import org.jpokemon.trainer.Event;

import com.jpokemon.util.FeedbackInputField;
import com.jpokemon.util.ui.selector.EventSelector;
import com.jpokemon.util.ui.selector.RequirementTypeSelector;

public class RequirementPanel extends JPanel {
  private RequirementData requirementData;
  private JPanel extraPanel = new JPanel();
  private RequirementTypeSelector requirementTypeSelector = new RequirementTypeSelector();
  private boolean atomicOperationFlag = false;
  private EventSelector eventSelector;
  private FeedbackInputField pokedexCountField;

  private static final long serialVersionUID = 1L;

  public RequirementPanel(RequirementData r) {
    requirementData = r;

    buildRequirementTypeSelector();
    add(new JLabel("#" + requirementData.getId() + " in #" + requirementData.getActionsetId()));
    add(requirementTypeSelector);
    add(extraPanel);

    onRequirementTypeChange();
  }

  protected void buildRequirementTypeSelector() {
    requirementTypeSelector.reload();
    requirementTypeSelector.setSelectedItem(requirementData.getRequirement());
    requirementTypeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onRequirementTypeChange();
      }
    });
  }

  protected void onRequirementTypeChange() {
    String requirement = (String) requirementTypeSelector.getSelectedItem();

    requirementData.setRequirement(requirement).commit();

    extraPanel.removeAll();
    if ("event".equals(requirement)) {
      buildEventRequirement();
    }
    else if ("pokedex".equals(requirement)) {
      buildPokedexRequirement();
    }

    revalidate();
  }

  protected void buildEventRequirement() {
    int eventId = 1;
    try {
      eventId = Integer.parseInt(requirementData.getOptions());
    }
    catch (NumberFormatException e) {
      requirementData.setOptions("1");
      requirementData.commit();
    }

    if (eventSelector == null) {
      eventSelector = new EventSelector();
      eventSelector.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          if (atomicOperationFlag) {
            return;
          }
          onEventChange();
        }
      });
    }

    atomicOperationFlag = true;
    eventSelector.reload();
    eventSelector.setSelectedItem(Event.get(eventId));
    atomicOperationFlag = false;

    extraPanel.add(eventSelector);
  }

  protected void onEventChange() {
    int eventId = ((Event) eventSelector.getSelectedItem()).getNumber();
    requirementData.setOptions(eventId + "");
    requirementData.commit();
  }

  protected void buildPokedexRequirement() {
    int pokedexCount = 0;
    try {
      pokedexCount = Integer.parseInt(requirementData.getOptions());
    }
    catch (NumberFormatException e) {
      requirementData.setOptions("0");
      requirementData.commit();
    }

    if (pokedexCountField == null) {
      pokedexCountField = new FeedbackInputField();
      pokedexCountField.setPreferredSize(new Dimension(40, 20));
      pokedexCountField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onPokedexCountChange();
        }
      });
    }

    pokedexCountField.setSavedValue(pokedexCount + "");
    pokedexCountField.setText(pokedexCount + "");
    extraPanel.add(pokedexCountField);
  }

  protected void onPokedexCountChange() {
    try {
      int pokedexCount = Integer.parseInt(pokedexCountField.getText());
      requirementData.setOptions(pokedexCount + "");
      requirementData.commit();
      pokedexCountField.setSavedValue(pokedexCount + "");
    }
    catch (NumberFormatException e) {
      pokedexCountField.setText(pokedexCountField.getSavedValue());
    }
  }
}