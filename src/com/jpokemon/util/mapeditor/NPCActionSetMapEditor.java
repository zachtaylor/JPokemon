package com.jpokemon.util.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import org.jpokemon.overworld.npc.NPCActionSetMap;

import com.jpokemon.util.ui.selector.AreaSelector;
import com.jpokemon.util.ui.selector.NPCSelector;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.SqlStatement;

public class NPCActionSetMapEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "NPC ActionSet Mappings";

  public NPCActionSetMapEditor() {
    JPanel northPanel = new JPanel();
    JPanel westPanel = new JPanel();

    areaSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAreaSelect();
      }
    });
    northPanel.add(areaSelector);

    npcSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNPCSelect();
      }
    });
    westPanel.add(npcSelector);

    JPanel centerPanel = childrenPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(centerPanel, BorderLayout.CENTER);
    editorPanel.add(westPanel, BorderLayout.WEST);
  }

  @Override
  public JPanel getEditor() {
    readyToEdit = false;

    npcSelector.reload();
    areaSelector.reload();

    childrenPanel.removeAll();

    int areaID = areaSelector.getCurrentElement().getNumber();
    int npcNumber = npcSelector.getCurrentElement().getNumber();

    List<Integer> seenActionSetNumbers = new ArrayList<Integer>();

    // Add ones that are in the current area
    for (NPCActionSetMap npcActionSetMap : NPCActionSetMap.get(areaID)) {
      if (npcActionSetMap.getNumber() != npcNumber) {
        continue;
      }

      seenActionSetNumbers.add(npcActionSetMap.getActionset());
      childrenPanel.add(new NPCActionSetMapCheckBox(npcActionSetMap, true));
    }
    // Add ones that are not in the current area
    for (NPCActionSetMap npcActionSetMap : NPCActionSetMap.getByNPCNumber(npcNumber)) {
      if (seenActionSetNumbers.contains(npcActionSetMap.getActionset())) {
        continue;
      }

      seenActionSetNumbers.add(npcActionSetMap.getActionset());
      childrenPanel.add(new NPCActionSetMapCheckBox(npcActionSetMap, false));
    }

    childrenPanel.add(new JPanel());

    readyToEdit = true;
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(600, 300);
  }

  private void onAreaSelect() {
    if (!readyToEdit)
      return;

    getEditor();
    editorPanel.repaint();
  }

  private void onNPCSelect() {
    if (!readyToEdit)
      return;

    getEditor();
    editorPanel.repaint();
  }

  private class NPCActionSetMapCheckBox extends JCheckBox {
    public NPCActionSetMapCheckBox(NPCActionSetMap npcasm, boolean select) {
      npcActionSetMap = npcasm;
      setSelected(select);
      setText(npcActionSetMap.toString());
      addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          onCheckBoxClick();
        }
      });
    }

    private void onCheckBoxClick() {
      if (isSelected()) {
        commitAddition();
      }
      else {
        commitDeletion();
      }

      getEditor();
    }

    private void commitAddition() {
      if (npcActionSetMap.getArea() == 0) {
        int newAreaNumber = areaSelector.getCurrentElement().getNumber();

        npcActionSetMap.setArea(newAreaNumber);

        try {
          SqlStatement.update(npcActionSetMap).where("number").eq(npcActionSetMap.getNumber()).and("area").eq(0).and("actionset").eq(npcActionSetMap.getActionset()).execute();
        } catch (DataConnectionException e) {
          e.printStackTrace();
        }
      }
      else {
        NPCActionSetMap newNPCActionSetMap = new NPCActionSetMap();

        newNPCActionSetMap.setArea(areaSelector.getCurrentElement().getNumber());
        newNPCActionSetMap.setNumber(npcActionSetMap.getNumber());
        newNPCActionSetMap.setActionset(npcActionSetMap.getActionset());
        newNPCActionSetMap.setOption(npcActionSetMap.getOption());

        try {
          SqlStatement.insert(newNPCActionSetMap).execute();
        } catch (DataConnectionException e) {
          e.printStackTrace();
        }
      }
    }

    private void commitDeletion() {
      int occuranceCount = 0;

      for (NPCActionSetMap npcasm : NPCActionSetMap.getByNPCNumber(npcActionSetMap.getNumber())) {
        if (npcasm.getActionset() == npcActionSetMap.getActionset()) {
          occuranceCount++;
        }
      }

      if (occuranceCount == 1) {
        int oldAreaNumber = npcActionSetMap.getArea();

        npcActionSetMap.setArea(0);

        try {
          SqlStatement.update(npcActionSetMap).where("number").eq(npcActionSetMap.getNumber()).and("area").eq(oldAreaNumber).and("actionset").eq(npcActionSetMap.getActionset()).execute();
        } catch (DataConnectionException e) {
          e.printStackTrace();
        }
      }
      else {
        try {
          SqlStatement.delete(npcActionSetMap).where("number").eq(npcActionSetMap.getNumber()).and("area").eq(npcActionSetMap.getArea()).and("actionset").eq(npcActionSetMap.getActionset()).execute();
        } catch (DataConnectionException e) {
          e.printStackTrace();
        }
      }
    }

    private NPCActionSetMap npcActionSetMap;

    private static final long serialVersionUID = 1L;
  }

  private JPanel childrenPanel;
  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private NPCSelector npcSelector = new NPCSelector();
  private AreaSelector areaSelector = new AreaSelector();
}