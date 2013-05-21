package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.npc.NPC;
import org.jpokemon.map.npc.NPCType;
import org.jpokemon.service.ImageService;

import com.jpokemon.JPokemonButton;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class NPCEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "NPCs";

  public NPCEditor() {
    JPanel northPanel = new JPanel();

    allNPCs.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickSelectNPC();
      }
    });
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

    npcTypes.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickNPCType();
      }
    });
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

    nameField.setMinimumSize(new Dimension(75, 16));
    nameField.setMaximumSize(new Dimension(75, 16));
    nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onEnterNewNPCName();
      }
    });
    nameAndTypeName.add(nameField);

    useTypePrefix.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickUseTypePrefix();
      }
    });
    nameAndTypeName.add(useTypePrefix);

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

    readyToEdit = false;

    npcs.clear();
    allNPCs.removeAllItems();
    for (int i = 1; (npc = NPC.get(i)) != null; i++) {
      npcs.add(npc);
      allNPCs.addItem(npc.toString());
    }

    npcTypes.removeAllItems();
    for (int i = 1; (npcType = NPCType.get(i)) != null; i++) {
      npcTypes.addItem(npcType.toString());
    }

    if (npcs.size() > currentNPCIndex) {
      currentNPC = npcs.get(currentNPCIndex);
      iconLabel.setIcon(ImageService.npc(currentNPC.getIcon()));

      if (currentNPC.getNameRaw().contains("{typename} ")) {
        useTypePrefix.setSelected(true);
        typeNameLabel.setText(currentNPC.getType().getName());
      }
      else {
        useTypePrefix.setSelected(false);
        typeNameLabel.setText(null);
      }

      nameField.setText(currentNPC.getNameRaw().replaceAll("\\{typename\\} ", ""));
      npcTypes.setSelectedIndex(currentNPC.getType().getNumber() - 1);
    }
    else {
      iconLabel.setIcon(null);
      typeNameLabel.setText("No NPC loaded");
    }

    readyToEdit = true;
    return editorPanel;
  }

  private void onClickSelectNPC() {
    if (!readyToEdit)
      return;

    currentNPCIndex = allNPCs.getSelectedIndex();

    getEditor();
  }

  private void onClickNewNPC() {
    System.out.println("New NPC clicked");
  }

  private void onEnterNewNPCName() {
    if (!readyToEdit)
      return;

    String name = nameField.getText();

    if (useTypePrefix.isSelected())
      name = "{typename} " + name;

    currentNPC.setName(name);
    commitChange();
    getEditor();
  }

  private void onClickNPCType() {
    if (!readyToEdit)
      return;

    currentNPC.setType(npcTypes.getSelectedIndex() + 1);
    commitChange();
    getEditor();
  }

  private void onClickUseTypePrefix() {
    if (!readyToEdit)
      return;

    if (useTypePrefix.isSelected()) {
      currentNPC.setName("{typename} " + currentNPC.getNameRaw());
    }
    else {
      currentNPC.setName(currentNPC.getNameRaw().replaceAll("\\{typename\\} ", ""));
    }
    commitChange();
    getEditor();
  }

  private void commitChange() {
    System.out.println(currentNPC.toString());
    // TODO
  }

  private NPC currentNPC;
  private int currentNPCIndex = 0;
  boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private List<NPC> npcs = new ArrayList<NPC>();
  private JTextField nameField = new JTextField();
  private JLabel typeNameLabel = new JLabel(), iconLabel = new JLabel();
  private JComboBox allNPCs = new JComboBox(), npcTypes = new JComboBox();
  private JCheckBox useTypePrefix = new JCheckBox("Use type prefix", false);
}