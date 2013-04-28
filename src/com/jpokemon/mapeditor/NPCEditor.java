package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

    JPanel southPanel = new JPanel();

    JButton saveNPC = new JPokemonButton("Save");
    saveNPC.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickSaveNPC();
      }
    });
    southPanel.add(saveNPC);

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
    centerPanel.add(nameLabel);
    centerPanel.add(new JPanel());

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(southPanel, BorderLayout.SOUTH);
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
      nameLabel.setText(currentNPC.getName());
      npcTypes.setSelectedIndex(currentNPC.getType().getNumber() - 1);
    }
    else {
      iconLabel.setIcon(null);
      nameLabel.setText("No NPC loaded");
    }

    return editorPanel;
  }

  private void onClickNewNPC() {
    System.out.println("New NPC clicked");
  }

  private void onClickSaveNPC() {
    System.out.println("Save NPC clicked");
  }

  private NPC currentNPC;
  private JPanel editorPanel = new JPanel();
  private List<NPC> npcs = new ArrayList<NPC>();
  private JLabel nameLabel = new JLabel(), iconLabel = new JLabel();
  private JComboBox allNPCs = new JComboBox(), npcTypes = new JComboBox();
}