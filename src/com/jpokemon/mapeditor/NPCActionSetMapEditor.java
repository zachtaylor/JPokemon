package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.npc.NPCActionSetMap;

import com.jpokemon.mapeditor.widget.selector.AreaSelector;
import com.jpokemon.mapeditor.widget.selector.NPCSelector;
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

    int areaID = areaSelector.getCurrentElement().getNumber();
    int npcNumber = npcSelector.getCurrentElement().getNumber();

    childrenPanel.removeAll();
    for (NPCActionSetMap npcActionSetMap : NPCActionSetMap.get(areaID)) {
      if (npcActionSetMap.getNumber() != npcNumber) {
        continue;
      }

      childrenPanel.add(new NPCActionSetMapPanel(npcActionSetMap));
      childrenPanel.add(new JPanel());
    }
    
    // TODO : Add an add row button which adds a new actionsetmap option and id

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

  private class NPCActionSetMapPanel extends JPanel {
    public NPCActionSetMapPanel(NPCActionSetMap npcasm) {
      npcActionSetMap = npcasm;

      JLabel idLabel = new JLabel(npcActionSetMap.getActionset() + ":");

      nameField.setMinimumSize(new Dimension(160, 16));
      nameField.setMaximumSize(new Dimension(160, 16));
      nameField.setText(npcActionSetMap.getOption());
      nameField.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onNameFieldEnter();
        }
      });

      setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
      add(idLabel);
      add(nameField);
    }

    private void onNameFieldEnter() {
      String newOption = nameField.getText();

      npcActionSetMap.setOption(newOption);

      try {
        SqlStatement.update(npcActionSetMap).where("area").eq(npcActionSetMap.getArea()).and("number").eq(npcActionSetMap.getNumber()).and("actionset").eq(npcActionSetMap.getActionset()).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private NPCActionSetMap npcActionSetMap;
    private JTextField nameField = new JTextField();

    private static final long serialVersionUID = 1L;
  }

  private JPanel childrenPanel;
  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private NPCSelector npcSelector = new NPCSelector();
  private AreaSelector areaSelector = new AreaSelector();
}