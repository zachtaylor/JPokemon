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

import org.jpokemon.overworld.npc.NPC;

import com.jpokemon.JPokemonButton;
import com.jpokemon.mapeditor.widget.selector.NPCSelector;
import com.jpokemon.mapeditor.widget.selector.NPCTypeSelector;
import com.jpokemon.ui.ImageLoader;

public class NPCEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "NPCs";

  public NPCEditor() {
    JPanel northPanel = new JPanel();

    npcSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickSelectNPC();
      }
    });
    northPanel.add(npcSelector);

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

    npcTypeSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickNPCType();
      }
    });
    eastPanel.add(npcTypeSelector);

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
    readyToEdit = false;

    npcSelector.reload();
    npcTypeSelector.reload();

    iconLabel.setIcon(ImageLoader.npc(npcSelector.getCurrentElement().getIcon()));

    if (npcSelector.getCurrentElement().getName().contains("{typename} ")) {
      useTypePrefix.setSelected(true);
      typeNameLabel.setText(npcSelector.getCurrentElement().getNPCType().getName());
    }
    else {
      useTypePrefix.setSelected(false);
      typeNameLabel.setText(null);
    }

    nameField.setText(npcSelector.getCurrentElement().getName().replaceAll("\\{typename\\} ", ""));

    readyToEdit = true;
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(480, 120);
  }

  private void onClickSelectNPC() {
    if (!readyToEdit)
      return;

    getEditor();
  }

  private void onClickNewNPC() {
    NPC.createNew();
    getEditor();
  }

  private void onEnterNewNPCName() {
    if (!readyToEdit)
      return;

    String name = nameField.getText();

    if (useTypePrefix.isSelected())
      name = "{typename} " + name;

    npcSelector.getCurrentElement().setName(name);
    commitChange();
    getEditor();
  }

  private void onClickNPCType() {
    if (!readyToEdit)
      return;

    npcSelector.getCurrentElement().setType(npcTypeSelector.getCurrentElement().getNumber());
    commitChange();
    getEditor();
  }

  private void onClickUseTypePrefix() {
    if (!readyToEdit)
      return;

    NPC currentNPC = npcSelector.getCurrentElement();

    if (useTypePrefix.isSelected()) {
      currentNPC.setName("{typename} " + currentNPC.getName());
    }
    else {
      currentNPC.setName(currentNPC.getName().replaceAll("\\{typename\\} ", ""));
    }
    commitChange();
    getEditor();
  }

  private void commitChange() {
    npcSelector.getCurrentElement().commit();
  }

  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private JTextField nameField = new JTextField();
  private NPCSelector npcSelector = new NPCSelector();
  private NPCTypeSelector npcTypeSelector = new NPCTypeSelector();
  private JLabel typeNameLabel = new JLabel(), iconLabel = new JLabel();
  private JCheckBox useTypePrefix = new JCheckBox("Use type prefix", false);
}