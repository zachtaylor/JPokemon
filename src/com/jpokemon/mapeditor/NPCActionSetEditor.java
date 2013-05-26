package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.npc.NPCActionSet;
import org.jpokemon.map.npc.NPCActionSetMap;
import org.jpokemon.map.npc.NPCRequirement;

import com.jpokemon.mapeditor.widget.selector.ActionTypeSelector;
import com.jpokemon.mapeditor.widget.selector.EventSelector;
import com.jpokemon.mapeditor.widget.selector.NPCActionSetSelector;
import com.jpokemon.mapeditor.widget.selector.NPCSelector;
import com.jpokemon.mapeditor.widget.selector.RequirementTypeSelector;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.SqlStatement;

public class NPCActionSetEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "NPC ActionSet Definition";

  public NPCActionSetEditor() {
    JPanel northPanel = new JPanel();

    npcSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNPCSelect();
      }
    });
    northPanel.add(npcSelector);

    JPanel westPanel = new JPanel();
    westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

    JPanel npcActionSetSelectorAndActionSetNameField = new JPanel();
    westPanel.add(new JPanel());
    westPanel.add(npcActionSetSelectorAndActionSetNameField);

    JButton addNPCActionSet = new JButton("Add ActionSet");
    addNPCActionSet.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAddNPCActionSetClick();
      }
    });
    westPanel.add(addNPCActionSet);
    westPanel.add(new JPanel());

    npcActionSetSelector = new NPCActionSetSelector(0);
    npcActionSetSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNPCActionSetMapSelect();
      }
    });
    npcActionSetSelectorAndActionSetNameField.add(npcActionSetSelector);

    nameField.setPreferredSize(new Dimension(180, 20));
    // nameField.setMinimumSize();
    // nameField.setMaximumSize(new Dimension(160, 16));
    nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNameFieldEnter();
      }
    });
    npcActionSetSelectorAndActionSetNameField.add(nameField);

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

    JLabel actionSetContainerTitle = new JLabel("Actions in ActionSet");
    centerPanel.add(actionSetContainerTitle);

    actionSetContainer.setLayout(new BoxLayout(actionSetContainer, BoxLayout.Y_AXIS));
    centerPanel.add(actionSetContainer);

    JButton newAction = new JButton("New Action");
    newAction.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNewActionClick();
      }
    });
    centerPanel.add(newAction);

    JPanel eastPanel = new JPanel();
    eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

    JLabel requirementSetContainerTitle = new JLabel("Requirements of ActionSet");
    eastPanel.add(requirementSetContainerTitle);

    requirementSetContainer.setLayout(new BoxLayout(requirementSetContainer, BoxLayout.Y_AXIS));
    eastPanel.add(requirementSetContainer);

    JButton newRequirement = new JButton("New Requirement");
    newRequirement.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        onNewRequirementClick();
      }
    });
    eastPanel.add(newRequirement);

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(westPanel, BorderLayout.WEST);
    editorPanel.add(centerPanel, BorderLayout.CENTER);
    editorPanel.add(eastPanel, BorderLayout.EAST);
  }

  @Override
  public JPanel getEditor() {
    readyToEdit = false;

    npcSelector.reload();
    npcActionSetSelector.setNPCNumber(npcSelector.getCurrentElement().getNumber());
    npcActionSetSelector.reload();

    nameField.setText(npcActionSetSelector.getCurrentElement().getOption());

    actionSetContainer.removeAll();
    for (NPCActionSet npcActionSet : NPCActionSet.get(npcSelector.getCurrentElement().getNumber(), npcActionSetSelector.getCurrentElement().getActionset())) {
      actionSetContainer.add(new NPCActionSetPanel(npcActionSet));
    }

    requirementSetContainer.removeAll();
    for (NPCRequirement npcRequirement : NPCRequirement.get(npcSelector.getCurrentElement().getNumber(), npcActionSetSelector.getCurrentElement().getActionset())) {
      requirementSetContainer.add(new NPCRequirementPanel(npcRequirement));
    }

    readyToEdit = true;
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(1280, 240);
  }

  private void onAddNPCActionSetClick() {
    NPCActionSetMap npcActionSetMap = new NPCActionSetMap();

    int npcActionSetID = 0;
    for (NPCActionSetMap npcasm : NPCActionSetMap.getByNPCNumber(npcSelector.getCurrentElement().getNumber())) {
      if (npcActionSetID <= npcasm.getActionset()) {
        npcActionSetID = npcasm.getActionset() + 1;
      }
    }

    npcActionSetMap.setArea(0);
    npcActionSetMap.setNumber(npcSelector.getCurrentElement().getNumber());
    npcActionSetMap.setActionset(npcActionSetID);
    npcActionSetMap.setOption("undefined");

    try {
      SqlStatement.insert(npcActionSetMap).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    getEditor();
  }

  private void onNPCSelect() {
    if (!readyToEdit)
      return;

    getEditor();
  }

  private void onNPCActionSetMapSelect() {
    getEditor();
  }

  private void onNameFieldEnter() {
    String newOption = nameField.getText();

    for (NPCActionSetMap npcasm : NPCActionSetMap.getByNPCNumber(npcActionSetSelector.getCurrentElement().getNumber())) {
      if (npcasm.getActionset() == npcActionSetSelector.getCurrentElement().getActionset()) {
        npcasm.setOption(newOption);

        try {
          SqlStatement.update(npcasm).where("area").eq(npcasm.getArea()).and("number").eq(npcasm.getNumber()).and("actionset").eq(npcasm.getActionset()).execute();
        } catch (DataConnectionException e) {
          e.printStackTrace();
        }
      }
    }

    getEditor();
  }

  private void onNewActionClick() {
    NPCActionSet npcActionSet = new NPCActionSet();

    npcActionSet.setNumber(npcSelector.getCurrentElement().getNumber());
    npcActionSet.setActionset(npcActionSetSelector.getCurrentElement().getActionset());
    npcActionSet.setType(0);
    npcActionSet.setData("undefined");

    try {
      SqlStatement.insert(npcActionSet).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    getEditor();
  }

  private void onNewRequirementClick() {
    NPCRequirement npcRequirement = new NPCRequirement();

    npcRequirement.setNumber(npcSelector.getCurrentElement().getNumber());
    npcRequirement.setActionset(npcActionSetSelector.getCurrentElement().getActionset());
    npcRequirement.setType(0);
    npcRequirement.setData(0);

    try {
      SqlStatement.insert(npcRequirement).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    getEditor();
  }

  private class NPCActionSetPanel extends JPanel {
    public NPCActionSetPanel(NPCActionSet npcas) {
      npcActionSet = npcas;

      actionTypeSelector.reload();
      actionTypeSelector.setSelectedIndex(npcas.getType());
      actionTypeSelector.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onActionTypeSelect();
        }
      });
      add(actionTypeSelector);

      dataExplanationLabel = new JLabel("data: ");
      add(dataExplanationLabel);

      dataField.setPreferredSize(new Dimension(240, 20));
      dataField.setText(npcas.getData());
      dataField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onDataFieldEnter();
        }
      });
      add(dataField);
    }

    private void onActionTypeSelect() {
      int oldActionType = npcActionSet.getType();
      int newActionType = actionTypeSelector.getSelectedIndex();

      npcActionSet.setType(newActionType);

      try {
        SqlStatement.update(npcActionSet).where("number").eq(npcActionSet.getNumber()).and("actionset").eq(npcActionSet.getActionset()).and("type").eq(oldActionType).and("data").eq(npcActionSet.getData()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private void onDataFieldEnter() {
      String oldData = npcActionSet.getData();
      String newData = dataField.getText();

      npcActionSet.setData(newData);

      try {
        SqlStatement.update(npcActionSet).where("number").eq(npcActionSet.getNumber()).and("actionset").eq(npcActionSet.getActionset()).and("type").eq(npcActionSet.getType()).and("data").eq(oldData).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private NPCActionSet npcActionSet;
    private JLabel dataExplanationLabel;
    private JTextField dataField = new JTextField();
    private ActionTypeSelector actionTypeSelector = new ActionTypeSelector();

    private static final long serialVersionUID = 1L;
  }

  private class NPCRequirementPanel extends JPanel {
    public NPCRequirementPanel(NPCRequirement npcr) {
      npcRequirement = npcr;

      requirementTypeSelector.reload();
      requirementTypeSelector.setSelectedIndex(npcr.getType());
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
      eventSelector.setSelectedIndex(Math.abs(npcRequirement.getData()) - 1);
      eventSelector.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          onEventSelect();
        }
      });
      add(eventSelector);

      invertSelection = new JCheckBox("Has not done yet");
      invertSelection.setSelected(npcRequirement.getData() < 0);
      invertSelection.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onInvertSelectionClick();
        }
      });
      add(invertSelection);

      dataField.setText(Math.abs(npcRequirement.getData()) + "");
    }

    private void addPokedexStuff() {
      dataField.setPreferredSize(new Dimension(80, 20));
      dataField.setText(Math.abs(npcRequirement.getData()) + "");
      dataField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onDataFieldEnter();
        }
      });
      add(dataField);

      invertSelection = new JCheckBox("Less than");
      invertSelection.setSelected(npcRequirement.getData() < 0);
      invertSelection.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onInvertSelectionClick();
        }
      });
      add(invertSelection);
    }

    private void onRequirementTypeSelect() {
      int oldActionType = npcRequirement.getType();
      int newActionType = requirementTypeSelector.getSelectedIndex();

      npcRequirement.setType(newActionType);

      try {
        SqlStatement.update(npcRequirement).where("number").eq(npcRequirement.getNumber()).and("actionset").eq(npcRequirement.getActionset()).and("type").eq(oldActionType).and("data").eq(npcRequirement.getData()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private void onEventSelect() {
      int requirementData = eventSelector.getCurrentElement().getNumber();

      if (invertSelection.isSelected()) {
        requirementData = -requirementData;
      }

      dataField.setText(requirementData + "");
      onDataFieldEnter();
    }

    private void onInvertSelectionClick() {
      onDataFieldEnter();
    }

    private void onDataFieldEnter() {
      int oldData = npcRequirement.getData();
      int newData = Math.abs(Integer.parseInt(dataField.getText()));

      if (invertSelection.isSelected()) {
        newData = -newData;
      }

      npcRequirement.setData(newData);

      try {
        SqlStatement.update(npcRequirement).where("number").eq(npcRequirement.getNumber()).and("actionset").eq(npcRequirement.getActionset()).and("type").eq(npcRequirement.getType()).and("data").eq(oldData).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private JCheckBox invertSelection;
    private NPCRequirement npcRequirement;
    private JTextField dataField = new JTextField();
    private EventSelector eventSelector = new EventSelector();
    private RequirementTypeSelector requirementTypeSelector = new RequirementTypeSelector();

    private static final long serialVersionUID = 1L;
  }

  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private JTextField nameField = new JTextField();
  private JPanel actionSetContainer = new JPanel();
  private NPCActionSetSelector npcActionSetSelector;
  private NPCSelector npcSelector = new NPCSelector();
  private JPanel requirementSetContainer = new JPanel();
}