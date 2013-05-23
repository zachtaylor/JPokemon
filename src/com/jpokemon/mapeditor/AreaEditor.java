package com.jpokemon.mapeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.Area;

import com.jpokemon.JPokemonButton;
import com.jpokemon.mapeditor.widget.AreaSelector;

public class AreaEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "Areas";

  public AreaEditor() {
    areaSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickSelectArea();
      }
    });
    editorPanel.add(areaSelector);

    nameField.setMinimumSize(new Dimension(75, 16));
    nameField.setMaximumSize(new Dimension(75, 16));
    nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onEnterNewAreaName();
      }
    });
    editorPanel.add(nameField);

    JButton newArea = new JPokemonButton("New");
    newArea.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        AreaEditor.this.onClickNewArea();
      }
    });
    editorPanel.add(newArea);
  }

  @Override
  public JPanel getEditor() {
    readyToEdit = false;

    areaSelector.reload();

    if (areaSelector.getArea() != null) {
      nameField.setText(areaSelector.getArea().getName());
    }

    readyToEdit = true;
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(480, 40);
  }

  private void onClickNewArea() {
    Area.createNew();
    getEditor();
  }

  private void onClickSelectArea() {
    if (!readyToEdit)
      return;

    getEditor();
  }

  private void onEnterNewAreaName() {
    if (!readyToEdit)
      return;

    Area area = areaSelector.getArea();
    String name = nameField.getText();

    area.setName(name);
    commitChange();
    getEditor();
  }

  private void commitChange() {
    areaSelector.getArea().commit();
  }

  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private JTextField nameField = new JTextField();
  private AreaSelector areaSelector = new AreaSelector();
}