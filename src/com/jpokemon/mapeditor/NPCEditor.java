package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.npc.NPC;
import org.jpokemon.map.npc.NPCType;
import org.jpokemon.service.ImageService;

import com.jpokemon.JPokemonButton;

public class NPCEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "NPCs";

  public NPCEditor() {
    JPanel northPanel = new JPanel();

    northPanel.add(allNPCs);

    JButton newNPC = new JPokemonButton("New");
    newNPC.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        NPCEditor.this.onClickNewNPC();
      }
    });
    northPanel.add(newNPC);

    JPanel eastPanel = new JPanel();
    eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

    eastPanel.add(new JPanel());
    eastPanel.add(npcTypes);
    eastPanel.add(new JPanel());

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

    centerPanel.add(new JPanel());
    centerPanel.add(iconLabel);
    centerPanel.add(new JPanel());

    JPanel nameAndTypeName = new JPanel();
    nameAndTypeName.setLayout(new BoxLayout(nameAndTypeName, BoxLayout.X_AXIS));

    nameAndTypeName.add(typeNameLabel);

    nameField.setMaximumSize(new Dimension(75, 16));
    nameAndTypeName.add(nameField);

    centerPanel.add(nameAndTypeName);
    centerPanel.add(new JPanel());

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(eastPanel, BorderLayout.EAST);
    editorPanel.add(centerPanel, BorderLayout.CENTER);
  }

  @Override
  public JPanel getEditor() {
    NPC npc;
    NPCType npcType;

    npcs.clear();
    allNPCs.removeAll();
    for (int i = 1; (npc = NPC.get(i)) != null; i++) {
      npcs.add(npc);
      allNPCs.addItem(npc.toString());
    }

    npcTypes.removeAll();
    for (int i = 1; (npcType = NPCType.get(i)) != null; i++) {
      npcTypes.addItem(npcType.getName());
    }

    if (npcs.size() > 0) {
      currentNPC = npcs.get(0);
      iconLabel.setIcon(ImageService.npc(currentNPC.getIcon()));

      if (currentNPC.getName().contains("{typename}"))
        typeNameLabel.setText(currentNPC.getType().getName());
      else
        typeNameLabel.setText(null);

      nameField.setText(currentNPC.getName().replaceAll("\\{typename\\}", ""));
      npcTypes.setSelectedIndex(currentNPC.getType().getNumber() - 1);
    }
    else {
      iconLabel.setIcon(null);
      typeNameLabel.setText("No NPC loaded");
    }

    return editorPanel;
  }

  private void onClickNewNPC() {
    System.out.println("New NPC clicked");
  }

  private NPC currentNPC;
  private JPanel editorPanel = new JPanel();
  private List<NPC> npcs = new ArrayList<NPC>();
  private JLabel typeNameLabel = new JLabel(), iconLabel = new JLabel();
  private JTextField nameField = new JTextField();
  private JComboBox allNPCs = new JComboBox(), npcTypes = new JComboBox();
}