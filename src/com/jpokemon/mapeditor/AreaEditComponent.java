package com.jpokemon.mapeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.Area;

import com.jpokemon.JPokemonButton;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class AreaEditComponent implements MapEditComponent {
  public static final String BUTTON_NAME = "Areas";

  public AreaEditComponent() {
    allAreas.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onClickSelectArea();
      }
    });
    editorPanel.add(allAreas);

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
        AreaEditComponent.this.onClickNewArea();
      }
    });
    editorPanel.add(newArea);
  }

  @Override
  public JPanel getEditor() {
    Area area;
    readyToEdit = false;

    areas.clear();
    allAreas.removeAllItems();
    for (int i = 1; (area = Area.get(i)) != null; i++) {
      areas.add(area);
      allAreas.addItem(area.toString());
    }

    if (areas.size() > currentAreaID) {
      currentArea = areas.get(currentAreaID);

      nameField.setText(currentArea.getName());
      allAreas.setSelectedIndex(currentAreaID);
    }

    readyToEdit = true;
    return editorPanel;
  }

  private void onClickNewArea() {
    currentArea = Area.createNew();
    currentAreaID = allAreas.getItemCount();
    getEditor();
  }

  private void onClickSelectArea() {
    if (!readyToEdit)
      return;

    currentAreaID = allAreas.getSelectedIndex();

    getEditor();
  }

  private void onEnterNewAreaName() {
    if (!readyToEdit)
      return;

    String name = nameField.getText();

    currentArea.setName(name);
    commitChange();
    getEditor();
  }

  private void commitChange() {
    currentArea.commit();
  }

  private Area currentArea;
  private int currentAreaID;
  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private JComboBox allAreas = new JComboBox();
  private JTextField nameField = new JTextField();
  private List<Area> areas = new ArrayList<Area>();
}