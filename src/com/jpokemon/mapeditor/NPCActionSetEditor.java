package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.npc.NPCActionSetMap;

import com.jpokemon.mapeditor.widget.selector.NPCActionSetSelector;
import com.jpokemon.mapeditor.widget.selector.NPCSelector;
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

    JPanel centerPanel = new JPanel();

    npcActionSetMapSelector = new NPCActionSetSelector(0);
    npcActionSetMapSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNPCActionSetMapSelect();
      }
    });
    centerPanel.add(npcActionSetMapSelector);

    nameField.setPreferredSize(new Dimension(180, 20));
    // nameField.setMinimumSize();
    // nameField.setMaximumSize(new Dimension(160, 16));
    nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNameFieldEnter();
      }
    });
    centerPanel.add(nameField);

    JPanel southPanel = new JPanel();

    addNPCActionSet = new JButton("Add ActionSet");
    addNPCActionSet.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAddNPCActionSetClick();
      }
    });
    southPanel.add(addNPCActionSet);

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(centerPanel, BorderLayout.CENTER);
    editorPanel.add(southPanel, BorderLayout.SOUTH);
  }

  @Override
  public JPanel getEditor() {
    readyToEdit = false;

    npcSelector.reload();
    npcActionSetMapSelector.setNPCNumber(npcSelector.getCurrentElement().getNumber());
    //
    npcActionSetMapSelector.reload();

    nameField.setText(npcActionSetMapSelector.getCurrentElement().getOption());

    readyToEdit = true;
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(100, 100);
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

    for (NPCActionSetMap npcasm : NPCActionSetMap.getByNPCNumber(npcActionSetMapSelector.getCurrentElement().getNumber())) {
      if (npcasm.getActionset() == npcActionSetMapSelector.getCurrentElement().getActionset()) {
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

  private JButton addNPCActionSet;
  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private JTextField nameField = new JTextField();
  private NPCSelector npcSelector = new NPCSelector();
  private NPCActionSetSelector npcActionSetMapSelector;

}