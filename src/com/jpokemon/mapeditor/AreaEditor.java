package com.jpokemon.mapeditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jpokemon.map.gps.Area;
import org.jpokemon.map.gps.Border;

import com.jpokemon.JPokemonButton;
import com.jpokemon.mapeditor.widget.selector.AreaSelector;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.SqlStatement;

public class AreaEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "Areas";

  public AreaEditor() {
    JButton newArea = new JPokemonButton("New");
    newArea.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        AreaEditor.this.onClickNewArea();
      }
    });
    editorPanel.add(newArea);

    areaSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAreaSelect();
      }
    });
    editorPanel.add(areaSelector);

    nameField.setMinimumSize(new Dimension(75, 20));
    nameField.setMaximumSize(new Dimension(75, 20));
    nameField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onEnterNewAreaName();
      }
    });
    editorPanel.add(nameField);

    newBorderButton = new JButton("Add Border");
    newBorderButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNewBorderButtonClick();
      }
    });

    childrenPanel.setLayout(new BoxLayout(childrenPanel, BoxLayout.Y_AXIS));
    childrenPanel.add(newBorderButton);
    editorPanel.add(childrenPanel);
  }

  @Override
  public JPanel getEditor() {
    readyToEdit = false;

    areaSelector.reload();

    if (areaSelector.getCurrentElement() != null) {
      nameField.setText(areaSelector.getCurrentElement().getName());

      childrenPanel.removeAll();
      for (Border border : Border.get(areaSelector.getCurrentElement().getNumber())) {
        childrenPanel.add(new BorderPanel(border));
      }
      childrenPanel.add(newBorderButton);
    }

    readyToEdit = true;
    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(480, 140);
  }

  private void onClickNewArea() {
    Area.createNew();
    getEditor();
  }

  private void onAreaSelect() {
    if (!readyToEdit)
      return;

    getEditor();
  }

  private void onEnterNewAreaName() {
    if (!readyToEdit)
      return;

    Area area = areaSelector.getCurrentElement();
    String name = nameField.getText();

    area.setName(name);
    commitChange();
    getEditor();
  }

  private void onNewBorderButtonClick() {
    Border border = new Border();
    border.setArea(areaSelector.getCurrentElement().getNumber());
    border.setNext(0);

    try {
      SqlStatement.insert(border).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    getEditor();
  }

  private void commitChange() {
    areaSelector.getCurrentElement().commit();
  }

  private class BorderPanel extends JPanel {
    public BorderPanel(Border b) {
      border = b;

      add(new JLabel("can access: "));
      add(new JPanel());

      areaSelector.reload();
      areaSelector.setSelectedIndex(border.getNext() - 1);
      areaSelector.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent arg0) {
          onAreaSelect();
        }
      });
      add(areaSelector);
    }

    private void onAreaSelect() {
      int oldNextArea = border.getNext();
      int newNextArea = areaSelector.getCurrentElement().getNumber();

      border.setNext(newNextArea);

      try {
        SqlStatement.update(border).where("area").eq(border.getArea()).and("next").eq(oldNextArea).execute();
      } catch (DataConnectionException e) {
        e.printStackTrace();
      }

      getEditor();
    }

    private Border border;
    private AreaSelector areaSelector = new AreaSelector();

    private static final long serialVersionUID = 1L;
  }

  private JButton newBorderButton;
  private boolean readyToEdit = false;
  private JPanel editorPanel = new JPanel();
  private JPanel childrenPanel = new JPanel();
  private JTextField nameField = new JTextField();
  private AreaSelector areaSelector = new AreaSelector();
}