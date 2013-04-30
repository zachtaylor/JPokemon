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

import org.jpokemon.map.npc.NPCType;
import org.jpokemon.service.ImageService;

public class NPCTypeEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "NPC Types";

  public NPCTypeEditor() {
    JPanel northPanel = new JPanel();

    npcTypes.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickSelectNPCType();
      }
    });
    northPanel.add(npcTypes);

    JButton newNPCType = new JButton("New");
    newNPCType.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickNewNPCType();
      }
    });
    northPanel.add(newNPCType);

    JPanel centerPanel = new JPanel();
    centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

    centerPanel.add(icon);

    JPanel nameAndLabel = new JPanel();
    nameAndLabel.add(new JLabel("Type name: "));
    nameField.setMinimumSize(new Dimension(75, 16));
    nameField.setMaximumSize(new Dimension(75, 16));
    nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onEnterNewName();
      }
    });
    nameAndLabel.add(nameField);
    centerPanel.add(nameAndLabel);

    JPanel iconNameAndLabel = new JPanel();
    iconNameAndLabel.add(new JLabel("Icon path: "));
    iconNameField.setMinimumSize(new Dimension(75, 16));
    iconNameField.setMaximumSize(new Dimension(75, 16));
    iconNameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onEnterNewIconPath();
      }
    });
    iconNameAndLabel.add(iconNameField);
    centerPanel.add(iconNameAndLabel);

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(centerPanel, BorderLayout.CENTER);
  }

  @Override
  public JPanel getEditor() {
    readyToEdit = false;

    NPCType npcType;
    npcTypes.removeAllItems();
    allNPCTypes.clear();
    for (int i = 1; (npcType = NPCType.get(i)) != null; i++) {
      npcTypes.addItem(npcType.toString());
      allNPCTypes.add(npcType);
    }

    if (allNPCTypes.size() > currentTypeIndex) {
      currentNPCType = allNPCTypes.get(currentTypeIndex);

      npcTypes.setSelectedIndex(currentTypeIndex);
      icon.setIcon(ImageService.npc(currentNPCType.getIcon()));
      nameField.setText(currentNPCType.getName());
      iconNameField.setText(currentNPCType.getIcon());
    }

    readyToEdit = true;
    return editorPanel;
  }

  private void onClickSelectNPCType() {
    if (!readyToEdit)
      return;

    currentTypeIndex = npcTypes.getSelectedIndex();

    getEditor();
  }

  private void onClickNewNPCType() {
    System.out.println("New NPCType clicked");
  }

  private void onEnterNewName() {
    if (!readyToEdit)
      return;

    currentNPCType.setName(nameField.getText());

    commitChange();
    getEditor();
  }

  private void onEnterNewIconPath() {
    if (!readyToEdit)
      return;

    currentNPCType.setIcon(iconNameField.getText());

    commitChange();
    getEditor();
  }

  private void commitChange() {
    System.out.println(currentNPCType.toString());
  }

  private NPCType currentNPCType;
  private int currentTypeIndex = 0;
  private boolean readyToEdit = false;
  private JLabel icon = new JLabel();
  private JPanel editorPanel = new JPanel();
  private JComboBox npcTypes = new JComboBox();
  private List<NPCType> allNPCTypes = new ArrayList<NPCType>();
  private JTextField nameField = new JTextField(), iconNameField = new JTextField();
}