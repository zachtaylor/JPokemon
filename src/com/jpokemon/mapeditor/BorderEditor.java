package com.jpokemon.mapeditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jpokemon.overworld.map.BorderAction;
import org.jpokemon.overworld.map.BorderRequirement;

import com.jpokemon.mapeditor.widget.panel.ActionPanel;
import com.jpokemon.mapeditor.widget.panel.RequirementPanel;
import com.jpokemon.mapeditor.widget.selector.AreaSelector;
import com.jpokemon.mapeditor.widget.selector.BorderSelector;
import com.njkremer.Sqlite.DataConnectionException;
import com.njkremer.Sqlite.SqlStatement;

public class BorderEditor implements MapEditComponent {
  public static final String BUTTON_NAME = "Border Actions";

  public BorderEditor() {
    JPanel northPanel = new JPanel();

    areaSelector.reload();
    areaSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onAreaSelect();
      }
    });
    northPanel.add(areaSelector);

    borderSelector.setArea(areaSelector.getCurrentElement().getNumber());
    borderSelector.reload();
    borderSelector.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onBorderSelect();
      }
    });
    northPanel.add(borderSelector);

    JPanel westPanel = new JPanel();
    westPanel.setLayout(new BoxLayout(westPanel, BoxLayout.Y_AXIS));

    JLabel actionSetContainerTitle = new JLabel("Actions Performed on Border");
    westPanel.add(actionSetContainerTitle);

    actionSetContainer.setLayout(new BoxLayout(actionSetContainer, BoxLayout.Y_AXIS));
    westPanel.add(actionSetContainer);

    JButton newAction = new JButton("New Action");
    newAction.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent arg0) {
        onNewActionClick();
      }
    });
    westPanel.add(newAction);

    JPanel eastPanel = new JPanel();
    eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.Y_AXIS));

    JLabel requirementSetContainerTitle = new JLabel("Requirements of Border");
    eastPanel.add(requirementSetContainerTitle);

    requirementSetContainer.setLayout(new BoxLayout(requirementSetContainer, BoxLayout.Y_AXIS));
    eastPanel.add(requirementSetContainer);

    JButton newRequirement = new JButton("New Requirement");
    newRequirement.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        onNewRequirementClick();
      }
    });
    eastPanel.add(newRequirement);

    editorPanel.setLayout(new BorderLayout());
    editorPanel.add(northPanel, BorderLayout.NORTH);
    editorPanel.add(westPanel, BorderLayout.WEST);
    editorPanel.add(eastPanel, BorderLayout.EAST);
  }

  @Override
  public JPanel getEditor() {
    actionSetContainer.removeAll();
    requirementSetContainer.removeAll();

    if (borderSelector.getCurrentElement() != null) {
      for (BorderAction borderAction : BorderAction.get(borderSelector.getCurrentElement().getArea(), borderSelector.getCurrentElement().getNext())) {
        actionSetContainer.add(new ActionPanel(this, borderAction));
      }

      for (BorderRequirement borderRequirement : BorderRequirement.get(borderSelector.getCurrentElement().getArea(), borderSelector.getCurrentElement().getNext())) {
        requirementSetContainer.add(new RequirementPanel(this, borderRequirement));
      }
    }

    return editorPanel;
  }

  @Override
  public Dimension getSize() {
    return new Dimension(1360, 240);
  }

  private void onAreaSelect() {
    borderSelector.setArea(areaSelector.getCurrentElement().getNumber());
    borderSelector.reload();

    getEditor();
  }

  private void onBorderSelect() {
    getEditor();
  }

  private void onNewActionClick() {
    BorderAction borderAction = new BorderAction();

    borderAction.setArea(borderSelector.getCurrentElement().getArea());
    borderAction.setNext(borderSelector.getCurrentElement().getNext());
    borderAction.setType("undefined");
    borderAction.setData("undefined");

    try {
      SqlStatement.insert(borderAction).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    getEditor();
  }

  private void onNewRequirementClick() {
    BorderRequirement borderRequirement = new BorderRequirement();

    borderRequirement.setArea(borderSelector.getCurrentElement().getArea());
    borderRequirement.setNext(borderSelector.getCurrentElement().getNext());
    borderRequirement.setType(0);
    borderRequirement.setData(0);

    try {
      SqlStatement.insert(borderRequirement).execute();
    } catch (DataConnectionException e) {
      e.printStackTrace();
    }

    getEditor();
  }

  private JPanel editorPanel = new JPanel();
  private JPanel actionSetContainer = new JPanel();
  private JPanel requirementSetContainer = new JPanel();
  private AreaSelector areaSelector = new AreaSelector();
  private BorderSelector borderSelector = new BorderSelector(0);
}