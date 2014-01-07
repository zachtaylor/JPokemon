package com.jpokemon.interactioneditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jpokemon.interaction.ActionData;
import org.jpokemon.interaction.ActionSet;
import org.jpokemon.interaction.RequirementData;

import com.jpokemon.util.FeedbackInputField;

public class InteractionEditor extends JFrame {
  private List<ActionSet> actionSets;
  private boolean atomicOperationFlag = false;
  private JLabel actionSetIdsLabel = new JLabel("ActionSets: ");
  private JPanel centerPanel = new JPanel(), northPanel = new JPanel(), southPanel = new JPanel();
  private JPanel actionsPanel = new JPanel();
  private JPanel requirementsPanel = new JPanel();
  private FeedbackInputField nameField = new FeedbackInputField();
  private JComboBox<String> contextCombobox = new JComboBox<String>();
  private JComboBox<String> optionCombobox = new JComboBox<String>();

  private static final String viewAllText = "(Show All)";
  private static final long serialVersionUID = 1L;

  public InteractionEditor() {
    super("Interaction Editor");

    buildNorthPanel();
    buildSouthPanel();

    setLayout(new BorderLayout());
    add(northPanel, BorderLayout.NORTH);
    add(southPanel, BorderLayout.SOUTH);

    buildCenterPanel();
    JScrollPane scrollPane = new JScrollPane(centerPanel);
    add(scrollPane, BorderLayout.CENTER);

    pack();
    setVisible(true);
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
  }

  protected void buildNorthPanel() {
    nameField.setPreferredSize(new Dimension(120, 25));
    nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNameChange();
      }
    });
    northPanel.add(new JLabel("entity:"));
    northPanel.add(nameField);

    contextCombobox.setEditable(true);
    contextCombobox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (atomicOperationFlag || arg0.getActionCommand().equals("comboBoxEdited")) {
          return;
        }
        onContextChange();
      }
    });
    northPanel.add(new JLabel("context:"));
    northPanel.add(contextCombobox);

    optionCombobox.setEditable(true);
    optionCombobox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        if (atomicOperationFlag || arg0.getActionCommand().equals("comboBoxEdited")) {
          return;
        }
        onOptionChange();
      }
    });
    northPanel.add(new JLabel("option name:"));
    northPanel.add(optionCombobox);
  }

  protected void onNameChange() {
    String name = nameField.getText();
    nameField.setSavedValue(name);

    actionSets = ActionSet.get(name);

    atomicOperationFlag = true;
    fillContextCombobox();
    fillOptionCombobox();
    atomicOperationFlag = false;

    fillActionsAndRequirements();
  }

  protected void onContextChange() {
    String name = nameField.getSavedValue();
    String context = (String) contextCombobox.getSelectedItem();

    if (context == null || context.isEmpty() || context.equals(viewAllText)) {
      actionSets = ActionSet.get(name);
    }
    else {
      actionSets = ActionSet.get(context, name);
    }

    atomicOperationFlag = true;
    fillOptionCombobox();
    atomicOperationFlag = false;

    fillActionsAndRequirements();
  }

  protected void onOptionChange() {
    String name = nameField.getSavedValue();
    String context = (String) contextCombobox.getSelectedItem();
    String option = (String) optionCombobox.getSelectedItem();

    if (option == null || option.isEmpty()) {
      optionCombobox.setSelectedItem(option = viewAllText);
    }
    if (context == null || context.isEmpty()) {
      contextCombobox.setSelectedItem(context = viewAllText);
    }

    if (!viewAllText.equals(context)) {
      actionSets = ActionSet.get(context, name);
    }
    else {
      actionSets = ActionSet.get(name);
    }

    if (!viewAllText.equals(option)) {
      filterActionSets(option);

      if (actionSets.isEmpty()) {
        if (name == null || name.isEmpty() || viewAllText.equals(context)) {
          return;
        }

        new ActionSet().setName(name).setContext(context).setOption(option).commit();
        actionSets = ActionSet.get(context, name);
        filterActionSets(option);
      }
    }

    fillActionsAndRequirements();
  }

  protected void buildSouthPanel() {
    JButton addActionButton = new JButton("Add Action");
    addActionButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAddAction();
      }
    });
    southPanel.add(addActionButton);

    JButton addRequirementButton = new JButton("Add Requirement");
    addRequirementButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAddRequirement();
      }
    });
    southPanel.add(addRequirementButton);
  }

  protected void onAddAction() {
    if (actionSets.size() != 1) {
      JOptionPane.showMessageDialog(this, "Please filter a single actionset to add this action to");
      return;
    }

    int actionSetId = actionSets.get(0).getId();

    new ActionData().setActionsetId(actionSetId).setAction("undefined").commit();
    fillActionsAndRequirements();
  }

  protected void onAddRequirement() {
    if (actionSets.size() != 1) {
      JOptionPane.showMessageDialog(this, "Please filter a single actionset to add this requirement to");
      return;
    }

    int actionSetId = actionSets.get(0).getId();

    new RequirementData().setActionsetId(actionSetId).setRequirement("undefined").commit();
    fillActionsAndRequirements();
  }

  protected void buildCenterPanel() {
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

    centerPanel.add(actionSetIdsLabel);

    JPanel jp = new JPanel();
    jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
    centerPanel.add(jp);

    actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
    jp.add(actionsPanel);

    requirementsPanel.setLayout(new BoxLayout(requirementsPanel, BoxLayout.Y_AXIS));
    jp.add(requirementsPanel);
  }

  protected void filterActionSets(String option) {
    ActionSet foundActionSet = null;

    for (ActionSet actionSet : actionSets) {
      if (actionSet.getOption().equals(option)) {
        foundActionSet = actionSet;
        break;
      }
    }

    actionSets = new ArrayList<ActionSet>();
    if (foundActionSet != null) {
      actionSets.add(foundActionSet);
    }
  }

  protected void fillContextCombobox() {
    List<String> contexts = new ArrayList<String>();

    for (ActionSet actionSet : actionSets) {
      if (!contexts.contains(actionSet.getContext())) {
        contexts.add(actionSet.getContext());
      }
    }

    contextCombobox.removeAllItems();
    contextCombobox.addItem(viewAllText);
    for (String context : contexts) {
      contextCombobox.addItem(context);
    }
  }

  protected void fillOptionCombobox() {
    optionCombobox.removeAllItems();
    optionCombobox.addItem(viewAllText);
    for (ActionSet actionSet : actionSets) {
      optionCombobox.addItem(actionSet.getOption());
    }
  }

  protected void fillActionsAndRequirements() {
    List<Integer> actionSetIds = new ArrayList<Integer>();

    actionsPanel.removeAll();
    actionsPanel.add(new JLabel("Actions"));

    requirementsPanel.removeAll();
    requirementsPanel.add(new JLabel("Requirements"));

    for (ActionSet actionSet : actionSets) {
      for (ActionData actionData : ActionData.get(actionSet.getId())) {
        actionsPanel.add(new ActionPanel(actionData));
      }
      for (RequirementData requirementData : RequirementData.get(actionSet.getId())) {
        requirementsPanel.add(new RequirementPanel(requirementData));
      }
      actionSetIds.add(actionSet.getId());
    }

    actionSetIdsLabel.setText("ActionSet IDs: " + actionSetIds.toString());
  }
}