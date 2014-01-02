package com.jpokemon.interactioneditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.jpokemon.interaction.ActionSet;

import com.jpokemon.util.FeedbackInputField;

public class InteractionEditor extends JFrame {
  private JPanel centerPanel = new JPanel(), northPanel = new JPanel(), southPanel = new JPanel();
  private FeedbackInputField nameField = new FeedbackInputField();
  private JComboBox<String> contextCombobox = new JComboBox<String>();

  private static final long serialVersionUID = 1L;

  public InteractionEditor() {
    super("Interaction Editor");

    buildNorthPanel();
    buildSouthPanel();

    setLayout(new BorderLayout());
    add(northPanel, BorderLayout.NORTH);
    add(southPanel, BorderLayout.SOUTH);

    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
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
        onContextChange();
      }
    });
    northPanel.add(new JLabel("context:"));
    northPanel.add(contextCombobox);
  }

  protected void onNameChange() {
    String name = nameField.getText();
    nameField.setSavedValue(name);

    fillContextCombobox();
  }

  protected void onContextChange() {
    String context = (String) contextCombobox.getSelectedItem();
    System.out.println(context);
  }

  protected void buildSouthPanel() {
    JButton addInteractionButton = new JButton("Add Interaction container");
    addInteractionButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAddInteraction();
      }
    });
    southPanel.add(addInteractionButton);

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

  protected void onAddInteraction() {
    String entityName = nameField.getText();
    String context = (String) contextCombobox.getSelectedItem();

    if (entityName == null || entityName.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Please specify an entity name");
      return;
    }
    if (context == null || context.isEmpty() || context.equals("All")) {
      JOptionPane.showMessageDialog(this, "Please specify a context");
      return;
    }
    if (!entityName.equals(nameField.getSavedValue())) {
      JOptionPane.showMessageDialog(this, "Please save the entity name");
      return;
    }

    new ActionSet().setName(entityName).setContext(context).commit();
  }

  protected void onAddAction() {
  }

  protected void onAddRequirement() {
  }

  protected void buildCenterPanel() {
  }

  protected void fillContextCombobox() {
    String entityName = nameField.getText();
    List<ActionSet> actionSets = ActionSet.get(entityName);

    contextCombobox.removeAllItems();
    contextCombobox.addItem("All");
    for (ActionSet actionSet : actionSets) {
      contextCombobox.addItem(actionSet.getContext());
    }
  }

  protected void fillActions(List<ActionSet> actionSets) {
  }

  protected void fillRequirements(List<ActionSet> actionSets) {
  }
}